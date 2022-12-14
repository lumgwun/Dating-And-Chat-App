package com.lahoriagency.cikolive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.AppServerUser;
import com.lahoriagency.cikolive.Classes.GPSLocationListener;
import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Fragments.ContentFragment;
import com.lahoriagency.cikolive.Fragments.DialogsFragment;
import com.lahoriagency.cikolive.Fragments.EmptyLoginFragment;
import com.lahoriagency.cikolive.Fragments.MatchFragment;
import com.lahoriagency.cikolive.Fragments.SwipeFragment;
import com.lahoriagency.cikolive.Fragments.TestFragment;
import com.lahoriagency.cikolive.Fragments.UserFragment;
import com.lahoriagency.cikolive.Interfaces.OnChangeViewListener;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class FindMatchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnChangeViewListener, SwipeFragment.OnMatchCreated, MatchFragment.MatchDialogActionsListener{

    private AppBarConfiguration appBarConfiguration;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 20; // 20 minutes
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = DialogsFragment.REQUEST_DIALOG_ID_FOR_UPDATE;
    private static final int REQUEST_FINE_LOCATION = 0;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    public ContentFragment contentFragment = null;

    protected GoogleApiClient googleApiClient;
    protected LocationRequest locationRequest;
    LocationListener locationListener;

    private MatchFragment matchDialogFragment;
    private List<UserProfileInfo> matchDialogQueue;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2,gson3;
    String json, json1, json2;
    private QBUser currentUser;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    SharedPreferences userPreferences;
    int profileID;
    private String userName,password,profileName;
    private QBUser qbUser;
    private UserProfileInfo opponentProfileInfo,myProfileInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.act_find_match);
        FragmentManager fm = getSupportFragmentManager();
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        setTitle("Find Match Mate");
        currentUser= new QBUser();
        savedProfile= new SavedProfile();
        opponentProfileInfo = new UserProfileInfo();
        myProfileInfo = new UserProfileInfo();

        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        userName = userPreferences.getString("SAVED_PROFILE_EMAIL", "");
        password = userPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileName = userPreferences.getString("SAVED_PROFILE_NAME", "");
        userName = userPreferences.getString("PROFILE_USERNAME", "");
        password = userPreferences.getString("PROFILE_PASSWORD", "");
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        myProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        //opponentProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);

        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        locationListener = new GPSLocationListener();
        locationDialog();
        matchDialogFragment = new MatchFragment();
        matchDialogFragment.setActionsListener(this);

        matchDialogQueue = new ArrayList<>();
    }
    public void locationDialog() {

        googleApiClient = new GoogleApiClient.Builder(FindMatchActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(MIN_TIME_BW_UPDATES);
        locationRequest.setFastestInterval(MIN_TIME_BW_UPDATES / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(FindMatchActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (googleApiClient != null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != RESULT_OK) {
                locationDialog();
            }
        } else if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
            Fragment fragment = contentFragment.getFragmentForPosition(2);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == UserFragment.REQUEST_SETTINGS_CODE) {
            if (data != null) {
                if (data.getIntExtra("action", 0) == SettingsActivity.LOGOUT_ACTION) {
                    logout();
                } else if (data.getIntExtra("action", 0) == SettingsActivity.START_TEST_ACTION) {
                    showTest();
                }
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag_match);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationDialog();
            }
        }
    }


    @Override
    public void showTest() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_match, new TestFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void logout() {
        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent loginIntent = new Intent(this, SignInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void showSettings() {

    }

    @Override
    public void showContent() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_match, new ContentFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public ContentFragment getContentFragment() {
        return contentFragment;
    }

    public void setContentFragment(ContentFragment contentFragment) {
        this.contentFragment = contentFragment;
    }


    @Override
    public void showMatchDialog(UserProfileInfo userProfileInfo, boolean fromQueue) {
        if (fromQueue) {
            showAndFillMatchDialog(matchDialogQueue.remove(0));
        } else {
            if (matchDialogFragment.isAddedToLayout() || matchDialogFragment.isAdded()) {
                matchDialogQueue.add(userProfileInfo);
            } else {
                showAndFillMatchDialog(userProfileInfo);
            }
        }
    }

    @Override
    public void showMatchDialog(SavedProfile savedProfile, boolean fromQueue) {

    }

    @Override
    public void showMatchDialog(AppServerUser appServerUser, boolean fromQueue) {

    }

    private void showAndFillMatchDialog(UserProfileInfo userProfileInfo) {
        QBUserCustomData userCustomData = new Gson().fromJson(SharedPrefsHelper.getInstance().getQbUser().getCustomData(), QBUserCustomData.class);
        matchDialogFragment.setImageLeftLink(userCustomData.getProfilePhotoData().get(0).getLink());
        matchDialogFragment.setImageRightLink(userProfileInfo.getPhotoLinks().get(0));
        matchDialogFragment.setRecipientId(userProfileInfo.getUserQbId());
        matchDialogFragment.setMatchUserName(userProfileInfo.getName());
        matchDialogFragment.setMatchValue(userProfileInfo.getMatchValue());
        matchDialogFragment.setAddedToLayout(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frag_match, matchDialogFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void keepSwipingButtonClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        matchDialogFragment.setAddedToLayout(false);
        ft.remove(matchDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void sendMessageClicked(Integer recipientId) {
        matchDialogQueue.clear();
        keepSwipingButtonClicked();
        contentFragment.openChatDialog(recipientId);
    }

    @Override
    public void showMatchDialogIfAny() {
        if (!matchDialogQueue.isEmpty()) {
            showMatchDialog((AppServerUser) null, true);
        }
    }

    //@Override
    public void hideContent() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frag_match, new EmptyLoginFragment(), "HIDE_CONTENT");
        fragmentTransaction.commit();
    }


}