package com.lahoriagency.cikolive;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.LoginService;
import com.lahoriagency.cikolive.Classes.PermissionsChecker;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.NewPackage.ChatMainAct;
import com.lahoriagency.cikolive.NewPackage.SignUpActivity;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.quickblox.users.model.QBUser;



@SuppressWarnings("deprecation")
public class SplashActivity extends AppCompatActivity {

    Runnable runnable;
    Handler handler;
    SessionManager sessionManager;
    AnimationDrawable loadingAnimation;
    ImageView loadingBar;
    private SavedProfile savedProfile;
    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_DELAY = 1500;
    private String message;
    boolean overlayChecked;
    boolean miOverlayChecked;

    private static final String OVERLAY_PERMISSION_CHECKED_KEY = "overlay_checked";
    private static final String MI_OVERLAY_PERMISSION_CHECKED_KEY = "mi_overlay_checked";

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1764;

    private SharedPrefsHelper sharedPrefsHelper;
    private SharedPreferences userPreferences;
    private int profileID;
    private Gson gson,gson1;
    private String json,json1;
    private QBUser qbUser;
    private static final String PREF_NAME = "Ciko";
    /*@Override
    public void onClick(View v) {
        handler.removeCallbacks(this);
        run();
    }*/

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        savedProfile=new SavedProfile();
        qbUser= new QBUser();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        gson1= new Gson();
        json = userPreferences.getString("LastSavedProfileUsed", "");
        json1 = userPreferences.getString("LastQBUserUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        qbUser = gson.fromJson(json1, QBUser.class);
        if (qbUser !=null) {
            LoginService.start(SplashActivity.this, sharedPrefsHelper.getQbUser());
            Bundle userBundle=new Bundle();
            userBundle.putParcelable("SavedProfile",savedProfile);
            userBundle.putParcelable("QBUser", (Parcelable) qbUser);
            Intent helpIntent = new Intent(SplashActivity.this, SignUpActivity.class);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(helpIntent);
            //MainActivity.start(SplashActivity.this);

        }
        loadingBar = (ImageView) findViewById(R.id.logoSplash);
        loadingBar.setBackgroundResource(R.xml.loading);
        loadingAnimation = (AnimationDrawable) loadingBar.getBackground();
        findViewById(R.id.splash_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tintStatusBar(ContextCompat.getColor(this, R.color.white));
        tintStatusBar(getColor(R.color.app_color));
        fillVersion();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = getIntent().getExtras().getString(Consts.EXTRA_FCM_MESSAGE);
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (checkOverlayPermissions()) {
            runNextScreen();

        }

    }
    public void tintStatusBar(int color) {


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        loadingAnimation.start();
        super.onWindowFocusChanged(hasFocus);
    }
    private void runNextScreen() {
        savedProfile = new SavedProfile();
        qbUser = new QBUser();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        gson1 = new Gson();
        json = userPreferences.getString("LastSavedProfileUsed", "");
        json1 = userPreferences.getString("LastQBUsereUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        qbUser = gson.fromJson(json1, QBUser.class);
        if (qbUser != null) {
            LoginService.start(SplashActivity.this, qbUser);
            Bundle userBundle = new Bundle();
            userBundle.putParcelable("SavedProfile", savedProfile);
            userBundle.putParcelable("QBUser", (Parcelable) qbUser);
            Intent helpIntent = new Intent(SplashActivity.this, ChatMainAct.class);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            helpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(helpIntent);
            //MainActivity.start(SplashActivity.this);

        }
        /*if (sharedPrefsHelper.hasQbUser()) {
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

        }*/
        else{
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
        if(sharedPrefsHelper !=null){
            overlayChecked = sharedPrefsHelper.get(OVERLAY_PERMISSION_CHECKED_KEY, false);
            miOverlayChecked = sharedPrefsHelper.get(MI_OVERLAY_PERMISSION_CHECKED_KEY, false);

        }


        if (!Settings.canDrawOverlays(this) && !overlayChecked) {
            Log.e(TAG, "Android Overlay Permission NOT Granted");
            buildOverlayPermissionAlertDialog();
            return false;
        } else if (PermissionsChecker.isMiUi() && !miOverlayChecked) {
            Log.e(TAG, "Xiaomi Device. Need additional Overlay Permissions");
            buildMIUIOverlayPermissionAlertDialog();
            return false;
        }
        if(sharedPrefsHelper !=null){
            Log.e(TAG, "All Overlay Permission Granted");
            sharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true);
            sharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true);

        }

        return true;
    }

    private void buildOverlayPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Overlay Permission Required");
        sharedPrefsHelper= new SharedPrefsHelper();
        builder.setIcon(R.drawable.error_fg);
        builder.setMessage("To receive calls in background - \nPlease Allow overlay permission in Android Settings");
        builder.setCancelable(false);


        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.longToast("You might miss calls while your application in background");
                if(sharedPrefsHelper !=null){
                    sharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true);

                }

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
        sharedPrefsHelper= new SharedPrefsHelper();
        builder.setMessage("Please make sure that all additional permissions granted");
        builder.setCancelable(false);

        builder.setNeutralButton("I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(sharedPrefsHelper !=null){
                    sharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true);

                }

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


}