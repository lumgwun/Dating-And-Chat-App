package com.lahoriagency.cikolive;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.CallService;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.ErrorUtils;
import com.lahoriagency.cikolive.Classes.FragmentExecuotr;
import com.lahoriagency.cikolive.Classes.LoginService;
import com.lahoriagency.cikolive.Classes.PermissionsChecker;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.QbUsersDbManager;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SettingsUtil;
import com.lahoriagency.cikolive.Classes.SettingsUtils;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.UsersUtils;
import com.lahoriagency.cikolive.Classes.WebRtcSessionManager;
import com.lahoriagency.cikolive.Fragments.AudioConversationFragment;
import com.lahoriagency.cikolive.Fragments.ConversationFragment;
import com.lahoriagency.cikolive.Fragments.IncomeCallFragment;
import com.lahoriagency.cikolive.Fragments.ScreenShareFragment;
import com.lahoriagency.cikolive.Fragments.VideoConversationFragment;
import com.lahoriagency.cikolive.Interfaces.ConversationFragmentCallback;
import com.lahoriagency.cikolive.Interfaces.IncomeCallFragmentCallbackListener;
import com.lahoriagency.cikolive.Interfaces.ReconnectionCallback;
import com.lahoriagency.cikolive.Video_And_Call.BaseConversationFragment;
import com.lahoriagency.cikolive.Video_And_Call.OpponentsActivity;
import com.lahoriagency.cikolive.Video_And_Call.PermissionsActivity;
import com.quickblox.chat.QBChatService;
import com.quickblox.conference.ConferenceSession;
import com.quickblox.conference.callbacks.ConferenceSessionCallbacks;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCScreenCapturer;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionListener;
import org.webrtc.CameraVideoCapturer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lahoriagency.cikolive.Classes.CallService.ONE_OPPONENT;
import static com.lahoriagency.cikolive.Classes.CallService.ReconnectionState.COMPLETED;
import static com.lahoriagency.cikolive.Classes.CallService.ReconnectionState.FAILED;
import static com.lahoriagency.cikolive.Classes.CallService.ReconnectionState.IN_PROGRESS;
import static com.quickblox.videochat.webrtc.BaseSession.QBRTCSessionState.QB_RTC_SESSION_PENDING;


