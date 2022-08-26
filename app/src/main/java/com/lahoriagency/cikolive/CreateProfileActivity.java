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
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.lahoriagency.cikolive.Classes.App;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


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
    int profileID,customerID;
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
    String dateOfBirth;
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
    private CancellationTokenSource cancellationTokenSource;

    String joinedDate, localityString,profileName;
    private View mTarget;
    //private DrawView mHistory;
    private EditText emailTextView, passwordTextView;
    private Button fireBaseBtn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    int PERMISSION_ALL = 1;
    private CircleImageView profilePix;
    int timeOfDay;
    private static boolean isPersistenceEnabled = false;
    private LinearLayoutCompat layoutUp,layoutDown;
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
    AppCompatEditText edtSurname, edtFirstName,edtPhone;
    private CountryCodePicker countryCodePicker;
    private long referrerClickTime,appInstallTime;
    private  boolean instantExperienceLaunched;
    private SavedProfile LastProfileUsed;
    private String deviceID;
    private QBUser qbUser;
    private QBResRequestExecutor requestExecutor = new QBResRequestExecutor();



    String machineUser,userName, office,state,role,userLocation,passwordStg,surname, emailStrg,phoneNO, dob,gender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_profile);
        init();
        //listeners();
        setTitle("Create a new  Profile");
        dbHelper=new DBHelper(this);
        LastProfileUsed= new SavedProfile();
        random1 = new SecureRandom();
        qbUser= new QBUser();

        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        sessionManager = new SessionManager(this);
        //addImageAdapter = new AddImageAdapter();
        welcomeMessage="Welcome to the Community, chat and build your networth";
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        txtLoc = findViewById(R.id.whereYou);
        layoutUp = findViewById(R.id.layoutUp);
        layoutDown = findViewById(R.id.layOutSecond);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        profileID = ThreadLocalRandom.current().nextInt(122, 1631);
        profilePix = findViewById(R.id.profile_image_);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email);
        dobText = findViewById(R.id.dob_create);
        passwordTextView = findViewById(R.id.passwd);
        fireBaseBtn = findViewById(R.id.btnregister);
        spnGender = findViewById(R.id.gender);
        edtSurname = findViewById(R.id.surName);
        edtFirstName = findViewById(R.id.first_Name_);
        edtPhone = findViewById(R.id.fone_number);
        countryCodePicker = findViewById(R.id.ccpCiko);
        textReturnedEmail = findViewById(R.id.welcomeEmail);
        dobText.setOnClickListener(this::dobPick);
        profilePix.setOnClickListener(this::doSelectPix);
        StringBuilder welcomeString = new StringBuilder();
        getUserRefferer(referrerClient);
        countryCodePicker.registerCarrierNumberEditText(edtPhone);
        countryCodePicker.getFullNumberWithPlus();

        countryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code
            }
        });


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


        profilePix.startAnimation(translater);
        profilePix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(CreateProfileActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(CreateProfileActivity.this, PERMISSIONS, PERMISSION_ALL);
                }

                final PopupMenu popup = new PopupMenu(CreateProfileActivity.this, profilePix);
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



        dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                newMonth = month + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateProfileActivity.this, R.style.DatePickerDialogStyle, mDateSetListener, day, month, year);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //make transparent background.
                dialog.show();
                dob = year + "-" + newMonth + "-" + day;
                dateOfBirth = day + "-" + newMonth + "-" + year;
                dobText.setText("Your date of Birth:" + dob);

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                Log.d(TAG, "onDateSet: date of Birth: " + day + "-" + month + "-" + year);
                dob = year + "-" + newMonth + "-" + day;
                dateOfBirth = day + "-" + newMonth + "-" + year;
                dobText.setText("Your date of Birth:" + dob);


            }


        };
        dateOfBirth = day + "-" + newMonth + "-" + year;

        //token= getIntent().getStringExtra("TOKEN");
        //birthdayID = random.nextInt((int) (Math.random() * 1001) + 1010);
        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        joinedDate = mdformat.format(calendar.getTime());

        fireBaseBtn.setOnClickListener(this::verifyEmail);
        progressbar = findViewById(R.id.progressbar);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register");


        fireBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailTextView.getText().toString();
                password = passwordTextView.getText().toString();

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
                    emailTextView.setError("Invalid Email");
                    emailTextView.setFocusable(true);
                } else if (password.length() < 6) {
                    passwordTextView.setError("Length Must be greater than 6 character");
                    passwordTextView.setFocusable(true);
                } else {
                    registerNewUser(email, password);
                }



            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                getDeviceID(deviceID);
                emailStrg = emailTextView.getText().toString();
                passwordStg = passwordTextView.getText().toString();
                getUserCountry(CreateProfileActivity.this);
                emailTextView.setText("Your UserName and  Email: emailStrg");
                passwordTextView.setText("Your Password: passwordStg");
                surname = edtSurname.getText().toString();
                uFirstName = edtFirstName.getText().toString();

                phoneNO=countryCodePicker.getFullNumberWithPlus();

                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please select your Gender!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(uFirstName)) {
                    Toast.makeText(CreateProfileActivity.this,
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    qbUser= new QBUser(emailStrg,passwordStg,emailStrg);
                    LastProfileUsed= new SavedProfile(surname, uFirstName,emailStrg,passwordStg,dateOfBirth,gender,phoneNO,country,cityStrg,mImageUri);
                    //registerNewUser(emailStrg,passwordStg);
                    startSignUpNewUser(qbUser,LastProfileUsed);

                }


                //startActivity(new Intent(CreateProfileActivity.this, SignInActivity.class));
            }
        });
    }
    private void registerNewUser(String email, String password) {

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    layoutUp = findViewById(R.id.layoutUp);
                    textReturnedEmail = findViewById(R.id.welcomeEmail);
                    layoutDown = findViewById(R.id.layOutSecond);
                    layoutUp.setVisibility(View.GONE);
                    layoutDown.setVisibility(View.VISIBLE);
                    Toast.makeText(CreateProfileActivity.this,
                            "Account creation started!",
                            Toast.LENGTH_LONG)
                            .show();

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
                            CreateProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void startSignUpNewUser(final QBUser newUser, SavedProfile lastProfileUsed) {
        Log.d(TAG, "SignUp New User");
        showProgressDialog(R.string.dlg_creating_new_user);
        Bundle userBundle=new Bundle();
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.d(TAG, "SignUp Successful");
                        saveUserData(newUser);
                        dataBaseCall(lastProfileUsed);
                        userBundle.putParcelable("QBUser", (Parcelable) newUser);
                        userBundle.putParcelable("SavedProfile",lastProfileUsed);

                        finishRegisteration(profileID,surname, uFirstName,emailStrg,passwordStg,dateOfBirth,gender,phoneNO,country,cityStrg,mImageUri);

                        Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                        intent.putExtras(userBundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(CreateProfileActivity.this, "Thank you" + "" +
                                "for Signing up " + "" + uFirstName + "" + "on the App", Toast.LENGTH_LONG).show();
                        setResult(Activity.RESULT_OK, new Intent());
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            //
                            finish();
                        } else {
                            hideProgressDialog();
                            ToastUtils.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }
    private QBUser createQBUserWithCurrentData(String userLogin, String userFullName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userLogin) && !TextUtils.isEmpty(userFullName)) {
            qbUser = new QBUser();
            qbUser.setLogin(userLogin);
            qbUser.setFullName(userFullName);
            qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        }
        return qbUser;
    }
    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
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

    private void finishRegisteration(int profileID, String surname, String uFirstName, String emailStrg, String passwordStg, String dateOfBirth, String gender, String phoneNO, String country, String cityStrg, Uri mImageUri) {
        if (userPreferences == null){
            userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            json = gson.toJson(LastProfileUsed);
            String name=surname+""+uFirstName;
            prefsEditor.putBoolean("rememberMe", true);
            prefsEditor.putString("SAVED_PROFILE_NAME", name);
            prefsEditor.putInt("SAVED_PROFILE_ID", profileID);
            prefsEditor.putString("SAVED_PROFILE_LOC", cityStrg);
            prefsEditor.putString("SAVED_PROFILE_PHOTO", String.valueOf(mImageUri));
            prefsEditor.putString("SAVED_PROFILE_EMAIL", emailStrg);
            prefsEditor.putString("SAVED_PROFILE_GENDER", gender);
            prefsEditor.putString("SAVED_PROFILE_GENDER", gender);
            prefsEditor.putString("SAVED_PROFILE_PHONE", phoneNO);
            prefsEditor.putString("SAVED_PROFILE_PASSWORD", passwordStg);
            prefsEditor.putString("SAVED_PROFILE_DEVICEID", deviceID);
            prefsEditor.putString("SAVED_PROFILE_COUNTRY", country);
            prefsEditor.putString("SAVED_PROFILE_REFERRER", referrer);
            prefsEditor.putString("SAVED_PROFILE_AGE", referrer);
            prefsEditor.putString("SAVED_PROFILE_DOB", dateOfBirth);
            prefsEditor.putString("LastProfileUsed", json).apply();

        }
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
            String name=surname+""+uFirstName;

            userBundle.putString("SAVED_PROFILE_NAME", name);
            userBundle.putInt("SAVED_PROFILE_ID", profileID);
            userBundle.putString("SAVED_PROFILE_LOC", cityStrg);
            userBundle.putString("SAVED_PROFILE_PHOTO", String.valueOf(mImageUri));
            userBundle.putString("SAVED_PROFILE_EMAIL", emailStrg);
            userBundle.putString("SAVED_PROFILE_GENDER", gender);
            userBundle.putString("SAVED_PROFILE_GENDER", gender);
            userBundle.putString("SAVED_PROFILE_PHONE", phoneNO);
            userBundle.putString("SAVED_PROFILE_PASSWORD", passwordStg);
            userBundle.putString("SAVED_PROFILE_DEVICEID", deviceID);
            userBundle.putString("SAVED_PROFILE_COUNTRY", country);
            userBundle.putString("SAVED_PROFILE_REFERRER", referrer);
            userBundle.putString("SAVED_PROFILE_AGE", referrer);
            userBundle.putString("SAVED_PROFILE_DOB", dateOfBirth);

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
        txtLoc = findViewById(R.id.whereYou);
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
                txtLoc = findViewById(R.id.whereYou);
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
        txtLoc = findViewById(R.id.whereYou);
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