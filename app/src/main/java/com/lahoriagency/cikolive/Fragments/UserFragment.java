package com.lahoriagency.cikolive.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.ImageLoader;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.ProfileEditActivity;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SettingsActivity;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;


import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class UserFragment extends Fragment {
    public static final int REQUEST_SETTINGS_CODE = 231;

    private final MyPreferences myPreferences = AppE.getPreferences();

    private ImageLoader imageLoader;
    private CircleImageView profilePhoto;
    private SharedPreferences userPreferences;
    private SavedProfile savedProfile;
    private Gson gson;
    Gson gson2, gson1;
    String json, json1, json2;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    private static final String PREF_NAME = "Ciko";
    private QBUser currentUser;
    private UserProfileInfo userProfileInfo;
    private int profileID;
    private String userName,password,name;
    private QBUserCustomData userCustomData;
    private Date birthday;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.imageLoader = AppE.getImageLoader();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        savedProfile= new SavedProfile();
        currentUser= new QBUser();
        userCustomData= new QBUserCustomData();
        userProfileInfo= new UserProfileInfo();
        QBSettings.getInstance().init(getContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        ImageView settingsImage = view.findViewById(R.id.settings);
        ImageView profileEditImage = view.findViewById(R.id.profile_edit_image);
        TextView profileName = view.findViewById(R.id.fragment_user_profile_name);
        profilePhoto = view.findViewById(R.id.fragment_user_profile_image);
        userPreferences = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        gson1= new Gson();
        gson2= new Gson();
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        userName = userPreferences.getString("SAVED_PROFILE_EMAIL", "");
        password = userPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        if(savedProfile !=null){
            userCustomData=savedProfile.getQbUserCustomData();
        }
        if (userCustomData != null) {
            if (userCustomData.getProfilePhotoData().size() > 0) {
                String link = userCustomData.getProfilePhotoData().get(0).getLink();
                imageLoader.downloadImage(link, profilePhoto);
            }
        }
        String age = "";
        if(myPreferences !=null){
            name = myPreferences.getFirstName();
            birthday=myPreferences.getBirthday();

        }

        if (birthday != null) {
            age = ", " + (new Date().getYear() - birthday.getYear());
        }
        profileName.setText(String.format("%s%s", name, age));

        settingsImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                Objects.requireNonNull(getActivity()).startActivityForResult(intent, REQUEST_SETTINGS_CODE);
            }
        });

        profileEditImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(savedProfile !=null){
            userCustomData=savedProfile.getQbUserCustomData();
        }
        if (userCustomData != null) {
            if (userCustomData.getProfilePhotoData().size() > 0) {
                String link = userCustomData.getProfilePhotoData().get(0).getLink();
                imageLoader.downloadImage(link, profilePhoto);
            }
        }
        if (userCustomData != null) {
            if (userCustomData.getProfilePhotoData().size() > 0) {
                String link = userCustomData.getProfilePhotoData().get(0).getLink();
                imageLoader.downloadImage(link, profilePhoto);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
