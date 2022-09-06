package com.lahoriagency.cikolive;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.LoginService;
import com.lahoriagency.cikolive.Classes.PermissionsChecker;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Utils.SessionManager;

import static com.lahoriagency.cikolive.Utils.Const.PREF_NAME;

@SuppressWarnings("deprecation")
public class SplashActivity extends BaseActivity implements Runnable, View.OnClickListener {

    Runnable runnable;
    Handler handler;
    SessionManager sessionManager;
    AnimationDrawable loadingAnimation;
    ImageView loadingBar;
    private SavedProfile savedProfile;
    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_DELAY = 1500;
    private String message;

    private static final String OVERLAY_PERMISSION_CHECKED_KEY = "overlay_checked";
    private static final String MI_OVERLAY_PERMISSION_CHECKED_KEY = "mi_overlay_checked";

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1764;

    private SharedPrefsHelper sharedPrefsHelper;
    private SharedPreferences userPreferences;
    private int profileID;
    private Gson gson,gson1;
    private String json,json1;
    @Override
    public void onClick(View v) {
        handler.removeCallbacks(this);
        run();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        savedProfile=new SavedProfile();
        loadingBar = (ImageView) findViewById(R.id.logoSplash);
        loadingBar.setBackgroundResource(R.xml.loading);
        loadingAnimation = (AnimationDrawable) loadingBar.getBackground();
        findViewById(R.id.splash_root).setOnClickListener(this);
        tintStatusBar(ContextCompat.getColor(this, R.color.white));
        tintStatusBar(getColor(R.color.app_color));
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
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        loadingAnimation.start();
        super.onWindowFocusChanged(hasFocus);
    }
    private void runNextScreen() {
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        if (sharedPrefsHelper.hasQbUser()) {
            LoginService.start(SplashActivity.this, sharedPrefsHelper.getQbUser());
            Bundle userBundle=new Bundle();
            userBundle.putParcelable("SavedProfile",savedProfile);
            userBundle.putParcelable("QBUser", (Parcelable) sharedPrefsHelper.getQbUser());
            Intent helpIntent = new Intent(SplashActivity.this, MainActivity.class);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(helpIntent);
            //MainActivity.start(SplashActivity.this);

        }
        if(savedProfile !=null){
            Bundle userBundle=new Bundle();
            userBundle.putParcelable("SavedProfile",savedProfile);
            userBundle.putParcelable("QBUser", (Parcelable) sharedPrefsHelper.getQbUser());
            Intent helpIntent = new Intent(SplashActivity.this, MainActivity.class);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(helpIntent);


        } else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingAnimation.stop();
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




    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    @Override
    public void run() {

    }
}