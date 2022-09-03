package com.lahoriagency.cikolive;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;

import com.quickblox.users.model.QBUser;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatting1);
        init();
        listeners();
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


}