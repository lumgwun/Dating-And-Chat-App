package com.lahoriagency.cikolive.Classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.lahoriagency.cikolive.CallActivity;
import com.lahoriagency.cikolive.Fragments.AudioConversationFragment;
import com.lahoriagency.cikolive.Fragments.VideoConversationFragment;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Conference.BaseConversationFragment;
import com.quickblox.chat.QBChatService;
import com.quickblox.conference.ConferenceClient;
import com.quickblox.conference.ConferenceSession;
import com.quickblox.conference.QBConferenceRole;
import com.quickblox.conference.WsException;
import com.quickblox.conference.callbacks.ConferenceEntityCallback;
import com.quickblox.conference.callbacks.ConferenceSessionCallbacks;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBMediaStreamManager;
import com.quickblox.videochat.webrtc.QBRTCAudioTrack;
import com.quickblox.videochat.webrtc.QBRTCCameraVideoCapturer;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.quickblox.videochat.webrtc.QBRTCScreenCapturer;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.QBSignalingSpec;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientAudioTracksCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSignalingCallback;
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionListener;
import org.webrtc.CameraVideoCapturer;

import org.webrtc.VideoSink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.lahoriagency.cikolive.Classes.Consts.MAX_OPPONENTS_COUNT;

@SuppressWarnings("deprecation")
public class CallService extends Service {

    private static final String TAG = CallService.class.getSimpleName();

    private static final int SERVICE_ID = 787;
    private static final String CHANNEL_ID = "Quickblox channel";
    private static final String CHANNEL_NAME = "Quickblox background service";
    public static final int ONE_OPPONENT = 1;

    private HashMap<Integer, QBRTCVideoTrack> videoTrackMap = new HashMap<>();
    private CallServiceBinder callServiceBinder = new CallServiceBinder();
    private NetworkConnectionListener networkConnectionListener;
    private NetworkConnectionChecker networkConnectionChecker;
    private SessionEventsListener sessionEventsListener;
    private SessionStateListener sessionStateListener;
    private ConnectionListenerImpl connectionListener;
    private QBRTCSignalingListener signalingListener;
    private VideoTrackListener videoTrackListener;
    private AppRTCAudioManager appRTCAudioManager;
    private AppRTCAudioManager.OnAudioManagerStateListener listener;
    private Long expirationReconnectionTime = 0L;
    private CallTimerListener callTimerListener;
    private boolean sharingScreenState = false;
    private boolean isCallState = false;
    private QBRTCSession currentSession;
    private QBRTCClient rtcClient;
    private RingtonePlayer ringtonePlayer;
    private Context context;

    private CallTimerTask callTimerTask = new CallTimerTask();
    private Timer callTimer = new Timer();
    private Long callTime;


    private static final long SECONDS_13 = 13000;
    private static final String ICE_FAILED_REASON = "ICE failed";


    //private AudioTrackListener audioTrackListener;
    //private ConferenceSessionListener conferenceSessionListener;
    private final ArrayList<CurrentCallStateCallback> currentCallStateCallbackList = new ArrayList<>();
    private ArrayList<Integer> opponentsIDsList = new ArrayList<>();
    private Map<Integer, Boolean> onlineParticipants = new HashMap<>();
    private OnlineParticipantsChangeListener onlineParticipantsChangeListener;
    //private OnlineParticipantsCheckerCountdown onlineParticipantsCheckerCountdown;
    private UsersConnectDisconnectCallback usersConnectDisconnectCallback;
    private AppRTCAudioManager audioManager;
    private String roomID;
    private String roomTitle;
    private String dialogID;
    private boolean asListenerRole;
    private boolean previousDeviceEarPiece;
    private ConferenceSession currentSessionC;
    private ConferenceClient conferenceClient;
    private ReconnectionState reconnectionState = ReconnectionState.DEFAULT;
    private final Set<ReconnectionListener> reconnectionListeners = new HashSet<>();



    private AudioTrackListener audioTrackListener;
    private ConferenceSessionListener conferenceSessionListener;
    private OnlineParticipantsCheckerCountdown onlineParticipantsCheckerCountdown;


    public static void start(Context context, String roomID, String roomTitle, String dialogID, List<Integer> occupants, boolean listenerRole) {
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(Consts.EXTRA_ROOM_ID, roomID);
        intent.putExtra(Consts.EXTRA_ROOM_TITLE, roomTitle);
        intent.putExtra(Consts.EXTRA_DIALOG_ID, dialogID);
        intent.putExtra(Consts.EXTRA_DIALOG_OCCUPANTS, (Serializable) occupants);
        intent.putExtra(Consts.EXTRA_AS_LISTENER, listenerRole);

        context.startService(intent);
    }


    public ReconnectionState getReconnectionState() {
        return reconnectionState;
    }

    public void setReconnectionState(ReconnectionState reconnectionState) {
        this.reconnectionState = reconnectionState;
    }



