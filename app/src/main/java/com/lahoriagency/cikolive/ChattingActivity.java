package com.lahoriagency.cikolive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;

import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Conference.ChatInfoAct;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class ChattingActivity extends BaseActivity {

    private AppBarConfiguration appBarConfiguration;
    Dialog dialog;

    BitmapTransformation bitmapTransformation;
    private QBUser cloudUser;
    private String userName,password;
    private PreferencesManager preferencesManager;
    SharedPreferences userPreferences;
    Gson gson, gson1;
    int profileID;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    String json, json1, json2;
    private QBUser qbUser;
    ImageView btnVideoCall,btnSend,btnAdd,btnBack,profImg,btnMenu,btnCamera;
    EditText etMassage;
    private SharedPrefsHelper sharedPrefsHelper;
    private QBResRequestExecutor requestExecutor = new QBResRequestExecutor();
    Bundle userExtras;
    private TextView txtName,txtAge,txtLoc,txtChatMsg,txtChatTime, txtMyChat,txtMyChatTime;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    private int savedProfileID;
    private Diamond diamond;
    int diamondCount,diamondID;
    private int collections,qbUserID;
    Gson gson2,gson3;
    String json3, name,profileName;
    private UserProfileInfo userProfileInfo;
    private Bundle activityBundle;
    private QBUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatting1);
        FirebaseApp.initializeApp(this);
        checkInternetConnection();
        cloudUser= new QBUser();
        currentUser= new QBUser();
        savedProfile= new SavedProfile();
        activityBundle= new Bundle();
        gson = new Gson();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        init();
        listeners();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        userName = userPreferences.getString("SAVED_PROFILE_EMAIL", "");
        password = userPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileName = userPreferences.getString("SAVED_PROFILE_NAME", "");
        sharedPrefsHelper= new SharedPrefsHelper();
        btnVideoCall = findViewById(R.id.btn_video_call);
        btnCamera =  findViewById(R.id.btn_camera);
        btnSend =  findViewById(R.id.btn_send);
        btnAdd = findViewById(R.id.btn_add);
        btnBack = findViewById(R.id.btn_back);
        etMassage = findViewById(R.id.et_massage);
        profImg = findViewById(R.id.img_profile);
        txtName = findViewById(R.id.tv_name);
        txtAge = findViewById(R.id.tv_age);
        txtLoc = findViewById(R.id.tv_location);
        btnMenu = findViewById(R.id.btn_menu);
        txtChatMsg = findViewById(R.id.msg_chat);
        txtChatTime = findViewById(R.id.msg_time);
        txtMyChat = findViewById(R.id.my_chat_msg);
        txtMyChatTime = findViewById(R.id.my_chat_time);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean checkInternetConnection() {
        boolean hasInternetConnection = hasInternetConnection();
        if (!hasInternetConnection) {
            showWarningDialog("Internet connection failed");
        }

        return hasInternetConnection;
    }

    public void showWarningDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*SharedPreferences sharedPreferences =
                settingsFragment.getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);*/
    }
    private void listeners() {
        btnBack.setOnClickListener(view -> onBackPressed());

        btnSend.setOnClickListener(view -> {
            etMassage.clearFocus();
            etMassage.setText("");
            dialog.show();
        });

        btnVideoCall.setOnClickListener(view -> startActivity(new Intent(this, CallActivity.class)));


    }


    private void init() {

        /*Glide.with(this)
                .load("https://www.whatsappprofiledpimages.com/wp-content/uploads/2021/08/Profile-Photo-Wallpaper.jpg")
                .into(binding.img);*/

        createDialog();

    }

    private void createDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_by_diamonds_, null);
        dialog = new Dialog(this);
        dialog.setContentView(v);
        LinearLayout btnCancel = dialog.findViewById(R.id.btn_cancelBuy);
        TextView btnBuy = dialog.findViewById(R.id.btn_buyDiamond);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        btnCancel.setOnClickListener(view1 -> dialog.dismiss());
        btnBuy.setOnClickListener(view1 -> {
            dialog.dismiss();
            startActivity(new Intent(this, PurchaseDiamondActivity.class));
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        cloudUser= new QBUser();
        currentUser= new QBUser();
        savedProfile= new SavedProfile();
        activityBundle= new Bundle();
        gson = new Gson();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        int id = item.getItemId();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        activityBundle.putParcelable("QBUser", (Parcelable) currentUser);
        activityBundle.putParcelable("SavedProfile",savedProfile);
        activityBundle.putParcelable("UserProfileInfo",userProfileInfo);
        activityBundle.putInt("SAVED_PROFILE_ID",profileID);
        switch (id) {
            case R.id.menu_profileA:
                Intent profileIntent = new Intent(ChattingActivity.this, ProfileActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                profileIntent.putExtras(activityBundle);
                startActivity(profileIntent);

                return true;

            case R.id.menu_set_main:
                Intent settingsIntent = new Intent(ChattingActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                settingsIntent.putExtras(activityBundle);
                startActivity(settingsIntent);

                return true;

            case R.id.me_chatting:
                Intent chatIntent = new Intent(ChattingActivity.this, ChattingActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chatIntent.putExtras(activityBundle);
                startActivity(chatIntent);

                return true;
            case R.id.menu_chat:
                Intent cIntent = new Intent(ChattingActivity.this, ChatAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                cIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                cIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cIntent.putExtras(activityBundle);
                startActivity(cIntent);

                return true;


            case R.id.menu_set_chat_info:
                Intent mIntent = new Intent(ChattingActivity.this, ChatInfoAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtras(activityBundle);
                startActivity(mIntent);

                return true;


            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}