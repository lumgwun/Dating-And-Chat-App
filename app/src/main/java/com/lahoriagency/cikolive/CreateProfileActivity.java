package com.lahoriagency.cikolive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.multidex.BuildConfig;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.lahoriagency.cikolive.Adapters.AddImageAdapter;
import com.lahoriagency.cikolive.Adapters.PictureAdapter;
import com.lahoriagency.cikolive.Adapters.ProfMediaAdapter;
import com.lahoriagency.cikolive.Adapters.VideoAdapter;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.Classes.LoginReply;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Classes.ValidationUtils;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.SavedProfileDAO;
import com.lahoriagency.cikolive.NewPackage.AppRefReceiver;
import com.lahoriagency.cikolive.NewPackage.ChatDialogActivity;
import com.lahoriagency.cikolive.NewPackage.ChatMainAct;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;


@SuppressWarnings("deprecation")
public class CreateProfileActivity extends BaseActivity {
    private String cityStrg;

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
                    Toast.makeText(CreateProfileActivity.this, "Network Provider update", Toast.LENGTH_SHORT).show();
                }
            });


            try {
                Geocoder newGeocoder = new Geocoder(CreateProfileActivity.this, Locale.ENGLISH);
                List<Address> newAddresses = newGeocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder street = new StringBuilder();
                if (Geocoder.isPresent()) {

                    Address newAddress = newAddresses.get(0);
                    txtLoc = findViewById(R.id.where_You_Are);
                    txtLoc.setVisibility(View.VISIBLE);

                    localityString = newAddress.getLocality();
                    country=newAddress.getCountryName();
                    cityStrg= newAddress.getCountryName();

                    String city = localityString+","+country;

                    if(city !=null){
                        txtLoc.setText(MessageFormat.format("My Loc:{0}",  city));

                    }

                    Toast.makeText(CreateProfileActivity.this, street,
                            Toast.LENGTH_SHORT).show();

                }


            } catch (IndexOutOfBoundsException | IOException e) {

                Log.e("tag", e.getMessage());
            }

        }

    };

    AddImageAdapter addImageAdapter;
    SessionManager sessionManager;
    //private EditText edtEmail, edtPassword, edtName;
    //private Button mRegister,btnUpdate;
    private TextView existaccount;
    private ProgressDialog progressDialog;
    int profileID;
    int customerID;
    String machinePref;
    Uri pictureLink;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1;
    String json, json1, nIN;
    private QBUser cloudUser;
    private  QBUser currentUser;
    private boolean logged;
    private boolean firstVisit;
    private SavedProfile savedProfile;
    private String userName,password;
    Gson gson2,gson3;
    String json2,json3, name;
    private UserProfileInfo userProfileInfo;
    private Bundle activityBundle;
    private int savedProfileID;
    private Diamond diamond;
    int diamondCount,diamondID;
    private int collections,qbUserID;
    public static final int PICTURE_REQUEST_CODE = 505;
    ContentLoadingProgressBar progressBar;
    private DBHelper dbHelper;
    private LocationManager locationManager;
    private LocationCallback locationCallback;
    String address,country;
    private LocationRequest request;
    AppCompatEditText firstName;
    AppCompatEditText surname1;
    Bitmap thumbnail;
    //private GoogleApiClient googleApiClient;
    protected DatePickerDialog datePickerDialog;
    Random ran;
    private double accountBalance;
    SecureRandom random;

    Context context;
    String myAge,aboutMe,myIntrest;
    PreferenceManager preferenceManager;
    private Bundle bundle;
    AppCompatTextView dobText;
    String mImageUriString;
    private ProgressBar loadingPB;
    private boolean locationPermissionGranted;
    String managerUserName;
    String mLastUpdateTime, selectedGender, selectedOffice, selectedState;
    String uFirstName, uSurname,  uUserName;
    AppCompatSpinner  spnGender;
    Location mCurrentLocation = null;
    SQLiteDatabase sqLiteDatabase;
    private Uri mImageUri;
    ByteArrayOutputStream bytes;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private Calendar calendar;
    List<Address> addresses;
    private Calendar cal;
    private FusedLocationProviderClient fusedLocationClient;
    int selectedId, day, month, year, newMonth;
    File destination;
    double latitude = 0;
    double longitude = 0;
    Geocoder geocoder;
    Date date;
    private String picturePath,welcomeMessage;
    private Random random1;
    private final String ALPHABET = "10123456346666";
    private String referrerUrl;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String REQUIRED = "Required";
    private static final int RESULT_CAMERA_CODE = 22;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Spinner spnUsers;
    Location customerLoc;
    LatLng userLatLng;
    Location location;
    private int userGender;
    private CancellationTokenSource cancellationTokenSource;
    //map_API_key": "AIzaSyDfJ4j1Cuo_rBSN3zxa4tXm3FprvSGsId4"

    String joinedDate, localityString,profileName;
    private View mTarget;
    //private DrawView mHistory;
    private AppCompatEditText edtEmailAddress, passwordTextView;
    private AppCompatButton fireBaseBtn;
    private ContentLoadingProgressBar progressbar;
    private FirebaseAuth mAuth;
    int PERMISSION_ALL = 1;
    private CircleImageView profilePix;
    int timeOfDay;
    private static final boolean isPersistenceEnabled = false;
    private LinearLayoutCompat layoutUp;
    private ScrollView scrollLayoutDown;
    SharedPreferences userPreferences;
    private List<Uri> mediaPathList;

    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    int PERMISSION_ALL33 = 2;
    InstallReferrerClient referrerClient;
    private String m_szWLANMAC;
    private String m_szBTMAC;
    private String m_szLongID;
    private MessageDigest m;
    private WifiManager wm;
    private BluetoothAdapter m_BluetoothAdapter;
    private  String response;
    private final String responseString="registered";
    private LoginReply loginReply;
    String referrer = "";
    String[] PERMISSIONS33 = {
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,Manifest.permission.INSTALL_LOCATION_PROVIDER, Manifest.permission.SEND_SMS
    };
    AppCompatTextView txtLoc, textReturnedWelcome;
    AppCompatEditText edtAge,et_interests,et_aboutC,edt_Name,et_Password, et_diamond,edtPhone;
    private CountryCodePicker countryCodePicker;
    private long referrerClickTime,appInstallTime;
    private  boolean instantExperienceLaunched;
    private SavedProfile LastProfileUsed;
    private String deviceID;
    private QBUser qbUser;
    private AppCompatButton btnSignUp,btnFinal;
    private RecyclerView recyclerViewPhoto, recyclerViewVideo;
    private QBResRequestExecutor requestExecutor ;
    private CheckBox checkBox;
    private RadioGroup radioSexGroup;
    private  TextView txtSignIn;
    private ImageView btn_back;
    private CircularImageView profile_image_;

    String lookingFor=null;
    String age=null;
    int myGender=0;
    String email=null;
    String UserLocation=null;
    Uri profilePicture=null;
    int savedProfID=0;
    String interest=null;
    String dateJoined=null;
    String status=null;
    private  int myAgeInt;
    private long profileIDLong;
    public static int SPLASH_SCREEN = 3000;
    QBEntityCallback<QBUser> callback;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    RecyclerView picVideoRecyView;
    private int resultCode;
    int PICK_IMAGE_MULTIPLE = 2;
    String realImagePath,giftTime;
    private Bitmap bitmap;
    private ProfMediaAdapter profMediaAdapter;
    ArrayList<Uri> mArrayUri;
    Uri deepLink = null;
    private Uri profileImageUri,profileVideoUri;
    private FirebaseDynamicLinks firebaseDynamicLinks;
    private DynamicLink dynamicLink;
    private VideoAdapter videoAdapter;
    private VideoAdapter.VideoListener videoListener;
    String diamondStr, office,state,role,userLocation,passwordStg,emailStrg,gender;
    private ClipData clipData;
    private int noOfDiamond;
    private PictureAdapter pictureAdapter;
    private PictureAdapter.ItemListener pictureListener;
    private RadioButton radioBtnFemale,radioBtnMale;
    private AppRefReceiver appRefReceiver;
    public static void start(Context context) {
        Intent intent = new Intent(context, CreateProfileActivity.class);
        context.startActivity(intent);

    }
    ActivityResultLauncher<Intent> mGetPixContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            resultCode=result.getResultCode();
                            Intent data = result.getData();
                            //mImageUri = data.getData();
                            userProfileInfo= new UserProfileInfo();

                            mArrayUri = new ArrayList<Uri>();
                            ArrayList<String> photoResult = new ArrayList<>();

                            if (data != null) {
                                clipData = data.getClipData();
                            }
                            if(clipData != null) {
                                for(int i=0;i<clipData.getItemCount();i++) {
                                    ClipData.Item videoItem = clipData.getItemAt(i);
                                    Uri photoURI = videoItem.getUri();
                                    String filePath = getPath(CreateProfileActivity.this, photoURI);
                                    photoResult.add(filePath);
                                }
                                userProfileInfo.setPhotoLinks(photoResult);
                                pictureAdapter = new PictureAdapter(CreateProfileActivity.this,photoResult,pictureListener);
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(CreateProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewPhoto.setLayoutManager(layoutManager);
                                recyclerViewPhoto.setItemAnimator(new DefaultItemAnimator());
                                SnapHelper snapHelper = new PagerSnapHelper();
                                snapHelper.attachToRecyclerView(recyclerViewPhoto);
                                recyclerViewPhoto.setAdapter(pictureAdapter);
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), profileImageUri);
                                    profile_image_.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Uri videoURI = data.getData();
                                String filePath = getPath(CreateProfileActivity.this, videoURI);
                                photoResult.add(filePath);
                            }

                            Toast.makeText(CreateProfileActivity.this, "Image picking returned successful", Toast.LENGTH_SHORT).show();
                            //doProcessing();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(CreateProfileActivity.this, "Activity canceled", Toast.LENGTH_SHORT).show();
                            //finish();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + result.getResultCode());
                    }
                }
                /*@Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        finish();
                    }
                }*/
            });
    ActivityResultLauncher<Intent> mGetVideoContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            resultCode=result.getResultCode();
                            Intent data = result.getData();
                            userProfileInfo= new UserProfileInfo();
                            mArrayUri = new ArrayList<Uri>();
                            ArrayList<String> videoResult = new ArrayList<>();

                            if (data != null) {
                                clipData = data.getClipData();
                            }
                            if(clipData != null) {
                                for(int i=0;i<clipData.getItemCount();i++) {
                                    ClipData.Item videoItem = clipData.getItemAt(i);
                                    Uri videoURI = videoItem.getUri();
                                    String filePath = getPath(CreateProfileActivity.this, videoURI);
                                    videoResult.add(filePath);
                                }
                                userProfileInfo.setVideoLinks(videoResult);
                                videoAdapter = new VideoAdapter(CreateProfileActivity.this, mArrayUri, videoListener);
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(CreateProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewVideo.setLayoutManager(layoutManager);
                                recyclerViewVideo.setItemAnimator(new DefaultItemAnimator());
                                SnapHelper snapHelper = new PagerSnapHelper();
                                snapHelper.attachToRecyclerView(recyclerViewVideo);
                                recyclerViewVideo.setAdapter(videoAdapter);
                                recyclerViewVideo.setNestedScrollingEnabled(false);
                                recyclerViewVideo.setClickable(true);
                            }
                            else {
                                Uri videoURI = data.getData();
                                String filePath = getPath(CreateProfileActivity.this, videoURI);
                                videoResult.add(filePath);
                            }

                            if (data != null && data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                //mArrayUri=data.getClipData().

                                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                                for(int i = 0; i < count; i++) {
                                    Uri videoUri = data.getClipData().getItemAt(i).getUri();
                                }
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    profileVideoUri = item.getUri();




                                }
                            }
                            Toast.makeText(CreateProfileActivity.this, "Video picking returned successful", Toast.LENGTH_SHORT).show();
                            //doProcessing();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(CreateProfileActivity.this, "Activity canceled", Toast.LENGTH_SHORT).show();
                            //finish();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + result.getResultCode());
                    }
                }
                /*@Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        finish();
                    }
                }*/
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_profile);
        if (!hasPermissions(CreateProfileActivity.this, PERMISSIONS33)) {
            ActivityCompat.requestPermissions(CreateProfileActivity.this, PERMISSIONS33, PERMISSION_ALL33);
        }
        checkInternetConnection();
        FirebaseApp.initializeApp(this);
        appRefReceiver= new AppRefReceiver();
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        init();
        mArrayUri = new ArrayList<Uri>();
        getDeviceID(deviceID);
        calendar = Calendar.getInstance();
        loginReply= new LoginReply();

        //listeners();
        setTitle("Create a new  Profile");
        dbHelper=new DBHelper(this);
        LastProfileUsed= new SavedProfile();
        random1 = new SecureRandom();
        qbUser= new QBUser();
        cloudUser= new QBUser();
        currentUser= new QBUser();
        diamond = new Diamond();
        savedProfile= new SavedProfile();
        activityBundle= new Bundle();
        gson = new Gson();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        getCikoLiveReferrer(referrerClient);
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        userPreferences= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        requestExecutor = new QBResRequestExecutor();
        //addImageAdapter = new AddImageAdapter();
        welcomeMessage="Welcome to the Community, chat and build your networth";
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
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        txtLoc = findViewById(R.id.where_You_Are);
        recyclerViewPhoto = findViewById(R.id.rv_picP);
        recyclerViewVideo = findViewById(R.id.rv_videos);
        edtAge = findViewById(R.id.et_ageC);
        edt_Name = findViewById(R.id.et_nameC);
        et_aboutC = findViewById(R.id.et_aboutC);
        et_interests = findViewById(R.id.et_interestsC);
        checkBox = findViewById(R.id.checkboxC);
        edtEmailAddress = findViewById(R.id.et_emailC);
        et_Password = findViewById(R.id.et_PasswordC);
        et_diamond = findViewById(R.id.et_diamond);

        radioSexGroup=(RadioGroup)findViewById(R.id.sexgroup);

        btnSignUp = findViewById(R.id.btn_submit);
        //btnUpdate = findViewById(R.id.btn_submit);
        spnGender = findViewById(R.id.spn_Gender);
        txtSignIn = findViewById(R.id.txt_Btn_SignIn);
        btn_back = findViewById(R.id.btn_back);
        fireBaseBtn = findViewById(R.id.btn_Firebase);
        layoutUp = findViewById(R.id.layoutTop);
        scrollLayoutDown = findViewById(R.id.layoutDown);
        textReturnedWelcome = findViewById(R.id.WelcomeE);
        //picVideoRecyView = findViewById(R.id.rv_picP);

        recyclerViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(CreateProfileActivity.this, PERMISSIONS33)) {
                    ActivityCompat.requestPermissions(CreateProfileActivity.this, PERMISSIONS, PERMISSION_ALL33);
                }
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.setType("*/*");
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mGetVideoContent.launch(intent);



            }

        });
        recyclerViewVideo.setOnClickListener(this::getVideos);

        if(savedProfile !=null){
            savedProfileID=savedProfile.getSavedProfID();

        }
        if(currentUser !=null){
            qbUserID=currentUser.getId();
        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
            diamondID=diamond.getDiamondWalletID();
            collections=diamond.getDiamondCollections();

        }
        activityBundle.putParcelable("QBUser", (Parcelable) currentUser);
        activityBundle.putParcelable("SavedProfile",savedProfile);
        activityBundle.putParcelable("UserProfileInfo",userProfileInfo);

        Animation translater22 = AnimationUtils.loadAnimation(this, R.anim.bounce);


        btnSignUp.setOnClickListener(this::registerUser);
        btnSignUp.setAnimation(translater22);

        profileID = ThreadLocalRandom.current().nextInt(122, 1631);
        profile_image_ = findViewById(R.id.takePhoto);
        mAuth = FirebaseAuth.getInstance();


        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(CreateProfileActivity.this, SignInActivity.class);
                chatIntent.putExtras(activityBundle);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(chatIntent);

            }
        });


        profile_image_.setOnClickListener(this::doSelectPix);
        StringBuilder welcomeString = new StringBuilder();

        timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);


        day = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = getResources().getStringArray(R.array.days);
        String dow = "";

        switch (day) {
            case Calendar.SUNDAY:
                dow = days[0];
                break;
            case Calendar.MONDAY:
                dow = days[1];
                break;
            case Calendar.TUESDAY:
                dow = days[2];
                break;
            case Calendar.WEDNESDAY:
                dow = days[3];
                break;
            case Calendar.THURSDAY:
                dow = days[4];
                break;
            case Calendar.FRIDAY:
                dow = days[5];
                break;
            case Calendar.SATURDAY:
                dow = days[6];
                break;
            default:
                break;
        }


        welcomeString.append(getString(R.string.happy))
                .append(dow);

        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectedGender = spnGender.getSelectedItem().toString();
                selectedGender = (String) parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Animation translater = AnimationUtils.loadAnimation(this, R.anim.bounce);


        profile_image_.startAnimation(translater);
        profile_image_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(CreateProfileActivity.this, PERMISSIONS33)) {
                    ActivityCompat.requestPermissions(CreateProfileActivity.this, PERMISSIONS, PERMISSION_ALL33);
                }
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.setType("*/*");
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mGetPixContent.launch(intent);

                /*final PopupMenu popup = new PopupMenu(CreateProfileActivity.this, profile_image_);
                popup.getMenuInflater().inflate(R.menu.profile_pix, popup.getMenu());
                setTitle("Photo selection in Progress...");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.Camera) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, RESULT_CAMERA_CODE);

                        }

                        if (item.getItemId() == R.id.Gallery) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }
                        if (item.getItemId() == R.id.cancel_action) {
                            setTitle("Ciko Live onBoarding");
                        }

                        return true;
                    }
                });
                popup.show();//showing popup menu*/


            }



        });


        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        joinedDate = mdformat.format(calendar.getTime());

        fireBaseBtn.setOnClickListener(this::doFireBase);
        progressbar = findViewById(R.id.progressBarC);
        radioBtnMale = findViewById(R.id.sex_Male);
        radioBtnFemale = findViewById(R.id.sex_Female);


        progressDialog = new ProgressDialog(this);
        profileID = ThreadLocalRandom.current().nextInt(1125, 10450);
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        .setUrl("https://cikolive.page.link/")
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.lahoriagency.cikolive",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();
        handleFirebaseDynamicLink(profileID);


