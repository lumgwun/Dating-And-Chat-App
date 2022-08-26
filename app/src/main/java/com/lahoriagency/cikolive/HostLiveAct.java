package com.lahoriagency.cikolive;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.quickblox.users.model.QBUser;

public class HostLiveAct extends AppCompatActivity {
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
        setContentView(R.layout.act_host_live);
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
    }
}