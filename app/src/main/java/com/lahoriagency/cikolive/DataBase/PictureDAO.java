package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.SavedProfile.PICTURE_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.PICTURE_URI;
import static com.lahoriagency.cikolive.Classes.SavedProfile.PROFILE_PIC_SAVEDPROF_ID;
import static java.lang.String.valueOf;

public class PictureDAO extends DBHelperDAO{
    public PictureDAO(Context context) {
        super(context);
    }



}
