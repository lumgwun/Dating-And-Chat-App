package com.lahoriagency.cikolive.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lahoriagency.cikolive.Classes.PasswordHelpers;
import com.lahoriagency.cikolive.Classes.SavedProfile;

import java.util.ArrayList;
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
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_STATUS;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_USERPROF_INFO_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_TABLE;
import static java.lang.String.valueOf;


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
                    savedProfile.setSavedPName(cursor.getString(1));
                    savedProfile.setSavedPAge(cursor.getString(2));
                    savedProfile.setSavedPLocation(cursor.getString(3));
                    savedProfile.setSavedPGender(cursor.getInt(4));
                    savedProfile.setSavedPImage(Uri.parse(cursor.getString(5)));
                    savedProfile.setSavedPCountry(cursor.getString(11));
                    savedProfile.setSavedPAboutMe(cursor.getString(13));
                    savedProfile.setSavedPDateJoined(cursor.getString(16));
                    savedProfile.setSavedPLookingFor(cursor.getString(15));
                    profileArrayList.add(savedProfile);
                } while (cursor.moveToNext());
            }
        }
        return profileArrayList;
    }
    Cursor check_usernumber_exist(String user_email,String status){
        String query = "SELECT * FROM "+USER_PROF_INFO_TABLE+" WHERE "+SAVED_PROFILE_EMAIL+" ="+user_email+ " AND "+SAVED_PROFILE_STATUS+" = "+status;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if(db != null){
            cursor = db.rawQuery(query,null);
            return cursor;
        }
        else{
            return cursor;
        }
    }

    public long insertSavedProfile(SavedProfile savedProfile) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int qbUserID = savedProfile.getSavedProfQBID();
        int savedProfID = savedProfile.getSavedProfID();
        int userInfoProfID = savedProfile.getSavedProfUserProfInfoID();
        String dateJoined =savedProfile.getSavedPDateJoined();
        String name =savedProfile.getSavedPName();
        String phone =savedProfile.getSavedPPhone();
        String aboutMe =savedProfile.getSavedPAboutMe();
        String interest =savedProfile.getSavedPMyInterest();
        String dob =savedProfile.getSavedPDob();
        String lastSeen =savedProfile.getSavedPLastSeen();
        String country =savedProfile.getSavedPCountry();
        String lookingFor =savedProfile.getSavedPLookingFor();
        int gender=savedProfile.getSavedPGender();
        String deviceID =savedProfile.getSavedPDeviceID();
        String pass =savedProfile.getSavedPPassword();
        String passHash = PasswordHelpers.passwordHash(pass);
        String loc =savedProfile.getSavedPLocation();
        String age =savedProfile.getSavedPAge();
        Uri photo =savedProfile.getSavedPImage();
        String status =savedProfile.getSavedPStatus();


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
        contentValues.put(SAVED_PROFILE_STATUS, status);
        contentValues.put(SAVED_PROFILE_MY_INT, interest);
        contentValues.put(SAVED_PROFILE_LOOKING_GENDER, lookingFor);
        contentValues.put(SAVED_PROFILE_DATE_JOINED, dateJoined);
        contentValues.put(SAVED_PROFILE_USERPROF_INFO_ID, userInfoProfID);
        contentValues.put(SAVED_PROFILE_QBID, qbUserID);
        sqLiteDatabase.insert(USER_PROF_INFO_TABLE, null, contentValues);

        return savedProfID;
    }
    public long insertFirstSavedProfile(String email,String password,String dateJoined,String deviceID,String country,String status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int savedProfID=0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SAVED_PROFILE_EMAIL, email);
        contentValues.put(SAVED_PROFILE_PASSWORD, password);
        contentValues.put(SAVED_PROFILE_DEVICEID, deviceID);
        contentValues.put(SAVED_PROFILE_DATE_JOINED, dateJoined);
        contentValues.put(SAVED_PROFILE_COUNTRY, country);
        contentValues.put(SAVED_PROFILE_STATUS, status);
        sqLiteDatabase.insert(USER_PROF_INFO_TABLE, null, contentValues);

        return savedProfID;
    }
    public long insertFirstSavedProf(int qbUserID, String email, String password, String name, String dateJoined, Uri mImageUri, String status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int savedProfID=0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SAVED_PROFILE_EMAIL, email);
        contentValues.put(SAVED_PROFILE_PASSWORD, password);
        contentValues.put(SAVED_PROFILE_QBID, qbUserID);
        contentValues.put(SAVED_PROFILE_NAME, name);
        contentValues.put(SAVED_PROFILE_PHOTO, String.valueOf(mImageUri));
        contentValues.put(SAVED_PROFILE_DATE_JOINED, dateJoined);
        contentValues.put(SAVED_PROFILE_STATUS, status);
        sqLiteDatabase.insert(USER_PROF_INFO_TABLE, null, contentValues);

        return savedProfID;
    }
    public void updateProfileQBUserID(int savedProfileID,int qbUserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues stocksUpdateValues = new ContentValues();
        String selection = SAVED_PROFILE_ID + "=? ";
        String[] selectionArgs = new String[]{valueOf(savedProfileID)};
        stocksUpdateValues.put(SAVED_PROFILE_QBID, qbUserID);
        db.update(USER_PROF_INFO_TABLE, stocksUpdateValues, selection, selectionArgs);


    }
    public void updateSavedProfile(int savedProfileID,String age,String aboutMe,String interest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues savedProfUpdateValues = new ContentValues();
        String selection = SAVED_PROFILE_ID + "=? ";
        String[] selectionArgs = new String[]{valueOf(savedProfileID)};
        savedProfUpdateValues.put(SAVED_PROFILE_AGE, age);
        savedProfUpdateValues.put(SAVED_PROFILE_ABOUT_ME, aboutMe);
        savedProfUpdateValues.put(SAVED_PROFILE_MY_INT, interest);
        db.update(USER_PROF_INFO_TABLE, savedProfUpdateValues, selection, selectionArgs);

    }
    public void deleteSavedProfile(int savedProfileID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = SAVED_PROFILE_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(savedProfileID)};
        db.delete(USER_PROF_INFO_TABLE,
                selection,
                selectionArgs);

    }

}
