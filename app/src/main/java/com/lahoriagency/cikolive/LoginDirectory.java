package com.lahoriagency.cikolive;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lahoriagency.cikolive.DataBase.DBHelper;

public class LoginDirectory extends AppCompatActivity {
    private ProgressDialog progressDialog;
    int profileID,customerID;
    String machinePref;
    Uri pictureLink;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private static final String PREF_NAME = "Ciko";
    private DBHelper dbHelper;
    String machineUser,userName, office,state,role,dbRole,joinedDate,passwordStg,surname, emailStrg,phoneNO, firstName, dob,gender,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_directory);
        setTitle("Login Director");
        dbHelper=new DBHelper(this);
        userExtras=getIntent().getExtras();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        machinePref = sharedPref.getString("Machine", "");
        profileID = sharedPref.getInt("PROFILE_ID", 0);
        userName = sharedPref.getString("PROFILE_USERNAME", "");
        office = sharedPref.getString("PROFILE_OFFICE", "");
        state = sharedPref.getString("PROFILE_STATE", "");
        role = sharedPref.getString("PROFILE_ROLE", "");
        passwordStg = sharedPref.getString("PROFILE_PASSWORD", "");
        joinedDate = sharedPref.getString("PROFILE_DATE_JOINED", "");
        surname = sharedPref.getString("PROFILE_SURNAME", "");
        emailStrg = sharedPref.getString("PROFILE_EMAIL", "");
        phoneNO = sharedPref.getString("PROFILE_PHONE", "");
        firstName = sharedPref.getString("PROFILE_FIRSTNAME", "");
        dob = sharedPref.getString("PROFILE_DOB", "");
        customerID = sharedPref.getInt("CUSTOMER_ID", 0);
        gender = sharedPref.getString("PROFILE_GENDER", "");
        address = sharedPref.getString("PROFILE_ADDRESS", "");
        pictureLink = Uri.parse(sharedPref.getString("PICTURE_URI", ""));
        dbRole=dbHelper.getProfileRoleByUserNameAndPassword(userName,password);
    }
}