//https://cikolive.page.link/finishSignUp?SAVED_PROFILE_ID="+profileID
        fireBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password;
                email = edtEmailAddress.getText().toString();
                password = et_Password.getText().toString();
                name=edt_Name.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter Your Name!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmailAddress.setError("Invalid Email");
                    edtEmailAddress.setFocusable(true);
                } else if (password.length() < 4) {
                    passwordTextView.setError("Length Must be greater than 4 character");
                    passwordTextView.setFocusable(true);
                } else {
                    scrollLayoutDown.setVisibility(View.VISIBLE);
                    layoutUp.setVisibility(View.GONE);
                    completeSignUp(getIntent());
                    //registerNewUser(email, password,joinedDate,name);
                    registerNewFireBaseUser(email, password,joinedDate,name,actionCodeSettings,profileID);

                }



            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                getDeviceID(deviceID);
                emailStrg = edtEmailAddress.getText().toString();
                passwordStg = et_Password.getText().toString();
                name=edt_Name.getText().toString();
                myAge = edtAge.getText().toString();
                myIntrest = et_interests.getText().toString();
                aboutMe = et_aboutC.getText().toString();
                getUserCountry(CreateProfileActivity.this);
                textReturnedWelcome.setText("Welcome Back"+" "+name);
                //passwordTextView.setText("Your Password: passwordStg");
                radioBtnMale = findViewById(R.id.sex_Male);
                radioBtnFemale = findViewById(R.id.sex_Female);
                profileName = edt_Name.getText().toString();
                diamondStr = et_diamond.getText().toString();

                RadioGroup radioGroup = findViewById(R.id.sexgroup);
                noOfDiamond= Integer.parseInt(diamondStr);
                //RadioButton radioGroup = (RadioButton) findViewById(radioSexGroup.getCheckedRadioButtonId());
                if (radioGroup != null) {
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            gender = (R.id.sex_Male == checkedId) ? "Male" : "Female";
                            String text = "You selected: " + userGender;
                            Toast.makeText(CreateProfileActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //userGender=radioButton.getText().equals("Male")?0:1;
                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please select the Gender!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(myAge)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter Your Age!!",
                            Toast.LENGTH_LONG)
                            .show();
                }if (TextUtils.isEmpty(myIntrest)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter Your Interest!!",
                            Toast.LENGTH_LONG)
                            .show();
                }if (TextUtils.isEmpty(aboutMe)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter Your About Me!!",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    myAgeInt= Integer.parseInt(myAge);
                    mImageUri= profileImageUri;
                    LastProfileUsed= new SavedProfile(profileID,profileName,emailStrg,passwordStg,aboutMe,myIntrest,myAge,gender,selectedGender,joinedDate,country,cityStrg,mImageUri);
                    qbUser= new QBUser();
                    qbUser.setCustomData(myAge);
                    qbUser.setEmail(emailStrg);
                    qbUser.setPassword(passwordStg);
                    qbUser.setCustomDataClass(LastProfileUsed.getClass());
                    qbUser.setFullName(profileName);
                    qbUser.setExternalId(String.valueOf(profileID));
                    //finishRegisteration(qbUser, LastProfileUsed);
                    startSignUpNewUser(profileID,qbUser,LastProfileUsed,myAgeInt,aboutMe,myIntrest,country,selectedGender,mImageUri,noOfDiamond);

                }


                //startActivity(new Intent(CreateProfileActivity.this, SignInActivity.class));
            }
        });
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            // OK, you can do your job
        }

    }
    private void getCikoLiveReferrer(InstallReferrerClient referrerClient) {
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        appRefReceiver= new AppRefReceiver();

                        try {
                            response = referrerClient.getInstallReferrer();

                            String referrerUrl = response.getInstallReferrer();

                            long referrerClickTime = response.getReferrerClickTimestampSeconds();

                            long appInstallTime = response.getInstallBeginTimestampSeconds();

                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                            referrer = response.getInstallReferrer();
                            //IntentFilter filter = new IntentFilter();
                            Intent intent = new Intent();
                            intent.setAction("com.example.broadcast.MY_NOTIFICATION");
                            intent.putExtra("data", "Nothing to see here, move along.");
                            sendBroadcast(intent);

                            IntentFilter filter = new IntentFilter("com.android.vending.INSTALL_REFERRER");
                            filter.addAction(Intent.ACTION_MAIN);
                            registerReceiver(appRefReceiver, filter, "android.permission.INSTALL_PACKAGES", null );
                            registerReceiver(appRefReceiver, filter );


                            //refrerTV.setText("Referrer is : \n" + referrerUrl + "\n" + "Referrer Click Time is : " + referrerClickTime + "\nApp Install Time : " + appInstallTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Toast.makeText(CreateProfileActivity.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        Toast.makeText(CreateProfileActivity.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                Toast.makeText(CreateProfileActivity.this, "Service disconnected..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleFirebaseDynamicLink(int profileID) {
        try {
            dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://cikolive.page.link/profile/?SAVED_PROFILE_ID=" + profileID))
                    .setDomainUriPrefix("https://cikolive.page.link/profile/")
                    .setAndroidParameters(
                            new DynamicLink.AndroidParameters.Builder("com.lahoriagency.cikolive").build())
                    .buildDynamicLink();
            try {
                String url = URLDecoder.decode(dynamicLink.getUri().toString(), "UTF-8");
                Log.d(TAG, "handleFirebaseDynamicLink: " + url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }




    }
    private void completeSignUp(Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_VIEW)) {
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(intent)
                        .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                // Get deep link from result (may be null if no link is found)
                                Uri deepLink = null;
                                if (pendingDynamicLinkData != null) {
                                    deepLink = pendingDynamicLinkData.getLink();
                                    Log.d(TAG, "handleFirebaseDynamicLink: " + deepLink);
                                } else {
                                    Log.d(TAG, "handleFirebaseDynamicLink: pendingDynamicLinkData null");
                                }


                                // Handle the deep link. For example, open the linked
                                // content, or apply promotional credit to the user's
                                // account.
                                // ...

                                // ...
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "getDynamicLink:onFailure", e);
                            }
                        });

            }
        }
    }

    private void registerNewFireBaseUser(String email, String password, String joinedDate, String name, ActionCodeSettings actionCodeSettings, int profileID) {
        showProgressDialog(R.string.dlg_creating_new_user);
        mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(CreateProfileActivity.this,
                                    "Registration Successful!!"
                                            + " Congratulations!",
                                    Toast.LENGTH_LONG)
                                    .show();
                            status="Verified";
                            layoutUp = findViewById(R.id.layoutTop);
                            textReturnedWelcome = findViewById(R.id.WelcomeE);
                            scrollLayoutDown = findViewById(R.id.layoutDown);
                            layoutUp.setVisibility(View.GONE);
                            scrollLayoutDown.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textReturnedWelcome.setText("Continue"+""+name);
                            saveToDB(email, password, status, joinedDate, country,deviceID,name,profileID);

                        }
                    }
                });

    }

    private void registerNewUser(String email, String password, String joinedDate, String name) {

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    status="Verified";
                    layoutUp = findViewById(R.id.layoutTop);
                    textReturnedWelcome = findViewById(R.id.WelcomeE);
                    scrollLayoutDown = findViewById(R.id.layoutDown);
                    layoutUp.setVisibility(View.GONE);
                    scrollLayoutDown.setVisibility(View.VISIBLE);
                    Toast.makeText(CreateProfileActivity.this,
                            "Account creation started!",
                            Toast.LENGTH_LONG)
                            .show();
                    //saveToDB(email,password,status,joinedDate,country,deviceID);

                    progressBar.setVisibility(View.GONE);
                    textReturnedWelcome.setText("Continue"+""+name);



                }
                else {

                    // Registration failed
                    Toast.makeText(CreateProfileActivity.this,
                            "Registration failed!!"
                                    + " Please try again later",
                            Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);
                    Intent intent
                            = new Intent(CreateProfileActivity.this,
                            SignInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    @SuppressLint("Range")
    public void getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            realImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            mediaPathList.add(Uri.parse(realImagePath));
            cursor.close();
        }
    }
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private void saveDrafts() {
        edtEmailAddress = findViewById(R.id.et_emailC);
        et_Password = findViewById(R.id.et_PasswordC);
        emailStrg = edtEmailAddress.getText().toString();
        passwordStg = et_Password.getText().toString();
        SharedPrefsHelper.getInstance().save("SAVED_PROFILE_EMAIL", emailStrg);
        SharedPrefsHelper.getInstance().save("SAVED_PROFILE_PASSWORD", passwordStg);
    }

    private void saveToDB(String email, String password, String status, String joinedDate, String country, String deviceID, String name, int profileID) {
        SavedProfileDAO dbHelper = new SavedProfileDAO(this);
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            dbHelper.open();
            sqLiteDatabase = dbHelper.getWritableDatabase();
            profileIDLong= dbHelper.insertFirstSavedProfile(email,password,joinedDate,deviceID,country,status,name,profileID);

        }
    }
    private void SignUpUser(final QBUser newUser, final SavedProfile lastProfileUsed) {
        newUser.setCustomDataClass(lastProfileUsed.getClass());
        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void startSignUpNewUser(int profileID, final QBUser newUser, SavedProfile lastProfileUsed, int myAge, String aboutMe, String myIntrest, String country, String selectedGender, Uri mImageUri, int noOfDiamond) {
        Log.d(TAG, "SignUp New User");
        showProgressDialog(R.string.dlg_creating_new_user);
        Bundle userBundle=new Bundle();
        calendar = Calendar.getInstance();
        if(mImageUri !=null){
             mImageUriString= String.valueOf(mImageUri);

        }

        requestExecutor = new QBResRequestExecutor();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        giftTime = mdformat.format(calendar.getTime());
        newUser.setCustomDataClass(lastProfileUsed.getClass());
        SavedProfileDAO dbHelper = new SavedProfileDAO(this);
        loginReply= new LoginReply();
        Diamond diamond1= new Diamond();
        int count =0;
        diamond1.setDiamondCount(50);
        DiamondTransfer diamondTransfer= new DiamondTransfer();
        diamondTransfer.setdH_Count(50);
        diamondTransfer.setdH_From("Ciko Live Admin");
        diamondTransfer.setdH_To("You");
        diamondTransfer.setdH_SProf_ID(profileID);
        diamondTransfer.setdH_Date(giftTime);
        diamondTransfer.setdH_Balance(50);
        lastProfileUsed.setSavedPDiamond(diamond1);
        lastProfileUsed.addDiamondHistory("Ciko Live Admin",giftTime,50);

        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                saveUserData(qbUser);
                hideProgressDialog();
                loginReply.setStatus(responseString);
                loginReply.setSexChoice(selectedGender);
                if(lastProfileUsed !=null){
                    CreateProfileActivity.this.profileID =lastProfileUsed.getSavedProfID();
                }
                if(CreateProfileActivity.this.profileID >0){

                    if(qbUser !=null){
                        qbUserID=qbUser.getId();

                    }
                    if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
                        dbHelper.open();
                        sqLiteDatabase = dbHelper.getWritableDatabase();
                        dbHelper.updateSavedProf(profileID,qbUserID,myAge,aboutMe,myIntrest, country,selectedGender,mImageUri, noOfDiamond);
                    }


                    userProfileInfo= new UserProfileInfo(qbUserID, profileID, myAge,name,aboutMe,myIntrest, country,selectedGender,mImageUriString);
                    lastProfileUsed.setSavedPUserProfileInfo(userProfileInfo);
                    lastProfileUsed.setDefaultDiamond(noOfDiamond);
                    lastProfileUsed.setLoginReply(loginReply);

                }
                finishRegisteration(qbUser,lastProfileUsed,userProfileInfo,loginReply, profileID, name, passwordStg, emailStrg);
                signIn(qbUser,userProfileInfo,loginReply);


                //saveToDBase(qsUserID);
                Toast.makeText(CreateProfileActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(CreateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error SignUp" + e.getMessage());
                if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                } else {
                    //hideProgressDialog();
                    ToastUtils.longToast(R.string.sign_up_error);
                }

            }
        });
    }
    private void  signIn(QBUser qbUser, UserProfileInfo userProfileInfo, LoginReply loginReply){
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                //startAfterLoginService(qbUser);
                Toast.makeText(CreateProfileActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                QBChatService.setDebugEnabled(true); // enable chat logging
                QBChatService.setDefaultPacketReplyTimeout(10000);

                QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
                chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
                chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
                chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
                QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);
                Intent chatDialogIntent = new Intent(CreateProfileActivity.this, ChatDialogActivity.class);
                chatDialogIntent.putExtra("QBUser", qbUser);
                chatDialogIntent.putExtra("password", password);
                chatDialogIntent.putExtra("userName", userName);
                chatDialogIntent.putExtra("id", qbUser.getId());
                chatDialogIntent.putExtra("UserProfileInfo", userProfileInfo);
                chatDialogIntent.putExtra("LoginReply", (Parcelable) loginReply);
                //startActivity(chatDialogIntent);

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(CreateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void finishRegisteration(QBUser qbUser, SavedProfile lastProfileUsed, UserProfileInfo userProfileInfo, LoginReply loginReply, int profileID, String name, String passwordStg, String emailStrg) {
        dbHelper= new DBHelper(this);
        gson1= new Gson();
        gson= new Gson();
        gson2= new Gson();
        //dbHelper.upDateUser(lastProfileUsed);
        if (userPreferences == null){

            userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            json = gson.toJson(LastProfileUsed);
            json1 = gson1.toJson(qbUser);
            json2 = gson2.toJson(userProfileInfo);
            if(lastProfileUsed !=null){
                deviceID = lastProfileUsed.getSavedPDeviceID();
                name =lastProfileUsed.getSavedPName();
                country =lastProfileUsed.getSavedPCountry();
                lookingFor =lastProfileUsed.getSavedPLookingFor();
                aboutMe =lastProfileUsed.getSavedPAboutMe();
                age =lastProfileUsed.getSavedPAge();
                gender =lastProfileUsed.getSavedPGender();
                email =lastProfileUsed.getSavedPEmail();
                UserLocation =lastProfileUsed.getSavedPLocation();
                password =lastProfileUsed.getSavedPPassword();
                interest =lastProfileUsed.getSavedPMyInterest();
                profilePicture =lastProfileUsed.getSavedPImage();
                dateJoined =lastProfileUsed.getSavedPDateJoined();
                savedProfID =lastProfileUsed.getSavedProfID();
                noOfDiamond=lastProfileUsed.getDefaultDiamond();
                qbUserID=lastProfileUsed.getSavedProfQBID();

            }

            prefsEditor.putBoolean("rememberMe", true);
            prefsEditor.putString("SAVED_PROFILE_NAME", name);
            prefsEditor.putInt("SAVED_PROFILE_ID", savedProfID);
            prefsEditor.putInt("SAVED_PROFILE_QBID", qbUserID);
            prefsEditor.putString("SAVED_PROFILE_LOC", UserLocation);
            prefsEditor.putString("SAVED_PROFILE_PHOTO", String.valueOf(profilePicture));
            prefsEditor.putString("SAVED_PROFILE_EMAIL", email);
            prefsEditor.putString("SAVED_PROFILE_GENDER", gender);
            prefsEditor.putString("SAVED_PROFILE_LOOKING_GENDER", lookingFor);
            prefsEditor.putString("SAVED_PROFILE_PASSWORD", password);
            prefsEditor.putString("SAVED_PROFILE_DEVICEID", deviceID);
            prefsEditor.putString("SAVED_PROFILE_COUNTRY", country);
            prefsEditor.putString("SAVED_PROFILE_MY_INT", interest);
            prefsEditor.putString("SAVED_PROFILE_DATE_JOINED", dateJoined);
            prefsEditor.putString("SAVED_PROFILE_ABOUT_ME", aboutMe);
            prefsEditor.putString("SAVED_PROFILE_AGE", age);
            prefsEditor.putInt("SAVED_PROFILE_DIAMOND", noOfDiamond);
            prefsEditor.putString("SAVED_PROFILE_LOC", referrer);
            prefsEditor.putString("LastQBUserUsed", json1);
            prefsEditor.putString("LastUserProfileInfoUsed", json2);
            prefsEditor.putString("LastSavedProfileUsed", json).apply();

            //dataBaseCall(lastProfileUsed);

            Bundle userBundle = new Bundle();
            if(LastProfileUsed !=null){
                this.profileID =LastProfileUsed.getSavedProfID();
            }

            userBundle.putString("SAVED_PROFILE_NAME", name);
            userBundle.putInt("SAVED_PROFILE_ID", profileID);
            userBundle.putInt("SAVED_PROFILE_QBID", qbUserID);
            userBundle.putString("SAVED_PROFILE_LOC", cityStrg);
            userBundle.putString("SAVED_PROFILE_PHOTO", String.valueOf(mImageUri));
            userBundle.putString("SAVED_PROFILE_EMAIL", emailStrg);
            userBundle.putString("SAVED_PROFILE_GENDER", gender);
            userBundle.putString("SAVED_PROFILE_PASSWORD", passwordStg);
            userBundle.putString("SAVED_PROFILE_DEVICEID", deviceID);
            userBundle.putString("SAVED_PROFILE_COUNTRY", country);
            userBundle.putString("SAVED_PROFILE_REFERRER", referrer);
            userBundle.putString("SAVED_PROFILE_AGE", age);
            userBundle.putInt("SAVED_PROFILE_DIAMOND", noOfDiamond);

            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(userBundle);
            userBundle.putParcelable("QBUser", (Parcelable) qbUser);
            userBundle.putParcelable("LastUserProfileInfoUsed",userProfileInfo);
            userBundle.putParcelable("lastSavedProfileUsed",lastProfileUsed);
            startActivity(intent);
            Toast.makeText(CreateProfileActivity.this, "Thank you" + "" +
                    "for Signing up " + "" + uFirstName + "" + "on the App", Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK, new Intent());




        }

    }


    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
    }

    private void checkUserExist(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                File f = new File("/data/data/" + getPackageName() +  "/shared_prefs/PREF_NAME.xml");

                if (f.exists()){
                    SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    Boolean is_logged_in = sharedPreferences.getBoolean("is_logged_in", true);
                    if (is_logged_in) {

                        Intent intent = new Intent(CreateProfileActivity.this, ChatMainAct.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Boolean first_time_login = sharedPreferences.getBoolean("first_time_login", true);
                        if (first_time_login) {

                            Intent intent = new Intent(CreateProfileActivity.this, CreateProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(CreateProfileActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                else{
                    Intent intent = new Intent(CreateProfileActivity.this, CreateProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_SCREEN);
    }

    public void doFireBase(View view) {
    }

    public void getVideos(View view) {
    }

    private class TextWatcherListener implements TextWatcher {
        private final EditText editText;
        private Timer timer = new Timer();

        private TextWatcherListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString().replace("  ", " ");
            if (!editText.getText().toString().equals(text)) {
                editText.setText(text);
                editText.setSelection(text.length());
            }
            validateFields();
        }

        @Override
        public void afterTextChanged(Editable s) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    saveDrafts();
                }
            }, 300);
        }
    }
    private Boolean validateFields() {
        Boolean loginValid = ValidationUtils.isLoginValid(this, et_Password);
        Boolean userNameValid = ValidationUtils.isFullNameValid(this, edtEmailAddress);

        if (loginValid) {
            et_Password.setVisibility(View.GONE);
        } else {
            et_Password.setVisibility(View.VISIBLE);
        }

        if (userNameValid) {
            edtEmailAddress.setVisibility(View.GONE);
        } else {
            edtEmailAddress.setVisibility(View.VISIBLE);
        }

        if (loginValid && userNameValid) {
            fireBaseBtn.setActivated(true);
            fireBaseBtn.setElevation(0F);
            fireBaseBtn.setTranslationZ(10F);
            return true;
        } else {
            fireBaseBtn.setActivated(false);
            fireBaseBtn.setElevation(0F);
            fireBaseBtn.setTranslationZ(0F);
            return false;
        }
    }



    private String getCurrentDeviceId(Context deviceID) {
        return Utils.generateDeviceId();
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void getDeviceID(String deviceID) {
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        m_szBTMAC = m_BluetoothAdapter.getAddress();
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            deviceID = m+ m_szWLANMAC + m_szBTMAC+wm;
            m.update(deviceID.getBytes(),0,deviceID.length());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }



        //deviceID=m_szLongID;
    }

    public void loadImageCenterCrop(String url, ImageView imageView,
                                    int width, int height) {
        Glide.with(CreateProfileActivity.this)
                .load(url)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_alert)
                .centerCrop()
                .into(imageView);
    }
    @Nullable
    public  Bitmap loadBitmap(RequestManager with, String url, int width, int height) {
        try {


            return Glide.with(CreateProfileActivity.this).asBitmap()
                    .load(url)
                    .centerCrop()
                    .submit(width, height)
                    .get();
        } catch (Exception e) {
            return null;
        }
    }
    public String generateUUID(int length) {
        String returnvalue = UUID.randomUUID().toString().replaceAll("-", "");
        return returnvalue;
    }

    public String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; 1 < length; i++) {
            returnValue.append(ALPHABET.charAt(random1.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public String generateRef(int length) {
        return generateUUID(length);
    }
    public  String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            country = tm.getSimCountryIso();
            if (country != null && country.length() == 2) { // SIM country code is available
                return country.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                country = tm.getNetworkCountryIso();
                if (country != null && country.length() == 2) { // network country code is available
                    return country.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
    public  String stringToDayAndMonth(String birthdayString,String[] mDateSplit) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int mMonth, mDay;

        if (birthdayString.startsWith("/")) {
            birthdayString = birthdayString.replaceFirst("/", "");
            mDateSplit = birthdayString.split("-");
            mDay = Integer.parseInt(mDateSplit[2]);
            mMonth = Integer.parseInt(mDateSplit[1]);
            birthdayString=mDay+"-"+mMonth;
        }
        return birthdayString;
    }


    private void dataBaseCall(SavedProfile LastProfileUsed) {

        new AsyncCaller().execute();

    }

    public void registerUser(View view) {
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(CreateProfileActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            dbHelper.insertNewSavedProfile(LastProfileUsed);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            Bundle userBundle = new Bundle();
            if(LastProfileUsed !=null){
                profileID=LastProfileUsed.getSavedProfID();
            }

            userBundle.putString("SAVED_PROFILE_NAME", name);
            userBundle.putInt("SAVED_PROFILE_ID", Math.toIntExact(profileID));
            userBundle.putString("SAVED_PROFILE_LOC", cityStrg);
            userBundle.putString("SAVED_PROFILE_PHOTO", String.valueOf(mImageUri));
            userBundle.putString("SAVED_PROFILE_EMAIL", emailStrg);
            userBundle.putString("SAVED_PROFILE_GENDER", gender);
            userBundle.putString("SAVED_PROFILE_GENDER", gender);
            userBundle.putString("SAVED_PROFILE_PASSWORD", passwordStg);
            userBundle.putString("SAVED_PROFILE_DEVICEID", deviceID);
            userBundle.putString("SAVED_PROFILE_COUNTRY", country);
            userBundle.putString("SAVED_PROFILE_REFERRER", referrer);
            userBundle.putString("SAVED_PROFILE_AGE", referrer);

            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
            intent.putExtras(userBundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(CreateProfileActivity.this, "Thank you" + "" +
                    "for Signing up " + "" + uFirstName + "" + "on the App", Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK, new Intent());
            finish();

        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == RESULT_CAMERA_CODE) {
            mImageUri = data.getData();
            Glide.with(CreateProfileActivity.this)
                    .asBitmap()
                    .load(mImageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_alert)
                    //.listener(listener)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .centerCrop()
                    .into(profilePix);


        }
        if ((data != null) && requestCode == RESULT_LOAD_IMAGE) {
            mImageUri = data.getData();
            Glide.with(CreateProfileActivity.this)
                    .asBitmap()
                    .load(mImageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_alert)
                    //.listener(listener)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .centerCrop()
                    .into(profilePix);


        }

    }
    private void getUserRefferer(InstallReferrerClient referrerClient) {
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();

                            referrerUrl = response.getInstallReferrer();

                            referrerClickTime = response.getReferrerClickTimestampSeconds();

                            appInstallTime = response.getInstallBeginTimestampSeconds();

                            instantExperienceLaunched = response.getGooglePlayInstantParam();

                            referrer = response.getInstallReferrer();

                            //refrerTV.setText("Referrer is : \n" + referrerUrl + "\n" + "Referrer Click Time is : " + referrerClickTime + "\nApp Install Time : " + appInstallTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Toast.makeText(CreateProfileActivity.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        Toast.makeText(CreateProfileActivity.this, "Fail to establish connection", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                Toast.makeText(CreateProfileActivity.this, "Service disconnected..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static boolean isOnline(ConnectivityManager cm) {
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void sendTextMessage(String uPhoneNumber, String smsMessage) {
        Bundle smsBundle = new Bundle();
        smsBundle.putString("PROFILE_PHONE", uPhoneNumber);
        smsBundle.putString("USER_PHONE", uPhoneNumber);
        smsBundle.putString("smsMessage", smsMessage);
        //smsBundle.putString("from", "Skylight Coop.");
        smsBundle.putString("to", uPhoneNumber);
        Intent otpIntent = new Intent(CreateProfileActivity.this, SMSAct.class);
        otpIntent.putExtras(smsBundle);
        otpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        setResult(RESULT_OK, otpIntent);
        //startActivity(itemPurchaseIntent);

    }
    private void doOtpNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Welcome to the Community")
                        .setContentText(welcomeMessage);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    protected void createLocationRequest() {
        if (!hasPermissions(CreateProfileActivity.this, PERMISSIONS33)) {
            ActivityCompat.requestPermissions(CreateProfileActivity.this, PERMISSIONS33, PERMISSION_ALL33);

        }


        request = new LocationRequest();
        request.setSmallestDisplacement(10);
        request.setFastestInterval(50000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(3);



    }
    private void setInitialLocation() {
        txtLoc = findViewById(R.id.where_You_Are);
        final double[] newLat = {0.00};
        final double[] newLng = {0.00};

        if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (locationPermissionGranted) {
            @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mCurrentLocation = task.getResult();
                        newLat[0] = mCurrentLocation.getLatitude();
                        newLng[0] = mCurrentLocation.getLongitude();

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());

                    }
                }
            });
        }
        if(mCurrentLocation !=null){
            newLat[0] = mCurrentLocation.getLatitude();
            newLng[0] = mCurrentLocation.getLongitude();

        }


        try {
            Geocoder newGeocoder = new Geocoder(CreateProfileActivity.this, Locale.ENGLISH);
            List<Address> newAddresses = newGeocoder.getFromLocation(newLat[0], newLng[0], 1);
            StringBuilder street = new StringBuilder();
            if (Geocoder.isPresent()) {

                //txtLoc.setVisibility(View.VISIBLE);
                txtLoc = findViewById(R.id.where_You_Are);
                txtLoc.setVisibility(View.VISIBLE);

                Address newAddress = newAddresses.get(0);

                localityString = newAddress.getLocality();
                country=newAddress.getCountryName();

                street.append(localityString).append("");
                txtLoc.setText("Where you are:"+""+localityString);

                Toast.makeText(CreateProfileActivity.this, street,
                        Toast.LENGTH_SHORT).show();

            } else {
                txtLoc.setVisibility(View.GONE);
                //go
            }


        } catch (IndexOutOfBoundsException | IOException e) {

            Log.e("tag", e.getMessage());
        }

    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            location = task.getResult();
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                userLatLng = new LatLng(latitude, longitude);

                            }
                        } else {

                            userLatLng = new LatLng(4.52871, 7.44507);
                            Log.d(TAG, "Current location is null. Using defaults.");
                            /*googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(cusLatLng, 17));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);*/
                        }
                    }
                });
                txtLoc = findViewById(R.id.where_You_Are);
                try {
                    geocoder = new Geocoder(CreateProfileActivity.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    StringBuilder str = new StringBuilder();
                    if (Geocoder.isPresent()) {
                        //txtLoc.setVisibility(View.VISIBLE);

                        Address returnAddress = addresses.get(1);

                        String localityString = returnAddress.getAdminArea();

                        str.append(localityString).append("");
                        //txtLoc.setText(MessageFormat.format("Your are here:  {0},{1}/{2}", latitude, longitude, str));


                        Toast.makeText(CreateProfileActivity.this, str,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        //go
                    }


                } catch (IllegalStateException | IOException e) {

                    Log.e("tag", e.getMessage());
                }


                fusedLocationClient.getCurrentLocation(2, cancellationTokenSource.getToken())
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location2) {
                                if (location2 != null) {
                                    latitude = location2.getLatitude();
                                    longitude = location2.getLongitude();
                                    userLatLng = new LatLng(latitude, longitude);


                                    setResult(Activity.RESULT_OK, new Intent());


                                } else {
                                    userLatLng = new LatLng(4.52871, 7.44507);
                                    Log.d(TAG, "Current location is null. Using defaults.");
                                }

                            }

                        });

                geocoder = new Geocoder(this, Locale.getDefault());

                if (location != null) {
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {

                        address = addresses.get(0).getAddressLine(0);
                        //localityString = address.getLocality();
                        //country=newAddress.getCountryName();



                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }



            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
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
        txtLoc = findViewById(R.id.where_You_Are);
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

                    Toast.makeText(CreateProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("Skylight App",
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
                    userLatLng = new LatLng(latitude, longitude);

                    geocoder = new Geocoder(this, Locale.getDefault());

                    if (location != null) {
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {

                            address = addresses.get(0).getAddressLine(0);
                            txtLoc.setVisibility(View.VISIBLE);
                            cityStrg= addresses.get(0).getLocality();
                            txtLoc.setText("Where you are:"+""+cityStrg);



                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                    String city = addresses.get(0).getLocality()+","+addresses.get(0).getCountryName();
                    if(city !=null){
                        txtLoc.setVisibility(View.VISIBLE);
                        txtLoc.setText(MessageFormat.format("My Loc:{0}",  city));

                    }


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

        }




    }

    public Bitmap getBitmap(String path) {
        Bitmap myBitmap = null;
        File imgFile = new File(path);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
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


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);//you can cancel it by pressing back button
        progressDialog.setMessage("signing up wait ...");
        progressBar.show();//displays the progress bar
    }


    public static boolean isEmailValid(EditText email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email.getText().toString();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private void registerUser(String emaill, final String pass, final String uname) {
        progressDialog.show();
        HashMap<Object, String> hashMap = new HashMap<>();
        //hashMap.put("email", email);
        //hashMap.put("uid", uid);
        hashMap.put("name", uname);
        hashMap.put("onlineStatus", "online");
        hashMap.put("typingTo", "noOne");
        hashMap.put("image", "");
        Intent mainIntent = new Intent(CreateProfileActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.putExtra("HashMap",hashMap);
        startActivity(mainIntent);
        progressDialog.dismiss();
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void init() {

    }


    public void verifyEmail(View view) {
    }

    public void doSelectPix(View view) {
    }

    public void dobPick(View view) {
    }

    public void updateProfile(View view) {
    }
}