package com.lahoriagency.cikolive.Classes;

import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.common.GoogleApiAvailability;
import com.lahoriagency.cikolive.R;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.messages.services.QBPushManager;

public class AppPush extends MultiDexApplication {
    private static final String TAG = AppPush.class.getSimpleName();

    private static final String APPLICATION_ID = "";
    private static final String AUTH_KEY = "";
    private static final String AUTH_SECRET = "";
    private static final String ACCOUNT_KEY = "";

    public static final String DEFAULT_USER_PASSWORD = "quickblox";

    private static AppPush instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActivityLifecyclePush.init();
        checkConfig();
        initCredentials();
        initQBSessionManager();
        initPushManager();
    }

    private void checkConfig() {
        if (TextUtils.isEmpty(APPLICATION_ID) || TextUtils.isEmpty(AUTH_KEY) || TextUtils.isEmpty(AUTH_SECRET) || TextUtils.isEmpty(ACCOUNT_KEY)
                || TextUtils.isEmpty(DEFAULT_USER_PASSWORD)) {
            throw new AssertionError(getString(R.string.error_qb_credentials_empty));
        }
    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        // Uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    private void initQBSessionManager() {
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {


            @Override
            public void onSessionCreated(com.quickblox.auth.session.QBSession qbSession) {
                Log.d(TAG, "Session Created");
            }

            @Override
            public void onSessionUpdated(QBSessionParameters qbSessionParameters) {
                Log.d(TAG, "Session Updated");
            }

            @Override
            public void onSessionDeleted() {
                Log.d(TAG, "Session Deleted");
            }

            @Override
            public void onSessionRestored(com.quickblox.auth.session.QBSession qbSession) {
                Log.d(TAG, "Session Restored");
            }

            @Override
            public void onSessionExpired() {
                Log.d(TAG, "Session Expired");
            }

            @Override
            public void onProviderSessionExpired(String provider) {
                Log.d(TAG, "Session Expired for provider:" + provider);
            }
        });
    }

    private void initPushManager() {
        QBPushManager.getInstance().addListener(new QBPushManager.QBSubscribeListener() {
            @Override
            public void onSubscriptionCreated() {
                ToastUtils.shortToast("Subscription Created");
                Log.d(TAG, "Subscription Created");
            }

            @Override
            public void onSubscriptionError(Exception e, int resultCode) {
                Log.d(TAG, "SubscriptionError" + e.getLocalizedMessage());
                if (resultCode >= 0) {
                    String error = GoogleApiAvailability.getInstance().getErrorString(resultCode);
                    Log.d(TAG, "SubscriptionError playServicesAbility: " + error);
                }
                ToastUtils.shortToast(e.getLocalizedMessage());
            }

            @Override
            public void onSubscriptionDeleted(boolean success) {
                Log.d(TAG, "Subscription Deleted");
            }
        });
    }

    public static synchronized AppPush getInstance() {
        return instance;
    }
}