    public CallService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CallService.class);
        context.startService(intent);



    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, CallService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        currentSession = WebRtcSessionManager.getInstance(getApplicationContext()).getCurrentSession();
        clearButtonsState();
        initNetworkChecker();
        initRTCClient();
        initListeners();
        initAudioManager();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
        //mp.start();

        if (notification == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, notification);
        ringtone.play();
        initConferenceClient();
        initListeners();
        initAudioManager();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = initNotification();
        startForeground(SERVICE_ID, notification);

        startForeground(SERVICE_ID, notification);
        if (intent != null) {
            roomID = intent.getStringExtra(Consts.EXTRA_ROOM_ID);
            roomTitle = intent.getStringExtra(Consts.EXTRA_ROOM_TITLE);
            dialogID = intent.getStringExtra(Consts.EXTRA_DIALOG_ID);
            opponentsIDsList = (ArrayList<Integer>) intent.getSerializableExtra(Consts.EXTRA_DIALOG_OCCUPANTS);
            asListenerRole = intent.getBooleanExtra(Consts.EXTRA_AS_LISTENER, false);

            if (!isListenerRole() && !roomID.equals(dialogID)) {
                onlineParticipantsCheckerCountdown = new OnlineParticipantsCheckerCountdown(Long.MAX_VALUE, 3000);
                onlineParticipantsCheckerCountdown.start();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkConnectionChecker.unregisterListener(networkConnectionListener);
        removeConnectionListener(connectionListener);

        releaseCurrentSession();
        releaseAudioManager();

        stopCallTimer();
        clearButtonsState();
        clearCallState();
        stopForeground(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        if (onlineParticipantsCheckerCountdown != null && !isListenerRole()) {
            onlineParticipantsCheckerCountdown.cancel();
        }
        removeVideoTrackRenders();
        releaseAudioManager();

        stopForeground(true);

    }



    private Notification initNotification() {
        Intent notifyIntent = new Intent(this, CallActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int intentFlag = 0;
        intentFlag = PendingIntent.FLAG_IMMUTABLE;
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, intentFlag);

        String notificationTitle = getString(R.string.notification_title);
        String notificationText = getString(R.string.notification_text, "");

        String callTime = getCallTime();
        if (!TextUtils.isEmpty(callTime)) {
            notificationText = getString(R.string.notification_text, callTime);
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(notificationTitle);
        bigTextStyle.bigText(notificationText);

        String channelID = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
                : getString(R.string.app_name);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setStyle(bigTextStyle);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationText);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmapIcon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(NotificationManager.IMPORTANCE_LOW);
        } else {
            builder.setPriority(Notification.PRIORITY_LOW);
        }
        builder.setContentIntent(notifyPendingIntent);

        return (Notification) builder.build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelID, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_LOW);
        channel.setLightColor(getColor(R.color.green));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
        return channelID;
    }
    public void setOnlineParticipantsChangeListener(OnlineParticipantsChangeListener onlineParticipantsChangeListener) {
        this.onlineParticipantsChangeListener = onlineParticipantsChangeListener;
    }

    public void subscribeReconnectionListener(ReconnectionListener reconnectionListener) {
        reconnectionListeners.add(reconnectionListener);
    }

    public void unsubscribeReconnectionListener(ReconnectionListener reconnectionListener) {
        reconnectionListeners.remove(reconnectionListener);
    }



    private String getCallTime() {
        String time = "";
        if (callTime != null) {
            String format = String.format("%%0%dd", 2);
            Long elapsedTime = callTime / 1000;
            String seconds = String.format(format, elapsedTime % 60);
            String minutes = String.format(format, elapsedTime % 3600 / 60);
            String hours = String.format(format, elapsedTime / 3600);

            time = minutes + ":" + seconds;
            if (!TextUtils.isEmpty(hours) && hours != "00") {
                time = hours + ":" + minutes + ":" + seconds;
            }
        }
        return time;
    }

    public void playRingtone() {
        ringtonePlayer.play(true);
    }

    public void stopRingtone() {
        ringtonePlayer.stop();
    }

    private void initNetworkChecker() {
        networkConnectionChecker = new NetworkConnectionChecker(getApplication());
        networkConnectionListener = new NetworkConnectionListener();
        networkConnectionChecker.registerListener(networkConnectionListener);
    }

    private void initRTCClient() {
        rtcClient = QBRTCClient.getInstance(this);
        rtcClient.setCameraErrorHandler(new CameraEventsListener());

        QBRTCConfig.setMaxOpponentsCount(MAX_OPPONENTS_COUNT);
        QBRTCConfig.setDebugEnabled(true);

        SettingsUtil.configRTCTimers(this);

        rtcClient.prepareToProcessCalls();
    }

    private void initListeners() {
        sessionStateListener = new SessionStateListener();
        addSessionStateListener(sessionStateListener);

        signalingListener = new QBRTCSignalingListener();
        addSignalingListener(signalingListener);

        videoTrackListener = new VideoTrackListener();
        addVideoTrackListener(videoTrackListener);

        connectionListener = new ConnectionListenerImpl();
        addConnectionListener(connectionListener);

        sessionEventsListener = new SessionEventsListener();
        addSessionEventsListener(sessionEventsListener);

        sessionStateListener = new SessionStateListener();
        addSessionStateListener(sessionStateListener);

        videoTrackListener = new VideoTrackListener();
        addVideoTrackListener(videoTrackListener);

        audioTrackListener = new AudioTrackListener();
        addAudioTrackListener(audioTrackListener);

        conferenceSessionListener = new ConferenceSessionListener();
        addConferenceSessionListener(conferenceSessionListener);
    }
    public void initAudioManager() {
        appRTCAudioManager = AppRTCAudioManager.create(this,listener);

        appRTCAudioManager.setOnWiredHeadsetStateListener(new AppRTCAudioManager.OnWiredHeadsetStateListener() {
            @Override
            public void onWiredHeadsetStateChanged(boolean plugged, boolean hasMicrophone) {
                ToastUtils.shortToast("Headset " + (plugged ? "Plugged" : "Unplugged"));
            }
        });

        appRTCAudioManager.setBluetoothAudioDeviceStateListener(new AppRTCAudioManager.BluetoothAudioDeviceStateListener() {
            @Override
            public void onStateChanged(boolean connected) {
                ToastUtils.shortToast("Bluetooth " + (connected ? "Connected" : "Disconnected"));
            }
        });

        appRTCAudioManager.start(new AppRTCAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(AppRTCAudioManager.AudioDevice audioDevice, Set<AppRTCAudioManager.AudioDevice> set) {
                ToastUtils.shortToast("Audio Device Switched to " + audioDevice);
            }
        });

        if (currentSessionExist() && currentSession.getConferenceType() == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO) {
            appRTCAudioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
        }
        if (audioManager == null) {
            audioManager = AppRTCAudioManager.create(this);
            audioManager.selectAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
            previousDeviceEarPiece = false;
            Log.d(TAG, "AppRTCAudioManager.AudioDevice.SPEAKER_PHONE");

            audioManager.setOnWiredHeadsetStateListener((plugged, hasMicrophone) -> {

                if (!plugged) {
                    if (previousDeviceEarPiece) {
                        setAudioDeviceDelayed(AppRTCAudioManager.AudioDevice.EARPIECE);
                    } else {
                        setAudioDeviceDelayed(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
                    }
                }
            });
            audioManager.start((audioDevice, set) ->
                    Log.d(TAG, "Audio Device Switched to " + audioDevice)
            );
        }
    }


    private void setAudioDeviceDelayed(final AppRTCAudioManager.AudioDevice audioDevice) {
        new Handler().postDelayed(() -> audioManager.selectAudioDevice(audioDevice), 500);
    }

    private void releaseAudioManager() {
        if (audioManager != null) {
            audioManager.stop();
        }
    }

    public void leaveCurrentSession() {
        currentSessionC.leave();
        releaseCurrentSession();
    }

    private void releaseCurrentSession() {
        Log.d(TAG, "Release current session");
        removeSessionStateListener(sessionStateListener);
        removeVideoTrackListener(videoTrackListener);
        removeAudioTrackListener(audioTrackListener);
        removeConferenceSessionListener(conferenceSessionListener);
        Log.d(TAG, "Release current session");
        removeSignalingListener(signalingListener);
        removeSessionEventsListener(sessionEventsListener);
        currentSession = null;
        CallService.stop(this);
    }

    private void removeVideoTrackListener(VideoTrackListener videoTrackListener) {

    }


    private void addConferenceSessionListener(ConferenceSessionListener listener) {
        if (currentSession != null) {
            currentSessionC.addConferenceSessionListener(listener);
        }
    }

    private void removeConferenceSessionListener(ConferenceSessionListener listener) {
        if (currentSession != null) {
            currentSessionC.removeConferenceSessionListener(listener);
        }
    }



    public boolean currentSessionExist() {
        return currentSession != null;
    }

    public ArrayList<Integer> getOpponentsIDsList() {
        return opponentsIDsList;
    }

    public ArrayList<Integer> getActivePublishers() {
        return new ArrayList<>(currentSessionC.getActivePublishers());
    }

    public void getOnlineParticipants(ConferenceEntityCallback<Map<Integer, Boolean>> callback) {
        if (currentSession != null) {
            currentSessionC.getOnlineParticipants(new ConferenceEntityCallback<Map<Integer, Boolean>>() {
                @Override
                public void onSuccess(Map<Integer, Boolean> integerBooleanMap) {
                    onlineParticipants = integerBooleanMap;
                    callback.onSuccess(integerBooleanMap);
                }

                @Override
                public void onError(WsException e) {
                    callback.onError(e);
                }
            });
        }
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public boolean isListenerRole() {
        return asListenerRole;
    }


    public void setAudioEnabled(boolean enabled) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null
                && currentSession.getMediaStreamManager().getLocalAudioTrack() != null) {
            currentSession.getMediaStreamManager().getLocalAudioTrack().setEnabled(enabled);
        }
        if (currentSession != null) {
            currentSession.getMediaStreamManager().getLocalAudioTrack().setEnabled(enabled);
        }
    }

    public boolean isAudioEnabledForUser(Integer userID) {
        if (currentSession.getMediaStreamManager() != null) {
            boolean isAudioEnabled = true;
            try {
                isAudioEnabled = currentSession.getMediaStreamManager().getAudioTrack(userID).enabled();
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.d(TAG, e.getMessage());
                }
            }
            return isAudioEnabled;
        } else {
            return false;
        }
    }

    public void setAudioEnabledForUser(Integer userID, boolean isAudioEnabled) {
        currentSession.getMediaStreamManager().getAudioTrack(userID).setEnabled(isAudioEnabled);
    }


    public boolean isSharingScreenState() {
        return sharingScreenState;
    }


    private void removeVideoTrackRenders() {
        Log.d(TAG, "removeVideoTrackRenders");
        if (!videoTrackMap.isEmpty()) {
            for (Map.Entry<Integer, QBRTCVideoTrack> entry : videoTrackMap.entrySet()) {
                Integer userId = (Integer) entry.getKey();
                QBRTCVideoTrack videoTrack = (QBRTCVideoTrack) entry.getValue();
                Integer currentUserID = currentSessionC.getCurrentUserID();
                boolean remoteVideoTrack = !userId.equals(currentUserID);
                if (remoteVideoTrack) {
                    VideoSink renderer = videoTrack.getRenderer();
                    if (renderer != null) {
                        videoTrack.removeRenderer(renderer);
                    }
                }
            }
        }
    }

    public void startScreenSharing(Intent data) {
        sharingScreenState = true;
        if (data != null && currentSession != null) {
            currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCScreenCapturer(data, null));
            setVideoEnabled(true);
        }
        sharingScreenState = true;
        if (currentSession != null) {
            currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCScreenCapturer(data, null));
        }
    }

    public void stopScreenSharing() {
        sharingScreenState = false;
        if (currentSession != null) {
            try {
                currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCCameraVideoCapturer(this, null));
            } catch (QBRTCCameraVideoCapturer.QBRTCCameraCapturerException e) {
                Log.i(TAG, "Error: device doesn't have camera");
            }
        }
        sharingScreenState = false;
        if (currentSession != null) {
            try {
                currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCCameraVideoCapturer(this, null));
            } catch (QBRTCCameraVideoCapturer.QBRTCCameraCapturerException e) {
                Log.i(TAG, "Error: device doesn't have camera");
            }
        }
    }
    public void joinConference() {
        QBConferenceRole conferenceRole = asListenerRole ? QBConferenceRole.LISTENER : QBConferenceRole.PUBLISHER;

        currentSessionC.joinDialog(roomID, conferenceRole, new ConferenceEntityCallback<ArrayList<Integer>>() {
            @Override
            public void onSuccess(ArrayList<Integer> publishers) {
                // empty
            }

            @Override
            public void onError(WsException exception) {
                Log.d(TAG, "onError joinDialog exception= " + exception);
            }
        });
    }

    private void notifyCallStateListenersCallStarted() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStarted();
        }
    }

    public void setUsersConnectDisconnectCallback(UsersConnectDisconnectCallback callback) {
        this.usersConnectDisconnectCallback = callback;
    }

    public void removeUsersConnectDisconnectCallback() {
        this.usersConnectDisconnectCallback = null;
    }

    public String getDialogID() {
        return dialogID;
    }

    public void setDialogID(String dialogID) {
        this.dialogID = dialogID;
    }

    //////////////////////////////////////////
    //    Call Service Binder
    //////////////////////////////////////////

    public class CallServiceBinder extends Binder {
        public CallService getService() {
            return CallService.this;
        }
    }

    //////////////////////////////////////////
    //    Conference Session Callbacks
    //////////////////////////////////////////

    private class ConferenceSessionListener implements ConferenceSessionCallbacks {
        @Override
        public void onPublishersReceived(ArrayList<Integer> publishersList) {
            currentSessionC.subscribeToPublisher(publishersList.get(0));
        }

        @Override
        public void onPublisherLeft(Integer userID) {
            Log.d(TAG, "OnPublisherLeft userID" + userID);
        }

        @Override
        public void onMediaReceived(String type, boolean success) {
            Log.d(TAG, "OnMediaReceived type " + type + ", success" + success);
        }

        @Override
        public void onSlowLinkReceived(boolean uplink, int nacks) {
            Log.d(TAG, "OnSlowLinkReceived uplink " + uplink + ", nacks" + nacks);
        }

        @Override
        public void onError(WsException exception) {
            Log.d(TAG, "OnError exception= " + exception.getMessage());
            if (exception.getMessage().equals(ICE_FAILED_REASON)) {
                releaseCurrentSession();
            }
        }

        @Override
        public void onSessionClosed(ConferenceSession session) {
            if (session.equals(currentSession) && reconnectionState == ReconnectionState.IN_PROGRESS) {
                new ReconnectionTimer().reconnect();
            }
        }
    }

    private class ReconnectionTimer {
        private Timer timer = new Timer();

        private long lastDelay = 0;
        private long delay = 1000;
        private long newDelay = 0;

        void reconnect() {
            if (newDelay >= SECONDS_13) {
                reconnectionState = ReconnectionState.FAILED;
                for (ReconnectionListener reconnectionListener : reconnectionListeners) {
                    reconnectionListener.onChangedState(reconnectionState);
                }
                leaveCurrentSession();
                return;
            }
            newDelay = lastDelay + delay;
            lastDelay = delay;
            delay = newDelay;

            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ConferenceClient client = ConferenceClient.getInstance(getApplicationContext());
                    QBRTCTypes.QBConferenceType conferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
                    client.createSession(currentSessionC.getCurrentUserID(), conferenceType, new ConferenceEntityCallback<ConferenceSession>() {
                        @Override
                        public void onSuccess(ConferenceSession conferenceSession) {
                            WebRtcSessionManager.getInstance(context).getCurrentSessionC();
                            WebRtcSessionManager.getInstance(context).setCurrentSession(currentSession);
                            currentSessionC = conferenceSession;
                            initListeners();
                            timer.purge();
                            timer.cancel();
                            timer = null;
                            reconnectionState = ReconnectionState.COMPLETED;
                            for (ReconnectionListener reconnectionListener : reconnectionListeners) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    reconnectionListener.onChangedState(reconnectionState);
                                });
                            }
                        }

                        @Override
                        public void onError(WsException exception) {
                            reconnect();
                        }
                    });
                }
            }, newDelay);
        }
    }

    //////////////////////////////////////////
    //    Session State Callback
    //////////////////////////////////////////

    private class SessionStateListener implements QBRTCSessionStateCallback<ConferenceSession> {
        @Override
        public void onStateChanged(ConferenceSession conferenceSession, BaseSession.QBRTCSessionState qbrtcSessionState) {
            // empty
        }

        @Override
        public void onConnectedToUser(ConferenceSession conferenceSession, Integer userID) {
            Log.d(TAG, "onConnectedToUser userID= " + userID + " sessionID= " + conferenceSession.getSessionID());
            notifyCallStateListenersCallStarted();
            if (usersConnectDisconnectCallback != null) {
                usersConnectDisconnectCallback.onUserConnected(userID);
            }
            if (userID.equals(currentSessionC.getCurrentUserID()) && reconnectionState == ReconnectionState.IN_PROGRESS) {
                reconnectionState = ReconnectionState.COMPLETED;
                for (ReconnectionListener reconnectionListener : reconnectionListeners) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        reconnectionListener.onChangedState(reconnectionState);
                    });
                }
            }
        }

        @Override
        public void onDisconnectedFromUser(ConferenceSession conferenceSession, Integer userID) {
            if (userID.equals(currentSessionC.getCurrentUserID()) || conferenceSession.getConferenceRole() == QBConferenceRole.LISTENER) {
                reconnectionState = ReconnectionState.IN_PROGRESS;
                for (ReconnectionListener reconnectionListener : reconnectionListeners) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        reconnectionListener.onChangedState(reconnectionState);
                    });
                }
                currentSessionC.leave();
            }
        }

        @Override
        public void onConnectionClosedForUser(ConferenceSession conferenceSession, Integer userID) {
            Log.d(TAG, "QBRTCSessionStateCallbackImpl onConnectionClosedForUser userID=" + userID);
            removeVideoTrack(userID);
            if (usersConnectDisconnectCallback != null) {
                usersConnectDisconnectCallback.onUserDisconnected(userID);
            }
        }
    }

    //////////////////////////////////////////
    //    Camera Events Handler
    //////////////////////////////////////////

    /*private class SessionStateListener implements QBRTCSessionStateCallback<QBRTCSession> {
        @Override
        public void onStateChanged(QBRTCSession qbrtcSession, BaseSession.QBRTCSessionState qbrtcSessionState) {

        }

        @Override
        public void onConnectedToUser(QBRTCSession qbrtcSession, Integer integer) {
            stopRingtone();
            isCallState = true;
            Log.d(TAG, "onConnectedToUser() is started");
            startCallTimer();
        }

        @Override
        public void onDisconnectedFromUser(QBRTCSession qbrtcSession, Integer userID) {
            Log.d(TAG, "Disconnected from user: " + userID);
        }

        @Override
        public void onConnectionClosedForUser(QBRTCSession qbrtcSession, Integer userID) {
            if (userID != null) {
                Log.d(TAG, "Connection closed for user: " + userID);
                ToastUtils.shortToast("The user: " + userID + " has left the call");
                removeVideoTrack(userID);
            }
        }
    }*/
    private class CameraEventsListener implements CameraVideoCapturer.CameraEventsHandler {
        @Override
        public void onCameraError(String s) {
            // empty
        }

        @Override
        public void onCameraDisconnected() {
            // empty
        }

        @Override
        public void onCameraFreezed(String s) {
          ToastUtils.shortToast("Camera Freezed");
            hangUpCurrentSession(new HashMap<>());
            if (currentSession != null) {
                try {
                    currentSession.getMediaStreamManager().getVideoCapturer().stopCapture();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int videoWidth = QBRTCMediaConfig.VideoQuality.VGA_VIDEO.width;
                        int videoHeight = QBRTCMediaConfig.VideoQuality.VGA_VIDEO.height;
                        if (currentSession != null && currentSession.getMediaStreamManager() != null
                                && currentSession.getMediaStreamManager().getVideoCapturer() != null) {
                            currentSession.getMediaStreamManager().getVideoCapturer().startCapture(videoWidth, videoHeight, 30);
                        }
                    }
                }, 3000);
            }
        }

        @Override
        public void onCameraOpening(String s) {
            ToastUtils.shortToast("Camera Opening");
        }

        @Override
        public void onFirstFrameAvailable() {
            ToastUtils.shortToast("Camera onFirstFrameAvailable");
        }

        @Override
        public void onCameraClosed() {
            ToastUtils.shortToast("Camera Closed");
        }
    }



    private class VideoTrackListener implements QBRTCClientVideoTracksCallbacks<ConferenceSession> {
        @Override
        public void onLocalVideoTrackReceive(ConferenceSession qbrtcSession, QBRTCVideoTrack videoTrack) {
            Log.d(TAG, "onLocalVideoTrackReceive()");
            if (videoTrack != null) {
                int userID = currentSessionC.getCurrentUserID();
                removeVideoTrack(userID);
                addVideoTrack(userID, videoTrack);
            }
        }

        @Override
        public void onRemoteVideoTrackReceive(ConferenceSession session, QBRTCVideoTrack videoTrack, Integer userID) {
            Log.d(TAG, "onRemoteVideoTrackReceive for Opponent= " + userID);
            if (videoTrack != null && userID != null) {
                addVideoTrack(userID, videoTrack);
            }
        }
    }






    //////////////////////////////////////////
    //    Video Tracks Callbacks
    //////////////////////////////////////////



    private class AudioTrackListener implements QBRTCClientAudioTracksCallback<ConferenceSession> {
        @Override
        public void onLocalAudioTrackReceive(ConferenceSession conferenceSession, QBRTCAudioTrack qbrtcAudioTrack) {
            Log.d(TAG, "onLocalAudioTrackReceive()");
            boolean isMicEnabled = ((AppConference) getApplicationContext()).getSharedPrefsHelper().get(Consts.PREF_MIC_ENABLED, true);
            currentSession.getMediaStreamManager().getLocalAudioTrack().setEnabled(isMicEnabled);
        }

        @Override
        public void onRemoteAudioTrackReceive(ConferenceSession conferenceSession, QBRTCAudioTrack qbrtcAudioTrack, Integer userID) {
            Log.d(TAG, "onRemoteAudioTrackReceive for Opponent= " + userID);
        }
    }

    private class OnlineParticipantsCheckerCountdown extends CountDownTimer {

        OnlineParticipantsCheckerCountdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getOnlineParticipants(new ConferenceEntityCallback<Map<Integer, Boolean>>() {
                @Override
                public void onSuccess(Map<Integer, Boolean> integerBooleanMap) {
                    if (onlineParticipantsChangeListener != null) {
                        onlineParticipantsChangeListener.onParticipantsCountChanged(onlineParticipants);
                    }
                    onlineParticipants = integerBooleanMap;
                }

                @Override
                public void onError(WsException exception) {
                    if (exception != null) {
                        Log.d(TAG, "Error Getting Online Participants - " + exception.getMessage());
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            start();
        }
    }


    //Listeners
    public void addConnectionListener(ConnectionListener connectionListener) {
        QBChatService.getInstance().addConnectionListener(connectionListener);
    }

    public void removeConnectionListener(ConnectionListener connectionListener) {
        QBChatService.getInstance().removeConnectionListener(connectionListener);
    }

    public void addSessionStateListener(QBRTCSessionStateCallback callback) {
        if (currentSession != null) {
            currentSession.addSessionCallbacksListener(callback);
        }
    }

    public void removeSessionStateListener(QBRTCSessionStateCallback callback) {
        if (currentSession != null) {
            currentSession.removeSessionCallbacksListener(callback);
        }
    }

    public void addVideoTrackListener(QBRTCClientVideoTracksCallbacks callback) {
        if (currentSession != null) {
            currentSession.addVideoTrackCallbacksListener(callback);
        }

    }


    public void removeVideoTrackListener(QBRTCClientVideoTracksCallbacks<QBRTCSession> callback) {
        if (currentSession != null) {
            currentSession.removeVideoTrackCallbacksListener(callback);
        }

    }

    public void addSignalingListener(QBRTCSignalingCallback callback) {
        if (currentSession != null) {
            currentSession.addSignalingCallback(callback);
        }
    }

    public void removeSignalingListener(QBRTCSignalingCallback callback) {
        if (currentSession != null) {
            currentSession.removeSignalingCallback(callback);
        }
    }

    public void addSessionEventsListener(QBRTCSessionEventsCallback callback) {
        rtcClient.addSessionCallbacksListener(callback);
    }

    public void removeSessionEventsListener(QBRTCSessionEventsCallback callback) {
        rtcClient.removeSessionsCallbacksListener(callback);
    }

    // Common methods
    public void acceptCall(Map<String, String> userInfo) {
        if (currentSession != null) {
            currentSession.acceptCall(userInfo);
        }
    }

    public void startCall(Map<String, String> userInfo) {
        if (currentSession != null) {
            currentSession.startCall(userInfo);
        }
    }

    public void rejectCurrentSession(Map<String, String> userInfo) {
        if (currentSession != null) {
            currentSession.rejectCall(userInfo);
        }
    }

    public boolean hangUpCurrentSession(Map<String, String> userInfo) {
        stopRingtone();
        boolean result = false;
        if (currentSession != null) {
            currentSession.hangUp(userInfo);
            result = true;
        }
        return result;
    }



    public Integer getCallerId() {
        if (currentSession != null) {
            return currentSession.getCallerID();
        } else {
            return null;
        }
    }

    public List<Integer> getOpponents() {
        if (currentSession != null) {
            return currentSession.getOpponents();
        } else {
            return null;
        }
    }

    public boolean isVideoCall() {
        return QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO == currentSession.getConferenceType();
    }

    public void setVideoEnabled(boolean videoEnabled) {
        QBMediaStreamManager streamManager = currentSession.getMediaStreamManager();
        QBRTCVideoTrack videoTrack = streamManager.getLocalVideoTrack();
        if (currentSession != null && videoTrack != null) {
            videoTrack.setEnabled(videoEnabled);
        }
    }

    public BaseSession.QBRTCSessionState getCurrentSessionState() {
        return currentSession != null ? currentSession.getState() : null;
    }

    public boolean isMediaStreamManagerExist() {
        return currentSession != null && currentSession.getMediaStreamManager() != null;
    }

    public QBRTCTypes.QBRTCConnectionState getPeerChannel(Integer userID) {
        QBRTCTypes.QBRTCConnectionState result = null;
        if (currentSession != null && currentSession.getPeerConnection(userID) != null) {
            result = currentSession.getPeerConnection(userID).getState();
        }
        return result;
    }

    public boolean isCurrentSession(QBRTCSession session) {
        boolean isCurrentSession = false;
        if (session != null && currentSession != null) {
            isCurrentSession = currentSession.getSessionID().equals(session.getSessionID());
        }
        return isCurrentSession;
    }

    public void switchCamera(CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            QBRTCCameraVideoCapturer videoCapturer = (QBRTCCameraVideoCapturer) currentSession.getMediaStreamManager().getVideoCapturer();
            videoCapturer.switchCamera(cameraSwitchHandler);
        }
    }

    public boolean isCameraFront() {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            QBRTCCameraVideoCapturer videoCapturer = (QBRTCCameraVideoCapturer) currentSession.getMediaStreamManager().getVideoCapturer();
            return videoCapturer.getCameraName().contains("front");
        }
        return true;
    }

    public void switchAudio() {
        Log.v(TAG, "onSwitchAudio(), SelectedAudioDevice() = " + appRTCAudioManager.getSelectedAudioDevice());
        if (appRTCAudioManager.getSelectedAudioDevice() != AppRTCAudioManager.AudioDevice.SPEAKER_PHONE) {
            appRTCAudioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
        } else {
            if (appRTCAudioManager.getAudioDevices().contains(AppRTCAudioManager.AudioDevice.WIRED_HEADSET)) {
                appRTCAudioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET);
            } else if (appRTCAudioManager.getAudioDevices().contains(AppRTCAudioManager.AudioDevice.WIRED_HEADSET)) {
                appRTCAudioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET);
            } else {
                appRTCAudioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
            }
        }
    }


    public boolean isCallMode() {
        return isCallState;
    }

    public HashMap<Integer, QBRTCVideoTrack> getVideoTrackMap() {
        return videoTrackMap;
    }

    private void addVideoTrack(Integer userId, QBRTCVideoTrack videoTrack) {
        videoTrackMap.put(userId, videoTrack);
    }

    public QBRTCVideoTrack getVideoTrack(Integer userId) {
        return videoTrackMap.get(userId);
    }


    private void removeVideoTrack(int userId) {
        QBRTCVideoTrack videoTrack = getVideoTrack(userId);
        if (videoTrack != null) {
            VideoSink renderer = videoTrack.getRenderer();
            videoTrack.removeRenderer(renderer);
        }
        videoTrackMap.remove(userId);
    }

    public void setCallTimerCallback(CallTimerListener callback) {
        callTimerListener = callback;
    }

    public void removeCallTimerCallback() {
        callTimerListener = null;
    }

    private void startCallTimer() {
        if (callTime == null) {
            callTime = 1000L;
        }
        if (!callTimerTask.isRunning()) {
            callTimer.scheduleAtFixedRate(callTimerTask, 0, 1000L);
        }
    }

    private void stopCallTimer() {
        callTimerListener = null;

        callTimer.cancel();
        callTimer.purge();
    }

    public void clearButtonsState() {
        SharedPrefsHelper.getInstance().delete(BaseConversationFragment.MIC_ENABLED);
        SharedPrefsHelper.getInstance().delete(AudioConversationFragment.SPEAKER_ENABLED);
        SharedPrefsHelper.getInstance().delete(VideoConversationFragment.CAMERA_ENABLED);
        SharedPrefsHelper.getInstance().delete(VideoConversationFragment.IS_CURRENT_CAMERA_FRONT);
    }

    public void clearCallState() {
        SharedPrefsHelper.getInstance().delete(Consts.EXTRA_IS_INCOMING_CALL);
    }

    private class CallTimerTask extends TimerTask {
        private boolean isRunning = false;

        @Override
        public void run() {
            isRunning = true;
            callTime = callTime + 1000L;
            Notification notification = initNotification();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(SERVICE_ID, notification);
            }

            if (callTimerListener != null) {
                String callTime = getCallTime();
                if (!TextUtils.isEmpty(callTime)) {
                    callTimerListener.onCallTimeUpdate(callTime);
                }
            }
        }

        public boolean isRunning() {
            return isRunning;
        }
    }



    private class ConnectionListenerImpl extends AbstractConnectionListener {
        @Override
        public void connectionClosedOnError(Exception e) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int reconnectHangUpTimeMillis = SettingsUtil.getPreferenceInt(sharedPref,
                    getApplicationContext(), R.string.pref_disconnect_time_interval_key,
                    R.string.pref_disconnect_time_interval_default_value) * 1000;
            expirationReconnectionTime = System.currentTimeMillis() + reconnectHangUpTimeMillis;
        }

        @Override
        public void reconnectionSuccessful() {

        }

        @Override
        public void reconnectingIn(int seconds) {
            Log.i(TAG, "reconnectingIn " + seconds);
            if (!isCallState && expirationReconnectionTime < System.currentTimeMillis()) {
                hangUpCurrentSession(new HashMap<>());
            }
        }
    }

    private class SessionEventsListener implements QBRTCClientSessionCallbacks {
        @Override
        public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {

        }

        @Override
        public void onSessionStartClose(QBRTCSession session) {
            if (session == WebRtcSessionManager.getInstance(getApplicationContext()).getCurrentSession()) {
                CallService.stop(getApplicationContext());
            }
        }

        @Override
        public void onReceiveHangUpFromUser(QBRTCSession session, Integer userID, Map<String, String> map) {
            stopRingtone();
            if (session == WebRtcSessionManager.getInstance(getApplicationContext()).getCurrentSession()) {
                Log.d(TAG, "Initiator HangUp the Call");

                int numberOpponents = session.getOpponents().size();
                if (numberOpponents == ONE_OPPONENT) {
                    if (userID.equals(session.getCallerID()) && currentSession != null) {
                        currentSession.hangUp(new HashMap<>());
                    }
                } else {
                    removeVideoTrack(userID);
                }

                QBUser participant = QbUsersDbManager.getInstance(getApplicationContext()).getUserById(userID);
                String participantName = participant != null ? participant.getFullName() : userID.toString();
                ToastUtils.shortToast("User " + participantName + " " + getString(R.string.text_status_hang_up) + " conversation");
            }
        }

        @Override
        public void onCallAcceptByUser(QBRTCSession session, Integer userID, Map<String, String> map) {
            stopRingtone();
            if (session != WebRtcSessionManager.getInstance(getApplicationContext()).getCurrentSession()) {
                return;
            }
        }

        @Override
        public void onReceiveNewSession(QBRTCSession session) {
            Log.d(TAG, "Session " + session.getSessionID() + " are Income");
            if (WebRtcSessionManager.getInstance(getApplicationContext()).getCurrentSession() != null) {
                session.rejectCall(null);
            }
        }

        @Override
        public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {
            ToastUtils.longToast("Call was stopped by UserNoActions timer");
            clearCallState();
            clearButtonsState();
            WebRtcSessionManager.getInstance(getApplicationContext()).setCurrentSession(null);
            CallService.stop(CallService.this);
        }

        @Override
        public void onSessionClosed(QBRTCSession session) {
            stopRingtone();
            Log.d(TAG, "Session " + session.getSessionID() + " onSessionClosed()");
            if (session == currentSession) {
                Log.d(TAG, "Stopping Session");
                CallService.stop(CallService.this);
            }
        }

        @Override
        public void onCallRejectByUser(QBRTCSession session, Integer integer, Map<String, String> map) {
            stopRingtone();
            if (session == WebRtcSessionManager.getInstance(getApplicationContext()).getCurrentSession()) {
                return;
            }
        }
    }



    private class QBRTCSignalingListener implements QBRTCSignalingCallback {
        @Override
        public void onSuccessSendingPacket(QBSignalingSpec.QBSignalCMD qbSignalCMD, Integer integer) {

        }

        @Override
        public void onErrorSendingPacket(QBSignalingSpec.QBSignalCMD qbSignalCMD, Integer integer, QBRTCSignalException e) {
            ToastUtils.shortToast(R.string.dlg_signal_error);
        }
    }

    private class NetworkConnectionListener implements NetworkConnectionChecker.OnConnectivityChangedListener {

        @Override
        public void connectivityChanged(boolean availableNow) {
            ToastUtils.shortToast("Internet Connection " + (availableNow ? "Available" : "Unavailable"));
        }
    }

    public interface CallTimerListener {
        void onCallTimeUpdate(String time);
    }

    /*private class ConferenceSessionListener implements ConferenceSessionCallbacks {
        @Override
        public void onPublishersReceived(ArrayList<Integer> publishersList) {
            currentSessionC.subscribeToPublisher(publishersList.get(0));
        }

        @Override
        public void onPublisherLeft(Integer userID) {
            Log.d(TAG, "OnPublisherLeft userID" + userID);
        }

        @Override
        public void onMediaReceived(String type, boolean success) {
            Log.d(TAG, "OnMediaReceived type " + type + ", success" + success);
        }

        @Override
        public void onSlowLinkReceived(boolean uplink, int nacks) {
            Log.d(TAG, "OnSlowLinkReceived uplink " + uplink + ", nacks" + nacks);
        }

        @Override
        public void onError(WsException exception) {
            Log.d(TAG, "OnError exception= " + exception.getMessage());
            if (exception.getMessage().equals(ICE_FAILED_REASON)) {
                releaseCurrentSession();
            }
        }

        @Override
        public void onSessionClosed(ConferenceSession session) {
            if (session.equals(currentSession) && reconnectionState == ReconnectionState.IN_PROGRESS) {
                new ReconnectionTimer().reconnect();
            }
        }
    }
    private class ReconnectionTimer {
        private Timer timer = new Timer();

        private long lastDelay = 0;
        private long delay = 1000;
        private long newDelay = 0;

        void reconnect() {
            if (newDelay >= SECONDS_13) {
                reconnectionState = ReconnectionState.FAILED;
                for (ReconnectionListener reconnectionListener : reconnectionListeners) {
                    reconnectionListener.onChangedState(reconnectionState);
                }
                leaveCurrentSession();
                return;
            }
            newDelay = lastDelay + delay;
            lastDelay = delay;
            delay = newDelay;

            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ConferenceClient client = ConferenceClient.getInstance(getApplicationContext());
                    QBRTCTypes.QBConferenceType conferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
                    client.createSession(currentSessionC.getCurrentUserID(), conferenceType, new ConferenceEntityCallback<ConferenceSession>() {
                        @Override
                        public void onSuccess(ConferenceSession conferenceSession) {
                            WebRtcSessionManager.getInstance().setCurrentSession(conferenceSession);
                            currentSessionC = conferenceSession;
                            initListeners();
                            timer.purge();
                            timer.cancel();
                            timer = null;
                            reconnectionState = ReconnectionState.COMPLETED;
                            for (ReconnectionListener reconnectionListener : reconnectionListeners) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    reconnectionListener.onChangedState(reconnectionState);
                                });
                            }
                        }

                        @Override
                        public void onError(WsException exception) {
                            reconnect();
                        }
                    });
                }
            }, newDelay);
        }
    }*/

    private void initConferenceClient() {
        conferenceClient = ConferenceClient.getInstance(this);
        conferenceClient.setCameraErrorHandler(new CameraEventsListener());
        QBRTCConfig.setDebugEnabled(true);


    }


    private void addAudioTrackListener(QBRTCClientAudioTracksCallback callback) {
        if (currentSession != null) {
            currentSession.addAudioTrackCallbacksListener(callback);
        }
    }

    public void removeAudioTrackListener(QBRTCClientAudioTracksCallback callback) {
        if (currentSession != null) {
            currentSession.removeAudioTrackCallbacksListener(callback);
        }
    }


    //////////////////////////////////////////
    //    Camera Events Handler
    //////////////////////////////////////////


    //////////////////////////////////////////
    //    Video Tracks Callbacks
    //////////////////////////////////////////


    /*private class AudioTrackListener implements QBRTCClientAudioTracksCallback<ConferenceSession> {
        @Override
        public void onLocalAudioTrackReceive(ConferenceSession conferenceSession, QBRTCAudioTrack qbrtcAudioTrack) {
            Log.d(TAG, "onLocalAudioTrackReceive()");
            boolean isMicEnabled = ((App) getApplicationContext()).getSharedPrefsHelper().get(Consts.PREF_MIC_ENABLED, true);
            currentSession.getMediaStreamManager().getLocalAudioTrack().setEnabled(isMicEnabled);
        }

        @Override
        public void onRemoteAudioTrackReceive(ConferenceSession conferenceSession, QBRTCAudioTrack qbrtcAudioTrack, Integer userID) {
            Log.d(TAG, "onRemoteAudioTrackReceive for Opponent= " + userID);
        }
    }

    private class OnlineParticipantsCheckerCountdown extends CountDownTimer {

        OnlineParticipantsCheckerCountdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getOnlineParticipants(new ConferenceEntityCallback<Map<Integer, Boolean>>() {
                @Override
                public void onSuccess(Map<Integer, Boolean> integerBooleanMap) {
                    if (onlineParticipantsChangeListener != null) {
                        onlineParticipantsChangeListener.onParticipantsCountChanged(onlineParticipants);
                    }
                    onlineParticipants = integerBooleanMap;
                }

                @Override
                public void onError(WsException exception) {
                    if (exception != null) {
                        Log.d(TAG, "Error Getting Online Participants - " + exception.getMessage());
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            start();
        }
    }*/

    public interface CurrentCallStateCallback extends CallActivity.CurrentCallStateCallback {
        void onCallStarted();

        @Override
        void onCallStopped();

        @Override
        void onOpponentsListUpdated(ArrayList<QBUser> newUsers);

        @Override
        void onCallTimeUpdate(String time);
    }

    public interface UsersConnectDisconnectCallback {
        void onUserConnected(Integer userID);

        void onUserDisconnected(Integer userID);
    }

    public interface OnlineParticipantsChangeListener {
        void onParticipantsCountChanged(Map<Integer, Boolean> onlineParticipants);
    }

    public interface ReconnectionListener {
        void onChangedState(ReconnectionState reconnectionState);
    }

    public enum ReconnectionState {
        COMPLETED,
        IN_PROGRESS,
        FAILED,
        DEFAULT
    }



}