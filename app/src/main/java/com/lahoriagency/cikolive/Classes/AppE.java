package com.lahoriagency.cikolive.Classes;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;

import java.io.IOException;
import java.util.Calendar;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class AppE extends MultiDexApplication {
    public static final String TAG = AppE.class.getSimpleName();

    private static AppE instance;
    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";
    private static QbConfigs qbConfigs;
    private static SampleConfigs sampleConfigs;
    private static ImageLoader imageLoader;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";

    private static Context context;
    public static int year = Calendar.getInstance().get(Calendar.YEAR);

    private static PreferencesManager preferencesManager;
    private static MyPreferences preferences;
    private static Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActivityLifecycle.init(this);

        initQbConfigs();

        AppE.context = getApplicationContext();
        preferencesManager = new PreferencesManager(AppE.context);
        preferences = preferencesManager.getMyPreferences();
        imageLoader = new ImageLoader();
        initSampleConfigs();
        initCredentials();
    }

    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    private void initQbConfigs() {
        Log.e(TAG, "QB CONFIG FILE NAME: " + getQbConfigFileName());
        qbConfigs = CoreConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }

    public static synchronized AppE getInstance() {
        return instance;
    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
            QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
            QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
        }
    }


    public static Gson getGson() {
        return gson;
    }

    public static QbConfigs getQbConfigs(){
        return qbConfigs;
    }

    public static String getQbConfigFileName(){
        return QB_CONFIG_DEFAULT_FILE_NAME;
    }

    public static Context getAppContext() {
        return AppE.context;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static MyPreferences getPreferences() {
        return preferences;
    }

    public static PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }
}
