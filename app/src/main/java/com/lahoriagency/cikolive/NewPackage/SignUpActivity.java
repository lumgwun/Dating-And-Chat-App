package com.lahoriagency.cikolive.NewPackage;


import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.BaseActNew;
import com.lahoriagency.cikolive.BaseActivity;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.SavedProfileDAO;
import com.lahoriagency.cikolive.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class SignUpActivity extends BaseActivity implements View.OnClickListener{
    private AppCompatButton submitButton, cancelButton;
    private EditText userNameEditText, passEditText, fullNameEditText,edtAge,edtPhoneNo;
    private SavedProfileDAO savedProfileDAO;
    SharedPreferences userPreferences;
    private int qbUserID;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    private FirebaseAuth auth;
    public static final int PICTURE_REQUEST_CODE = 305;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String REQUIRED = "Required";
    private static final int RESULT_CAMERA_CODE = 22;
    Gson gson, gson1,gson2,gson3;
    String json, json1,json2,json3, name;
    private static boolean isPersistenceEnabled = false;
    private Uri mImageUri;
    private CircleImageView profilePix;
    private QBUser qbUser;
    private long savedProfileID;
    private SavedProfile savedProfile;
    private DBHelper dbHelper;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "Ciko";
    private UserProfileInfo userProfileInfo;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    int PERMISSION_ALL33 = 2;
    String[] PERMISSIONS33 = {
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.SEND_SMS
    };
    String joinedDate;
    private Calendar cal;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_up);
        FirebaseApp.initializeApp(this);
        dbHelper= new DBHelper(this);
        savedProfileDAO= new SavedProfileDAO(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        initViews();
        setClicks();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        qbUser= new QBUser();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        auth = FirebaseAuth.getInstance();
        cal = Calendar.getInstance();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        //FirebaseAuthSettings firebaseAuthSettings = auth.getFirebaseAuthSettings();
        savedProfileDAO= new SavedProfileDAO(this);
        registerSessions();
        QBDialogCustomData qbDialogCustomData= new QBDialogCustomData();
    }


    private void registerSessions() {
        FirebaseApp.initializeApp(this);
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClicks() {
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void initViews() {
        submitButton = findViewById(R.id.signup_btnLogin);
        cancelButton = findViewById(R.id.signup_btnCancel);
        userNameEditText = findViewById(R.id.signup_editLogin);
        passEditText = findViewById(R.id.signup_editPassword);
        fullNameEditText = findViewById(R.id.signup_editFullName);

        //edtAge = findViewById(R.id.signup_editFullName);
        //edtPhoneNo = findViewById(R.id.signup_editFullName);
    }

    @Override
    public void onClick(View v) {
        userNameEditText = findViewById(R.id.signup_editLogin);
        passEditText = findViewById(R.id.signup_editPassword);
        fullNameEditText = findViewById(R.id.signup_editFullName);
        //edtAge = findViewById(R.id.signup_editFullName);
        //edtPhoneNo = findViewById(R.id.signup_editFullName);
        switch (v.getId())
        {
            case R.id.signup_btnCancel:
                finish();
                break;

            case R.id.signup_btnLogin:
                String userName = userNameEditText.getText().toString();
                String password = passEditText.getText().toString();

                QBUser qbUser = new QBUser(userName, password);
                name= fullNameEditText.getText().toString();


                qbUser.setFullName(name);
                cal = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                joinedDate = mdformat.format(cal.getTime());

                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        saveUserData(qbUser);
                        if(qbUser !=null){
                            qbUserID=qbUser.getFileId();

                        }
                        userProfileInfo= new UserProfileInfo(qbUserID,name);
                        saveToDBase(qbUserID,userName,password,name,joinedDate,qbUser,userProfileInfo);
                        Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                        } else {
                            //hideProgressDialog();
                            ToastUtils.longToast(R.string.sign_up_error);
                        }
                    }
                });
                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        //startAfterLoginService(qbUser);
                        Toast.makeText(SignUpActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        QBChatService.setDebugEnabled(true); // enable chat logging

                        QBChatService.setDefaultPacketReplyTimeout(10000);

                        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
                        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
                        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
                        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
                        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);
                        Intent chatDialogIntent = new Intent(SignUpActivity.this, ChatDialogActivity.class);
                        chatDialogIntent.putExtra("QBUser", qbUser);
                        chatDialogIntent.putExtra("password", password);
                        chatDialogIntent.putExtra("userName", userName);
                        chatDialogIntent.putExtra("id", qbUser.getId());
                        startActivity(chatDialogIntent);

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.profile_p:

                if (!hasPermissions(SignUpActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(SignUpActivity.this, PERMISSIONS, PERMISSION_ALL);
                }

                final PopupMenu popup = new PopupMenu(SignUpActivity.this, profilePix);
                popup.getMenuInflater().inflate(R.menu.picture, popup.getMenu());
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

    }

    private void saveToDBase(int qbUserID, String userName, String password, String name, String joinedDate, QBUser qbUser, UserProfileInfo userProfileInfo) {
        dbHelper= new DBHelper(this);
        savedProfile=new SavedProfile();
        savedProfileDAO= new SavedProfileDAO(this);
        dbHelper.onOpen(sqLiteDatabase);
        gson = new Gson();
        gson1 = new Gson();
        SavedProfile lastSavedProfileUsed=new SavedProfile();
        QBUser lastQBUserUsed=new QBUser();
        UserProfileInfo LastUserProfileInfoUsed= new UserProfileInfo();


        savedProfileID=savedProfileDAO.insertFirstSavedProf(qbUserID,userName,password,name,joinedDate,this.mImageUri,"New");
        LastUserProfileInfoUsed=userProfileInfo;
        if(savedProfileID >0){
            setTitle("onBoarding was successful");
            savedProfile.setSavedProfID(Math.toIntExact(savedProfileID));
            savedProfile.setSavedPDateJoined(joinedDate);
            savedProfile.setSavedPImage(this.mImageUri);
            savedProfile.setSavedPName(name);
            savedProfile.setSavedPEmail(userName);
            savedProfile.setSavedPPassword(password);
            savedProfile.setSavedProfQBID(qbUserID);
            savedProfile.setSavedPQbUser(qbUser);
            lastQBUserUsed=qbUser;
            lastSavedProfileUsed=savedProfile;

            json = gson.toJson(lastSavedProfileUsed);
            json1 = gson1.toJson(lastQBUserUsed);
            json2 = gson2.toJson(LastUserProfileInfoUsed);

            SharedPreferences.Editor editor = userPreferences.edit();
            editor.putInt("SAVED_PROFILE_ID", (int) savedProfileID);
            editor.putInt("SAVED_PROFILE_QBID", qbUserID);
            editor.putString("SAVED_PROFILE_PHOTO", String.valueOf(this.mImageUri));
            editor.putString("SAVED_PROFILE_EMAIL", userName);
            editor.putString("SAVED_PROFILE_DATE_JOINED", joinedDate);
            editor.putString("SAVED_PROFILE_PASSWORD", password);
            editor.putString("SAVED_PROFILE_USERNAME", userName);
            editor.putString("LastQBUserUsed", json1);
            editor.putString("LastUserProfileInfoUsed", json2);
            editor.putString("LastSavedProfileUsed", json).apply();

        }else {
            setTitle("onBoarding failed,try again later");

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == RESULT_CAMERA_CODE) {
            this.mImageUri = data.getData();
            Bitmap logoBitmap = drawableToBitmap(this.profilePix.getDrawable());
            Glide.with(SignUpActivity.this)
                    .asBitmap()
                    .load(this.mImageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_alert)
                    //.listener(listener)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .centerCrop()
                    .into(this.profilePix);
            this.profilePix.setImageBitmap(addGradient(logoBitmap));

        }
        if ((data != null) && requestCode == RESULT_LOAD_IMAGE) {
            this.mImageUri = data.getData();
            Bitmap logoBitmap = drawableToBitmap(this.profilePix.getDrawable());
            Glide.with(SignUpActivity.this)
                    .asBitmap()
                    .load(this.mImageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_alert)
                    //.listener(listener)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .centerCrop()
                    .into(this.profilePix);
            this.profilePix.setImageBitmap(addGradient(logoBitmap));

        }

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


    public void SelectPix(View view) {
    }

    private class LoginEditTextWatcher implements TextWatcher {
        private EditText editText;

        private LoginEditTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editText.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void goUserName(View view) {
    }

    public void goGoggle(View view) {
    }



    /*private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(String.valueOf(edtUser.getText()),
                String.valueOf(edtPassword.getText()));
    }
    private QBUser createQBUserWithCurrentData(String userLogin, String userFullName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userLogin) && !TextUtils.isEmpty(userFullName)) {
            qbUser = new QBUser();
            qbUser.setLogin(userLogin);
            qbUser.setFullName(userFullName);
            qbUser.setPassword(AppChat.USER_DEFAULT_PASSWORD);
        }
        return qbUser;
    }*/

    private void saveUserData(QBUser qbUser) {
        com.lahoriagency.cikolive.Classes.SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
    }
    private String getCurrentDeviceId(Context deviceID) {
        return Utils.generateDeviceId();
    }

}