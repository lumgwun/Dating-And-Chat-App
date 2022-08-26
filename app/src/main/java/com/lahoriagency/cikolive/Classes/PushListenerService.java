package com.lahoriagency.cikolive.Classes;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SplashActivity;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;
import com.quickblox.users.model.QBUser;

import java.util.Map;

public class PushListenerService extends QBFcmPushListenerService {
    private static final String TAG = PushListenerService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;

    protected void showNotification(String message) {
        NotificationUtils.showNotification(App.getInstance(), SplashActivity.class,
                App.getInstance().getString(R.string.notification_title), message,
                R.drawable.logo_yellow, NOTIFICATION_ID);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {

            QBUser qbUser = sharedPrefsHelper.getQbUser();
            Log.d(TAG, "App has logged user" + qbUser.getId());
            LoginService.start(this, qbUser);
        }
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);
        if (ActivityLifecycle.getInstance().isBackground()) {
            showNotification(message);
        }
    }
}
