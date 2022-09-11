package com.lahoriagency.cikolive.NewPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private AppCompatButton submitButton, cancelButton;
    private EditText userNameEditText, passEditText, fullNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_up);

        initViews();
        setClicks();

        registerSessions();
    }

    private void registerSessions() {
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
    }

    @Override
    public void onClick(View v) {
        userNameEditText = findViewById(R.id.signup_editLogin);
        passEditText = findViewById(R.id.signup_editPassword);
        fullNameEditText = findViewById(R.id.signup_editFullName);
        switch (v.getId())
        {
            case R.id.signup_btnCancel:
                finish();
                break;

            case R.id.signup_btnLogin:
                String userName = userNameEditText.getText().toString();
                String password = passEditText.getText().toString();

                QBUser qbUser = new QBUser(userName, password);

                qbUser.setFullName(fullNameEditText.getText().toString());

                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        saveUserData(qbUser);
                        //loginToChat(result);
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
        }

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

    public void goFB(View view) {
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