package com.lahoriagency.cikolive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.quickblox.users.model.QBUser;


public class SubmitRedeemActivity extends BaseActivity {

    private AppBarConfiguration appBarConfiguration;
    private AppCompatButton btnSubmit;
    //private AppCompatEditText editAmount;
    private AppCompatSpinner spnPaymentAmt;
    private String selectedDiamond;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2,userName,password,profileName;
    private QBUser qbUser;
    private Bundle payBundle;
    private int profileID,numberOfDiamonds;
    private String strgAmount;
    private long amount;
    private TextView txtAmtToPay;
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
        txtAmtToPay =findViewById(R.id.amtToPayJHG);
        btnSubmit =findViewById(R.id.submit_pay);
        spnPaymentAmt =findViewById(R.id.spnGateWays);

        spnPaymentAmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectedGateWay = spnPaymentGateWays.getSelectedItem().toString();
                selectedDiamond = (String) parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(selectedDiamond !=null){
            if(selectedDiamond.equalsIgnoreCase("500 Diamonds")){
                amount=10;
                numberOfDiamonds=500;
            }
            if(selectedDiamond.equalsIgnoreCase("1,000 Diamonds")){
                amount=20;
                numberOfDiamonds=1000;
            }
            if(selectedDiamond.equalsIgnoreCase("2,000 Diamonds")){
                amount=40;
                numberOfDiamonds=2000;
            }
            if(selectedDiamond.equalsIgnoreCase("3,000 Diamonds")){
                amount=60;
                numberOfDiamonds=3000;
            }
            if(selectedDiamond.equalsIgnoreCase("4,000 Diamonds")){
                amount=80;
                numberOfDiamonds=4000;
            }
            if(selectedDiamond.equalsIgnoreCase("5,000 Diamonds")){
                amount=100;
                numberOfDiamonds=5000;
            }
            if(selectedDiamond.equalsIgnoreCase("10,000 Diamonds")){
                amount=200;
                numberOfDiamonds=10000;
            }
            if(selectedDiamond.equalsIgnoreCase("20,000 Diamonds")){
                amount=400;
                numberOfDiamonds=20000;
            }
            if(selectedDiamond.equalsIgnoreCase("30,000 Diamonds")){
                amount=600;
                numberOfDiamonds=30000;
            }
            if(selectedDiamond.equalsIgnoreCase("40,000 Diamonds")){
                amount=800;
                numberOfDiamonds=40000;
            }
            if(selectedDiamond.equalsIgnoreCase("50,000 Diamonds")){
                amount=1000;
                numberOfDiamonds=50000;
            }
            if(selectedDiamond.equalsIgnoreCase("60,000 Diamonds")){
                amount=1200;
                numberOfDiamonds=60000;
            }
            if(selectedDiamond.equalsIgnoreCase("70,000 Diamonds")){
                amount=1400;
                numberOfDiamonds=70000;
            }
            if(selectedDiamond.equalsIgnoreCase("80,000 Diamonds")){
                amount=1600;
                numberOfDiamonds=80000;
            }
            if(selectedDiamond.equalsIgnoreCase("90,000 Diamonds")){
                amount=1800;
                numberOfDiamonds=90000;
            }
            if(selectedDiamond.equalsIgnoreCase("100,000 Diamonds")){
                amount=2000;
                numberOfDiamonds=100000;
            }
            if(selectedDiamond.equalsIgnoreCase("1,000,000 Diamonds")){
                amount=20000;
                numberOfDiamonds=1000000;
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payBundle.putParcelable("SavedProfile",savedProfile);
                payBundle.putParcelable("QBUser", (Parcelable) qbUser);
                payBundle.putString("SAVED_PROFILE_NAME",profileName);
                payBundle.putInt("SAVED_PROFILE_ID",profileID);
                payBundle.putInt("numberOfDiamonds",numberOfDiamonds);
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