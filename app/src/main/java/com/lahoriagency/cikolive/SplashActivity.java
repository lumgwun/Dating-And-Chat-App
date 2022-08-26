package com.lahoriagency.cikolive;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.LoginService;
import com.lahoriagency.cikolive.Classes.PermissionsChecker;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Utils.Const;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.lahoriagency.cikolive.Video_And_Call.OpponentsActivity;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

@SuppressWarnings("deprecation")
public class SplashActivity extends BaseActivity {

    Runnable runnable;
    Handler handler;
    SessionManager sessionManager;
    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_DELAY = 1500;
    private String message;

    private static final String OVERLAY_PERMISSION_CHECKED_KEY = "overlay_checked";
    private static final String MI_OVERLAY_PERMISSION_CHECKED_KEY = "mi_overlay_checked";

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1764;

    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        tintStatusBar(ContextCompat.getColor(this, R.color.white));
        tintStatusBar(getColor(R.color.app_color));
        init();
        fillVersion();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = getIntent().getExtras().getString(Consts.EXTRA_FCM_MESSAGE);
        }
        sharedPrefsHelper = SharedPrefsHelper.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (checkOverlayPermissions()) {
            runNextScreen();
        }

    }
    private void runNextScreen() {
        if (sharedPrefsHelper.hasQbUser()) {
            LoginService.start(SplashActivity.this, sharedPrefsHelper.getQbUser());
            OpponentsActivity.start(SplashActivity.this);
            restoreChatSession();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SignInActivity.start(SplashActivity.this);
                    finish();
                }
            }, SPLASH_DELAY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (checkOverlayPermissions()) {
                runNextScreen();
            }
        }
    }

    private void fillVersion() {
        String appName = getString(R.string.app_name);
        ((TextView) findViewById(R.id.text_splash_app_title)).setText(appName);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            ((TextView) findViewById(R.id.text_splash_app_version)).setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(SplashActivity.this, "Error ", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkOverlayPermissions() {
        Log.e(TAG, "Checking Permissions");
        boolean overlayChecked = sharedPrefsHelper.get(OVERLAY_PERMISSION_CHECKED_KEY, false);
        boolean miOverlayChecked = sharedPrefsHelper.get(MI_OVERLAY_PERMISSION_CHECKED_KEY, false);

        if (!Settings.canDrawOverlays(this) && !overlayChecked) {
            Log.e(TAG, "Android Overlay Permission NOT Granted");
            buildOverlayPermissionAlertDialog();
            return false;
        } else if (PermissionsChecker.isMiUi() && !miOverlayChecked) {
            Log.e(TAG, "Xiaomi Device. Need additional Overlay Permissions");
            buildMIUIOverlayPermissionAlertDialog();
            return false;
        }
        Log.e(TAG, "All Overlay Permission Granted");
        sharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true);
        sharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true);
        return true;
    }

    private void buildOverlayPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Overlay Permission Required");
        builder.setIcon(R.drawable.error_fg);
        builder.setMessage("To receive calls in background - \nPlease Allow overlay permission in Android Settings");
        builder.setCancelable(false);

        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.longToast("You might miss calls while your application in background");
                sharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true);
            }
        });

        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAndroidOverlayPermissionsSettings();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.create();
        alertDialog.show();
    }

    private void showAndroidOverlayPermissionsSettings() {
        if (!Settings.canDrawOverlays(SplashActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Application Already has Overlay Permission");
        }
    }

    private void buildMIUIOverlayPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Additional Overlay Permission Required");
        builder.setIcon(R.drawable.error_fg);
        builder.setMessage("Please make sure that all additional permissions granted");
        builder.setCancelable(false);

        builder.setNeutralButton("I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true);
                runNextScreen();
            }
        });

        builder.setPositiveButton("Mi Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMiUiPermissionsSettings();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.create();
        alertDialog.show();
    }

    private void showMiUiPermissionsSettings() {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", getPackageName());
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }


    private void init() {

        sessionManager = new SessionManager(this);
        runnable = () -> {

            if (Boolean.TRUE.equals(sessionManager.getBooleanValue(Const.IS_LOGGED_IN))) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));


            } else {
                startActivity(new Intent(SplashActivity.this, CreateProfileActivity.class));
            }
            finish();
        };

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 1000);



    }
    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            DialogsActivity.start(this);
            finish();
        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                SignInActivity.start(this);
                finish();
            } else {
                loginToChat(currentUser);
            }
        }
    }

    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null || user == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }

    private void loginToChat(final QBUser user) {
        showProgressDialog(R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v(TAG, "Chat login onSuccess()");
                hideProgressDialog();
                DialogsActivity.start(SplashActivity.this);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getMessage().equals("You have already logged in chat")) {
                    loginToChat(user);
                } else {
                    hideProgressDialog();
                    Log.w(TAG, "Chat login onError(): " + e);
                    showErrorSnackbar(R.string.error_recreate_session, e,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loginToChat(user);
                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {


    }
}