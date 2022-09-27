package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lahoriagency.cikolive.Classes.ModelItem;
import com.lahoriagency.cikolive.Classes.SavedProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.DiamondTransfer.CREATE_DIAMOND_HISTORY_TABLE;
import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_TABLE;
import static com.lahoriagency.cikolive.Classes.ModelItem.CREATE_MODEL_TYPE_TABLE;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_ACTOR_IMAGE;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_ACTOR_NAME;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_AGE;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_DIAMOND;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_IS_SHORT;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_ITEM_ID;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_ITEM_TABLE;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_LOCATION;
import static com.lahoriagency.cikolive.Classes.ModelItem.MODEL_PROF_ID;
import static com.lahoriagency.cikolive.Classes.AppServerUser.CREATE_QB_USER_TABLE;
import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_TABLE;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.CREATE_REDEEM_REQUEST_TABLE;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.CREATE_PIXTURE_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.CREATE_SAVED_PROFILES_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.PICTURE_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.PICTURE_URI;
import static com.lahoriagency.cikolive.Classes.SavedProfile.PROFILE_PIC_SAVEDPROF_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_COUNTRY;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_DEVICEID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_EMAIL;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_LOC;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_NAME;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_PASSWORD;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;
import static com.lahoriagency.cikolive.Classes.TimeLine.CREATE_ACCOUNT_TIMELINE_TABLE;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TABLE;
import static com.lahoriagency.cikolive.Classes.Transaction.CREATE_TRANSACTIONS_TABLE;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTIONS_TABLE;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.CREATE_USER_PROF_INFO_TABLE;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_TABLE;
import static java.lang.String.valueOf;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    public static String DB_PATH = "/data/D";
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
    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DATABASE_NAME;
        sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
    }
    public SQLiteDatabase openDataBase(SQLiteDatabase db) {
        if(db.isOpen()){
            sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
            return sqLiteDatabase;
        }
        //sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");
        return sqLiteDatabase;
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            openDataBase();
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("App - create", e.getMessage());
            }
        }

    }
    private boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            Log.e("Skylight App - check", e.getMessage());
        }
        if (tempDB != null)
            //tempDB.close();
            return tempDB != null ? true : false;
        return false;
    }

    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            String outputFileName = DB_PATH + DATABASE_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }

    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
        db.execSQL(CREATE_MODEL_TYPE_TABLE);
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        db.execSQL(CREATE_USER_PROF_INFO_TABLE);
        db.execSQL(CREATE_QB_USER_TABLE);
        db.execSQL(CREATE_ACCOUNT_TIMELINE_TABLE);
        db.execSQL(CREATE_DIAMOND_HISTORY_TABLE);
        db.execSQL(CREATE_REDEEM_REQUEST_TABLE);
        db.execSQL(CREATE_PIXTURE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_STUDENTS);
        db.execSQL(" DROP TABLE IF EXISTS "+ SAVED_PROFILE_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ MODEL_ITEM_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ TRANSACTIONS_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ QBUSER_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ USER_PROF_INFO_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ TIMELINE_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ DH_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ REDEEM_REQUEST_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ PICTURE_TABLE);

        onCreate(db);
    }
    public long insertNewModelItem(ModelItem modelItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int modelID = modelItem.getModelID();
        String name =modelItem.getActorName();
        String actorImage =modelItem.getActorImage();
        int modelItemAge =modelItem.getAge();
        String location1 =modelItem.getLocation();
        int diamondCount =modelItem.getDiamond();
        int profID =modelItem.getModelProfID();
        boolean isShort =modelItem.isShort();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MODEL_ACTOR_NAME, name);
        contentValues.put(MODEL_ACTOR_IMAGE, actorImage);
        contentValues.put(MODEL_AGE, modelItemAge);
        contentValues.put(MODEL_LOCATION, location1);
        contentValues.put(MODEL_DIAMOND, diamondCount);
        contentValues.put(MODEL_ITEM_ID, modelID);
        contentValues.put(MODEL_PROF_ID, profID);
        contentValues.put(MODEL_IS_SHORT, isShort);
        sqLiteDatabase.insert(MODEL_ITEM_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return modelID;
    }
    public void deleteModelItem(int modelItemID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = MODEL_ITEM_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(modelItemID)};

        db.delete(MODEL_ITEM_TABLE,
                selection,
                selectionArgs);


    }


    public int getModelItemCount(String location) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = MODEL_LOCATION + "=? ";
        String[] selectionArgs = new String[]{valueOf(location)};
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(
                "SELECT COUNT (*) FROM " + MODEL_ITEM_TABLE + " WHERE " + selection,
                selectionArgs
        );
        int count = 0;
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    count=cursor.getColumnIndex(MODEL_PROF_ID);
                } while (cursor.moveToNext());
                cursor.close();
            }

        }

        db.close();
        return count;

    }

    public void updateModelItemDiamond(int modelItemID,int newDiamondNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String selection = MODEL_ITEM_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(modelItemID)};
        contentValues.put(MODEL_DIAMOND, newDiamondNo);
        db.update(MODEL_ITEM_TABLE, contentValues, selection, selectionArgs);
        //db.close();


    }
    public ArrayList<ModelItem> getAllModelUsers(String userLoc) {
        ArrayList<ModelItem> itemArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = MODEL_LOCATION + "=?";
        String[] selectionArgs = new String[]{valueOf(userLoc)};

        Cursor cursor = db.query(MODEL_ITEM_TABLE, null, selection, selectionArgs, null,
                null, null);
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    getModelItemFromCursor(itemArrayList, cursor);
                } while (cursor.moveToNext());
                cursor.close();
            }

        }

        db.close();

        return itemArrayList;

    }
    private void getModelItemFromCursor(ArrayList<ModelItem> modelItems, Cursor cursor) {
        try {

            while (cursor.moveToNext()) {
                int modelItemID = cursor.getInt(0);
                String itemName = cursor.getString(1);
                int age = cursor.getInt(2);
                int diamondNo = cursor.getInt(3);
                String isShort = cursor.getString(4);
                String img = cursor.getString(5);
                String location = cursor.getString(6);
                String status = cursor.getString(7);

                modelItems.add(new ModelItem (modelItemID, itemName, age,diamondNo,isShort,img,location,status));
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }


    }

    public long insertNewSavedProfile(SavedProfile lastProfileUsed) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String deviceID = lastProfileUsed.getSavedPDeviceID();
        String name =lastProfileUsed.getSavedPName();
        String country =lastProfileUsed.getSavedPCountry();
        String phone =lastProfileUsed.getSavedPPhone();
        String dob =lastProfileUsed.getSavedPDob();
        String age =lastProfileUsed.getSavedPAge();
        int gender =lastProfileUsed.getSavedPGender();
        String email =lastProfileUsed.getSavedPEmail();
        String location =lastProfileUsed.getSavedPLocation();
        String password =lastProfileUsed.getSavedPPassword();
        String referrer =lastProfileUsed.getSavedPReferrer();
        Uri profilePicture =lastProfileUsed.getSavedPImage();
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
        String deviceID = lastProfileUsed.getSavedPDeviceID();
        String name =lastProfileUsed.getSavedPName();
        String country =lastProfileUsed.getSavedPCountry();
        String phone =lastProfileUsed.getSavedPPhone();
        String dob =lastProfileUsed.getSavedPDob();
        String age =lastProfileUsed.getSavedPAge();
        int gender =lastProfileUsed.getSavedPGender();
        String email =lastProfileUsed.getSavedPEmail();
        String location =lastProfileUsed.getSavedPLocation();
        String password =lastProfileUsed.getSavedPPassword();
        String referrer =lastProfileUsed.getSavedPReferrer();
        Uri profilePicture =lastProfileUsed.getSavedPImage();
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
                savedProfile.setSavedPName(cursor.getString(1));
                savedProfile.setSavedPCountry(cursor.getString(10));
                savedProfile.setSavedPLocation(cursor.getString(3));
                savedProfile.setSavedPImage(Uri.parse(cursor.getString(5)));
                savedProfile.setSavedPGender(cursor.getInt(4));
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
    public ArrayList<String> getProfPictures(int savedProfID) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = PROFILE_PIC_SAVEDPROF_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(savedProfID)};

        try (Cursor cursor = db.query(PICTURE_TABLE, null, selection, selectionArgs, null,
                null, null)) {
            if (null != cursor)
                if (cursor.getCount() > 0) {
                    while (!cursor.isAfterLast()) {
                        array_list.add(cursor.getString(2));
                        cursor.moveToNext();
                    }
                    db.close();
                }
        }

        return array_list;
    }
    public ArrayList<String> getPictures() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor res = db.rawQuery("select * from PICTURE_TABLE", null)) {
            res.moveToFirst();

            while (!res.isAfterLast()) {
                array_list.add(res.getString(3));
                res.moveToNext();
            }
        }
        return array_list;
    }
    public Bitmap getProfilePicture(int userId) {
        String picturePath=null;
        Bitmap profilePicture=null;

        picturePath = getPicturePath(userId);
        if (picturePath == null || picturePath.length() == 0){
            return null;
        }else {
            profilePicture = BitmapFactory.decodeFile(picturePath);
        }



        return (profilePicture);
    }

    private String getPicturePath(int profileId) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = PROFILE_PIC_SAVEDPROF_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(profileId)};

        @SuppressLint("Recycle") Cursor pictureCursor = db.query(PICTURE_TABLE,
                null,
                selection, selectionArgs,
                null,
                null,
                null);
        pictureCursor.moveToNext();

        String picturePath = pictureCursor.getString(0);

        return (picturePath);
    }
    public void deletePicture(int userId) {
        String picturePath = getPicturePath(userId); // See above
        if (picturePath != null && picturePath.length() != 0) {
            File reportFilePath = new File(picturePath);
            reportFilePath.delete();
        }

        SQLiteDatabase db = getWritableDatabase();
        String selection = PROFILE_PIC_SAVEDPROF_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(userId)};

        db.delete(PICTURE_TABLE,
                selection, selectionArgs);

    }
    public long insertProfilePicture(int profileID,  Uri profilePicture) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_PIC_SAVEDPROF_ID, profileID);
        contentValues.put(PICTURE_URI, valueOf(profilePicture));
        return sqLiteDatabase.insert(PICTURE_TABLE, null, contentValues);
    }
    public long insertProfilePicture(Uri profilePicture) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PICTURE_URI, valueOf(profilePicture));
        return sqLiteDatabase.insert(PICTURE_TABLE, null, contentValues);
    }



}
