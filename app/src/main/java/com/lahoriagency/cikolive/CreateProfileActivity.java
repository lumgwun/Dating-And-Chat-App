package com.lahoriagency.cikolive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.multidex.BuildConfig;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.lahoriagency.cikolive.Adapters.AddImageAdapter;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.ValidationUtils;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.SavedProfileDAO;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_EMAIL;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PASSWORD;


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
    private EditText edtEmail, edtPassword, edtName;
    private Button mRegister,btnUpdate;
    private TextView existaccount;
    private ProgressDialog progressDialog;
    long profileID;
    int customerID;
    String machinePref;
    Uri pictureLink;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private static final String PREF_NAME = "Ciko";
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
    String uPhoneNumber;
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

    String joinedDate, localityString,profileName;
    private View mTarget;
    //private DrawView mHistory;
    private EditText edtEmailAddress, passwordTextView;
    private AppCompatButton fireBaseBtn;
    private ContentLoadingProgressBar progressbar;
    private FirebaseAuth mAuth;
    int PERMISSION_ALL = 1;
    private CircleImageView profilePix;
    int timeOfDay;
    private static boolean isPersistenceEnabled = false;
    private LinearLayoutCompat layoutUp;
    private ScrollView layoutDown;
    Gson gson, gson1;
    String json, json1, nIN;
    SharedPreferences userPreferences;

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
    String referrer = "";
    String[] PERMISSIONS33 = {
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.SEND_SMS
    };
    AppCompatTextView txtLoc,textReturnedEmail;
    AppCompatEditText edtAge,et_interests,et_aboutC,edt_Name,et_Password, edtFirstName,edtPhone;
    private CountryCodePicker countryCodePicker;
    private long referrerClickTime,appInstallTime;
    private  boolean instantExperienceLaunched;
    private SavedProfile LastProfileUsed;
    private String deviceID;
    private QBUser qbUser;
    private AppCompatButton btnSignUp;
    private RecyclerView recyclerViewPhoto, recyclerViewVideo;
    private QBResRequestExecutor requestExecutor ;
    private CheckBox checkBox;
    private RadioGroup radioSexGroup;
    private  TextView txtSignIn;
    private ImageView btn_back;
    private CircularImageView profile_image_;

    String name=null;
    String lookingFor=null;
    String age=null;
    int myGender=0;
    String email=null;
    String UserLocation=null;
    String password=null;
    Uri profilePicture=null;
    int savedProfID=0;
    String interest=null;
    String dateJoined=null;
    String status=null;
    private int qbUserID;
    public static int SPLASH_SCREEN = 3000;
    QBEntityCallback<QBUser> callback;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    String machineUser,userName, office,state,role,userLocation,passwordStg,emailStrg,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_profile);
        checkInternetConnection();
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        init();
        getDeviceID(deviceID);

        //listeners();
        setTitle("Create a new  Profile");
        dbHelper=new DBHelper(this);
        LastProfileUsed= new SavedProfile();
        random1 = new SecureRandom();
        qbUser= new QBUser();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        requestExecutor = new QBResRequestExecutor();
        //addImageAdapter = new AddImageAdapter();
        welcomeMessage="Welcome to the Community, chat and build your networth";
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        txtLoc = findViewById(R.id.where_You_Are);
        recyclerViewPhoto = findViewById(R.id.rv_images);
        recyclerViewVideo = findViewById(R.id.rv_videos);
        edtAge = findViewById(R.id.et_ageC);
        edt_Name = findViewById(R.id.et_nameC);
        et_aboutC = findViewById(R.id.et_aboutC);
        et_interests = findViewById(R.id.et_interestsC);
        checkBox = findViewById(R.id.checkboxC);
        edtEmailAddress = findViewById(R.id.et_emailC);
        et_Password = findViewById(R.id.et_PasswordC);
        radioSexGroup=(RadioGroup)findViewById(R.id.sexgroup);
        btnSignUp = findViewById(R.id.btn_submit);
        spnGender = findViewById(R.id.spn_Gender);
        txtSignIn = findViewById(R.id.txt_Btn_SignIn);
        btn_back = findViewById(R.id.btn_back);
        fireBaseBtn = findViewById(R.id.btn_Firebase);
        layoutUp = findViewById(R.id.layoutTop);
        layoutDown = findViewById(R.id.layoutDown);
        textReturnedEmail = findViewById(R.id.WelcomeE);


        btnSignUp.setOnClickListener(this::registerUser);

        profileID = ThreadLocalRandom.current().nextInt(122, 1631);
        profile_image_ = findViewById(R.id.takePhoto);
        mAuth = FirebaseAuth.getInstance();


        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(CreateProfileActivity.this, SignInActivity.class);
                chatIntent.putExtras(bundle);
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
                selectedGender = spnGender.getSelectedItem().toString();
                selectedGender = (String) parent.getSelectedItem();
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
                if (!hasPermissions(CreateProfileActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(CreateProfileActivity.this, PERMISSIONS, PERMISSION_ALL);
                }

                final PopupMenu popup = new PopupMenu(CreateProfileActivity.this, profile_image_);
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
                            setTitle("Skylight onBoarding");
                        }

                        return true;
                    }
                });
                popup.show();//showing popup menu


            }



        });


        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        joinedDate = mdformat.format(calendar.getTime());

        fireBaseBtn.setOnClickListener(this::verifyEmail);
        progressbar = findViewById(R.id.progressBarC);


        progressDialog = new ProgressDialog(this);



        fireBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = edtEmailAddress.getText().toString();
                password = edtPassword.getText().toString();

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

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmailAddress.setError("Invalid Email");
                    edtEmailAddress.setFocusable(true);
                } else if (password.length() < 2) {
                    passwordTextView.setError("Length Must be greater than 2 character");
                    passwordTextView.setFocusable(true);
                } else {
                    progressDialog.setMessage("Register");
                    registerNewUser(email, password,joinedDate);
                }



            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                getDeviceID(deviceID);
                emailStrg = edtEmailAddress.getText().toString();
                passwordStg = edtPassword.getText().toString();
                myAge = edtAge.getText().toString();
                myIntrest = et_interests.getText().toString();
                aboutMe = et_aboutC.getText().toString();
                getUserCountry(CreateProfileActivity.this);
                textReturnedEmail.setText("Welcome Back"+" "+emailStrg);
                //passwordTextView.setText("Your Password: passwordStg");
                profileName = edt_Name.getText().toString();
                RadioButton radioButton = (RadioButton) findViewById(radioSexGroup.getCheckedRadioButtonId());
                userGender=radioButton.getText().equals("Male")?0:1;
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
                }if (TextUtils.isEmpty(profileName)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter Name!!",
                            Toast.LENGTH_LONG)
                            .show();
                } else {


                    LastProfileUsed= new SavedProfile(profileName,emailStrg,passwordStg,aboutMe,myIntrest,myAge,userGender,gender,joinedDate,country,cityStrg,mImageUri);
                    qbUser= new QBUser();
                    qbUser.setCustomData(myAge);
                    qbUser.setEmail(emailStrg);
                    qbUser.setPassword(passwordStg);
                    qbUser.setCustomDataClass(LastProfileUsed.getClass());
                    qbUser.setFullName(profileName);
                    qbUser.setExternalId(String.valueOf(profileID));
                    //finishRegisteration(qbUser, LastProfileUsed);
                    startSignUpNewUser(qbUser,LastProfileUsed);

                }


                //startActivity(new Intent(CreateProfileActivity.this, SignInActivity.class));
            }
        });
    }
    private void registerNewUser(String email, String password, String joinedDate) {

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    status="Verified";
                    layoutUp = findViewById(R.id.layoutTop);
                    textReturnedEmail = findViewById(R.id.WelcomeE);
                    layoutDown = findViewById(R.id.layoutDown);
                    layoutUp.setVisibility(View.GONE);
                    layoutDown.setVisibility(View.VISIBLE);
                    Toast.makeText(CreateProfileActivity.this,
                            "Account creation started!",
                            Toast.LENGTH_LONG)
                            .show();
                    saveToDB(email,password,status,joinedDate,country,deviceID);

                    progressBar.setVisibility(View.GONE);
                    textReturnedEmail.setText("Continue"+""+email);



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
    private void checkUserExist(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                File f = new File("/data/data/" + getPackageName() +  "/shared_prefs/UserDetails.xml");

                if (f.exists()){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
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
    private class TextWatcherListener implements TextWatcher {
        private EditText editText;
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
    private void saveDrafts() {
        edtEmailAddress = findViewById(R.id.et_emailC);
        et_Password = findViewById(R.id.et_PasswordC);
        emailStrg = edtEmailAddress.getText().toString();
        passwordStg = edtPassword.getText().toString();
        SharedPrefsHelper.getInstance().save("SAVED_PROFILE_EMAIL", edtEmailAddress.getText().toString());
        SharedPrefsHelper.getInstance().save("SAVED_PROFILE_PASSWORD", edtPassword.getText().toString());
    }

    private void saveToDB(String email, String password, String status, String joinedDate, String country,String deviceID) {
        SavedProfileDAO dbHelper = new SavedProfileDAO(this);
        profileID= dbHelper.insertFirstSavedProfile(email,password,joinedDate,deviceID,country,status);
    }
    private void SignUpUser(final QBUser newUser, final SavedProfile lastProfileUsed) {
        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void startSignUpNewUser(final QBUser newUser, SavedProfile lastProfileUsed) {
        Log.d(TAG, "SignUp New User");
        showProgressDialog(R.string.dlg_creating_new_user);
        Bundle userBundle=new Bundle();
        requestExecutor = new QBResRequestExecutor();
        SavedProfileDAO dbHelper = new SavedProfileDAO(this);

        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                saveUserData(qbUser);
                signIn(qbUser);
                if(lastProfileUsed !=null){
                    profileID=lastProfileUsed.getSavedProfID();
                }
                if(profileID>0){

                    if(qbUser !=null){
                        qbUserID=qbUser.getFileId();

                    }
                    dbHelper.updateProfileQBUserID(Math.toIntExact(profileID),qbUserID);
                    finishRegisteration(qbUser ,lastProfileUsed);

                }


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
    private void  signIn(QBUser qbUser){
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
                //startActivity(chatDialogIntent);

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(CreateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void finishRegisteration(QBUser qbUser, SavedProfile lastProfileUsed) {

        dbHelper= new DBHelper(this);
        gson1= new Gson();
        gson= new Gson();
        dbHelper.upDateUser(lastProfileUsed);
        if (userPreferences == null){

            userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            json = gson.toJson(LastProfileUsed);
            json1 = gson1.toJson(qbUser);
            if(lastProfileUsed !=null){
                deviceID = lastProfileUsed.getSavedPDeviceID();
                name =lastProfileUsed.getSavedPName();
                country =lastProfileUsed.getSavedPCountry();
                lookingFor =lastProfileUsed.getSavedPLookingFor();
                aboutMe =lastProfileUsed.getSavedPAboutMe();
                age =lastProfileUsed.getSavedPAge();
                myGender =lastProfileUsed.getSavedPGender();
                email =lastProfileUsed.getSavedPEmail();
                UserLocation =lastProfileUsed.getSavedPLocation();
                password =lastProfileUsed.getSavedPPassword();
                interest =lastProfileUsed.getSavedPMyInterest();
                profilePicture =lastProfileUsed.getSavedPImage();
                dateJoined =lastProfileUsed.getSavedPDateJoined();
                savedProfID =lastProfileUsed.getSavedProfID();

            }

            prefsEditor.putBoolean("rememberMe", true);
            prefsEditor.putString("SAVED_PROFILE_NAME", name);
            prefsEditor.putInt("SAVED_PROFILE_ID", savedProfID);
            prefsEditor.putString("SAVED_PROFILE_LOC", UserLocation);
            prefsEditor.putString("SAVED_PROFILE_PHOTO", String.valueOf(profilePicture));
            prefsEditor.putString("SAVED_PROFILE_EMAIL", email);
            prefsEditor.putInt("SAVED_PROFILE_GENDER", myGender);
            prefsEditor.putString("SAVED_PROFILE_LOOKING_GENDER", lookingFor);
            prefsEditor.putString("SAVED_PROFILE_PASSWORD", password);
            prefsEditor.putString("SAVED_PROFILE_DEVICEID", deviceID);
            prefsEditor.putString("SAVED_PROFILE_COUNTRY", country);
            prefsEditor.putString("SAVED_PROFILE_MY_INT", interest);
            prefsEditor.putString("SAVED_PROFILE_DATE_JOINED", dateJoined);
            prefsEditor.putString("SAVED_PROFILE_ABOUT_ME", aboutMe);
            prefsEditor.putString("SAVED_PROFILE_AGE", age);
            prefsEditor.putString("SAVED_PROFILE_LOC", referrer);
            prefsEditor.putString("LastQBUserUsed", json1);
            prefsEditor.putString("LastSavedProfileUsed", json).apply();

            //dataBaseCall(lastProfileUsed);

            Bundle userBundle = new Bundle();
            if(LastProfileUsed !=null){
                profileID=LastProfileUsed.getSavedProfID();
            }
            finishRegisteration(qbUser, lastProfileUsed);

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
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(userBundle);
            userBundle.putParcelable("QBUser", (Parcelable) qbUser);
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
        m.update(m_szLongID.getBytes(),0,m_szLongID.length());



        m_szLongID = m+ m_szWLANMAC + m_szBTMAC;
        deviceID=m_szLongID;
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
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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

                Address newAddress = newAddresses.get(0);

                localityString = newAddress.getLocality();
                country=newAddress.getCountryName();

                street.append(localityString).append("");

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
                            //txtLoc.setVisibility(View.VISIBLE);
                            cityStrg= addresses.get(0).getLocality();



                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                    String city = addresses.get(0).getLocality()+","+addresses.get(0).getCountryName();
                    if(city !=null){
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