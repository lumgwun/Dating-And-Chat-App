package com.lahoriagency.cikolive.NewPackage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import com.android.installreferrer.api.InstallReferrerClient;
import com.devspark.robototextview.widget.RobotoCheckBox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.QbDialogHolder;
import com.lahoriagency.cikolive.Classes.QbEntityCallbackImpl;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.CreateProfileActivity;
import com.lahoriagency.cikolive.Interfaces.ItemClickListener;
import com.lahoriagency.cikolive.ListUsersActivity;
import com.lahoriagency.cikolive.R;
import com.melnykov.fab.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.model.QBEntity;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import static android.content.ContentValues.TAG;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class ChatDialogActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {
    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;
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
    String json, json1, json2,user,password,name;
    private QBUser qbUser;
    private int userID;
    private com.github.clans.fab.FloatingActionButton fabAddSession;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabHome;

    private ArrayList<QBUser> users = new ArrayList<>();
    private DatePicker sessionDatePicker;
    private TimePicker startTimePicker;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    String strDateOfSession,strStartTimeOfSession;
    private int hour,minute;
    private  String am_pm;
    private AppCompatButton btnCreateSession;
    private CheckBox iAgreeCheckBox;
    private RobotoCheckBox rBtnMale, rbBtnFemale;
    private AppCompatEditText edtDiamond;
    private ImageView selectPix,closeBottom_sheet;
    InstallReferrerClient referrerClient;
    private UserProfileInfo userProfileInfo;
    private ProgressDialog progressDialog;
    private ContentLoadingProgressBar progressbar;
    SecureRandom random;
    private Bundle activityBundle;
    private int savedProfileID;
    private Diamond diamond;
    int PERMISSION_ALL33 = 23;
    int diamondCount,diamondID,diamondAmt;
    private int collections,qbUserID,sessionID;
    private SessionFreePaid sessionFreePaid,linkSession;
    private String startTimeOfSession,qbDialogID,selectedNoOfSessions, sessionEndMinutes,sessionDiamond,sessionTittle, sessionMaleGender,sessionFeMaleGender;
    SimpleDateFormat hourMinutes = new SimpleDateFormat("HH:mm");
    AppCompatTextView txtWelcome;
    AppCompatEditText edtSessionTittle;
    private CircularImageView userPhoto;
    private Uri userPix;
    private DynamicLink dynamicLink;
    private Date dateOfSession;
    private Uri uri;
    private CoordinatorLayout bottomSheetLayout;
    private  long maxDateAllowed;
    private Calendar newCalendar;
    private Spinner spnNoOfSession;
    private String startDateTime, qbDialogID33,path, limiter,limitExtra;
    public static final int REQUEST_DIALOG_ID_FOR_UPDATE = 15;
    Cursor calCursor;
    private Calendar cal33;
    private Bundle linkBundle;
    private int linkedSavedProfID;
    private TimePicker.OnTimeChangedListener mNullTimeChangedListener =
            new TimePicker.OnTimeChangedListener() {

                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                }
            };
    private TimePicker.OnTimeChangedListener mStartTimeChangedListener =
            new TimePicker.OnTimeChangedListener() {

                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    updateDisplay(view,  hourOfDay, minute);
                }
            };
    String[] PERMISSIONS33 = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
             Manifest.permission.READ_CALENDAR,Manifest.permission.SET_ALARM, Manifest.permission.WRITE_CALENDAR
    };
    ActivityResultLauncher<Intent> mSessionPixContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:

                            Toast.makeText(ChatDialogActivity.this, "Image picking returned successful", Toast.LENGTH_SHORT).show();
                            //doProcessing();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(ChatDialogActivity.this, "Activity canceled", Toast.LENGTH_SHORT).show();
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
                            //resultCode=result.getResultCode();
                            Intent data = result.getData();
                            userProfileInfo= new UserProfileInfo();
                            ArrayList<String> videoResult = new ArrayList<>();

                            if (data != null) {
                                //clipData = data.getClipData();

                            }
                            Toast.makeText(ChatDialogActivity.this, "Image picking returned successful", Toast.LENGTH_SHORT).show();
                            //doProcessing();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(ChatDialogActivity.this, "Activity canceled", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.act_chat_dialog);
        if (!hasPermissions(ChatDialogActivity.this, PERMISSIONS33)) {
            ActivityCompat.requestPermissions(ChatDialogActivity.this, PERMISSIONS33, PERMISSION_ALL33);
        }
        checkInternetConnection();
        FirebaseApp.initializeApp(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        savedProfile= new SavedProfile();
        gson= new Gson();
        linkBundle= new Bundle();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        dateOfSession= new Date();
        newCalendar=Calendar.getInstance();
        Animation translater22 = AnimationUtils.loadAnimation(this, R.anim.bounce);
        sessionFreePaid= new SessionFreePaid();
        linkSession= new SessionFreePaid();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        getSessionLink();
        if(savedProfile !=null){
            name=savedProfile.getSavedPName();
            userPix=savedProfile.getSavedPImage();
            savedProfileID=savedProfile.getSavedProfID();
        }
        linkBundle=getIntent().getExtras();
        fabHome = findViewById(R.id.fab3_chat_home);

        floatingActionButton = findViewById(R.id.chat_dialog_add_user);
        fabAddSession = findViewById(R.id.fab2_add_sessions);
        progressbar = findViewById(R.id.progressBar_Session);
        spnNoOfSession = findViewById(R.id.session_spinner);
        cal33 = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        if(linkBundle !=null){
            sessionID=linkBundle.getInt("sessionID");
            startDateTime=linkBundle.getString("startDateTime");
            linkedSavedProfID=linkBundle.getInt("SAVED_PROFILE_ID");
            qbDialogID33=linkBundle.getString("qbDialogID");
            linkSession=linkBundle.getParcelable("SessionFreePaid");
        }else {
            sessionID = ThreadLocalRandom.current().nextInt(122, 1631);
            startDateTime = mdformat.format(cal33.getTime());

        }



        handleFirebaseDynamicLink(linkSession,linkedSavedProfID,startDateTime,sessionID,qbDialogID33,savedProfile);


        View bottomSheet = findViewById(R.id.bottom_sheet_session);
        spnNoOfSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectedOffice = office.getSelectedItem().toString();
                selectedNoOfSessions = (String) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sessionDatePicker = findViewById(R.id.session_datePicker);
        startTimePicker = findViewById(R.id.session_startTime);
        btnCreateSession = findViewById(R.id.btn_submit_session);
        iAgreeCheckBox = findViewById(R.id.checkbox_IAgree);
        rBtnMale = findViewById(R.id.session_sex_Male);
        rbBtnFemale = findViewById(R.id.session_sex_Female);
        edtDiamond = findViewById(R.id.et_session_diamond);
        txtWelcome = findViewById(R.id.Welc_Name);
        userPhoto = findViewById(R.id.uProf_Im);
        txtWelcome.setText("Welcome"+""+name);

        edtSessionTittle = findViewById(R.id.et_sessionT);
        bottomSheetLayout = findViewById(R.id.bottom_sheet_session);

        selectPix = findViewById(R.id.session_picP);
        closeBottom_sheet = findViewById(R.id.btn_close_bs);

        startTimePicker.setIs24HourView(true);
        selectPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.setType("*/*");
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mSessionPixContent.launch(intent);

            }
        });
        final Calendar calendar33 = Calendar.getInstance();
        final int day = calendar33.get(Calendar.DAY_OF_MONTH);
        final int year = calendar33.get(Calendar.YEAR+1);
        final int month = calendar33.get(Calendar.DECEMBER);
        if(newCalendar !=null){
            newCalendar.set(year,month,day);

        }

        selectPix.setOnClickListener(this::doSelectSessionPix);
        Animation translater = AnimationUtils.loadAnimation(this, R.anim.bounce);

        selectPix.startAnimation(translater);
        sessionDatePicker.setMinDate(SystemClock.currentThreadTimeMillis());
        sessionDatePicker.setMaxDate(newCalendar.getTimeInMillis());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, sessionDatePicker.getYear());
        cal.set(Calendar.MONTH, sessionDatePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, sessionDatePicker.getDayOfMonth());
        dateOfSession = cal.getTime();
        int startYear=sessionDatePicker.getYear();
        int startMonth=sessionDatePicker.getMonth();
        int startDay=sessionDatePicker.getDayOfMonth();
        strDateOfSession = dateFormatter.format(dateOfSession);
        hour = startTimePicker.getHour();
        minute = startTimePicker.getMinute();
        if(hour >0){
            if(hour > 12) {
                am_pm = "PM";
                hour = hour - 12;
            }
            else {
                am_pm="AM";
            }
        }
        strStartTimeOfSession=hour +":"+ minute+" "+am_pm;
        startTimeOfSession=hour +":"+ minute;

        Date date = null;
        try {
            date = hourMinutes.parse(startTimeOfSession);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int intSession= Integer.parseInt(selectedNoOfSessions);
        calendar.add(Calendar.MINUTE, 15*intSession);
        sessionEndMinutes = hourMinutes.format(calendar.getTime());

        fabAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetLayout.setVisibility(View.VISIBLE);
                showBottomSheetDialog();
            }
        });
        fabAddSession.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                bottomSheetLayout.setVisibility(View.GONE);
                return false;
            }
        });
        btnCreateSession.setAnimation(translater22);
        btnCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionTittle = edtSessionTittle.getText().toString().trim();
                sessionDiamond = edtDiamond.getText().toString().trim();
                diamondAmt= Integer.parseInt(sessionDiamond);
                String status="";
                if (TextUtils.isEmpty(sessionTittle)) {
                    Toast.makeText(ChatDialogActivity.this,
                            "Please enter the session Tittle!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }else {
                    createSessionForChat(savedProfileID,sessionID,diamondAmt, sessionMaleGender,sessionFeMaleGender,sessionTittle,startTimeOfSession, sessionEndMinutes,status,sessionFreePaid,startYear,startMonth,startDay,hour,minute,dateOfSession,selectedNoOfSessions,intSession);
                }


            }
        });
        btnCreateSession.setOnClickListener(this::createSession);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        closeBottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    behavior.setPeekHeight(0);
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
        startTimePicker.setOnTimeChangedListener(mStartTimeChangedListener);

        /*startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                updateDisplay(startTimePicker,hourOfDay, minute);
            }
        });*/




        findIds();
        setClicks();

        loadChatDialogs();

    }
    private void getSessionLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
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

    }

    @SuppressLint("Recycle")
    private void createSessionForChat(int savedProfileID, int sessionID, int diamondAmt, String sessionMaleGender, String sessionFeMaleGender, String sessionTittle, String startTimeOfSession, String sessionEndTime, String status, SessionFreePaid sessionFreePaid, int startYear, int startMonth, int startDay, int hour, int minute, Date dateOfSession, String selectedNoOfSessions, int intSession) {
        final ProgressDialog progressDialog = new ProgressDialog(ChatDialogActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        long calID = 3;
        FirebaseApp.initializeApp(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, hour, minute);
        Calendar endTime = Calendar.getInstance();
        endTime.set(startYear, startMonth, startDay, hour, intSession % 60);
        ContentValues event = new ContentValues();
        ContentResolver contentResolver = getContentResolver();

        if(isEventInCal(ChatDialogActivity.this,"cal_meeting_id")){
            finish();

        }else {
            try {
                event.put(CalendarContract.Events.CALENDAR_ID, calID);
                event.put(CalendarContract.Events.TITLE, sessionTittle);
                event.put(CalendarContract.Events.DESCRIPTION, "Virtual Event on Ciko Live");
                event.put(CalendarContract.Events.EVENT_LOCATION, "Ciko Live App");
                event.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                event.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
                event.put(CalendarContract.Events.ALL_DAY, false);
                event.put(CalendarContract.Events.HAS_ALARM, true);

            } catch (NullPointerException e) {
                System.out.println("Oops!");
            }

            String projection[] = {"_id", "calendar_displayName"};
            //event.put(CalendarContract.Events.RRULE, EventFrequency);
            event.put(CalendarContract.Events.EVENT_TIMEZONE, "GMT+01:00");

            //endTime.set(startYear, startMonth, startDay, hour, minute+240);
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.Events.TITLE, sessionTittle);
            calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Ciko Live App");
             uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, event);
            calCursor = this.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, CalendarContract.Calendars.VISIBLE + " = 1 AND "  + CalendarContract.Calendars.IS_PRIMARY + "=1", null, CalendarContract.Calendars._ID + " ASC");
            if(calCursor.getCount() <= 0){
                calCursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, CalendarContract.Calendars.VISIBLE + " = 1", null, CalendarContract.Calendars._ID + " ASC");
            }
        }
        long eventId = Long.parseLong(uri.getLastPathSegment());
        ContentValues reminder = new ContentValues();
        reminder.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminder.put(CalendarContract.Reminders.MINUTES, 30);
        reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);


       /* Intent intent = new Intent(getBaseContext(),NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, beginTime.getTimeInMillis()-(10*60*1000),
                AlarmManager.INTERVAL_DAY *1, pendingIntent);

        ComponentName receiver = new ComponentName(getApplicationContext(),NotificationReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);*/


        savedProfile= new SavedProfile();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        if(qbUser !=null){
            user=qbUser.getEmail();
            password=qbUser.getPassword();
            name=qbUser.getFullName();
            userID=qbUser.getFileId();

        }
        List<Integer>occupantIDs= new ArrayList<>();
        QBEntity qbEntity= new QBEntity();
        qbEntity.setCreatedAt(dateOfSession);
        QBChatDialog grpSessionDialog = DialogUtils.buildDialog(sessionTittle, QBDialogType.parseByCode(9),occupantIDs);
        QBDialogCustomData dialogCustomData = new QBDialogCustomData("SessionFreePaid");
        dialogCustomData.put("SessionFreePaid", sessionFreePaid);
        dialogCustomData.putDate("Date", dateOfSession);
        dialogCustomData.put("EventID",eventId);
        dialogCustomData.copyFieldsTo(qbEntity);
        dialogCustomData.put("EventIntent",calendarIntent);
        grpSessionDialog.setCustomData(dialogCustomData);
        //SessionFreePaid sessionFreePaid1= new SessionFreePaid();
        QBRestChatService.createChatDialog(grpSessionDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                progressDialog.hide();
                savedProfile.addQbChatDialog(result);
                if(result !=null){
                    qbDialogID=result.getDialogId();

                }
                handleNewFirebaseDynamicLink(qbDialogID);

                Log.d("Chat created", result.toString());


            }

            @Override
            public void onError(QBResponseException responseException) {
            }
        });
        sessionFreePaid= new SessionFreePaid(sessionID,qbDialogID,eventId,occupantIDs,diamondAmt, sessionMaleGender,sessionFeMaleGender,sessionTittle,startTimeOfSession, sessionEndMinutes,status);
        updatedDialog(qbDialogID);



