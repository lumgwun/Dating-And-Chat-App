package com.lahoriagency.cikolive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.facebook.CallbackManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lahoriagency.cikolive.Adapters.AddImageAdapter;
import com.lahoriagency.cikolive.Adapters.TimelineAdapter;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.AppServerUser;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.GPSLocationListener;
import com.lahoriagency.cikolive.Classes.LoginReply;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.TimeLine;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Conference.ChatInfoAct;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.TimeLineDAO;
import com.lahoriagency.cikolive.Fragments.ContentFragment;
import com.lahoriagency.cikolive.Fragments.DialogsFragment;
import com.lahoriagency.cikolive.Fragments.EmptyLoginFragment;
import com.lahoriagency.cikolive.Fragments.MatchFragment;
import com.lahoriagency.cikolive.Fragments.SwipeFragment;
import com.lahoriagency.cikolive.Fragments.TestFragment;
import com.lahoriagency.cikolive.Fragments.UserFragment;
import com.lahoriagency.cikolive.Interfaces.GcmConsts;
import com.lahoriagency.cikolive.Interfaces.OnChangeViewListener;
import com.lahoriagency.cikolive.Interfaces.OnLoginChangeView;
import com.lahoriagency.cikolive.NewPackage.ChatDialogActivity;
import com.lahoriagency.cikolive.NewPackage.ChatMatchAct;
import com.lahoriagency.cikolive.NewPackage.ConfChatAct;
import com.lahoriagency.cikolive.SuperAdmin.SuperAdminOffice;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnChangeViewListener, SwipeFragment.OnMatchCreated, MatchFragment.MatchDialogActionsListener, OnLoginChangeView,SharedPreferences.OnSharedPreferenceChangeListener {
    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    int PERMISSION_ALL33 = 2;


    String[] PERMISSIONS33 = {
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.SEND_SMS
    };

    ActivityResultLauncher<Intent> mStartLocForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            userLatLng = intent.getParcelableExtra("UserLatLng");
                        }
                        // Handle the Intent
                    }
                }
            });
    private final android.location.LocationListener locationListenerNetwork = new android.location.LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Network Provider update", Toast.LENGTH_SHORT).show();
                }
            });


            try {
                Geocoder newGeocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
                List<Address> newAddresses = newGeocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder street = new StringBuilder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (Geocoder.isPresent()) {

                        Address newAddress = newAddresses.get(0);

                        localityString = newAddress.getLocality();

                        street.append(localityString).append("");

                        Toast.makeText(MainActivity.this, street,
                                Toast.LENGTH_SHORT).show();

                    }
                }


            } catch (IndexOutOfBoundsException | IOException e) {

                Log.e("tag", e.getMessage());
            }

        }

    };

    private static final String PREF_NAME = "Ciko";
    public static final int PICTURE_REQUEST_CODE = 505;
    private LocationManager locationManager;
    private LocationCallback locationCallback;
    int profileID;
    String address;
    Spinner spnUsers;
    Location customerLoc;
    File destination;
    LatLng userLatLng;

    double latitude = 0;
    double longitude = 0;
    Geocoder geocoder;
    Location location;
    private LocationRequest request;
    private CancellationTokenSource cancellationTokenSource;
    ContentLoadingProgressBar progressBar;
    List<Address> addresses;
    private FusedLocationProviderClient fusedLocationClient;
    String joinedDate,userLocation, localityString,profileName;
    private View mTarget;
    //private DrawView mHistory;
    int PERMISSION_ALL = 1;


    private AppBarConfiguration appBarConfiguration;
    AddImageAdapter addImageAdapter;
    SessionManager sessionManager;
    private EditText edtEmail, edtPassword, edtName;
    private Button mRegister;
    private TextView whereText;
    private ProgressDialog progressDialog;
    String machinePref;
    private Uri pictureLink;
    private SharedPreferences sharedPref;
    private Bundle userExtras;
    private SecureRandom secureRandom;
    private int timeOfDay;

    private DBHelper dbHelper;
    private Calendar calendar;
    private int selectedId, day, month, year, newMonth;
    private StringBuilder welcomeString;
    Gson gson, gson1;
    String json, json1, nIN;
    public  Boolean locationPermissionGranted;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    SharedPreferences userPreferences;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 20; // 20 minutes
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = DialogsFragment.REQUEST_DIALOG_ID_FOR_UPDATE;
    private static final int REQUEST_FINE_LOCATION = 0;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    public ContentFragment contentFragment = null;
    //private FBLoginFragment fbLoginFragment = null;

    protected GoogleApiClient googleApiClient;
    protected LocationRequest locationRequest;
    LocationListener locationListener;
    private OnLoginChangeView onLoginChangeView;
    private CallbackManager callbackManager;
    private QBUser cloudUser;
    private  QBUser currentUser;

    private PreferencesManager preferencesManager;
    private MyPreferences myPreferences;

    private boolean logged;
    private boolean firstVisit;
    ChipNavigationBar chipNavigationBar;
    private boolean isMainActivity;
    private SavedProfile savedProfile;
    private String userName,password;
    private String userSurname,userFirstName;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    Gson gson2,gson3;
    String json2,json3, name;
    private  UserProfileInfo userProfileInfo;
    private Bundle activityBundle;
    private TimelineAdapter timelineAdapter;
    private TimeLineDAO timeLineDAO;
    private RecyclerView timeLineRecyclerView;
    private ArrayList<TimeLine> timeLineArrayList;
    private int savedProfileID;
    private Diamond diamond;
    int diamondCount,diamondID;
    private int collections,qbUserID;
    private FloatingActionButton settingsFab;
    MainActivity mainActivity;
    FloatingActionButton fabConferenceChat;

    private MatchFragment matchDialogFragment;
    private List<UserProfileInfo> matchDialogQueue;
    public void start(Context context, boolean isMainActivity) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("whichActivity", isMainActivity);
        intent.putExtra("QBUser", cloudUser);
        context.startActivity(intent);
    }
    ActivityResultLauncher<Intent> mStartCloudLoginResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            cloudUser = intent.getParcelableExtra("QBUser");
                        }
                        // Handle the Intent
                    }
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });
    private void askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) ==
                PackageManager.PERMISSION_GRANTED) {
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternetConnection();
        FirebaseApp.initializeApp(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        setTitle("CIKO Dating App");
        changeFragment(this);
        cloudUser= new QBUser();
        timeLineArrayList= new ArrayList<>();
        timeLineDAO= new TimeLineDAO(this);
        timeLineRecyclerView = findViewById(R.id.timeLine_Main);
        currentUser= new QBUser();
        savedProfile= new SavedProfile();
        activityBundle= new Bundle();
        settingsFab = findViewById(R.id._navig_settings);
        chipNavigationBar = findViewById(R.id.bottom_nav_barC);
        fabConferenceChat = findViewById(R.id.fab_ex);


        //FragmentManager fm = getSupportFragmentManager();
        calendar=Calendar.getInstance();

        //currentUser = SharedPrefsHelper.getInstance().getQbUser();
        gson = new Gson();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        //fm.beginTransaction().add(R.id.main_content, fbLoginFragment).commit();
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        userName = userPreferences.getString("SAVED_PROFILE_EMAIL", "");
        password = userPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileName = userPreferences.getString("SAVED_PROFILE_NAME", "");

        whereText = findViewById(R.id.whereText);
        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(activityBundle);
                startActivity(myIntent);

            }
        });
        userExtras=getIntent().getExtras();
        if(currentUser==null){
            if(userExtras !=null){
                userLocation=userExtras.getString("UserLocation");
                currentUser=userExtras.getParcelable("QBUser");
                savedProfile=userExtras.getParcelable("SavedProfile");
                userProfileInfo=userExtras.getParcelable("UserProfileInfo");
            }

        }
        if(savedProfile !=null){
            savedProfileID=savedProfile.getSavedProfID();

        }

        matchDialogFragment = new MatchFragment();
        matchDialogFragment.setActionsListener(this);

        matchDialogQueue = new ArrayList<>();
        userExtras= new Bundle();
        if(currentUser !=null){
            qbUserID=currentUser.getId();
        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
            diamondID=diamond.getDiamondWalletID();
            collections=diamond.getDiamondCollections();

        }
        try {
            if(timeLineArrayList.size() >0){
                timeLineArrayList=timeLineDAO.getTimeLineByProfID(savedProfileID);

            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


        timelineAdapter = new TimelineAdapter(MainActivity.this, timeLineArrayList);
        SnapHelper snapHelper = new PagerSnapHelper();
        timeLineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(timeLineRecyclerView);
        timeLineRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        timeLineRecyclerView.setAdapter(timelineAdapter);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        try {
            locationListener = new GPSLocationListener();
            locationDialog();
            createLocationRequest();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            setInitialLocation();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            cancellationTokenSource = new CancellationTokenSource();
            String provider = locationManager.getBestProvider(criteria, true);
            if (provider != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                locationManager.requestLocationUpdates(provider, 2 * 60 * 1000, 10, locationListenerNetwork);
            }
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Network Provider update", Toast.LENGTH_SHORT).show();
                            }
                        });


                        try {
                            Geocoder newGeocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
                            List<Address> newAddresses = newGeocoder.getFromLocation(latitude, longitude, 1);
                            StringBuilder street = new StringBuilder();
                            if (Geocoder.isPresent()) {

                                Address newAddress = newAddresses.get(0);

                                String localityString = newAddress.getLocality();
                                whereText = findViewById(R.id.whereText);
                                whereText.setVisibility(View.VISIBLE);
                                street.append(localityString).append("");

                                whereText.setText(MessageFormat.format("Where you are:  {0}",  localityString));
                                Toast.makeText(MainActivity.this, localityString,
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                whereText.setVisibility(View.GONE);
                                //go
                            }


                        } catch (IndexOutOfBoundsException | IOException e) {

                            Log.e("tag", e.getMessage());
                        }

                    }
                }
            };


        } catch (Exception e ) {
            e.printStackTrace();
        }

        //fbLoginFragment = new FBLoginFragment();



        BottomNavigationView bottomNavigationView=findViewById(R.id.main_navigation);

        //bottomNavigationView.setSelectedItemId(R.id.home);

        chipNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        activityBundle.putParcelable("UserLocation",this.location);
        activityBundle.putParcelable("QBUser", (Parcelable) currentUser);
        activityBundle.putParcelable("SavedProfile",savedProfile);
        activityBundle.putParcelable("UserProfileInfo",userProfileInfo);
        activityBundle.putInt("SAVED_PROFILE_ID",profileID);
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onItemSelected(int i) {
                        //Fragment fragment = null;
                        switch (i){
                            case R.id.menu_purchaseD:
                                Intent myIntent = new Intent(MainActivity.this, PurchaseDiamondActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtras(activityBundle);
                                startActivity(myIntent);

                            case R.id.menu_chat_dialog:

                                Intent myIntentChatDialog = new Intent(MainActivity.this, ChatDialogActivity.class);
                                myIntentChatDialog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntentChatDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntentChatDialog.putExtras(activityBundle);
                                startActivity(myIntentChatDialog);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);


                            case R.id.menu_dialog2:
                                Intent dialogIntent = new Intent(MainActivity.this, DialogsActivity.class);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                dialogIntent.putExtras(activityBundle);
                                startActivity(dialogIntent);




                        }
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();*/
                    }
                });
        fabConferenceChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confChatIntent = new Intent(MainActivity.this, ConfChatAct.class);
                confChatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                confChatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                confChatIntent.putExtras(activityBundle);
                startActivity(confChatIntent);

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.menu_main_main:

                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                            overridePendingTransition(R.anim.slide_in_right,
                                    R.anim.slide_out_left);
                        }
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myIntent.putExtras(activityBundle);
                        startActivity(myIntent);
                        return true;
                    case R.id.menu_host_main:
                        Intent redeemIntent = new Intent(MainActivity.this, HostMainActivity.class);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        redeemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        redeemIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        redeemIntent.putExtras(activityBundle);
                        startActivity(redeemIntent);

                        return true;

                    case R.id.menu_find_match:
                        Intent findMatchIntent = new Intent(MainActivity.this, FindMatchActivity.class);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        findMatchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        findMatchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        findMatchIntent.putExtras(activityBundle);
                        startActivity(findMatchIntent);

                        return true;


                    case R.id.menu_matched_chat:
                        Intent chatMatchIntent = new Intent(MainActivity.this, ChatMatchAct.class);
                        chatMatchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        chatMatchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        chatMatchIntent.putExtras(activityBundle);
                        startActivity(chatMatchIntent);

                        return true;


                    case R.id.menu_Contents:
                        Intent contentIntent = new Intent(MainActivity.this, ContentAct.class);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        contentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contentIntent.putExtras(activityBundle);
                        startActivity(contentIntent);

                        return true;

                }
                return false;
            }
        });
        calendar=Calendar.getInstance();
        welcomeString = new StringBuilder();

        timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 5 && timeOfDay < 12) {
            welcomeString.append(getString(R.string.good_morning));
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            welcomeString.append(getString(R.string.good_afternoon));

        } else {
            welcomeString.append(getString(R.string.good_evening));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_profileA:
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                profileIntent.putExtras(activityBundle);
                startActivity(profileIntent);

                return true;

            case R.id.menu_set_main:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                settingsIntent.putExtras(activityBundle);
                startActivity(settingsIntent);

                return true;

            case R.id.me_chatting:
                Intent chatIntent = new Intent(MainActivity.this, ChattingActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chatIntent.putExtras(activityBundle);
                startActivity(chatIntent);

                return true;
            case R.id.menu_chat:
                Intent cIntent = new Intent(MainActivity.this, ChatAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                cIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                cIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cIntent.putExtras(activityBundle);
                startActivity(cIntent);

                return true;


            case R.id.menu_set_chat_info:
                Intent mIntent = new Intent(MainActivity.this, ChatInfoAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtras(activityBundle);
                startActivity(mIntent);

                return true;


            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean checkInternetConnection() {
        boolean hasInternetConnection = hasInternetConnection();
        if (!hasInternetConnection) {
            showWarningDialog("Internet connection failed");
        }

        return hasInternetConnection;
    }

    public void showWarningDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*SharedPreferences sharedPreferences =
                settingsFragment.getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    public void changeFragment(MainActivity mainActivity) {
        if (firstVisit) {
            firstVisit = false;
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_content, new TestFragment());
            fragmentTransaction.commit();
        } else {
            ContentFragment contentFragment = new ContentFragment();
            contentFragment.setOnMatchCreatedListener(mainActivity);
            if (mainActivity.getIntent() != null && mainActivity.getIntent().getExtras() != null) {
                if (mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_DIALOG_ID) != null) {
                    contentFragment.setDialogId(mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_DIALOG_ID));
                    contentFragment.setRecipientId(Integer.valueOf(mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_RECIPIENT_ID)));
                } else if (mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_NEW_PAIR) != null) {
                    contentFragment.setDialogId(mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_NEW_PAIR));
                }
            }

            mainActivity.setContentFragment(contentFragment);
            FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, contentFragment);
            transaction.commit();
        }
    }

    private void startLogin(boolean logFromSession) {
        if (!logged) {
            if(!logFromSession) {
            }
            onLoginChangeView.hideContent();
            logged = true;
            if(checkSignIn())
                restoreChatSession();
            else {
                QBUser user = new QBUser(myPreferences.getFbId().toString(), myPreferences.getFbId().toString());
                user.setFullName(myPreferences.getFirstName() + " " + myPreferences.getLastName());
                login(user);
            }
        }
    }

    private void handleLoginResponse(String response) {
        try {
            if (response != null) {
                LoginReply loginReply = AppE.getGson().fromJson(response, LoginReply.class);
                if (loginReply.isStatusOkay() | loginReply.getStatus().equals("registered")) {
                    myPreferences.setUserId(loginReply.getUserId());
                    myPreferences.setFirstName(loginReply.getFirstName());
                    myPreferences.setLastName(loginReply.getLastName());
                    myPreferences.setBirthday(new SimpleDateFormat("MM/dd/yyyy").parse(loginReply.getBirthday()));
                    myPreferences.setMinMatchValue(loginReply.getMinMatchValue());
                    myPreferences.setSexChoice(loginReply.getSexChoice());
                    myPreferences.setRadious(loginReply.getRadius());
                    myPreferences.setAgeRangeMin(loginReply.getAgeRangeMin());
                    myPreferences.setAgeRangeMax(loginReply.getAgeRangeMax());
                    myPreferences.setDescription(loginReply.getDescription());
                    myPreferences.setMbtiType(loginReply.getType() == null ? "" : loginReply.getType());
                    preferencesManager.savePreferences();
                    if(loginReply.getStatus().equals("registered")) {
                        firstVisit = true;
                    }
                } else {
                    Toast.makeText(this, "server error", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "login error" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void restoreChatSession(){
        if (!ChatHelper.getInstance().isLogged()) {
            currentUser = getUserFromSession();
            loginToChat(currentUser);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("QBUser", currentUser);
            startActivity(intent);
        }
    }

    private QBUser getUserFromSession(){
        currentUser = SharedPrefsHelper.getInstance().getQbUser();
        currentUser.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        return currentUser;
    }

    protected boolean checkSignIn() {
        return SharedPrefsHelper.getInstance().hasQbUser();
    }

    private void login(final QBUser user) {
        ChatHelper.getInstance().login2(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Intent chatIntent = new Intent(MainActivity.this, ChatAct.class);
                chatIntent.putExtras(bundle);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(chatIntent);
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
                Toast.makeText(AppE.getAppContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginToChat(final QBUser user) {

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                bundle.putParcelable("QBUser", (Parcelable) user);
                Intent chatIntent = new Intent(MainActivity.this, ChatAct.class);
                chatIntent.putExtras(bundle);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(chatIntent);
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void savePrefs(SavedProfile profile) {
        myPreferences.setImageURL(String.valueOf(profile.getSavedPImage()));
        preferencesManager.savePreferences();
    }
    /*private void setProfileTracker() {
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                this.stopTracking();
                Profile.setCurrentProfile(newProfile);

                if (newProfile == null)
                    logged = false;
                else if (newProfile != null) {
                    savePrefs(newProfile);
                }
            }

        };
        profileTracker.startTracking();
    }*/


    public void locationDialog() {

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(MIN_TIME_BW_UPDATES);
        locationRequest.setFastestInterval(MIN_TIME_BW_UPDATES / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (googleApiClient != null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                    locationDialog();
                }
            }
        } else if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
            Fragment fragment = contentFragment.getFragmentForPosition(2);
            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == UserFragment.REQUEST_SETTINGS_CODE) {
            if (data != null) {
                if (data.getIntExtra("action", 0) == SettingsActivity.LOGOUT_ACTION) {
                    logout();
                } else if (data.getIntExtra("action", 0) == SettingsActivity.START_TEST_ACTION) {
                    showTest();
                }
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }



    @Override
    public void showTest() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, new TestFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void logout() {
        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent loginIntent = new Intent(this, SignInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, new FBLoginFragment());
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.commit();*/
    }

    @Override
    public void showSettings() {

    }

    @Override
    public void showContent() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, new ContentFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public ContentFragment getContentFragment() {
        return contentFragment;
    }

    public void setContentFragment(ContentFragment contentFragment) {
        this.contentFragment = contentFragment;
    }

    @Override
    public void showMatchDialog(UserProfileInfo userProfileInfo, boolean fromQueue) {
        if (fromQueue) {
            showAndFillMatchDialog(matchDialogQueue.remove(0));
        } else {
            if (matchDialogFragment.isAddedToLayout() || matchDialogFragment.isAdded()) {
                matchDialogQueue.add(userProfileInfo);
            } else {
                showAndFillMatchDialog(userProfileInfo);
            }
        }
    }

    @Override
    public void showMatchDialog(SavedProfile savedProfile, boolean fromQueue) {

    }

    @Override
    public void showMatchDialog(AppServerUser appServerUser, boolean fromQueue) {

    }

    private void showAndFillMatchDialog(UserProfileInfo userProfileInfo) {
        QBUserCustomData userCustomData = new Gson().fromJson(SharedPrefsHelper.getInstance().getQbUser().getCustomData(), QBUserCustomData.class);
        matchDialogFragment.setImageLeftLink(userCustomData.getProfilePhotoData().get(0).getLink());
        matchDialogFragment.setImageRightLink(userProfileInfo.getPhotoLinks().get(0));
        matchDialogFragment.setRecipientId(userProfileInfo.getUserQbId());
        matchDialogFragment.setMatchUserName(userProfileInfo.getName());
        matchDialogFragment.setMatchValue(userProfileInfo.getMatchValue());
        matchDialogFragment.setAddedToLayout(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content, matchDialogFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void keepSwipingButtonClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        matchDialogFragment.setAddedToLayout(false);
        ft.remove(matchDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void sendMessageClicked(Integer recipientId) {
        matchDialogQueue.clear();
        keepSwipingButtonClicked();
        contentFragment.openChatDialog(recipientId);
    }

    @Override
    public void showMatchDialogIfAny() {
        if (!matchDialogQueue.isEmpty()) {
            showMatchDialog((AppServerUser) null, true);
        }
    }

    @Override
    public void hideContent() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_content, new EmptyLoginFragment(), "HIDE_CONTENT");
        fragmentTransaction.commit();
    }
    protected void createLocationRequest() {
        if (!hasPermissions(MainActivity.this, PERMISSIONS33)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS33, PERMISSION_ALL33);

        }


        request = new LocationRequest();
        request.setSmallestDisplacement(10);
        request.setFastestInterval(50000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(3);



    }
    private void setInitialLocation() {
        whereText = findViewById(R.id.whereText);
        final double[] newLat = {0.00};
        final double[] newLng = {0.00};

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (locationPermissionGranted) {
            @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        location = task.getResult();
                        newLat[0] = location.getLatitude();
                        newLng[0] = location.getLongitude();


                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());

                    }
                }
            });
        }
        if(location !=null){
            newLat[0] = location.getLatitude();
            newLng[0] = location.getLongitude();

        }


        try {
            Geocoder newGeocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
            List<Address> newAddresses = newGeocoder.getFromLocation(newLat[0], newLng[0], 1);
            StringBuilder street = new StringBuilder();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (Geocoder.isPresent()) {

                    //txtLoc.setVisibility(View.VISIBLE);

                    Address newAddress = newAddresses.get(0);

                    String localityString = newAddress.getLocality();
                    whereText = findViewById(R.id.whereText);
                    whereText.setVisibility(View.VISIBLE);

                    street.append(localityString).append("");
                    whereText.setText("Where you are"+localityString);

                    Toast.makeText(MainActivity.this, street,
                            Toast.LENGTH_SHORT).show();

                } else {
                    whereText.setVisibility(View.GONE);
                    //go
                }
            }


        } catch (IndexOutOfBoundsException | IOException e) {

            Log.e("tag", e.getMessage());
        }

    }
    private boolean checkPermissions() {
        int fineLocationPermissionState = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);

        int backgroundLocationPermissionState = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            backgroundLocationPermissionState = ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }

        return (fineLocationPermissionState == PackageManager.PERMISSION_GRANTED) &&
                (backgroundLocationPermissionState == PackageManager.PERMISSION_GRANTED);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //locationTracker.onRequestPermission(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        whereText = findViewById(R.id.whereText);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1002: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        //setupLocationManager();

                    }
                } else {

                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("Awajima App",
                            BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            break;
            case 1000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    fusedLocationClient.getCurrentLocation(2, cancellationTokenSource.getToken())
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location2) {
                                    if (location2 != null) {

                                        latitude = location2.getLatitude();
                                        longitude = location2.getLongitude();
                                        userLatLng = new LatLng(latitude, longitude);
                                        long duration = 1000;


                                    }
                                    userLatLng = new LatLng(latitude, longitude);


                                }
                            });
                    //MainActivity.this.location2 = location2;

                    geocoder = new Geocoder(this, Locale.getDefault());

                    if (geocoder != null) {
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {

                            address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getLocality();
                            whereText = findViewById(R.id.whereText);
                            whereText.setVisibility(View.VISIBLE);

                            whereText.setText("Where you are"+city);



                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }


                    //txtLoc.setText(MessageFormat.format("My Loc:{0},{1}/{2}", latitude, longitude, city));


                } else {
                    Intent intent = new Intent();
                    intent.setAction(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("Skylight App",
                            BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == REQUEST_FINE_LOCATION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                        locationDialog();
                    }
                }
            }

        }




    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }


    public static boolean isOnline(ConnectivityManager cm) {
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    public void goAdmin(View view) {
        Intent myIntent = new Intent(MainActivity.this, SuperAdminOffice.class);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
    }
}