package com.lahoriagency.cikolive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.quickblox.users.model.QBUser;


public class SubmitRedeemActivity extends BaseActivity {

    private AppBarConfiguration appBarConfiguration;
    private AppCompatButton btnSubmit;
    private AppCompatEditText editAmount;
    private AppCompatSpinner spnPaymentGateWays;
    private String selectedGateWay;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2,userName,password,profileName;
    private QBUser qbUser;
    private Bundle payBundle;
    private int profileID;
    private String strgAmount;
    private long amount;
    public static void start(Context context) {
        Intent intent = new Intent(context, SubmitRedeemActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_submit_redeem);
        setTitle("Redeem Payment");
        savedProfile= new SavedProfile();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        payBundle= new Bundle();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        profileID = sharedPref.getInt("SAVED_PROFILE_ID", 0);
        userName = sharedPref.getString("SAVED_PROFILE_EMAIL", "");
        password = sharedPref.getString("SAVED_PROFILE_PASSWORD", "");
        profileName = sharedPref.getString("SAVED_PROFILE_NAME", "");
        editAmount =findViewById(R.id.amtToPay);
        btnSubmit =findViewById(R.id.submit_pay);
        spnPaymentGateWays =findViewById(R.id.spnGateWays);

        spnPaymentGateWays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectedGateWay = spnPaymentGateWays.getSelectedItem().toString();
                selectedGateWay = (String) parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strgAmount=editAmount.getText().toString();
                amount= Long.parseLong((strgAmount));
                payBundle.putParcelable("SavedProfile",savedProfile);
                payBundle.putParcelable("QBUser", (Parcelable) qbUser);
                payBundle.putString("SAVED_PROFILE_NAME",profileName);
                payBundle.putInt("SAVED_PROFILE_ID",profileID);
                payBundle.putLong("Amount",amount);
                Intent dialogIntent = new Intent(SubmitRedeemActivity.this, GooglePayCheckoutAct.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtras(payBundle);
                startActivity(dialogIntent);

            }
        });
        btnSubmit.setOnClickListener(this::submitNewPay);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                bundle.putInt("SavedProfile",profileID);
                Intent intent = new Intent(SubmitRedeemActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
    }


    public void submitNewPay(View view) {
    }
}