//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//
//            }
//        });
    }
    private void handleNewFirebaseDynamicLink(String qbDialogID) {
        try {
            dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://cikolive.page.link/session/?QbDialogID=" + qbDialogID))
                    .setDomainUriPrefix("https://cikolive.page.link/session/")
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
    private void handleFirebaseDynamicLink(SessionFreePaid linkSession, int linkedSavedProfID, String startDateTime, int sessionID, String qbDialogID33, SavedProfile savedProfile) {
        try {

            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            Uri deepLink = null;
                            String genderMale, genderFemale;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                if (deepLink != null) {
                                    //limiter = deepLink.getQueryParameter("session");
                                    limitExtra = deepLink.getQuery();
                                    path = deepLink.getPath();
                                }

                            }

                            if (path != null && path.equals("/session")) {
                                if(limitExtra.equalsIgnoreCase(qbDialogID33)){
                                    savedProfileID=linkSession.getSessionProfID();

                                }
                                if(savedProfileID==linkSession.getSessionProfID()){
                                    Bundle newBundle = new Bundle();
                                    Intent chatDialogIntent = new Intent(ChatDialogActivity.this, ChatDialogHostAct.class);
                                    newBundle.putParcelable("SessionFreePaid",linkSession);
                                    newBundle.putParcelable("SavedProfile",savedProfile);
                                    newBundle.putParcelable("SessionFreePaid",linkSession);
                                    newBundle.putInt("SAVED_PROFILE_ID",savedProfileID);
                                    newBundle.putString("startDateTime",startDateTime);
                                    newBundle.putString("qbDialogID",qbDialogID);

                                    chatDialogIntent.putExtras(newBundle);
                                    startActivity(chatDialogIntent);

                                }else {
                                    Bundle newBundle = new Bundle();
                                    Intent chatDialogIntent = new Intent(ChatDialogActivity.this, ChatSessionAction.class);
                                    newBundle.putParcelable("SessionFreePaid",linkSession);
                                    newBundle.putString("startDateTime",startDateTime);
                                    newBundle.putString("qbDialogID",qbDialogID);
                                    chatDialogIntent.putExtras(newBundle);
                                    startActivity(chatDialogIntent);
                                }

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

    private void updatedDialog(final String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(final QBChatDialog result, Bundle bundle) {
                QbDialogHolder.getInstance().addDialog(result);
                ChatHelper.getInstance().getUsersFromDialog(result, new QbEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {

                    }
                });
            }
            @Override
            public void onError(QBResponseException e) {
            }
        });
    }
    public boolean isEventInCal(Context context, String cal_meeting_id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[] { "_id" }, " _id = ? ",
                new String[] { cal_meeting_id }, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chat_dialog_add_user:
                Intent allUsersIntent = new Intent(ChatDialogActivity.this, ListUsersActivity.class);
                startActivity(allUsersIntent);
                break;
            case R.id.session_sex_Male:
                if (rBtnMale.isChecked()){
                    sessionMaleGender = "Male";
                    Toast.makeText(ChatDialogActivity.this, "Male", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.session_sex_Female:
                if (rbBtnFemale.isChecked()){
                    sessionFeMaleGender = "Female";
                    Toast.makeText(ChatDialogActivity.this, "Female", Toast.LENGTH_LONG).show();
                }

                break;
        }

    }
    private void completeSessionSignUp(Intent intent) {
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
    public static boolean isOnline(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();


    }
    @Override
    protected void onStop() {
        super.onStop();



    }

    private void updateDisplay(TimePicker timePicker, int hourOfDay, int minute) {
        int nextMinute = 0;

        timePicker.setOnTimeChangedListener(mNullTimeChangedListener);

        if (minute >= 45 && minute <= 59)
            nextMinute = 45;
        else if (minute  >= 30)
            nextMinute = 30;
        else if (minute >= 15)
            nextMinute = 15;
        else if (minute > 0)
            nextMinute = 0;
        else {
            nextMinute = 45;
        }

        if (minute - nextMinute == 1) {
            if (minute >= 45 && minute <= 59)
                nextMinute = 00;
            else if(minute  >= 30)
                nextMinute = 45;
            else if(minute >= 15)
                nextMinute = 30;
            else if(minute > 0)
                nextMinute = 15;
            else {
                nextMinute = 15;
            }
        }

        timePicker.setCurrentMinute(nextMinute);

        timePicker.setOnTimeChangedListener(mStartTimeChangedListener);

    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bs_add_session);

        closeBottom_sheet = bottomSheetDialog.findViewById(R.id.btn_close_bs);

        bottomSheetDialog.show();

        closeBottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });


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
        progressbar.show();//displays the progress bar
    }
    public void shareThroughEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jan@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
    }
    private void loadChatDialogs() {
        FirebaseApp.initializeApp(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        savedProfile= new SavedProfile();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);

        QBRequestGetBuilder qbRequestGetBuilder = new QBRequestGetBuilder();
        qbRequestGetBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null, qbRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                ChatDialogAdapter chatDialogAdapter = new ChatDialogAdapter(ChatDialogActivity.this, qbChatDialogs);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChatDialogActivity.this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(chatDialogAdapter);
                recyclerView.setClickable(true);
                chatDialogAdapter.setItemClickListener(ChatDialogActivity.this);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }


    private void setClicks() {
        floatingActionButton.setOnClickListener(this);
    }

    private void findIds() {
        floatingActionButton = findViewById(R.id.chat_dialog_add_user);
        recyclerView = findViewById(R.id.chat_dialog_recycler_view);
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadChatDialogs();
    }

    @Override
    public void onClick1(View view, int position) {
        //ChatActions();
    }

    public void createSession(View view) {
    }

    public void doSelectSessionPix(View view) {
    }

//    private void ChatActions() {
//
//
//        users.add(QBChatService.getInstance().getUser());
//
//        users.add(SellerQbUser);
//
//        if (isPrivateDialogExist(users)) {
//            users.remove(ChatHelper.getCurrentUser());
//            QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(users.get(0));
//
//            ChatActivity.startForResult(activity, REQUEST_DIALOG_ID_FOR_UPDATE, existingPrivateDialog);
//            users.clear();
//        } else {
//            ProgressDialogFragment.show(getSupportFragmentManager(), R.string.loading_chat);
//            createDialog(users);
//            users.clear();
//        }
//
//    }
}