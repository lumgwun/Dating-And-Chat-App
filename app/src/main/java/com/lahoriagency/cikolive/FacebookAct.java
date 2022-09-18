package com.lahoriagency.cikolive;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Interfaces.OnLoginChangeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("deprecation")
public class FacebookAct extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    TextView txtHashKey;
    private  MessageDigest md;
    private OnLoginChangeView onLoginChangeView;
    private PreferencesManager preferencesManager;
    private MyPreferences myPreferences;
    private boolean firstVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_facebook);
        printHashKey();
        txtHashKey = findViewById(R.id.tv_hashKey);
        FacebookSdk.sdkInitialize(FacebookAct.this);
        callbackManager = CallbackManager.Factory.create();
        preferencesManager = AppE.getPreferencesManager();
        myPreferences = AppE.getPreferences();
        dOfacebookLogin();

    }

    private void dOfacebookLogin() {
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                AccessToken accessToken = loginResult.getAccessToken();
                                myPreferences.setFbAccessToken(accessToken.getToken());
                                myPreferences.setFbId(Long.parseLong(AccessToken.getCurrentAccessToken().getUserId()));
                                preferencesManager.savePreferences();
                                onLoginChangeView.hideContent();
                                GraphRequest request = GraphRequest.newMeRequest(

                                        loginResult.getAccessToken(),

                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response)
                                            {

                                                if (object != null) {
                                                    try {
                                                        String name = object.getString("name");
                                                        String email = object.getString("email");
                                                        long fbUserID = object.getLong("id");

                                                        disconnectFromFacebook();
                                                        myPreferences.setFbId(fbUserID);
                                                        myPreferences.setFirstName(name);
                                                        myPreferences.setEmail(email);
                                                        preferencesManager.savePreferences();
                                                        firstVisit = true;

                                                    }
                                                    catch (JSONException | NullPointerException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel()
                            {
                                Log.v("LoginScreen", "---onCancel");
                            }


                            @Override
                            public void onError(FacebookException error)
                            {
                                // here write code when get error
                                Log.v("LoginScreen", "----onError: "
                                        + error.getMessage());
                            }
                        });
    }
    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode,
                resultCode,
                data);
    }

    private void printHashKey() {
        txtHashKey = findViewById(R.id.tv_hashKey);
        try {

            PackageInfo info
                    = getPackageManager().getPackageInfo(
                    "com.lahoriagency.cikolive",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        }

        catch (PackageManager.NameNotFoundException e) {
        }

        catch (NoSuchAlgorithmException e) {
        }
        String hashKey= Base64.encodeToString(
                md.digest(),
                Base64.DEFAULT);
        txtHashKey.setText("Hash:"+hashKey);

    }
}