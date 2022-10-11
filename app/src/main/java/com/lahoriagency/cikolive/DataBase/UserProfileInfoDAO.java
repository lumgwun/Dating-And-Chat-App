package com.lahoriagency.cikolive.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;

import java.util.ArrayList;
import java.util.List;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_COUNTRY;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DATE_JOINED;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DEVICEID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_EMAIL;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_NAME;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PASSWORD;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PHOTO;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_STATUS;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_TABLE;
import static java.lang.String.valueOf;

public class UserProfileInfoDAO extends DBHelperDAO{
    private static final String WHERE_ID_EQUALS = SAVED_PROFILE_ID
            + " =?";
    public UserProfileInfoDAO(Context context) {
        super(context);
    }
    /*public List<UserProfileInfo> getAllUserProfInfo(String gender) {
        List<UserProfileInfo> userProfInfoArrayList = new ArrayList<>();
        String selection = TRANSACTION_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(gender)};

        String selectQuery = "SELECT * FROM " + USER_PROF_INFO_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    UserProfileInfo userProfileInfo = new UserProfileInfo();
                    userProfileInfo.setUpiLookingForGender(cursor.getString(1));
                    userProfileInfo.setAge(cursor.getInt(2));
                    userProfileInfo.setUpiMyCountry(cursor.getString(3));
                    userProfInfoArrayList.add(userProfileInfo);
                    userProfileInfo.setUpiPhoto(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        }
        return userProfInfoArrayList;
    }
    public long insertUserProfInfo(String email, String password, String dateJoined, String deviceID, String country, String status, String name, int profileID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int savedProfID=0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SAVED_PROFILE_EMAIL, email);
        contentValues.put(SAVED_PROFILE_PASSWORD, password);
        contentValues.put(SAVED_PROFILE_DEVICEID, deviceID);
        contentValues.put(SAVED_PROFILE_DATE_JOINED, dateJoined);
        contentValues.put(SAVED_PROFILE_COUNTRY, country);
        contentValues.put(SAVED_PROFILE_STATUS, status);
        contentValues.put(SAVED_PROFILE_PHOTO, country);
        contentValues.put(SAVED_PROFILE_NAME, name);
        contentValues.put(SAVED_PROFILE_ID, profileID);
        return sqLiteDatabase.insert(USER_PROF_INFO_TABLE, null, contentValues);

    }*/
}
