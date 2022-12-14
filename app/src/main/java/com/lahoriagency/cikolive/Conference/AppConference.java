package com.lahoriagency.cikolive.Conference;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;
import androidx.work.Logger;

import com.lahoriagency.cikolive.Classes.ActivityLifecycle;
import com.lahoriagency.cikolive.Classes.BackgroundListener;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.DialogsManager;
import com.lahoriagency.cikolive.Classes.ImageLoader;
import com.lahoriagency.cikolive.Classes.QBDialogsHolderImpl;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.NewPackage.ChatHelperCon;
import com.lahoriagency.cikolive.NewPackage.DialogsManagerCon;
import com.lahoriagency.cikolive.R;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.conference.ConferenceConfig;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class AppConference extends MultiDexApplication {
    public static final int CHAT_PORT = 5223;
    public static final int SOCKET_TIMEOUT = 300;
    public static final boolean KEEP_ALIVE = true;
    public static final boolean USE_TLS = true;
    public static final boolean AUTO_JOIN = false;
    public static final boolean AUTO_MARK_DELIVERED = true;
    public static final boolean RECONNECTION_ALLOWED = true;
    public static final boolean ALLOW_LISTEN_NETWORK = true;

    //Chat settings range
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_PORT_VALUE = 1000;
    private static final int MIN_SOCKET_TIMEOUT = 300;
    private static final int MAX_SOCKET_TIMEOUT = 60000;

    //App credentials
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "http://192.168.1.15:5000/";

    public static final String USER_DEFAULT_PASSWORD = "quickblox";

    private SharedPrefsHelper sharedPrefsHelper;
    private QBUsersHolder qbUsersHolder;
    private QBDialogsHolder qbDialogsHolder;
    private ChatHelperCon chatHelper;
    private DialogsManagerCon dialogsManager;
    private static ImageLoader imageLoader;
    private Context contxt;

    public static Logger logger;
    private ChatHelper chatHelperA;
    private DialogsManager dialogsManagerA;

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifecycle.init(this);
        checkAppCredentials();
        imageLoader = new ImageLoader();
        checkChatSettings();
        initCredentials();
        initConferenceConfig();
        initSharedPreferences();
        initUsersHolder();
        initDialogsHolder();
        initChatHelper();
        initChatHelperA();
        initDialogsManager();
        initDialogsManagerA();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new BackgroundListener());


    }


    private void initChatHelperA() {
        try {
            chatHelperA = new ChatHelper(getApplicationContext());

        } catch (NoClassDefFoundError e) {
            System.out.println("Oops!");
        }

    }

    public ChatHelper getChatHelperA() {
        try {
            return chatHelperA;
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initDialogsManagerA() {
        dialogsManagerA = new DialogsManager(getApplicationContext());
    }

    public DialogsManager getDialogsManagerA() {
        return dialogsManagerA;
    }
    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    private void initSharedPreferences() {
        sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
    }

    public SharedPrefsHelper getSharedPrefsHelper() {
        return sharedPrefsHelper;
    }

    private void initUsersHolder() {
        qbUsersHolder = new QBUsersHolderImpl();
    }

    public QBUsersHolder getQBUsersHolder() {
        return qbUsersHolder;
    }

    private void initDialogsHolder() {
        qbDialogsHolder = new QBDialogsHolderImpl();
    }

    public QBDialogsHolder getQBDialogsHolder() {
        return qbDialogsHolder;
    }

    private void initChatHelper() {
        try {
            chatHelper = new ChatHelperCon(getApplicationContext());

        } catch (NoClassDefFoundError e) {
            System.out.println("Oops!");
        }

    }
    private void initChatHelper(Context context) {
        try {
            chatHelper = new ChatHelperCon(context);

        } catch (NoClassDefFoundError e) {
            System.out.println("Oops!");
        }

    }

    public ChatHelperCon getChatHelper() {
        return chatHelper;
    }

    private void initDialogsManager() {
        dialogsManager = new DialogsManagerCon(getApplicationContext());
    }

    public DialogsManagerCon getDialogsManager() {
        return dialogsManager;
    }

    private void checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw new AssertionError(getString(R.string.error_qb_credentials_empty));
        }
    }

    private void checkChatSettings() {
        if (USER_DEFAULT_PASSWORD.isEmpty() || (CHAT_PORT < MIN_PORT_VALUE || CHAT_PORT > MAX_PORT_VALUE)
                || (SOCKET_TIMEOUT < MIN_SOCKET_TIMEOUT || SOCKET_TIMEOUT > MAX_SOCKET_TIMEOUT)) {
            throw new AssertionError(getString(R.string.error_chat_credentails_empty));
        }
    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        // Uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your.api.endpoint.com", "your.chat.endpoint.com", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    private void initConferenceConfig() {
        if (!TextUtils.isEmpty(SERVER_URL)) {
            ConferenceConfig.setUrl(SERVER_URL);
        } else {

            throw new AssertionError(getString(R.string.error_server_url_null));
        }
    }
}
