package com.lahoriagency.cikolive;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.quickblox.users.model.QBUser;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class HostMainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private AppCompatButton btnCancel,btnStart;
    private Dialog dialogLive;
    private LinearLayout btnGoLive;
    private Button btnRedeem;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    private SwitchCompat switchAvailOrNot;
    private ImageView btnBack,btnMenu;
    private TextView tv_count,tv_min,tv_total,tv_Wallet;
    private AppCompatButton btnPending,btnHistory,btnRedeemReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_host_main);
        setTitle("Host Activity");
        savedProfile= new SavedProfile();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        tv_count =findViewById(R.id.tv_count);
        tv_min =findViewById(R.id.tv_min);
        tv_Wallet =findViewById(R.id.tv_Wallet);
        tv_total =findViewById(R.id.tv_colectiont);
        btnPending =findViewById(R.id.btn_pending);
        btnMenu =findViewById(R.id.btn_menu);

        btnBack =findViewById(R.id.btn_back);
        btnCancel =findViewById(R.id.submit_pay);
        btnGoLive =findViewById(R.id.btn_goLive);
        btnHistory =findViewById(R.id.submit_pay);
        btnRedeem =findViewById(R.id.btn_redeem);
        btnRedeemReq =findViewById(R.id.submit_pay);
        switchAvailOrNot =findViewById(R.id.avail_for_call);
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnGoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLive.show();
            }
        });
        btnRedeemReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent intent = new Intent(HostMainActivity.this, RedeemRequestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent intent = new Intent(HostMainActivity.this, SubmitRedeemActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent intent = new Intent(HostMainActivity.this, DiamondHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void createDeleteDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_start_live_, null);
        dialogLive = new Dialog(this);
        dialogLive.setContentView(v);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogLive.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        dialogLive.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogLive.getWindow().setAttributes(layoutParams);
        dialogLive.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        btnCancel.setOnClickListener(view1 -> dialogLive.dismiss());
        btnStart.setOnClickListener(view1 -> {
            dialogLive.dismiss();
            startActivity(new Intent(this, HostLiveAct.class));

        });
    }

}