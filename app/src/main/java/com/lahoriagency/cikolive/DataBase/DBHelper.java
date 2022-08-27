package com.lahoriagency.cikolive.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.lahoriagency.cikolive.Classes.SavedProfile;

import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.SavedProfile.CREATE_SAVED_PROFILES_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_AGE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_COUNTRY;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DEVICEID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DOB;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_EMAIL;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_GENDER;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_LOC;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_NAME;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PASSWORD;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PHONE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PHOTO;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_REFERRER;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ciko_live_db";
    private static final String TABLE_STUDENTS = "students";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String CITY = "class";

    public static final String DB_TABLE_NAME = "users";
    public static final String DB_COLUMN_ID = "ID";
    public static final String DB_COLUMN_USER_FULL_NAME = "userFullName";
    public static final String DB_COLUMN_USER_LOGIN = "userLogin";
    public static final String DB_COLUMN_USER_ID = "userID";
    public static final String DB_COLUMN_USER_PASSWORD = "userPass";
    public static final String DB_COLUMN_USER_TAG = "userTag";



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_STUDENT_TABLE = " CREATE TABLE " + TABLE_STUDENTS + "(" + ID + " INTEGER PRIMARY KEY, "
                + NAME + " TEXT, " + AGE + " TEXT, " + CITY + " TEXT " + ")";
        db.execSQL("create table " + DB_TABLE_NAME + " ("
                + DB_COLUMN_ID + " integer primary key autoincrement,"
                + DB_COLUMN_USER_ID + " integer,"
                + DB_COLUMN_USER_LOGIN + " text,"
                + DB_COLUMN_USER_PASSWORD + " text,"
                + DB_COLUMN_USER_FULL_NAME + " text,"
                + DB_COLUMN_USER_TAG + " text"
                + ");");

        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_SAVED_PROFILES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_STUDENTS);
        db.execSQL(" DROP TABLE IF EXISTS "+ SAVED_PROFILE_TABLE);

        onCreate(db);
    }
    public long insertNewSavedProfile(SavedProfile lastProfileUsed) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String deviceID = lastProfileUsed.getDeviceID();
        String name =lastProfileUsed.getName();
        String country =lastProfileUsed.getCountry();
        String phone =lastProfileUsed.getPhone();
        String dob =lastProfileUsed.getDob();
        String age =lastProfileUsed.getAge();
        int gender =lastProfileUsed.getGender();
        String email =lastProfileUsed.getEmail();
        String location =lastProfileUsed.getLocation();
        String password =lastProfileUsed.getPassword();
        String referrer =lastProfileUsed.getReferrer();
        Uri profilePicture =lastProfileUsed.getImage();
        int savedProfID =lastProfileUsed.getSavedProfID();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SAVED_PROFILE_NAME, name);
        contentValues.put(SAVED_PROFILE_DEVICEID, deviceID);
        contentValues.put(SAVED_PROFILE_LOC, location);
        contentValues.put(SAVED_PROFILE_EMAIL, email);
        contentValues.put(SAVED_PROFILE_PASSWORD, password);
        contentValues.put(SAVED_PROFILE_COUNTRY, country);
        sqLiteDatabase.insert(SAVED_PROFILE_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return savedProfID;
    }
    public void upDateUser(SavedProfile lastProfileUsed) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String deviceID = lastProfileUsed.getDeviceID();
        String name =lastProfileUsed.getName();
        String country =lastProfileUsed.getCountry();
        String phone =lastProfileUsed.getPhone();
        String dob =lastProfileUsed.getDob();
        String age =lastProfileUsed.getAge();
        int gender =lastProfileUsed.getGender();
        String email =lastProfileUsed.getEmail();
        String location =lastProfileUsed.getLocation();
        String password =lastProfileUsed.getPassword();
        String referrer =lastProfileUsed.getReferrer();
        Uri profilePicture =lastProfileUsed.getImage();
        int savedProfID =lastProfileUsed.getSavedProfID();
        ContentValues contentValues = new ContentValues();

    }
    public ArrayList<SavedProfile> getAllSavedProfiles(){
        ArrayList<SavedProfile> profileList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SAVED_PROFILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                SavedProfile savedProfile = new SavedProfile();
                savedProfile.setSavedProfID(Integer.parseInt(cursor.getString(0)));
                savedProfile.setName(cursor.getString(1));
                savedProfile.setCountry(cursor.getString(10));
                savedProfile.setLocation(cursor.getString(3));
                savedProfile.setImage(Uri.parse(cursor.getString(5)));
                savedProfile.setGender(cursor.getInt(4));
                profileList.add(savedProfile);
            }while(cursor.moveToNext());
        }
        return profileList;
    }
    public int getSavedProfileCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + SAVED_PROFILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }

    // Getting contacts Count
    public int getStudentsCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }

    public int getLastStudentsId(){
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENTS;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor != null && !cursor.isClosed()){
            cursor.moveToLast();

            if(cursor.getCount() == 0) {
                count = 1;
            }else{
                //count = cursor.getInt(cursor.getColumnIndex(ID));
            }
        }

        return count;

    }



}
