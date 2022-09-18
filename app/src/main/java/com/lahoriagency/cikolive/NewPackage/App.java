package com.lahoriagency.cikolive.NewPackage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;


import java.util.Locale;

@SuppressWarnings("deprecation")
public class App extends CoreApp {

    private static final String TAG = App.class.getSimpleName();
    private static SampleConfigs sampleConfigs;
    //private static CommonInstances commonInstances;
    private static App instance;
    Context _context;

    /*"app_id": "76720",
            "auth_key": "shkJz23eJhkVxOX",
            "auth_secret": "YUgd84Eedun2LWz",
            "account_key": "9bsxYem2DfdWm7xU79zc",
            "api_domain": "https://api.quickblox.com",
            "chat_domain": "chat.quickblox.com",
            "gcm_sender_id": "647783175343"*/

    private static QBResRequestExecutor qbResRequestExecutor;
    public App(Context context) {
        super(context);
        this._context = context;

    }
    public static App getContext() {
        return instance;
    }

    //public static CommonInstances getCommonInstances() {
//        return commonInstances;
//    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // commonInstances = new CommonInstances();
        ActivityLifecycle.init(this);
        //initSampleConfigs();


    }


//    private void initSampleConfigs() {
//        try {
//            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // setting locale for the whole application
    public void setResourceLocale(Locale locale) {
        getBaseContext().getResources().getConfiguration().setLocale(locale);
    }

    //for setting locale on the basis of language selection
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }
}
