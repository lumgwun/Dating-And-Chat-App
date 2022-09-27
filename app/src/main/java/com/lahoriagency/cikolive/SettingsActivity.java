package com.lahoriagency.cikolive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.facebook.login.LoginManager;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.BaseAsyncTask22;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QbDialogHolder;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SettingsRangeSeekbar;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.UpdateSettingsRequest;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jetbrains.annotations.NotNull;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;
import static com.lahoriagency.cikolive.Classes.AppChat.TAG;
import static com.lahoriagency.cikolive.Classes.Consts.EXTRA_DIALOG_ID;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatActivity {
    public static final int LOGOUT_ACTION = 942;
    public static final int START_TEST_ACTION = 466;

    private PreferencesManager preferencesManager;
    private MyPreferences myPreferences;


    private AppCompatSeekBar distanceSeekbar;
    private AppCompatSeekBar matchValueSeekbar;
    private RangeSlider ageRangeSeekbar;

    private TextView distanceTextView;
    private TextView matchValueTextView;
    private TextView ageRangeTextView;
    private TextView sexPreferenceTextView;

    private Switch menSwitch;
    private Switch womenSwitch;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    private UserProfileInfo userProfileInfo;
    public static final String userId = "userId";
    private  int qbUserID,qbUserFieldID,minMatchValue,ageLowerValue,ageHigherValue;
    private int minDistance,minValue,ageRangeMin,ageRangeMax,distanceSelected;
    private TextView txtDistanceProgress,txtMatch_progress,txtAgeProgress;
    private String sexChoice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        setTitle("My Match Preferences");
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        preferencesManager = AppE.getPreferencesManager();
        myPreferences = AppE.getPreferences();
        savedProfile= new SavedProfile();
        userProfileInfo= new UserProfileInfo();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        menSwitch = findViewById(R.id.sex_preference_men_switch);
        womenSwitch = findViewById(R.id.sex_preference_women_switch);
        sexPreferenceTextView = findViewById(R.id.sex_preference_textview);
        distanceSeekbar = findViewById(R.id.settings_distance_seekbar);
        distanceTextView = findViewById(R.id.distance_preference_textview);
        matchValueSeekbar = findViewById(R.id.settings_match_value_seekbar);
        matchValueTextView = findViewById(R.id.match_value_preference_textview);
        ageRangeSeekbar = findViewById(R.id.settings_age_range_seekbar);
        ageRangeTextView = findViewById(R.id.age_range_preference_textview);
        txtDistanceProgress = findViewById(R.id.distance_progress);
        txtMatch_progress = findViewById(R.id.match_progressT);
        txtAgeProgress = findViewById(R.id.age_progress);

        CardView logoutCardView = findViewById(R.id.logout_cardview);
        CardView startTestCardView = findViewById(R.id.start_test_cardview);
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        json2 = sharedPref.getString("LastQBUserUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        if(qbUser !=null){
            qbUserFieldID=qbUser.getFileId();
            qbUserID=qbUser.getFileId();

        }

        setActionBarSettings();

        menSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !womenSwitch.isChecked()) {
                    womenSwitch.setChecked(true);
                    sexPreferenceTextView.setText(R.string.women);
                } else if (isChecked && !womenSwitch.isChecked()) {
                    sexPreferenceTextView.setText(R.string.Men);
                } else if (isChecked && womenSwitch.isChecked()) {
                    sexPreferenceTextView.setText(R.string.men_and_women);
                } else if (!isChecked && womenSwitch.isChecked()) {
                    sexPreferenceTextView.setText(R.string.women);
                }
            }

        });

        womenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !menSwitch.isChecked()) {
                    menSwitch.setChecked(true);
                    sexPreferenceTextView.setText(R.string.Men);
                    myPreferences.setGender("Men");
                    preferencesManager.savePreferences();
                } else if (isChecked && !menSwitch.isChecked()) {
                    sexPreferenceTextView.setText(R.string.women);
                    myPreferences.setGender("Women");
                    preferencesManager.savePreferences();
                } else if (isChecked && menSwitch.isChecked()) {
                    sexPreferenceTextView.setText(R.string.men_and_women);
                    myPreferences.setGender("Men and Women");
                    preferencesManager.savePreferences();
                } else if (!isChecked && menSwitch.isChecked()) {
                    sexPreferenceTextView.setText(R.string.men);
                    myPreferences.setGender("Men");
                    preferencesManager.savePreferences();
                }
            }

        });

        if (menSwitch.isChecked())
            sexChoice += "M";
        myPreferences.setGender("Men");
        preferencesManager.savePreferences();
        if (womenSwitch.isChecked())
            sexChoice += "F";
        myPreferences.setGender("Women");
        preferencesManager.savePreferences();

        if (myPreferences.getSexChoice().contains("M"))
            menSwitch.setChecked(true);
        if (myPreferences.getSexChoice().contains("W"))
            womenSwitch.setChecked(true);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            distanceSeekbar.setMin(myPreferences.getRadious());
        }
        if(myPreferences !=null){
            minDistance=myPreferences.getRadious();
            minValue=myPreferences.getMinMatchValue();
            ageRangeMin=myPreferences.getAgeRangeMin();
            ageRangeMax=myPreferences.getAgeRangeMax();

        }*/


        distanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                distanceTextView.setVisibility(View.VISIBLE);
                distanceTextView.setText(String.format("%s km", minDistance));
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                distanceTextView.setVisibility(View.VISIBLE);
                distanceSelected = (int) Math.ceil(progress / 1000f);
                if(distanceSelected >0){
                    txtDistanceProgress.setText(String.format("%s km", distanceSelected));

                }

                if(distanceSelected >0){
                    txtMatch_progress.setText(String.format("%s km", distanceSelected));
                    myPreferences.setRadious(distanceSelected);
                    preferencesManager.savePreferences();

                }


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        matchValueSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                matchValueTextView.setVisibility(View.VISIBLE);
                matchValueTextView.setText(String.format("%s%%", minValue));

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                matchValueTextView.setVisibility(View.VISIBLE);
                minMatchValue = (int) Math.ceil(progress / 1000f);

                if(minMatchValue >0){
                    txtMatch_progress.setText(String.format("%s km", minMatchValue));
                    myPreferences.setMinMatchValue(minMatchValue);
                    preferencesManager.savePreferences();

                }


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myPreferences.setMinMatchValue(minMatchValue);
                preferencesManager.savePreferences();

            }
        });


        ageRangeSeekbar.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull @NotNull RangeSlider slider) {
                ageLowerValue = slider.getLeft();
                ageHigherValue=slider.getRight();
                ageRangeTextView.setText(String.format("%s-%s", ageLowerValue, ageHigherValue));

            }

            @Override
            public void onStopTrackingTouch(@NonNull @NotNull RangeSlider slider) {
                ageLowerValue = slider.getLeft();
                ageHigherValue=slider.getRight();

            }
        });

        myPreferences.setAgeRangeMin(ageLowerValue);
        myPreferences.setAgeRangeMax(ageHigherValue);
        preferencesManager.savePreferences();

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QBuserLogout();
                LoginManager.getInstance().logOut();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("action", LOGOUT_ACTION);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        startTestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("action", START_TEST_ACTION);
                setResult(Activity.RESULT_OK,returnIntent);

                UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(myPreferences.getUserId(),
                        myPreferences.getRadious(), myPreferences.getMinMatchValue(), myPreferences.getSexChoice(),
                        myPreferences.getAgeRangeMin(), myPreferences.getAgeRangeMax());
                BaseAsyncTask22<UpdateSettingsRequest> saveSettingsTask = new BaseAsyncTask22<>(ServerMethodsConsts.USERSETTINGS, updateSettingsRequest);
                saveSettingsTask.setHttpMethod("POST");
                saveSettingsTask.execute();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveSettings();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveSettings();
        super.onBackPressed();
    }

    private void setActionBarSettings() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(Html.fromHtml("<font color='#555555' align='right'>Settings</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.primary_purple), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
    }

    private void saveSettings() {
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
        json2 = sharedPref.getString("LastQBUserUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        if(qbUser !=null){
            qbUserFieldID=qbUser.getFileId();
            qbUserID=qbUser.getFileId();

        }
        myPreferences.setRadious(distanceSelected);

        myPreferences.setSexChoice(sexChoice);
        myPreferences.setMinMatchValue(minMatchValue);
        preferencesManager.savePreferences();

        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(myPreferences.getUserId(),
                myPreferences.getRadious(), myPreferences.getMinMatchValue(), myPreferences.getSexChoice(),
                myPreferences.getAgeRangeMin(), myPreferences.getAgeRangeMax());
        BaseAsyncTask22<UpdateSettingsRequest> saveSettingsTask = new BaseAsyncTask22<>(ServerMethodsConsts.USERSETTINGS, updateSettingsRequest);
        saveSettingsTask.setHttpMethod("POST");
        saveSettingsTask.execute();

        Bundle bundle= new Bundle();
        bundle.putParcelable("QBUser", (Parcelable) qbUser);
        bundle.putParcelable("SavedProfile",savedProfile);
        Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra(EXTRA_DIALOG_ID,userId);
        myIntent.putExtra(userId,EXTRA_DIALOG_ID);
        myIntent.putExtra(userId,qbUserFieldID);
        myIntent.putExtra(userId,qbUserID);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    private void QBuserLogout() {
        // TODO LOGOUT
        ChatHelper.getInstance().destroy();
        SubscribeService.unSubscribeFromPushes(AppE.getAppContext());
        SharedPrefsHelper.getInstance().removeQbUser();
        QbDialogHolder.getInstance().clear();

        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.d(TAG, "SignOut Successful");
                SharedPrefsHelper.getInstance().removeQbUser();
                Intent myIntent = new Intent(SettingsActivity.this, SignInActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Unable to SignOut: " + e.getMessage());

                /*View rootView = findViewById(R.id.activity_messages);
                showErrorSnackbar(rootView, R.string.error_logout, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userLogout();
                    }
                });*/
            }
        });
        //finish();

    }
}