@SuppressWarnings("deprecation")
public class CallActivity extends BaseActivity implements IncomeCallFragmentCallbackListener,
        QBRTCSessionStateCallback<ConferenceSession>, QBRTCClientSessionCallbacks,
        ConversationFragmentCallback, ScreenShareFragment.OnSharingEvents {
    private static final String TAG = CallActivity.class.getSimpleName();

    public static final String INCOME_CALL_FRAGMENT = "income_call_fragment";
    public static final int REQUEST_PERMISSION_SETTING = 545;
    private static final int REQUEST_CODE_OPEN_CONVERSATION_CHAT = 300;

    private ArrayList<CurrentCallStateCallback> currentCallStateCallbackList = new ArrayList<>();
    private QbUsersDbManager dbManager = QbUsersDbManager.getInstance(this);
    private Handler showIncomingCallWindowTaskHandler;
    private ConferenceSession currentSession;
    private QBRTCSession qbrtcSession;
    private ConnectionListenerImpl connectionListener;
    private ServiceConnection callServiceConnection;
    private Runnable showIncomingCallWindowTask;
    private boolean isInComingCall = false;
    private List<Integer> opponentsIdsList;
    private SharedPreferences sharedPref;
    private boolean isVideoCall = false;
    private PermissionsChecker checker;
    private CallService callService;
    private SharedPrefsHelper sharedPrefsHelper;
    private QBResRequestExecutor requestExecutor = new QBResRequestExecutor();
    private FloatingActionButton fab;
    ConferenceSessionCallbacks conferenceSessionCallbacks;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    private SharedPreferences settingsSharedPref;
    private String currentDialogID;
    private String currentRoomID;
    private String currentRoomTitle;

    private volatile boolean connectedToJanus;
    private final Set<ReconnectionCallback> reconnectionCallbacks = new HashSet<>();
    private final ReconnectionListenerImpl reconnectionListener = new ReconnectionListenerImpl(TAG);
    private LinearLayout reconnectingLayout;
    private String dialogID;
    private static final String ICE_FAILED_REASON = "ICE failed";
    private static final int REQUEST_CODE_MANAGE_GROUP = 132;

    public static void start(Context context, String roomID, String roomTitle, String dialogID,
                             List<Integer> occupants, boolean listenerRole) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(Consts.EXTRA_ROOM_ID, roomID);
        intent.putExtra(Consts.EXTRA_ROOM_TITLE, roomTitle);
        intent.putExtra(Consts.EXTRA_DIALOG_ID, dialogID);
        intent.putExtra(Consts.EXTRA_DIALOG_OCCUPANTS, (Serializable) occupants);
        intent.putExtra(Consts.EXTRA_AS_LISTENER, listenerRole);

        context.startActivity(intent);
        CallService.start(context, roomID, roomTitle, dialogID, occupants, listenerRole);
    }
    @Override
    public void addReconnectionCallback(ReconnectionCallback reconnectionCallback) {
        reconnectionCallbacks.add(reconnectionCallback);
    }

    @Override
    public void removeReconnectionCallback(ReconnectionCallback reconnectionCallback) {
        reconnectionCallbacks.remove(reconnectionCallback);
    }


    public static void start(Context context, boolean isIncomingCall) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);
        SharedPrefsHelper.getInstance().save(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);
        context.startActivity(intent);
        CallService.start(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checker = new PermissionsChecker(this);
        sharedPrefsHelper= new SharedPrefsHelper();
        currentRoomID = getIntent().getStringExtra(Consts.EXTRA_ROOM_ID);
        currentDialogID = getIntent().getStringExtra(Consts.EXTRA_DIALOG_ID);
        currentRoomTitle = getIntent().getStringExtra(Consts.EXTRA_ROOM_TITLE);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_video, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_audio, false);

        reconnectingLayout = findViewById(R.id.llReconnecting);

        Window w = getWindow();
        w.setStatusBarColor(ContextCompat.getColor(this, R.color.color_new_blue));

        // TODO: To set fullscreen style in a call:
        //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void initScreen() {
        SettingsUtils.setSettingsStrategy(settingsSharedPref, CallActivity.this);

        WebRtcSessionManager sessionManager = WebRtcSessionManager.getInstance(this);
        if (sessionManager.getCurrentSession() == null) {
            //we have already currentSession == null, so it's no reason to do further initialization
            finish();
            return;
        }
        qbrtcSession = sessionManager.getCurrentSession();
        initListeners(currentSession);

        if (callService != null && callService.isSharingScreenState()) {
            if (callService.getReconnectionState() == COMPLETED) {
                QBRTCScreenCapturer.requestPermissions(this);
            } else {
                startScreenSharing(null);
            }
        } else {
            startConversationFragment();
        }
        callService.setCallTimerCallback(new CallTimerCallback());
        isVideoCall = callService.isVideoCall();
        opponentsIdsList = callService.getOpponents();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefsHelper= new SharedPrefsHelper();

        initSettingsStrategy();
        addListeners();

        if (getIntent() != null && getIntent().getExtras() != null) {
            isInComingCall = getIntent().getExtras().getBoolean(Consts.EXTRA_IS_INCOMING_CALL, false);
        } else {
            isInComingCall = sharedPrefsHelper.get(Consts.EXTRA_IS_INCOMING_CALL, false);
        }

        if (callService.isCallMode()) {
            checkPermission();
            if (callService.isSharingScreenState()) {
                startScreenSharing(null);
                return;
            }
            addConversationFragment(isInComingCall);
        } else {
            if (!isInComingCall) {
                callService.playRingtone();
            }
            startSuitableFragment(isInComingCall);
        }
    }

    private void addListeners() {
        addSessionEventsListener(this);
        addSessionStateListener(this);

        connectionListener = new ConnectionListenerImpl();
        addConnectionListener(connectionListener);
    }



    private void bindCallService() {
        callServiceConnection = new CallServiceConnection();
        Intent intent = new Intent(this, CallService.class);
        bindService(intent, callServiceConnection, Context.BIND_AUTO_CREATE);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult requestCode=" + requestCode + ", resultCode= " + resultCode);
        if (requestCode == QBRTCScreenCapturer.REQUEST_MEDIA_PROJECTION
                && resultCode == Activity.RESULT_OK && data != null) {
            startScreenSharing(data);
            Log.i(TAG, "Starting Screen Capture");
        } else if (requestCode == QBRTCScreenCapturer.REQUEST_MEDIA_PROJECTION && resultCode == Activity.RESULT_CANCELED) {
            callService.stopScreenSharing();
            startConversationFragment();
        }
        if (requestCode == REQUEST_CODE_OPEN_CONVERSATION_CHAT) {
            Log.d(TAG, "Returning back from ChatActivity");
        }
        Log.i(TAG, "onActivityResult requestCode=" + requestCode + ", resultCode= " + resultCode);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            if (data != null) {
                boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
                if (isLoginSuccess) {
                    initScreen();
                } else {
                    CallService.stop(this);
                    finish();
                }
            }
        }
        if (requestCode == QBRTCScreenCapturer.REQUEST_MEDIA_PROJECTION
                && resultCode == Activity.RESULT_OK && data != null) {
            startScreenSharing(data);
            Log.i(TAG, "Starting Screen Capture");
        }
    }

    private void startScreenSharing(final Intent data) {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(ScreenShareFragment.class.getSimpleName());
        if (!(fragmentByTag instanceof ScreenShareFragment)) {
            ScreenShareFragment screenShareFragment = ScreenShareFragment.newInstance();
            FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, screenShareFragment, ScreenShareFragment.class.getSimpleName());

            if (data != null) {
                callService.startScreenSharing(data);
            }
        }
        ScreenShareFragment screenShareFragment = ScreenShareFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                screenShareFragment, ScreenShareFragment.class.getSimpleName())
                .commitAllowingStateLoss();

        callService.setVideoEnabled(true);
        callService.startScreenSharing(data);
        QBRTCScreenCapturer.requestPermissions(this);
    }

    private void startSuitableFragment(boolean isInComingCall) {
        sharedPrefsHelper= new SharedPrefsHelper();
        QBRTCSession session = WebRtcSessionManager.getInstance(this).getCurrentSession();
        if (session != null) {
            if (isInComingCall) {
                initIncomingCallTask();
                startLoadAbsentUsers();
                addIncomeCallFragment();
                checkPermission();
            } else {
                addConversationFragment(isInComingCall);
                getIntent().removeExtra(Consts.EXTRA_IS_INCOMING_CALL);
                sharedPrefsHelper.save(Consts.EXTRA_IS_INCOMING_CALL, false);
            }
        } else {
            finish();
        }
    }

    private void checkPermission() {
        boolean cam = SharedPrefsHelper.getInstance().get(Consts.PERMISSIONS[0], true);
        boolean mic = SharedPrefsHelper.getInstance().get(Consts.PERMISSIONS[1], true);



        if (isVideoCall && checker.lacksPermissions(Consts.PERMISSIONS)) {
            if (cam) {
                PermissionsActivity.startActivity(this, false, Consts.PERMISSIONS);
            } else {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                ErrorUtils.showSnackbar(rootView, R.string.error_permission_video, R.string.dlg_allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPermissionSystemSettings();
                    }
                });
            }
        } else if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
            if (mic) {
                PermissionsActivity.startActivity(this, true, Consts.PERMISSIONS);
            } else {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                ErrorUtils.showSnackbar(rootView, R.string.error_permission_audio, R.string.dlg_allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPermissionSystemSettings();
                    }
                });
            }
        }
    }

    private void startPermissionSystemSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    private void startLoadAbsentUsers() {
        ArrayList<QBUser> usersFromDb = dbManager.getAllUsers();
        ArrayList<Integer> allParticipantsOfCall = new ArrayList<>();

        if (opponentsIdsList != null) {
            allParticipantsOfCall.addAll(opponentsIdsList);
        }

        if (isInComingCall) {
            Integer callerID = callService.getCallerId();
            if (callerID != null) {
                allParticipantsOfCall.add(callerID);
            }
        }

        ArrayList<Integer> idsUsersNeedLoad = UsersUtils.getIdsNotLoadedUsers(usersFromDb, allParticipantsOfCall);
        if (!idsUsersNeedLoad.isEmpty()) {
            requestExecutor.loadUsersByIds(idsUsersNeedLoad, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                    dbManager.saveAllUsers(users, false);
                    notifyCallStateListenersNeedUpdateOpponentsList(users);
                }
            });
        }
    }

    private void initSettingsStrategy() {
        if (opponentsIdsList != null) {
            SettingsUtil.setSettingsStrategy(opponentsIdsList, sharedPref, this);
        }
    }

    private void initIncomingCallTask() {
        showIncomingCallWindowTaskHandler = new Handler(Looper.myLooper());
        showIncomingCallWindowTask = new Runnable() {
            @Override
            public void run() {
                ToastUtils.longToast("Call was stopped by UserNoActions timer");
                callService.clearCallState();
                callService.clearButtonsState();
                WebRtcSessionManager.getInstance(getApplicationContext()).setCurrentSession(null);
                CallService.stop(CallActivity.this);
                finish();
            }
        };
    }

    public void hangUpCurrentSession() {
        callService.stopRingtone();
        if (!callService.hangUpCurrentSession(new HashMap<>())) {
            CallService.stop(this);
            finish();
        }
    }

    private void startIncomeCallTimer(long time) {
        Log.d(TAG, "startIncomeCallTimer");
        showIncomingCallWindowTaskHandler.postAtTime(showIncomingCallWindowTask, SystemClock.uptimeMillis() + time);
    }

    private void stopIncomeCallTimer() {
        Log.d(TAG, "stopIncomeCallTimer");
        showIncomingCallWindowTaskHandler.removeCallbacks(showIncomingCallWindowTask);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindCallService();
        settingsSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        bindCallService();
        if (callService != null) {
            switch (callService.getReconnectionState()) {
                case COMPLETED:
                    reconnectingLayout.setVisibility(View.GONE);
                    for (ReconnectionCallback reconnectionCallback : reconnectionCallbacks) {
                        reconnectionCallback.completed();
                    }
                    break;
                case IN_PROGRESS:
                    reconnectingLayout.setVisibility(View.VISIBLE);
                    for (ReconnectionCallback reconnectionCallback : reconnectionCallbacks) {
                        reconnectionCallback.inProgress();
                    }
                    break;
                case FAILED:

                    callService.leaveCurrentSession();
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(callServiceConnection);
        if (callService != null) {
            removeListeners();
        }
        if (callServiceConnection != null) {
            unbindService(callServiceConnection);
            callServiceConnection = null;
        }

        callService.unsubscribeReconnectionListener(reconnectionListener);
        removeListeners();
    }

    @Override
    public void finish() {
        if (callService != null) {
            dialogID = callService.getDialogID();
        }
        DialogsActivity.start(this, dialogID);
        Log.d(TAG, "Starting Dialogs Activity to open dialog with ID : " + dialogID);

        Log.d(TAG, "finish CallActivity");
        CallService.stop(this);
        OpponentsActivity.start(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        // To prevent returning from Call Fragment
    }

    private void leaveCurrentSession() {
        callService.leaveCurrentSession();
        finish();
    }

    private void initListeners(ConferenceSession session) {
        if (session != null) {
            Log.d(TAG, "Init new ConferenceSession");
            this.currentSession.addSessionCallbacksListener(CallActivity.this);
            this.currentSession.addConferenceSessionListener(conferenceSessionCallbacks);
        }
    }

    private void releaseCurrentSession() {
        Log.d(TAG, "Release current session");
        if (currentSession != null) {
            leaveCurrentSession();
            removeListeners();
            this.currentSession = null;
        }
    }

    private void removeListeners() {
        if (currentSession != null) {
            this.currentSession.removeSessionCallbacksListener(CallActivity.this);
            this.currentSession.removeConferenceSessionListener(conferenceSessionCallbacks);
        }
        removeSessionEventsListener(this);
        removeSessionStateListener(this);
        removeConnectionListener(connectionListener);
        callService.removeCallTimerCallback();
    }

    private void startConversationFragment() {
        ArrayList<Integer> opponentsIDsList = callService.getOpponentsIDsList();
        boolean asListenerRole = callService.isListenerRole();
        boolean sharingScreenState = callService.isSharingScreenState();
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(Consts.EXTRA_DIALOG_OCCUPANTS, opponentsIDsList);
        bundle.putBoolean(Consts.EXTRA_AS_LISTENER, asListenerRole);
        bundle.putBoolean("from_screen_sharing", sharingScreenState);
        bundle.putString(Consts.EXTRA_ROOM_TITLE, currentRoomTitle);
        bundle.putString(Consts.EXTRA_ROOM_ID, currentRoomID);
        ConversationFragment conversationFragment = new ConversationFragment();
        conversationFragment.setArguments(bundle);
        callService.setOnlineParticipantsChangeListener(conversationFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                conversationFragment, conversationFragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public void addClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks) {
        if (currentSession != null) {
            currentSession.addSessionCallbacksListener(clientConnectionCallbacks);
        }
    }


    @Override
    public void onLeaveCurrentSession() {
        leaveCurrentSession();
    }

    @Override
    public void onSwitchCamera(CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler) {
        callService.switchCamera(cameraSwitchHandler);
    }

    @Override
    public void onStartJoinConference() {
        callService.joinConference();
    }



    @Override
    public boolean isScreenSharingState() {
        return callService.isSharingScreenState();
    }



    @Override
    public void onReturnToChat() {
        ChatAct.startForResultFromCall(CallActivity.this, REQUEST_CODE_OPEN_CONVERSATION_CHAT,
                callService.getDialogID(), true);
    }

    @Override
    public void onManageGroup() {
        ArrayList<Integer> publishers = callService.getActivePublishers();
        publishers.add(0, getChatHelper().getCurrentUser().getId());

        ManageGroupActivity.startForResult(CallActivity.this, REQUEST_CODE_MANAGE_GROUP,
                currentRoomTitle, publishers);
    }

    @Override
    public String getDialogID() {
        return callService.getDialogID();
    }

    @Override
    public String getRoomID() {
        return callService.getRoomID();
    }

    @Override
    public String getRoomTitle() {
        return callService.getRoomTitle();
    }

    @Override
    public boolean isListenerRole() {
        return callService.isListenerRole();
    }



    @Override
    public void onStopPreview() {
        callService.stopScreenSharing();
        startConversationFragment();
        callService.stopScreenSharing();
        addConversationFragment(isInComingCall);
    }



    @Override
    public void removeClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks) {
        if (currentSession != null) {
            currentSession.removeSessionCallbacksListener(clientConnectionCallbacks);
        }
    }

    @Override
    public void addCurrentCallStateCallback(CallService.CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.add(currentCallStateCallback);
    }

    @Override
    public void removeCurrentCallStateCallback(CallService.CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.remove(currentCallStateCallback);
    }

    ////////////////////////////// ConferenceSessionCallbacks ////////////////////////////

    /*@Override
    public void onPublishersReceived(ArrayList<Integer> publishersList) {
        Log.d(TAG, "OnPublishersReceived connectedToJanus " + connectedToJanus);
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
        if (WsHangUpException.class.isInstance(exception) && exception.getMessage() != null && exception.getMessage().equals(ICE_FAILED_REASON)) {
            ToastUtils.shortToast(getApplicationContext(), exception.getMessage());
            Log.d(TAG, "OnError exception= " + exception.getMessage());
            releaseCurrentSession();
            finish();
        } else {
            ToastUtils.shortToast(getApplicationContext(), (WsNoResponseException.class.isInstance(exception)) ? getString(R.string.packet_failed) : exception.getMessage());
        }
    }

    @Override
    public void onSessionClosed(final ConferenceSession session) {
        Log.d(TAG, "Session " + session.getSessionID() + " start stop session");
    }*/
    private class CallServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            CallService.stop(CallActivity.this);

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CallService.CallServiceBinder binder = (CallService.CallServiceBinder) service;
            callService = binder.getService();
            if (callService.currentSessionExist()) {
                if (QBChatService.getInstance().isLoggedIn()) {
                    initScreen();
                } else {
                    login();
                }
            } else {
                finish();
            }
            callService = binder.getService();
            if (callService.currentSessionExist()) {
                currentDialogID = callService.getDialogID();
                if (callService.getReconnectionState() != CallService.ReconnectionState.IN_PROGRESS){
                    initScreen();
                }
            } else {
                //we have already currentSession == null, so it's no reason to do further initialization
                CallService.stop(CallActivity.this);
                finish();
            }
            callService.subscribeReconnectionListener(reconnectionListener);
        }

        private void login() {
            QBUser qbUser = SharedPrefsHelper.getInstance().getQbUser();
            Intent tempIntent = new Intent(CallActivity.this, LoginService.class);
            PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
            LoginService.start(CallActivity.this, qbUser, pendingIntent);
        }
    }



    private class ReconnectionListenerImpl implements CallService.ReconnectionListener {
        private final String tag;

        ReconnectionListenerImpl(String tag) {
            this.tag = tag;
        }

        @Override
        public void onChangedState(CallService.ReconnectionState reconnectionState) {
            switch (reconnectionState) {
                case COMPLETED:
                    reconnectingLayout.setVisibility(View.GONE);
                    for (ReconnectionCallback reconnectionCallback : reconnectionCallbacks) {
                        reconnectionCallback.completed();
                    }
                    initScreen();
                    callService.setReconnectionState(CallService.ReconnectionState.DEFAULT);
                    break;
                case IN_PROGRESS:
                    reconnectingLayout.setVisibility(View.VISIBLE);
                    for (ReconnectionCallback reconnectionCallback : reconnectionCallbacks) {
                        reconnectionCallback.inProgress();
                    }
                    break;
                case FAILED:
                    callService.leaveCurrentSession();
                    finish();
                    break;
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + tag.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            boolean equals;
            if (obj instanceof ReconnectionListenerImpl) {
                equals = TAG.equals(((ReconnectionListenerImpl) obj).tag);
            } else {
                equals = super.equals(obj);
            }
            return equals;
        }
    }

    // ---------------Chat callback methods implementation  ----------------------//

    @Override
    public void onConnectionClosedForUser(ConferenceSession session, Integer userID) {
        Log.d(TAG, "QBRTCSessionStateCallbackImpl onConnectionClosedForUser userID=" + userID);
    }

    @Override
    public void onConnectedToUser(ConferenceSession session, final Integer userID) {
        Log.d(TAG, "onConnectedToUser userID= " + userID + " sessionID= " + session.getSessionID());
    }

    @Override
    public void onStateChanged(ConferenceSession session, BaseSession.QBRTCSessionState state) {
        if (BaseSession.QBRTCSessionState.QB_RTC_SESSION_CONNECTED.equals(state)) {
            connectedToJanus = true;
            Log.d(TAG, "onStateChanged and begin subscribeToPublishersIfNeed");
        }
    }

    @Override
    public void onDisconnectedFromUser(ConferenceSession session, Integer userID) {
        Log.d(TAG, "QBRTCSessionStateCallbackImpl onDisconnectedFromUser userID=" + userID);
    }

    private void addIncomeCallFragment() {
        Log.d(TAG, "Adding IncomeCallFragment");
        if (callService.currentSessionExist()) {
            IncomeCallFragment fragment = new IncomeCallFragment();
            FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, fragment, INCOME_CALL_FRAGMENT);
        } else {
            Log.d(TAG, "SKIP Adding IncomeCallFragment");
        }
    }

    private void addConversationFragment(boolean isIncomingCall) {
        BaseConversationFragment conversationFragment = BaseConversationFragment.newInstance(
                isVideoCall
                        ? new VideoConversationFragment()
                        : new AudioConversationFragment(),
                isIncomingCall);
        FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, conversationFragment, conversationFragment.getClass().getSimpleName());
    }

    private void showNotificationPopUp(final int text, final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout connectionView = (LinearLayout) View.inflate(CallActivity.this, R.layout.connection_popup, null);
                if (show) {
                    ((TextView) connectionView.findViewById(R.id.notification)).setText(text);
                    if (connectionView.getParent() == null) {
                        ((ViewGroup) CallActivity.this.findViewById(R.id.fragment_container)).addView(connectionView);
                    }
                } else {
                    ((ViewGroup) CallActivity.this.findViewById(R.id.fragment_container)).removeView(connectionView);
                }
            }
        });
    }



    private class ConnectionListenerImpl extends AbstractConnectionListener {
        @Override
        public void connectionClosedOnError(Exception e) {
            showNotificationPopUp(R.string.connection_was_lost, true);
        }

        @Override
        public void reconnectionSuccessful() {
            showNotificationPopUp(R.string.connection_was_lost, false);
        }
    }

    /*@Override
    public void onDisconnectedFromUser(QBRTCSession session, Integer userID) {
        Log.d(TAG, "Disconnected from user: " + userID);
    }

    @Override
    public void onConnectedToUser(QBRTCSession session, final Integer userID) {
        notifyCallStateListenersCallStarted();
        if (isInComingCall) {
            stopIncomeCallTimer();
        }
        Log.d(TAG, "onConnectedToUser() is started");
    }

    @Override
    public void onConnectionClosedForUser(QBRTCSession session, Integer userID) {
        Log.d(TAG, "Connection closed for user: " + userID);
    }

    @Override
    public void onStateChanged(QBRTCSession qbrtcSession, BaseSession.QBRTCSessionState qbrtcSessionState) {

    }*/

    @Override
    public void onUserNotAnswer(QBRTCSession session, Integer userID) {
        if (callService.isCurrentSession(session)) {
            callService.stopRingtone();
        }
    }

    @Override
    public void onSessionStartClose(final QBRTCSession session) {
        if (callService.isCurrentSession(session)) {
            callService.removeSessionStateListener(this);
            notifyCallStateListenersCallStopped();
        }
    }

    @Override
    public void onReceiveHangUpFromUser(final QBRTCSession session, final Integer userID, Map<String, String> map) {
        if (callService.isCurrentSession(session)) {
            int numberOpponents = session.getOpponents().size();
            if (numberOpponents == ONE_OPPONENT) {
                hangUpCurrentSession();
                Log.d(TAG, "Initiator hung up the call");
            } else {
                if (session.getState() == QB_RTC_SESSION_PENDING && userID.equals(session.getCallerID())) {
                    hangUpCurrentSession();
                }
            }

            QBUser participant = dbManager.getUserById(userID);
            final String participantName = participant != null ? participant.getFullName() : String.valueOf(userID);
            ToastUtils.shortToast("User " + participantName + " " + getString(R.string.text_status_hang_up) + " conversation");
        }
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
        if (callService.isCurrentSession(session)) {
            callService.stopRingtone();
        }
    }

    @Override
    public void onReceiveNewSession(final QBRTCSession session) {
        Log.d(TAG, "Session " + session.getSessionID() + " Received");
    }


    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {
        startIncomeCallTimer(0);
    }

    @Override
    public void onSessionClosed(final QBRTCSession session) {
        if (callService.isCurrentSession(session)) {
            Log.d(TAG, "Stopping session");
            callService.stopForeground(true);
            finish();
        }
    }

    @Override
    public void onCallRejectByUser(QBRTCSession session, Integer userID, Map<String, String> userInfo) {
        if (callService.isCurrentSession(session)) {
            callService.stopRingtone();
        }
    }

    @Override
    public void onAcceptCurrentSession() {
        if (callService.currentSessionExist()) {
            addConversationFragment(true);
        } else {
            Log.d(TAG, "SKIP addConversationFragment method");
        }
    }

    @Override
    public void onRejectCurrentSession() {
        callService.rejectCurrentSession(new HashMap<>());
    }

    @Override
    public void addConnectionListener(ConnectionListener connectionCallback) {
        callService.addConnectionListener(connectionCallback);
    }

    @Override
    public void removeConnectionListener(ConnectionListener connectionCallback) {
        callService.removeConnectionListener(connectionCallback);
    }

    @Override
    public void addSessionStateListener(QBRTCSessionStateCallback clientConnectionCallbacks) {
        callService.addSessionStateListener(clientConnectionCallbacks);
    }

    @Override
    public void addSessionEventsListener(QBRTCSessionEventsCallback eventsCallback) {
        callService.addSessionEventsListener(eventsCallback);
    }

    @Override
    public void onSetAudioEnabled(boolean isAudioEnabled) {
        callService.setAudioEnabled(isAudioEnabled);
    }

    @Override
    public void onHangUpCurrentSession() {
        hangUpCurrentSession();
    }

    @Override
    public void onStartScreenSharing() {
        QBRTCScreenCapturer.requestPermissions(this);
    }



    @Override
    public void onSetVideoEnabled(boolean isNeedEnableCam) {
        callService.setVideoEnabled(isNeedEnableCam);
    }

    @Override
    public void onSwitchAudio() {
        callService.switchAudio();
    }

    @Override
    public void removeSessionStateListener(QBRTCSessionStateCallback clientConnectionCallbacks) {
        callService.removeSessionStateListener(clientConnectionCallbacks);
    }

    @Override
    public void removeSessionEventsListener(QBRTCSessionEventsCallback eventsCallback) {
        callService.removeSessionEventsListener(eventsCallback);
    }

    @Override
    public void addCurrentCallStateListener(CurrentCallStateCallback currentCallStateCallback) {
        if (currentCallStateCallback != null) {
            currentCallStateCallbackList.add(currentCallStateCallback);
        }
    }

    @Override
    public void removeCurrentCallStateListener(CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.remove(currentCallStateCallback);
    }

    @Override
    public void addOnChangeAudioDeviceListener(OnChangeAudioDevice onChangeDynamicCallback) {

    }

    @Override
    public void removeOnChangeAudioDeviceListener(OnChangeAudioDevice onChangeDynamicCallback) {

    }

    @Override
    public void acceptCall(Map<String, String> userInfo) {
        callService.acceptCall(userInfo);
    }

    @Override
    public void startCall(Map<String, String> userInfo) {
        callService.startCall(userInfo);
    }

    @Override
    public boolean isCameraFront() {
        return callService.isCameraFront();
    }

    @Override
    public boolean currentSessionExist() {
        return callService.currentSessionExist();
    }

    @Override
    public List<Integer> getOpponents() {
        return callService.getOpponents();
    }

    @Override
    public Integer getCallerId() {
        return callService.getCallerId();
    }

    @Override
    public void addVideoTrackListener(QBRTCClientVideoTracksCallbacks<QBRTCSession> callback) {
        callService.addVideoTrackListener(callback);
    }

    @Override
    public void removeVideoTrackListener(QBRTCClientVideoTracksCallbacks<QBRTCSession> callback) {
        callService.removeVideoTrackListener(callback);
    }

    @Override
    public BaseSession.QBRTCSessionState getCurrentSessionState() {
        return callService.getCurrentSessionState();
    }

    @Override
    public QBRTCTypes.QBRTCConnectionState getPeerChannel(Integer userId) {
        return callService.getPeerChannel(userId);
    }

    @Override
    public boolean isMediaStreamManagerExist() {
        return callService.isMediaStreamManagerExist();
    }

    @Override
    public boolean isCallState() {
        return callService.isCallMode();
    }

    @Override
    public HashMap<Integer, QBRTCVideoTrack> getVideoTrackMap() {
        return callService.getVideoTrackMap();
    }


    @Override
    public QBRTCVideoTrack getVideoTrack(Integer userId) {
        return callService.getVideoTrack(userId);
    }

    private void notifyCallStateListenersCallStarted() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStarted();
        }
    }

    private void notifyCallStateListenersCallStopped() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStopped();
        }
    }

    private void notifyCallStateListenersNeedUpdateOpponentsList(final ArrayList<QBUser> newUsers) {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onOpponentsListUpdated(newUsers);
        }
    }

    private void notifyCallStateListenersCallTime(String callTime) {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallTimeUpdate(callTime);
        }
    }


    private class CallTimerCallback implements CallService.CallTimerListener {
        @Override
        public void onCallTimeUpdate(String time) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyCallStateListenersCallTime(time);
                }
            });
        }
    }

    public interface OnChangeAudioDevice {
        void audioDeviceChanged(AppRTCAudioManager.AudioDevice newAudioDevice);
    }


    public interface CurrentCallStateCallback {
        void onCallStarted();

        void onCallStopped();

        void onOpponentsListUpdated(ArrayList<QBUser> newUsers);

        void onCallTimeUpdate(String time);
    }
}