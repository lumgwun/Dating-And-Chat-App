package com.lahoriagency.cikolive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.RedeemRequestAdapter;
import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.RedeemRequestDAO;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class RedeemRequestActivity extends AppCompatActivity implements RedeemRequestAdapter.OnItemsClickListener{

    private AppBarConfiguration appBarConfiguration;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    SharedPreferences userPreferences;
    private FloatingActionButton fab;

    DBHelper dbHelper;
    private RedeemRequestDAO redeemRequestDAO;
    private RecyclerView recyclerView;
    RedeemRequestAdapter hisAdapter;
    private ArrayList<RedeemRequest> redeemRequests;
    private SavedProfile savedProfile;
    private int savedProfID;

    SharedPreferences sharedPreferences;
    String SharedPrefUserPassword;
    String SharedPrefEmail;
    int SharedPrefProfileID;
    String SharedPrefSurName,SharedPrefUserName;
    String SharedPrefFirstName;
    int profileID;
    private QBUser qbUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_redeem_request);
        recyclerView = findViewById(R.id.recycler_Red_Request);
        fab = findViewById(R.id.dia_Fab);
        gson = new Gson();
        gson1 = new Gson();
        qbUser= new QBUser();
        savedProfile= new SavedProfile();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("SAVED_PROFILE_EMAIL", "");
        SharedPrefUserPassword=sharedPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        redeemRequestDAO= new RedeemRequestDAO(this);
        redeemRequests = new ArrayList<>();
        Bundle bundle= new Bundle();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent myIntent = new Intent(RedeemRequestActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });

        redeemRequests=redeemRequestDAO.getRedeemReqByProfID(profileID);
        hisAdapter = new RedeemRequestAdapter(this,  redeemRequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hisAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onItemClick(RedeemRequest redeemRequest) {
        Bundle requestBundle= new Bundle();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("SAVED_PROFILE_EMAIL", "");
        SharedPrefUserPassword=sharedPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        requestBundle.putParcelable("RedeemRequest",redeemRequest);
        requestBundle.putParcelable("QBUser", (Parcelable) qbUser);
        requestBundle.putParcelable("SavedProfile",savedProfile);
        requestBundle.putInt("SAVED_PROFILE_ID",profileID);
        requestBundle.putString("SAVED_PROFILE_EMAIL",SharedPrefUserName);

        Intent dialogIntent = new Intent(RedeemRequestActivity.this, SubmitRedeemActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtras(requestBundle);
        startActivity(dialogIntent);
    }
}