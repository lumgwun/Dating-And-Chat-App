package com.lahoriagency.cikolive;

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
    String json, json1, json2;
    private QBUser qbUser;

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
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        editAmount =findViewById(R.id.amtToPay);
        btnSubmit =findViewById(R.id.submit_pay);
        spnPaymentGateWays =findViewById(R.id.spnGateWays);

        spnPaymentGateWays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGateWay = spnPaymentGateWays.getSelectedItem().toString();
                selectedGateWay = (String) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnSubmit.setOnClickListener(this::submitNewPay);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
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