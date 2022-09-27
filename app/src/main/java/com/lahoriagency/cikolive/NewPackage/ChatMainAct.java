package com.lahoriagency.cikolive.NewPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.CreateProfileActivity;
import com.lahoriagency.cikolive.Interfaces.Consts;
import com.lahoriagency.cikolive.MainActivity;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SettingsActivity;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

public class ChatMainAct extends AppCompatActivity implements View.OnClickListener{
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";

    private Button loginButton, signUpButton;

    private EditText userEditText, passEditText;
    private int userId;

    private SharedPrefsHelper sharedPrefsHelper;
    private static final String PREF_NAME = "Ciko";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_main);
        FirebaseApp.initializeApp(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        initViews();
        setClicks();

        initializeFramework();

        restoreChatSession();
    }
    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged() && sharedPrefsHelper.hasQbUser()) {
            startLoginService(sharedPrefsHelper.getQbUser());

        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                Intent myIntent = new Intent(ChatMainAct.this, CreateProfileActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);


            } else {

                startLoginService(currentUser);
                //loginToChat(currentUser);
            }
        }
    }

    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        userId = qbSessionManager.getSessionParameters().getUserId();
        if(user!=null)
        {
            user.setId(userId);
        }

        return user;
    }

    protected void startLoginService(QBUser qbUser) {
        CallServiceNew.start(this, qbUser);
    }

    private void initializeFramework() {
        QBSettings.getInstance().init(ChatMainAct.this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    private void setClicks() {
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void initViews() {
        loginButton = findViewById(R.id.main_btnLogin);
        signUpButton = findViewById(R.id.main_btnSignup);

        passEditText = findViewById(R.id.main_editPassword);
        userEditText = findViewById(R.id.main_editLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btnLogin:
                final String userName = userEditText.getText().toString();
                final String password = passEditText.getText().toString();

                QBUser qbUser = new QBUser(userName, password);
                qbUser.setPassword(password);
                qbUser.setOldPassword(password);

                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        startAfterLoginService(qbUser);
                        Toast.makeText(ChatMainAct.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        QBChatService.setDebugEnabled(true); // enable chat logging

                        QBChatService.setDefaultPacketReplyTimeout(10000);

                        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
                        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
                        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
                        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
                        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);
                        Intent chatDialogIntent = new Intent(ChatMainAct.this, ChatDialogActivity.class);
                        chatDialogIntent.putExtra("QBUser", qbUser);
                        chatDialogIntent.putExtra("password", password);
                        chatDialogIntent.putExtra("userName", userName);
                        chatDialogIntent.putExtra("id", qbUser.getId());
                        startActivity(chatDialogIntent);

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(ChatMainAct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.main_btnSignup:
                startActivity(new Intent(ChatMainAct.this, SignUpActivity.class));
        }
    }

    private void startAfterLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, CallServiceNew.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallServiceNew.start(this, qbUser, pendingIntent);
    }
}