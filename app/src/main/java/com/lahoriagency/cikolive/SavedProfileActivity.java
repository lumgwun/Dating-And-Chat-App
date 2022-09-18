package com.lahoriagency.cikolive;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.lahoriagency.cikolive.Adapters.SavedProfileAdapter;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.DataBase.DBHelper;

import java.util.ArrayList;

public class SavedProfileActivity extends BaseActDiamond {
    SavedProfileAdapter savedProfileAdapter;
    private RecyclerView recyclerProfile;
    private ArrayList<SavedProfile> savedProfiles;
    SQLiteDatabase sqLiteDatabase;
    private DBHelper dBHelper;
    private TextView txtUserCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_saved_profile);
        dBHelper= new DBHelper(this);
        recyclerProfile = findViewById(R.id.recyclerViewProfiles);
        txtUserCount = findViewById(R.id.txtViewUserCount);
        savedProfiles=new ArrayList<SavedProfile>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerProfile.setLayoutManager(layoutManager);
        savedProfiles = dBHelper.getAllSavedProfiles();
        savedProfileAdapter = new SavedProfileAdapter(SavedProfileActivity.this,savedProfiles);
        recyclerProfile.setAdapter(savedProfileAdapter);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(recyclerProfile.getContext(),
                layoutManager.getOrientation());
        recyclerProfile.addItemDecoration(dividerItemDecoration1);
        recyclerProfile.setItemAnimator(new DefaultItemAnimator());
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerProfile);
        makeListOfSavedProfiles();
        init();
        listeners();
    }

    private void listeners() {
        onBackPressed();
    }

    private void init() {
        //savedProfileAdapter.updateItems(savedProfileList);

    }
}