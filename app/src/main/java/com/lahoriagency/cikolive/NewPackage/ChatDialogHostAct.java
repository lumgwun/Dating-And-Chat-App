package com.lahoriagency.cikolive.NewPackage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.Classes.QbUsersHolder;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.GooglePayCheckoutAct;
import com.lahoriagency.cikolive.ListUsersActivity;
import com.lahoriagency.cikolive.MainActivity;
import com.lahoriagency.cikolive.R;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class ChatDialogHostAct extends AppCompatActivity {
    private Bundle bundle;
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
    String json, json1, json2,actionDate,sessionDateTime,firstName,eventTittle,startDateTime,name;
    private QBUser qbUser;
    private int savedProfID;
    private Uri userPix;
    private SessionFreePaid sessionFreePaid;
    private int collections,qbUserID,sessionID;
    private Calendar calendar,newCalendar;
    private Uri mImageUri;
    ContentLoadingProgressBar progressBar;
    CircleImageView profilePix;
    private ArrayList<DiamondTransfer> diamondTransfers;
    AppCompatImageView imgGreetings;
    private ProgressDialog progressDialog;
    private List<Integer> usersList;
    private TextView txtTittle;
    private RecyclerView recyclerView,recyclerViewU;
    private AppCompatButton btnHome;
    private ArrayList<QBUser> qbUserWithoutCurrent;
    private String qbDialogID;

    private ArrayList<QBUser> bothUsersList = new ArrayList<>();

    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;


    private DialogsManager dialogsManager = new DialogsManager();

    private QBSystemMessagesManager systemMessagesManager;
    FloatingActionButton startFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_dialog_host);
        checkInternetConnection();
        setTitle("Session Host Area");
        bundle = new Bundle();
        savedProfile = new SavedProfile();
        usersList = new ArrayList<Integer>();
        diamondTransfers = new ArrayList<>();
        sessionFreePaid = new SessionFreePaid();
        FirebaseApp.initializeApp(this);
        txtTittle = findViewById(R.id.tittle_sess);
        startFAB = findViewById(R.id.fb_id_home);
        recyclerView = findViewById(R.id.recyler_diamondList);
        recyclerViewU = findViewById(R.id.recyler_session_users);
        btnHome = findViewById(R.id.btn_Hom);
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


        if (savedProfile != null) {
            name = savedProfile.getSavedPName();
            userPix = savedProfile.getSavedPImage();
            savedProfID=savedProfile.getSavedProfID();
        }
        newCalendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sessionDateTime = mdformat.format(newCalendar.getTime());

        if (qbUser != null) {
            qbUserID = qbUser.getId();
            name=qbUser.getFullName();
        }
        bundle = getIntent().getExtras();
        newCalendar=Calendar.getInstance();
        if (bundle != null) {
            sessionFreePaid = bundle.getParcelable("SessionFreePaid");
            sessionID=bundle.getInt("sessionID");
            startDateTime=bundle.getString("startDateTime");
            qbDialogID=bundle.getString("qbDialogID");

        }
        if(startDateTime.equalsIgnoreCase(sessionDateTime)){
            startFAB.setVisibility(View.VISIBLE);

        }
        if (sessionFreePaid != null) {
            usersList = sessionFreePaid.getSessionUserIDs();
            diamondTransfers=sessionFreePaid.getSessionDiamondTransfers();
            eventTittle=sessionFreePaid.getSessionTittle();
        }
        retrieveAllUser(qbDialogID);

        txtTittle.setText("Event Tittle"+eventTittle);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDialogHostAct.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                startActivity(intent);

            }
        });
        startFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnHome.setOnClickListener(this::dashB);
    }
    private void loadNewUsersForSystemMsgsByIDs(final Collection<Integer> userIDs, final QBPagedRequestBuilder requestBuilder,
                                                final ArrayList<QBUser> alreadyLoadedUsers, final QBEntityCallback<ArrayList<QBUser>> callback) {
        QBUsers.getUsersByIDs(userIDs, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                if (qbUsers != null) {
                    //((App) context).getQBUsersHolder().putUsers(qbUsers);
                    alreadyLoadedUsers.addAll(qbUsers);

                    if (bundle != null) {
                        int totalPages = (int) bundle.get(ChatHelper.TOTAL_PAGES_BUNDLE_PARAM);
                        int currentPage = (int) bundle.get(ChatHelper.CURRENT_PAGE_BUNDLE_PARAM);
                        if (totalPages > currentPage) {
                            requestBuilder.setPage(currentPage + 1);
                            loadNewUsersForSystemMsgsByIDs(userIDs, requestBuilder, alreadyLoadedUsers, callback);
                        } else {
                            callback.onSuccess(alreadyLoadedUsers, bundle);
                        }
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }
    private void retrieveAllUser(String qbDialogID) {
        QBPagedRequestBuilder qbRequestGetBuilder = new QBPagedRequestBuilder();
        qbRequestGetBuilder.setPage(1);
        qbRequestGetBuilder.setPerPage(100);
        recyclerViewU = findViewById(R.id.recyler_session_users);

        QBUsers.getUsers(qbRequestGetBuilder, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QbUsersHolder.getInstance().putUsers(qbUsers);

                usersList = new ArrayList<Integer>();
                String usersIds = QBDialogUtils.getOccupantsIdsStringFromList(usersList);
                qbUserWithoutCurrent = new ArrayList<>();

                for (QBUser qbUser: qbUsers)
                {
                    //
                    //if(!qbUser.getLogin().equals(QBChatService.().getUser().getLogin()))
                    {
                        qbUserWithoutCurrent.add(qbUser);
                    }
                }

                ListUsersAdapter listUsersAdapter = new ListUsersAdapter(ChatDialogHostAct.this, qbUserWithoutCurrent);
                recyclerViewU.setLayoutManager(new LinearLayoutManager(ChatDialogHostAct.this));
                recyclerViewU.setItemAnimator(new DefaultItemAnimator());
                recyclerViewU.setAdapter(listUsersAdapter);
                //listUsersAdapter.setItemClickListener(this);
                listUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error", e.getMessage());
            }
        });
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

    public void dashB(View view) {
    }
}