package com.lahoriagency.cikolive.Conference;

import android.util.Log;

import com.quickblox.conference.ConferenceSession;

public class WebRtcSessManagerCon {
    private static final String TAG = WebRtcSessManagerCon.class.getSimpleName();

    private static WebRtcSessManagerCon instance;
    private static ConferenceSession currentSession;

    private WebRtcSessManagerCon() {

    }

    public static WebRtcSessManagerCon getInstance() {
        if (instance == null) {
            Log.d(TAG, "New Instance of WebRtcSessionManager");
            instance = new WebRtcSessManagerCon();
        }

        return instance;
    }

    public ConferenceSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(ConferenceSession qbCurrentSession) {
        currentSession = qbCurrentSession;
    }
}
