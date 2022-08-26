package com.lahoriagency.cikolive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class RedeemRequestActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    SharedPreferences userPreferences;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_redeem_request);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}