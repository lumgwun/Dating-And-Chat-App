package com.lahoriagency.cikolive;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.BottomSheets.WebBottomSheet;
import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.AppPush;
import com.lahoriagency.cikolive.Classes.BaseAsyncTask22;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.Consts;
import com.lahoriagency.cikolive.Classes.ErrorUtils;
import com.lahoriagency.cikolive.Classes.KeyboardUtils;
import com.lahoriagency.cikolive.Classes.LoginReply;
import com.lahoriagency.cikolive.Classes.LoginRequest;
import com.lahoriagency.cikolive.Classes.LoginService;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.ResourceUtils;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.ValidationUtils;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.Fragments.ContentFragment;
import com.lahoriagency.cikolive.Fragments.TestFragment;
import com.lahoriagency.cikolive.Interfaces.GcmConsts;
import com.lahoriagency.cikolive.Interfaces.OnLoginChangeView;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;
import com.lahoriagency.cikolive.NewPackage.App;
import com.lahoriagency.cikolive.NewPackage.ChatMainAct;
import com.lahoriagency.cikolive.NewPackage.SignUpActivity;
import com.lahoriagency.cikolive.Utils.Const;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.lahoriagency.cikolive.Conference.OpponentsActivity;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class SignInActivity extends BaseActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    private AppBarConfiguration appBarConfiguration;
    SessionManager sessionManager;
    WebBottomSheet webBottomSheet;
    //private  AppChat appChat;
    private RelativeLayout  relGoogle,relUserName;
    private TextView txtPP;
    //private EditText email, password, name;
    //private Button mlogin;
    //private TextView newdnewaccount, reocverpass;

    private ProgressDialog loadingBar;
    GoogleSignInClient mGoogleSignInClient;
    DBHelper dbHelper;
    SavedProfile savedProfile;
    private static final int MIN_LENGTH = 3;
    Button btnForgotP,btnCreateNewAcct;
    private AppCompatEditText edtUser,edtPassword;
    private  Bundle fbBundle;
    private OnLoginChangeView onLoginChangeView;
    private CallbackManager callbackManager;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";

    private final List<String> permissionNeeds = Arrays.asList("email", "user_birthday");

    private PreferencesManager preferencesManager;
    private MyPreferences myPreferences;

    private boolean logged,isFacebook;
    private boolean firstVisit;
    private QBResRequestExecutor requestExecutor = new QBResRequestExecutor();
    private SharedPrefsHelper sharedPrefsHelper;
    private static final int MAX_LOGIN_LENGTH = 50;
    private static final int UNAUTHORIZED = 401;
    private String message;

    private Button buttonLogin;
    private EditText editTextLogin;
    private static final String PREF_NAME = "Ciko";
    private SharedPreferences userPreferences;
    private int profileID;
    private Gson gson,gson1;
    private String json,json1,userName,pass,refLink;
    private QBUser qbUser;
    private Bundle bundle;
    private boolean logFromSession;
    private Uri mInvitationUrl;



    private QBUser userForSave;
    public static void start(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);

    }
    ActivityResultLauncher<Intent> mStartFacebookLoginForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            fbBundle = intent.getBundleExtra("HashKey");
                        }
                        // Handle the Intent
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_in);
        setActionBarTitle(R.string.title_login_activity);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        FirebaseApp.initializeApp(this);
        savedProfile=new SavedProfile();
        bundle=new Bundle();
        qbUser= new QBUser();
        //appChat= new AppChat();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        gson1= new Gson();
        bundle=getIntent().getExtras();
        json = userPreferences.getString("LastSavedProfileUsed", "");
        json1 = userPreferences.getString("LastQBUserUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        qbUser = gson.fromJson(json1, QBUser.class);
        if(bundle !=null){
            qbUser=bundle.getParcelable("QBUser");
            savedProfile=bundle.getParcelable("SavedProfile");
            message = getIntent().getExtras().getString(Consts.EXTRA_FCM_MESSAGE);

        }
        refLink = "https://cikolive.page.link/?invitedby=" + savedProfile;
        if(savedProfile !=null){
            userName=savedProfile.getSavedPEmail();
            pass=savedProfile.getSavedPPassword();

        }
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(refLink))
                .setDomainUriPrefix("https://cikolive.page.link/")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.lahoriagency.cikolive")
                                .setMinimumVersion(1)
                                .build())
                /*.setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())*/
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = shortDynamicLink.getShortLink();
                        // ...
                    }
                });

        edtPassword = findViewById(R.id.password_signin);
        edtUser = findViewById(R.id.userName);
        relUserName = findViewById(R.id.userName_Rel);

        edtUser.addTextChangedListener(new LoginEditTextWatcher(edtUser));
        edtPassword.addTextChangedListener(new LoginEditTextWatcher(edtPassword));

        callbackManager = CallbackManager.Factory.create();

        if(qbUser !=null){
            startLoginService(qbUser);
            QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    hideProgressDialog();
                    OpponentsActivity.start(SignInActivity.this);
                    finish();
                }

                @Override
                public void onError(QBResponseException e) {
                    hideProgressDialog();
                    ToastUtils.longToast(R.string.update_user_error);
                }
            });
            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                Log.d(TAG, "SignIn Success: " + qbUser.getId().toString());
                SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                hideProgressDialog();
                MessagesActivity.start(SignInActivity.this, message);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "SignIn Error: " + e.getLocalizedMessage());
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    signUp(qbUser);
                } else {
                    showErrorSnackbar(findViewById(R.id.text_splash_app_title),
                            R.string.splash_signin_error, e, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    signIn();
                                }
                            });
                }
            }

        });
        }

        sessionManager = new SessionManager(this);
        //relFacebook = findViewById(R.id.btn_f);
        relGoogle = findViewById(R.id.btn_Google);
        btnForgotP = findViewById(R.id.forgot_login);
        btnCreateNewAcct = findViewById(R.id.signUp);
        edtPassword = findViewById(R.id.password_signin);
        edtUser = findViewById(R.id.userName);
        txtPP = findViewById(R.id.tv_privacyPolicy);
        relUserName.setOnClickListener(this::goUserName);
        //relFacebook.setOnClickListener(this::goFacebook);
        relGoogle.setOnClickListener(this::goGoggle);
        txtPP.setOnClickListener(this::goPP);
        btnForgotP.setOnClickListener(this::getPassword);
        btnCreateNewAcct.setOnClickListener(this::createANewAcct);


        loadingBar = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

        }



        txtPP.setOnClickListener(view -> {
            webBottomSheet = new WebBottomSheet(getResources().getString(R.string.privacy_policy));
            if (!webBottomSheet.isAdded()) {
                webBottomSheet.show(getSupportFragmentManager(), webBottomSheet.getClass().getSimpleName());
            }


        });


        relGoogle.setOnClickListener(view -> {
            sessionManager.saveBooleanValue(Const.IS_LOGGED_IN, true);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });


        relUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = edtUser.getText().toString().trim();
                pass = edtPassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                    edtUser.setError("Invalid Email");
                    edtUser.setFocusable(true);

                } else {
                    loginUser(userName, pass);
                }
            }
        });

        btnCreateNewAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        btnForgotP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        /*LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        myPreferences.setFbAccessToken(accessToken.getToken());
                        myPreferences.setFbId(Long.parseLong(AccessToken.getCurrentAccessToken().getUserId()));
                        preferencesManager.savePreferences();
                        onLoginChangeView.hideContent();
                        startLogin(false);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });*/

        ImageView imageView = findViewById(R.id.profImg);
        Bitmap logoBitmap = drawableToBitmap(imageView.getDrawable());
        imageView.setImageBitmap(addGradient(logoBitmap));

        RelativeLayout loginLayout = findViewById(R.id.login_layout);
        /*if (AccessToken.getCurrentAccessToken() != null) {
            loginLayout.setVisibility(View.INVISIBLE);
        }*/

    }


    protected void showErrorSnackbar(View rootLayout, @StringRes int resId, QBResponseException e, View.OnClickListener clickListener) {
        ErrorUtils.showSnackbar(rootLayout, resId, e, R.string.dlg_retry, clickListener);
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
        //LinearGradient shader = new LinearGradient(0, 0, width/2, height, ResourceUtils.getColor(R.color.primary_blue), ResourceUtils.getColor(R.color.primary_purple), Shader.TileMode.CLAMP);
        //paint.setShader(shader);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }


    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);//write your registered email
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emaill = emailet.getText().toString().trim();
                beginRecovery(emaill);//send a mail on the mail to recover password
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String emaill) {
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void loginUser(String emaill, String pass) {
        savedProfile=new SavedProfile();
        qbUser= new QBUser();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        gson1= new Gson();
        json = userPreferences.getString("LastSavedProfileUsed", "");
        json1 = userPreferences.getString("LastQBUserUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        qbUser = gson.fromJson(json1, QBUser.class);
        Bundle bundle= new Bundle();
        loadingBar.setMessage("Logging In....");
        loadingBar.show();
        sessionManager.saveBooleanValue(Const.IS_LOGGED_IN, true);
         bundle.putParcelable("QBUser", (Parcelable) qbUser);
        Intent loginIntent = new Intent(SignInActivity.this, ChatMainAct.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.putExtras(bundle);
        startActivity(loginIntent);
        startLogin(logFromSession);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void goPP(View view) {
        webBottomSheet = new WebBottomSheet(getResources().getString(R.string.privacy_policy));
        if (!webBottomSheet.isAdded()) {
            webBottomSheet.show(getSupportFragmentManager(), webBottomSheet.getClass().getSimpleName());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login_user_done:
                if (ValidationUtils.isLoginValid(this, edtUser) &&
                        ValidationUtils.isFoolNameValid(this, edtPassword)) {
                    hideKeyboard();
                    /*if (!logged) {
                        try {
                            if(!logFromSession) {
                                LoginRequest loginRequest = new LoginRequest(myPreferences.getFbId(), myPreferences.getFbAccessToken());
                                BaseAsyncTask22<LoginRequest> task = new BaseAsyncTask22<>(ServerMethodsConsts.LOGIN, loginRequest);
                                task.setHttpMethod("POST");
                                String result = task.execute().get();
                                handleLoginResponse(result);
                            }
                            onLoginChangeView.hideContent();
                            logged = true;
                            if(checkSignIn())
                                restoreChatSession();
                            else {
                                QBUser user = new QBUser(myPreferences.getFbId().toString(), myPreferences.getFbId().toString());
                                user.setFullName(myPreferences.getFirstName() + " " + myPreferences.getLastName());
                                loginToChat(user);
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }*/
                    //userForSave = createUserWithEnteredData();
                    //startSignUpNewUser(userForSave);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void hideKeyboard() {

        KeyboardUtils.hideKeyboard(edtUser);
        KeyboardUtils.hideKeyboard(edtPassword);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            hideProgressDialog();
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                //saveUserData(userForSave);
                signInCreatedUser(userForSave);
            } else {
                ToastUtils.longToast(getString(R.string.login_chat_login_error) + errorMessage);
                edtUser.setText(userForSave.getLogin());
                edtPassword.setText(userForSave.getFullName());
            }
        }
    }
    public void changeFragment(MainActivity mainActivity) {
        if (firstVisit) {
            firstVisit = false;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, new TestFragment());
            fragmentTransaction.commit();
        } else {
            ContentFragment contentFragment = new ContentFragment();
            contentFragment.setOnMatchCreatedListener(mainActivity);
            if (mainActivity.getIntent() != null && mainActivity.getIntent().getExtras() != null) {
                if (mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_DIALOG_ID) != null) {
                    contentFragment.setDialogId(mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_DIALOG_ID));
                    contentFragment.setRecipientId(Integer.valueOf(mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_RECIPIENT_ID)));
                } else if (mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_NEW_PAIR) != null) {
                    contentFragment.setDialogId(mainActivity.getIntent().getExtras().getString(GcmConsts.EXTRA_GCM_NEW_PAIR));
                }
            }

            mainActivity.setContentFragment(contentFragment);
            FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, contentFragment);
            transaction.commit();
        }
    }

    /*private void setProfileTracker() {
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                this.stopTracking();
                Profile.setCurrentProfile(newProfile);

                if (newProfile == null)
                    logged = false;
                else if (newProfile != null) {
                    savePrefs(newProfile);
                }
            }

        };
        profileTracker.startTracking();
    }*/

    private void startLogin(boolean logFromSession) {
        if (!logged) {
            try {
                if(!logFromSession) {
                    LoginRequest loginRequest = new LoginRequest(myPreferences.getFbId(), myPreferences.getFbAccessToken());
                    BaseAsyncTask22<LoginRequest> task = new BaseAsyncTask22<>(ServerMethodsConsts.LOGIN, loginRequest);
                    task.setHttpMethod("POST");
                    String result = task.execute().get();
                    handleLoginResponse(result);
                }
                onLoginChangeView.hideContent();
                logged = true;
                if(checkSignIn())
                    restoreChatSession();
                else {
                    QBUser user = new QBUser(myPreferences.getFbId().toString(), myPreferences.getFbId().toString());
                    user.setFullName(myPreferences.getFirstName() + " " + myPreferences.getLastName());
                    loginToChat(user);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLoginResponse(String response) {
        try {
            if (response != null) {
                LoginReply loginReply = AppE.getGson().fromJson(response, LoginReply.class);
                if (loginReply.isStatusOkay() | loginReply.getStatus().equals("registered")) {
                    myPreferences.setUserId(loginReply.getUserId());
                    myPreferences.setFirstName(loginReply.getFirstName());
                    myPreferences.setLastName(loginReply.getLastName());
                    myPreferences.setBirthday(new SimpleDateFormat("MM/dd/yyyy").parse(loginReply.getBirthday()));
                    myPreferences.setMinMatchValue(loginReply.getMinMatchValue());
                    myPreferences.setSexChoice(loginReply.getSexChoice());
                    myPreferences.setRadious(loginReply.getRadius());
                    myPreferences.setAgeRangeMin(loginReply.getAgeRangeMin());
                    myPreferences.setAgeRangeMax(loginReply.getAgeRangeMax());
                    myPreferences.setDescription(loginReply.getDescription());
                    myPreferences.setMbtiType(loginReply.getType() == null ? "" : loginReply.getType());
                    preferencesManager.savePreferences();
                    if(loginReply.getStatus().equals("registered")) {
                        firstVisit = true;
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "server error", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(SignInActivity.this, "login error" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void restoreChatSession(){
        if (!ChatHelper.getInstance().isLogged()) {
            QBUser currentUser = getUserFromSession();
            loginToChat(currentUser);
        } else {
            //changeFragment((MainActivity)getActivity());
        }
    }

    private QBUser getUserFromSession(){
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        return user;
    }


    protected boolean checkSignIn() {
        return SharedPrefsHelper.getInstance().hasQbUser();
    }
    private void signIn() {
        showProgressDialog(R.string.dlg_sign_in);
        String login = edtUser.getText().toString().trim();
        final QBUser qbUser = new QBUser(login, AppPush.DEFAULT_USER_PASSWORD);
        qbUser.setFullName(login);

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                Log.d(TAG, "SignIn Success: " + qbUser.getId().toString());
                SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                hideProgressDialog();
                MessagesActivity.start(SignInActivity.this, message);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "SignIn Error: " + e.getLocalizedMessage());
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    signUp(qbUser);
                } else {
                    showErrorSnackbar(findViewById(R.id.text_splash_app_title),
                            R.string.splash_signin_error, e, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    signIn();
                                }
                            });
                }
            }
        });
    }
    private void signUp(QBUser qbUser) {
        showProgressDialog(R.string.dlg_sign_up);
        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.d(TAG, "SignUp Success: " + qbUser.getId().toString());
                signIn();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "SignUp Error: " + e.getMessage());
                showErrorSnackbar(findViewById(R.id.text_splash_app_title),
                        R.string.splash_signup_error, e, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                signIn();
                            }
                        });
            }
        });
    }


    private void loginToChat(final QBUser user) {
        user.setPassword(AppChat.USER_DEFAULT_PASSWORD);
        userForSave = user;
        startLoginService(user);
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                bundle.putParcelable("QBUser", (Parcelable) user);
                Intent loginIntent = new Intent(SignInActivity.this, ChatMainAct.class);
                loginIntent.putExtras(bundle);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void savePrefs(Profile profile) {
        myPreferences.setImageURL(profile.getProfilePictureUri(200, 200).toString());
        preferencesManager.savePreferences();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void signInCreatedUser(final QBUser qbUser) {
        Log.d(TAG, "SignIn Started");
        QBUsers.signIn(qbUser);
        /*requestExecutor.signInUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Log.d(TAG, "SignIn Successful");
                sharedPrefsHelper.saveQbUser(userForSave);
                updateUserOnServer(qbUser);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.d(TAG, "Error SignIn" + responseException.getMessage());
                hideProgressDialog();
                ToastUtils.longToast(R.string.sign_in_error);
            }
        });*/
    }

    private void updateUserOnServer(QBUser user) {
        user.setPassword(null);
        QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                hideProgressDialog();
                OpponentsActivity.start(SignInActivity.this);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                ToastUtils.longToast(R.string.update_user_error);
            }
        });
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, LoginService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        LoginService.start(this, qbUser, pendingIntent);
    }


    public void goFacebook(View view) {
    }

    public void getPassword(View view) {
    }

    public void createANewAcct(View view) {
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
}