package com.lahoriagency.cikolive.NewPackage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.GooglePayCheckoutAct;
import com.lahoriagency.cikolive.MainActivity;
import com.lahoriagency.cikolive.R;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class ChatSessionAction extends AppCompatActivity {
    private DynamicLink dynamicLink;
    public static final String EXTRA_QB_USERS = "qb_users";
    private static final String PREF_NAME = "Ciko";
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    Gson gson, gson1,gson2;
    String json, json1, json2,actionDate,firstName,name;
    private QBUser qbUser;
    private int userID;
    private Uri userPix;
    private SessionFreePaid sessionFreePaid;
    private int collections,qbUserID,sessionID;
    private Calendar calendar,newCalendar;
    private Uri mImageUri;
    ContentLoadingProgressBar progressBar;
    CircleImageView profilePix;
    AppCompatImageView imgGreetings;
    private ProgressDialog progressDialog;
    private TextView txtWelcome,txtTittle,txtDate,txtLoc,txtDiamond,txtYourStatus,txtEvent_gender;
    private Bundle sessionBundle, homeBundle;
    private CheckBox iAgreeCheckBox;
    private String agreement,dialogID,eventTittle;
    private AppCompatButton btnPayWithDiamond;
    private Diamond diamond;
    private  int diamondCount;
    private int diamondCollectionCount,savedProfID,savedProfOfHostID;
    private int sessionDiamond;
    private long eventID;
    private QBChatDialog qbChatDialog;
    private String cal_meeting_id,startDateTime,diamondTime,sessionDateTime;
    private List<Integer> usersList;
    private ImageView backImgButton;
    private DiamondTransfer diamondTransfer;
    private  Bundle googleBundle;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), new
            ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result!=null&&result.getResultCode()==RESULT_OK){
                        if(result.getData()!=null ){
                            Intent data = result.getData();


                        }
                    }
                }
            });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_session_act);
        checkInternetConnection();
        setTitle("Session Arena");
        sessionBundle = new Bundle();
        homeBundle = new Bundle();
        savedProfile = new SavedProfile();
        googleBundle= new Bundle();
        diamondTransfer= new DiamondTransfer();
        diamond = new Diamond();
        usersList = new ArrayList<Integer>();
        sessionFreePaid = new SessionFreePaid();
        FirebaseApp.initializeApp(this);
        gson = new Gson();
        gson1 = new Gson();
        gson2 = new Gson();
        qbUser = new QBUser();
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        sessionFreePaid = new SessionFreePaid();
        sharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        txtWelcome = findViewById(R.id.welcome_txt);
        iAgreeCheckBox = findViewById(R.id.checkB_I);
        btnPayWithDiamond = findViewById(R.id.btn_pay_with_D);
        backImgButton = findViewById(R.id.btn_b_close44);
        txtTittle = findViewById(R.id.event_title);
        txtDate = findViewById(R.id.event_date);
        txtLoc = findViewById(R.id.event_loc);
        txtDiamond = findViewById(R.id.event_diamond);
        txtYourStatus = findViewById(R.id.event_titlew);
        txtEvent_gender = findViewById(R.id.event_gender);



        if (savedProfile != null) {
            name = savedProfile.getSavedPName();
            userPix = savedProfile.getSavedPImage();
            diamond = savedProfile.getSavedPDiamond();
            savedProfID=savedProfile.getSavedProfID();
        }
        if (diamond != null) {
            diamondCount = diamond.getDiamondCount();
            diamondCollectionCount = diamond.getDiamondCollections();
        }
        if (qbUser != null) {
            qbUserID = qbUser.getId();
            name=qbUser.getFullName();
        }
        sessionBundle = getIntent().getExtras();
        newCalendar=Calendar.getInstance();
        if (sessionBundle != null) {
            sessionFreePaid = sessionBundle.getParcelable("SessionFreePaid");
            sessionID=sessionBundle.getInt("sessionID");
            startDateTime=sessionBundle.getString("startDateTime");
        }
        if (sessionFreePaid != null) {
            usersList = sessionFreePaid.getSessionUserIDs();
            sessionDiamond=sessionFreePaid.getSessionDiamondAmt();
            dialogID=sessionFreePaid.getQbDialogID();
            eventID=sessionFreePaid.getEventId();
            eventTittle=sessionFreePaid.getSessionTittle();
        }
        newCalendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sessionDateTime = mdformat.format(newCalendar.getTime());
        //TODO

        homeBundle.putInt("qbUserID", qbUserID);
        homeBundle.putParcelable("QBUser", (Parcelable) qbUser);
        homeBundle.putParcelable("SavedProfile", savedProfile);


        googleBundle.putInt("qbUserID", qbUserID);
        googleBundle.putParcelable("QBUser", (Parcelable) qbUser);
        googleBundle.putParcelable("SavedProfile", savedProfile);
        googleBundle.putParcelable("SessionFreePaid", sessionFreePaid);
        googleBundle.putIntegerArrayList("UserList", (ArrayList<Integer>) usersList);
        Calendar cal = Calendar.getInstance();
        backImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatSessionAction.this, MainActivity.class);
                intent.putExtras(homeBundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

            }
        });
        backImgButton.setOnClickListener(this::goBackToTheHome);
        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat33 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        diamondTime = mdformat33.format(calendar.getTime());

        btnPayWithDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iAgreeCheckBox.isChecked()) {
                    agreement = "Yes I agree";
                }
                if (TextUtils.isEmpty(agreement)) {
                    Toast.makeText(ChatSessionAction.this,
                            "Please Agree to the terms",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    if (isEventValid(ChatSessionAction.this, cal_meeting_id)) {
                        if( diamondCount>=sessionDiamond){
                            diamondTransfer=new DiamondTransfer(sessionID,dialogID,eventID,qbUserID,savedProfID,savedProfOfHostID,name,eventTittle,diamondTime,sessionDiamond);
                            diamond.setDiamondCount(diamondCount-sessionDiamond);
                        }else {
                            Toast.makeText(ChatSessionAction.this,
                                    "Sorry, you do not have enough Diamond to register for this session",
                                    Toast.LENGTH_LONG)
                                    .show();
                            Intent intent = new Intent(ChatSessionAction.this, GooglePayCheckoutAct.class);
                            intent.putExtras(googleBundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.slide_in_right,
                                    R.anim.slide_out_left);
                            startForResult.launch(intent);
                        }

                    } else {
                        Toast.makeText(ChatSessionAction.this,
                                "Event is not Valid",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }

            }
        });
        /*if (userPreferences == null){
            userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        lastProfileUsed = customerProfile;
        Customer lastCustomerUsed = customer;
        json = gson.toJson(lastProfileUsed);
        json1 = gson1.toJson(lastCustomerUsed);
        SharedPreferences.Editor editor = userPreferences.edit();
            editor.putInt("PROFILE_ID", profileID1);
            editor.putString("PROFILE_DATE_JOINED", joinedDate);
            editor.putString("Machine", "Customer");
            editor.putString(PROFILE_ROLE, "Customer");
            editor.putString("LastCustomerUsed", json1);
            editor.putString("LastProfileUsed", json).apply();

        }*/
        Animation translater = AnimationUtils.loadAnimation(this, R.anim.bounce);

        btnPayWithDiamond.startAnimation(translater);
        btnPayWithDiamond.setOnClickListener(this::PayWithDiamond);

        handleFirebaseDynamicLink(sessionID);

        mdformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        actionDate = mdformat.format(cal.getTime());

        StringBuilder welcomeString = new StringBuilder();

        int timeOfDay = cal.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 5 && timeOfDay < 12) {
            welcomeString.append(getString(R.string.good_morning));
            //imgGreetings.setImageResource(R.drawable.good_morn3);
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            welcomeString.append(getString(R.string.good_afternoon));
            //imgGreetings.setImageResource(R.drawable.good_after1);
        } else {
            welcomeString.append(getString(R.string.good_evening));
            //imgGreetings.setImageResource(R.drawable.good_even2);
        }
        int day = cal.get(Calendar.DAY_OF_WEEK);
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

        txtWelcome.setText(welcomeString.append("Welcome" + "").append(name)
                .append("How are you today? ")
                .append(getString(R.string.happy))
                .append(dow));

    }
    public boolean isEventValid(Context context, String cal_meeting_id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[] { "_id" }, " _id = ? ",
                new String[] { cal_meeting_id }, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    private void handleFirebaseDynamicLink(int sessionID) {
        try {

            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "getDynamicLink:onFailure", e);
                        }
                    });

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
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);//you can cancel it by pressing back button
        progressDialog.setMessage("signing up wait ...");
        progressBar.show();//displays the progress bar
    }
    protected void sendSMSMessage() {
        /*Bundle smsBundle = new Bundle();
        String smsMessage = uFirstName + "" + "Welcome to the Skylight  App, may you have the best experience";
        smsBundle.putString(PROFILE_PHONE, otpPhoneNumber);
        smsBundle.putString("USER_PHONE", otpPhoneNumber);
        smsBundle.putString("smsMessage", smsMessage);
        smsBundle.putString("from", "Skylight");
        smsBundle.putString("to", otpPhoneNumber);
        Intent itemPurchaseIntent = new Intent(SignUpAct.this, SMSAct.class);
        itemPurchaseIntent.putExtras(smsBundle);
        itemPurchaseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);*/
        //System.out.println(message2.getSid());;

    }
    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
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


    @Override
    protected void onStart() {
        super.onStart();


    }
    @Override
    protected void onStop() {
        super.onStop();



    }
    public static boolean isOnline(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void PayWithDiamond(View view) {
    }

    public void goBackToTheHome(View view) {
    }
}