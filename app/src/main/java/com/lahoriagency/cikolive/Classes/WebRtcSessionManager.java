package com.lahoriagency.cikolive.Classes;
import android.content.Context;
import android.util.Log;

import com.lahoriagency.cikolive.CallActivity;
import com.quickblox.conference.ConferenceSession;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl;


public class WebRtcSessionManager extends QBRTCClientSessionCallbacksImpl {
    private static final String TAG = WebRtcSessionManager.class.getSimpleName();

    private static WebRtcSessionManager instance;
    private Context context;

    private static ConferenceSession currentSessionC;

    private static QBRTCSession currentSession;

    private WebRtcSessionManager(Context context) {
        this.context = context;
    }

    public static WebRtcSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new WebRtcSessionManager(context);
        }

        return instance;
    }
    public ConferenceSession getCurrentSessionC() {
        return currentSessionC;
    }

    public void setCurrentSessionC(ConferenceSession qbCurrentSession) {
        currentSessionC = qbCurrentSession;
    }

    public QBRTCSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(QBRTCSession qbCurrentSession) {
        currentSession = qbCurrentSession;
    }

    @Override
    public void onReceiveNewSession(QBRTCSession session) {
        Log.d(TAG, "onReceiveNewSession to WebRtcSessionManager");

        if (currentSession == null && session != null) {
            setCurrentSession(session);
            CallActivity.start(context, true);
        }
    }

    @Override
    public void onSessionClosed(QBRTCSession session) {
        Log.d(TAG, "onSessionClosed WebRtcSessionManager");

        if (session.equals(getCurrentSession())) {
            setCurrentSessionC(null);
            setCurrentSession(null);
        }
    }

}
