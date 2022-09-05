package com.lahoriagency.cikolive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.TimelineAdapter;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.TimeLine;
import com.lahoriagency.cikolive.DataBase.TimeLineDAO;

import java.util.ArrayList;

public class MyTimeLinesAct extends AppCompatActivity {
    private TimeLineDAO timeLineDAO;
    private RecyclerView recyclerView;
    TimelineAdapter timelineAdapter;
    private ArrayList<TimeLine> timeLineArrayList;
    private SavedProfile savedProfile;
    private int savedProfID;
    private SharedPreferences userPreferences;
    private Gson gson,gson1;
    private String json,json1;
    SharedPreferences sharedPreferences;
    String SharedPrefUserPassword;
    String SharedPrefEmail;
    int SharedPrefProfileID;
    String SharedPrefSurName,SharedPrefUserName;
    String SharedPrefFirstName;
    int profileID;
    private static final String PREF_NAME = "Ciko";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_time_lines);
        gson = new Gson();
        gson1 = new Gson();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("PROFILE_USERNAME", "");
        SharedPrefUserPassword=sharedPreferences.getString("PROFILE_PASSWORD", "");
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        profileID = userPreferences.getInt("PROFILE_ID", 0);
        timeLineDAO= new TimeLineDAO(this);
        timeLineArrayList = new ArrayList<TimeLine>();
        recyclerView = findViewById(R.id.recycler_my_Timelines);
        timeLineArrayList=timeLineDAO.getTimeLineByProfID(profileID);

        timelineAdapter = new TimelineAdapter(this,  timeLineArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(timelineAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
}