package com.lahoriagency.cikolive.SuperAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lahoriagency.cikolive.Adapters.RedeemRequestAdapter;
import com.lahoriagency.cikolive.Adapters.SavedProfileAdapter;
import com.lahoriagency.cikolive.Adapters.TranxAdapter;
import com.lahoriagency.cikolive.Adapters.UserAdapter2;
import com.lahoriagency.cikolive.AppSettingAct;
import com.lahoriagency.cikolive.AppSupportAct;
import com.lahoriagency.cikolive.AppTimeLineAct;
import com.lahoriagency.cikolive.BaseActNew;
import com.lahoriagency.cikolive.Classes.QbUsersHolder;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.Transaction;
import com.lahoriagency.cikolive.DataBase.TransactionDAO;
import com.lahoriagency.cikolive.Interfaces.ItemClickListener;
import com.lahoriagency.cikolive.ListUsersActivity;
import com.lahoriagency.cikolive.NewPackage.ListUsersAdapter;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SignInActivity;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


@SuppressWarnings("deprecation")
public class SuperAdminOffice extends BaseActNew implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener {
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
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    String  json2;
    private TranxAdapter grpTranxAdapter;
    private SavedProfileAdapter savedProfileAdapter;
    private UserAdapter2 qbUserAdapter;
    private RedeemRequestAdapter redeemRequestAdapter;
    private ArrayList<Transaction> transactionArrayList;
    ChipNavigationBar chipNavigationBar;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private String surname,firstName,names,cusUserName;
    private FloatingActionButton floatingActionButton;
    private AppCompatTextView txtRev,txtUsers,txtGrpChats;
    private RecyclerView usersRecyclerV, tranxRecyclerV;
    private ArrayList<QBUser> qbUserWithoutCurrent;
    private TransactionDAO transactionDAO;
    private TranxAdapter tranxAdapter;
    TranxAdapter.OnItemsClickListener onItemsClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_super_admin_office);
        gson = new Gson();
        gson1 = new Gson();
        transactionDAO= new TransactionDAO(this);
        transactionArrayList= new ArrayList<>();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("PROFILE_USERNAME", "");
        SharedPrefUserPassword=sharedPreferences.getString("PROFILE_PASSWORD", "");
        floatingActionButton = findViewById(R.id.fabSuper);
        chipNavigationBar = findViewById(R.id.bottom_nav_barSuper);
        ArcNavigationView navigationView = (ArcNavigationView) findViewById(R.id.nav_view_S);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.cus_drawer2);
        txtUsers = findViewById(R.id.ciko_users);
        txtGrpChats = findViewById(R.id.grpChats);
        txtRev = findViewById(R.id.ciko_revenue);
        usersRecyclerV = findViewById(R.id.users_BRecycler);
        tranxRecyclerV = findViewById(R.id.tranx_Recycler);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.setHomeAsUpIndicator(R.drawable.ic_home);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Super Admin BackOffice");
        retrieveAllUser();
        getTransactions();
        transactionArrayList=transactionDAO.getAllTranX();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();

                    }
                });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SuperAdminOffice.this, SuperAdminOffice.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);

            }
        });
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_admin_panel);
        chipNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        //Fragment fragment = null;
                        switch (i){
                            case R.id.cHome:
                                Intent myIntent = new Intent(SuperAdminOffice.this, SuperAdminOffice.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);

                            case R.id.cPurchases:

                                Intent chat = new Intent(SuperAdminOffice.this, TranXActList.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                chat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(chat);


                            case R.id.usersCiko:

                                Intent shop = new Intent(SuperAdminOffice.this, ListUsersActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                shop.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                shop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(shop);

                            case R.id.cikoSupport:

                                Intent pIntent = new Intent(SuperAdminOffice.this, AppSupportAct.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(pIntent);

                        }
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();*/
                    }
                });

    }

    @Override
    protected void initUI() {
        gson = new Gson();
        gson1 = new Gson();
        transactionDAO= new TransactionDAO(this);
        transactionArrayList= new ArrayList<>();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("PROFILE_USERNAME", "");
        SharedPrefUserPassword=sharedPreferences.getString("PROFILE_PASSWORD", "");
        floatingActionButton = findViewById(R.id.fabSuper);
        chipNavigationBar = findViewById(R.id.bottom_nav_barSuper);
        ArcNavigationView navigationView = (ArcNavigationView) findViewById(R.id.nav_view_S);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.cus_drawer2);
        txtUsers = findViewById(R.id.ciko_users);
        txtGrpChats = findViewById(R.id.grpChats);
        txtRev = findViewById(R.id.ciko_revenue);
        usersRecyclerV = findViewById(R.id.users_BRecycler);
        tranxRecyclerV = findViewById(R.id.tranx_Recycler);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.setHomeAsUpIndicator(R.drawable.ic_home);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Super Admin BackOffice");
        retrieveAllUser();
        getTransactions();
        transactionArrayList=transactionDAO.getAllTranX();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();

                    }
                });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SuperAdminOffice.this, SuperAdminOffice.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);

            }
        });
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_admin_panel);
        chipNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        //Fragment fragment = null;
                        switch (i){
                            case R.id.cHome:
                                Intent myIntent = new Intent(SuperAdminOffice.this, SuperAdminOffice.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);

                            case R.id.cPurchases:

                                Intent chat = new Intent(SuperAdminOffice.this, TranXActList.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                chat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(chat);


                            case R.id.usersCiko:

                                Intent shop = new Intent(SuperAdminOffice.this, ListUsersActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                shop.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                shop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(shop);

                            case R.id.cikoSupport:

                                Intent pIntent = new Intent(SuperAdminOffice.this, AppSupportAct.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(pIntent);

                        }
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();*/
                    }
                });


    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void getTransactions(){
        transactionDAO= new TransactionDAO(this);
        transactionArrayList= new ArrayList<>();
        tranxRecyclerV = findViewById(R.id.tranx_Recycler);
        transactionArrayList=transactionDAO.getAllTranX();

        tranxRecyclerV.setLayoutManager(new LinearLayoutManager(SuperAdminOffice.this, LinearLayoutManager.HORIZONTAL, false));
        tranxAdapter = new TranxAdapter(this, transactionArrayList);
        tranxRecyclerV.setItemAnimator(new DefaultItemAnimator());
        tranxRecyclerV.setAdapter(tranxAdapter);
        tranxRecyclerV.setNestedScrollingEnabled(false);
        tranxRecyclerV.setClickable(true);
        tranxAdapter.setCallback(onItemsClickListener);

    }
    private void retrieveAllUser() {
        QBPagedRequestBuilder qbRequestGetBuilder = new QBPagedRequestBuilder();
        qbRequestGetBuilder.setPage(1);
        qbRequestGetBuilder.setPerPage(100);
        usersRecyclerV = findViewById(R.id.users_BRecycler);

        QBUsers.getUsers(qbRequestGetBuilder, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QbUsersHolder.getInstance().putUsers(qbUsers);
                qbUserWithoutCurrent = new ArrayList<>();

                for (QBUser qbUser: qbUsers)
                {
                    //
                    //if(!qbUser.getLogin().equals(QBChatService.().getUser().getLogin()))
                    {
                        qbUserWithoutCurrent.add(qbUser);
                    }
                }

                ListUsersAdapter listUsersAdapter = new ListUsersAdapter(SuperAdminOffice.this, qbUserWithoutCurrent);
                usersRecyclerV.setLayoutManager(new LinearLayoutManager(SuperAdminOffice.this));
                usersRecyclerV.setItemAnimator(new DefaultItemAnimator());
                usersRecyclerV.setAdapter(listUsersAdapter);
                listUsersAdapter.setItemClickListener(SuperAdminOffice.this);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error", e.getMessage());
            }
        });
    }


    private void setupDrawerListener() {
        drawer = findViewById(R.id.cus_drawer2);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {

            case R.id.nav_Sett:
                //toolbar.setTitle("My Customer Packs");
                Intent myIntent = new Intent(SuperAdminOffice.this, AppSettingAct.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                return true;
            case R.id.nav_Super_support:
                //toolbar.setTitle("All Customer Packs");
                Intent allIntent = new Intent(SuperAdminOffice.this, AppSupportAct.class);
                allIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(allIntent);
                return true;
            case R.id.nav_sPurchases:
                //toolbar.setTitle("All Customer Packs");
                Intent purchaseIntent = new Intent(SuperAdminOffice.this, TranXActList.class);
                purchaseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(purchaseIntent);
                return true;
            case R.id.timeline_Super:
                //toolbar.setTitle("All Customer Packs");
                Intent timeLIntent = new Intent(SuperAdminOffice.this, AppTimeLineAct.class);
                timeLIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(timeLIntent);
                return true;
            case R.id.nav_dashS:
                //toolbar.setTitle("All Customer Packs");
                Intent supportIntent = new Intent(SuperAdminOffice.this, SuperAdminOffice.class);
                supportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(supportIntent);
                return true;

            case R.id.nav_log:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(SuperAdminOffice.this, SignInActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
                return true;
        }
        return true;

    }

    @Override
    public void onClick1(View view, int position) {

    }
}