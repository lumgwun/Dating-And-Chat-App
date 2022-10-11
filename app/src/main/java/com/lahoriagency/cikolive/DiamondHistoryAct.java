package com.lahoriagency.cikolive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.DiamondHisAdapter;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.DiamondHisDAO;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;


public class DiamondHistoryAct extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private FloatingActionButton fab;
    DBHelper dbHelper;
    private DiamondHisDAO diamondHisDAO;
    private RecyclerView recyclerView;
    DiamondHisAdapter hisAdapter;
    private ArrayList<DiamondTransfer> diamondHistories;
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
    int profileID,diamondC,collCount;
    private QBUser qbUser;
    private  Bundle bundle;
    private static final String PREF_NAME = "Ciko";
    private TextView txtWallet,txtCollection;
    private Diamond diamond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_diamond_history);
        dbHelper= new DBHelper(this);
        recyclerView = findViewById(R.id.recycler_my_Dia_His);
        fab = findViewById(R.id.dia_Fab);
        txtCollection = findViewById(R.id.d_Collects);
        txtWallet = findViewById(R.id.walletD_No);
        bundle = new Bundle();
        gson = new Gson();
        gson1 = new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        savedProfile= new SavedProfile();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("PROFILE_USERNAME", "");
        SharedPrefUserPassword=sharedPreferences.getString("PROFILE_PASSWORD", "");
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        profileID = userPreferences.getInt("PROFILE_ID", 0);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        diamondHisDAO= new DiamondHisDAO(this);
        diamondHistories = new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent myIntent = new Intent(DiamondHistoryAct.this, MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });
        if(savedProfile !=null){
            diamondHistories=savedProfile.getDiamondHistories();
            diamond=savedProfile.getSavedPDiamond();

            //diamondHistories=diamondHisDAO.getDiamondHisByProfID(profileID);

        }
        if(diamond !=null){
            diamondC=diamond.getDiamondCount();
            collCount=diamond.getDiamondCollections();

        }
        txtCollection.setText("My Collections"+collCount);
        txtWallet.setText("My Wallet Diamonds"+diamondC);

        hisAdapter = new DiamondHisAdapter(this,  diamondHistories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hisAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

}