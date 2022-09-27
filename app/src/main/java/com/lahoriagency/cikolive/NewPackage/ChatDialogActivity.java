package com.lahoriagency.cikolive.NewPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Interfaces.ItemClickListener;
import com.lahoriagency.cikolive.ListUsersActivity;
import com.lahoriagency.cikolive.R;
import com.melnykov.fab.FloatingActionButton;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

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

    private ArrayList<QBUser> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_dialog);
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

        findIds();
        setClicks();

        createSessionForChat();

        loadChatDialogs();
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
                chatDialogAdapter.setItemClickListener(ChatDialogActivity.this);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void createSessionForChat() {
        final ProgressDialog progressDialog = new ProgressDialog(ChatDialogActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
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
        if(qbUser !=null){
            user=qbUser.getEmail();
            password=qbUser.getPassword();
            name=qbUser.getFullName();
            userID=qbUser.getFileId();



            try {
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error", e.getMessage());
                    }
                });
            } catch (IllegalArgumentException e) {
                System.out.println("Oops!");
            }

        }






//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//
//            }
//        });
    }

    private void setClicks() {
        floatingActionButton.setOnClickListener(this);
    }

    private void findIds() {
        floatingActionButton = findViewById(R.id.chat_dialog_add_user);
        recyclerView = findViewById(R.id.chat_dialog_recycler_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_dialog_add_user:
                Intent allUsersIntent = new Intent(ChatDialogActivity.this, ListUsersActivity.class);
                startActivity(allUsersIntent);
                break;
        }
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