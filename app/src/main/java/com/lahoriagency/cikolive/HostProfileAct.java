package com.lahoriagency.cikolive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lahoriagency.cikolive.Adapters.AddImageAdapter;
import com.lahoriagency.cikolive.Adapters.AddVideoAdapter;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.ResourceUtils;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.SavedProfileDAO;
import com.lahoriagency.cikolive.Fragments.ContentFragment;
import com.lahoriagency.cikolive.Fragments.DialogsFragment;
import com.lahoriagency.cikolive.Interfaces.OnLoginChangeView;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.List;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "deprecation"})
public class HostProfileAct extends AppCompatActivity {

    private Uri mImageUri;
    ActivityResultLauncher<Intent> mGetPixContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            if (result.getData() != null) {

                                Intent data = result.getData();
                                mImageUri = data.getData();
                                if (mImageUri != null) {
                                    getScaledBitmap(mImageUri);
                                } else {
                                    Toast.makeText(HostProfileAct.this, "Error getting Photo",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }

                            Toast.makeText(HostProfileAct.this, "Image picking returned successful", Toast.LENGTH_SHORT).show();
                            //doProcessing();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(HostProfileAct.this, "Activity canceled", Toast.LENGTH_SHORT).show();
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
    private Button btnUpDate;

    private ImageView imgBack;
    private EditText edtAge,edtNames,edtAbout,edtInterest,edtBio,edtHr,edtAddress,edtEmail;
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
    SessionManager sessionManager;
    private EditText  edtPassword, edtName;
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
    private UserProfileInfo userProfileInfo;
    private Bundle activityBundle;
    ImageView imgButtonAddPix;
    ImageView imgButtonAddVideo;
    AddImageAdapter addImageAdapter;
    AddVideoAdapter addVideoAdapter;
    private RecyclerView rvImages,rvVideos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_host_profile);
        checkInternetConnection();
        BaseActDiamond baseActivity = new BaseActDiamond();
        FirebaseApp.initializeApp(this);
        dbHelper= new DBHelper(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        setTitle("CIKO Dating and Video chat App");
        cloudUser= new QBUser();
        currentUser= new QBUser();
        savedProfile= new SavedProfile();
        activityBundle= new Bundle();
        chipNavigationBar = findViewById(R.id.bottom_nav_barC);
        //FragmentManager fm = getSupportFragmentManager();
        calendar=Calendar.getInstance();

        currentUser = SharedPrefsHelper.getInstance().getQbUser();
        gson = new Gson();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        imgButtonAddPix = findViewById(R.id.btn_addPImage);
        imgButtonAddVideo = findViewById(R.id.btn_addVideo);

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

        btnUpDate = findViewById(R.id.btn_update);
        rvImages = findViewById(R.id.rv_images);
        rvVideos = findViewById(R.id.rv_videos);
        imgBack = findViewById(R.id.btn_back);
        edtAge = findViewById(R.id.et_age);
        edtNames = findViewById(R.id.et_name);
        edtAbout = findViewById(R.id.et_about);
        edtBio = findViewById(R.id.et_bio);
        edtHr = findViewById(R.id.et_hour);
        edtAddress = findViewById(R.id.et_address);
        edtInterest = findViewById(R.id.et_interests);
        edtEmail = findViewById(R.id.et_email);
        baseActivity.makeListOfImages();
        List<String> photos = dbHelper.getPictures();


        if(photos.size() == 0){
            makeFakeObjectToPersistInDatabase();

        }
        photos = dbHelper.getPictures();

        addImageAdapter = new AddImageAdapter(HostProfileAct.this, photos);
        rvImages.setAdapter(addImageAdapter);
        rvVideos.setAdapter(addImageAdapter);
        edtNames.setText(profileName);
        edtNames.setEnabled(false);
        //   byte [] image = convertToByteArray(IMAGES[position]);
        //  dbAdapter.insertValues(image);
        imgButtonAddPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imageIntent.setDataAndType(mImageUri,"image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                mGetPixContent.launch(imageIntent);

            }
        });
        imgButtonAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imageIntent.setDataAndType(mImageUri,"image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                mGetPixContent.launch(imageIntent);

            }
        });
        addImageAdapter.updateItems(baseActivity.listOfImages);
        btnUpDate.setOnClickListener(view -> {
            String age = edtAge.getText().toString().trim();
            String aboutMe = edtAbout.getText().toString().trim();
            String myBio = edtBio.getText().toString().trim();
            String myAddress = edtAddress.getText().toString().trim();
            String myInterest = edtInterest.getText().toString().trim();
            String myNames = edtNames.getText().toString().trim();

            if(TextUtils.isEmpty(age)){
                edtAge.setError("Age must be filled");

            }else {

            }
            if(TextUtils.isEmpty(aboutMe)){
                edtAge.setError("About me must be filled");

            }else {

            }
            if(TextUtils.isEmpty(myBio)){
                edtAge.setError("Your Bio data must be filled");

            }else {

            }
            if(TextUtils.isEmpty(myAddress)){

            }
            if(TextUtils.isEmpty(myInterest)){
                edtAge.setError("Fill your Interest");

            }
            if(userProfileInfo !=null){
                userProfileInfo.setAge(Integer.parseInt(age));
                userProfileInfo.setDescription(myBio);

            }
            if(savedProfile !=null){
                savedProfile.setSavedPMyInterest(myInterest);
                savedProfile.setSavedPAboutMe(aboutMe);
            }
            SavedProfileDAO savedProfileDAO = new SavedProfileDAO(this);

            savedProfileDAO.updateSavedProfile(profileID,age,aboutMe,myInterest);
            SharedPreferences.Editor editor = userPreferences.edit();
            editor.putString("USER_PROF_INFO_AGE", age);
            editor.putString("USER_PROF_INFO_NAME", myNames);
            editor.putString("USER_PROF_INFO_DESC", myBio);
            editor.putString("SAVED_PROFILE_MY_INT", myInterest);
            editor.putString("SAVED_PROFILE_ABOUT_ME", aboutMe);
            editor.putString("PROFILE_NEXT_OF_KIN", "");
            json1 = gson1.toJson(userProfileInfo);
            json = gson.toJson(userProfileInfo);
            editor.putString("PICTURE_URI", String.valueOf(mImageUri));
            editor.putString("LastSavedProfileUsed", json);
            editor.putString("LastUserProfileInfoUsed", json1).apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        //imgBack.setOnClickListener(view -> getActivity().onBackPressed());
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
    /*public void doProcessImages(){
        new AsyncTask<Void, Void, List<String>>() {

            public ProgressDialog dialog;

            @Override
            protected void onPreExecute() {




            }

            @Override
            protected List<String> doInBackground(Void... params) {
                dbHelper= new DBHelper(HostProfileAct.this);


                List<String> photos = dbHelper.getPictures();


                if(photos.size() == 0){
                    makeFakeObjectToPersistInDatabase();

                }
                photos = dbHelper.getPictures();



                return photos;
            }

            @Override
            protected void onPostExecute(List<String> photos) {
//                dialog.dismiss();

                addImageAdapter = new AddImageAdapter(HostProfileAct.this, photos);

                rvImages.setAdapter(addImageAdapter);

            }
        }.execute();


    }*/
    private void makeFakeObjectToPersistInDatabase(){

        String uri1 = "@drawable/photo_1";
        String uri2 = "@drawable/photo_2";
        String uri3 = "@drawable/photo_3";
        String uri4 = "@drawable/photo_4";

        dbHelper = new DBHelper(this);
        dbHelper.insertProfilePicture(Uri.parse(uri1));
        dbHelper.insertProfilePicture(Uri.parse(uri2));



        /*int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
        int imageResource2 = getResources().getIdentifier(uri2, null, getPackageName());

        Drawable res0 = getResources().getDrawable(imageResource1);
        Drawable res1 = getResources().getDrawable(imageResource2);


        Bitmap bitmap1 = ((BitmapDrawable)res0).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable)res1).getBitmap();

        UserProfileInfo photoModel0 = new UserProfileInfo();
        photoModel0.setByteBuffer(getBytes(bitmap1));

        UserProfileInfo photoModel1 = new UserProfileInfo();
        photoModel1.setByteBuffer(getBytes(bitmap2));*/





    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }



    public Bitmap addGradient(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, width/2, height, ResourceUtils.getColor(R.color.primary_blue), ResourceUtils.getColor(R.color.primary_purple), Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }
    private Bitmap getScaledBitmap(Uri uri) {
        Bitmap thumb = null;
        try {
            ContentResolver cr = getContentResolver();
            InputStream in = cr.openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            thumb = BitmapFactory.decodeStream(in, null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(HostProfileAct.this, "File not found", Toast.LENGTH_SHORT).show();
        }
        return thumb;
    }
    public Bitmap getBitmap(String path) {
        Bitmap myBitmap = null;
        File imgFile = new File(path);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public byte[] convertToByteArray(int image){

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(image);
        Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        return bitmapData;

    }

    public void doUpdate(View view) {
    }
}