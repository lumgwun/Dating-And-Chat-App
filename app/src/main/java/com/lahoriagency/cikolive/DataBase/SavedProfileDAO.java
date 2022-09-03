package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lahoriagency.cikolive.Classes.PasswordHelpers;
import com.lahoriagency.cikolive.Classes.SavedProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ABOUT_ME;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_AGE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_COUNTRY;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DATE_JOINED;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DEVICEID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DOB;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_EMAIL;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_GENDER;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_LAST_SEEN;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_LOC;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_LOOKING_GENDER;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_MY_INT;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_NAME;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PASSWORD;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PHONE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PHOTO;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_QBID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_USERPROF_INFO_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_TABLE;


public class SavedProfileDAO extends DBHelperDAO{
    private static final String WHERE_ID_EQUALS = USER_PROF_INFO_ID
            + " =?";
    public SavedProfileDAO(Context context) {
        super(context);
    }
    public List<SavedProfile> getAllSavedProfs() {
        List<SavedProfile> profileArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + USER_PROF_INFO_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    SavedProfile savedProfile = new SavedProfile();
                    savedProfile.setSavedProfID(Integer.parseInt(cursor.getString(0)));
                    savedProfile.setName(cursor.getString(1));
                    savedProfile.setAge(cursor.getString(2));
                    savedProfile.setLocation(cursor.getString(3));
                    savedProfile.setGender(cursor.getInt(4));
                    savedProfile.setImage(Uri.parse(cursor.getString(5)));
                    savedProfile.setCountry(cursor.getString(11));
                    savedProfile.setAboutMe(cursor.getString(13));
                    savedProfile.setDateJoined(cursor.getString(16));
                    savedProfile.setLookingFor(cursor.getString(15));
                    profileArrayList.add(savedProfile);
                } while (cursor.moveToNext());
            }
        }
        return profileArrayList;
    }
    public long insertSavedProfile(SavedProfile savedProfile) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int qbUserID = savedProfile.getSavedProfQBID();
        int savedProfID = savedProfile.getSavedProfID();
        int userInfoProfID = savedProfile.getSavedProfUserProfInfoID();
        String dateJoined =savedProfile.getDateJoined();
        String name =savedProfile.getName();
        String phone =savedProfile.getPhone();
        String aboutMe =savedProfile.getAboutMe();
        String interest =savedProfile.getMyInterest();
        String dob =savedProfile.getDob();
        String lastSeen =savedProfile.getLastSeen();
        String country =savedProfile.getCountry();
        String lookingFor =savedProfile.getLookingFor();
        int gender=savedProfile.getGender();
        String deviceID =savedProfile.getDeviceID();
        String pass =savedProfile.getPassword();
        String passHash = PasswordHelpers.passwordHash(pass);
        String loc =savedProfile.getLocation();
        String age =savedProfile.getAge();
        Uri photo =savedProfile.getImage();


        ContentValues contentValues = new ContentValues();
        contentValues.put(SAVED_PROFILE_NAME, name);
        contentValues.put(SAVED_PROFILE_AGE, age);
        contentValues.put(SAVED_PROFILE_LOC, loc);
        contentValues.put(SAVED_PROFILE_GENDER, gender);
        contentValues.put(SAVED_PROFILE_PHOTO, String.valueOf(photo));
        contentValues.put(SAVED_PROFILE_PHONE, phone);
        contentValues.put(SAVED_PROFILE_EMAIL, phone);
        contentValues.put(SAVED_PROFILE_PASSWORD, passHash);
        contentValues.put(SAVED_PROFILE_DEVICEID, deviceID);
        contentValues.put(SAVED_PROFILE_DOB, dob);
        contentValues.put(SAVED_PROFILE_LAST_SEEN, lastSeen);
        contentValues.put(SAVED_PROFILE_COUNTRY, country);
        contentValues.put(SAVED_PROFILE_ABOUT_ME, aboutMe);
        contentValues.put(SAVED_PROFILE_MY_INT, interest);
        contentValues.put(SAVED_PROFILE_LOOKING_GENDER, lookingFor);
        contentValues.put(SAVED_PROFILE_DATE_JOINED, dateJoined);
        contentValues.put(SAVED_PROFILE_USERPROF_INFO_ID, userInfoProfID);
        contentValues.put(SAVED_PROFILE_QBID, qbUserID);
        sqLiteDatabase.insert(USER_PROF_INFO_TABLE, null, contentValues);

        return savedProfID;
    }

}
