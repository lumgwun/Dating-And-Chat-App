package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lahoriagency.cikolive.Classes.DiamondTransfer;

import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_COUNT;
import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_DATE;
import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_FROM;
import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_ID;
import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_SAVED_PROF_ID;
import static com.lahoriagency.cikolive.Classes.DiamondTransfer.DH_TABLE;
import static java.lang.String.valueOf;

public class DiamondHisDAO extends DBHelperDAO{
    public DiamondHisDAO(Context context) {
        super(context);
    }
    public long insertNewDiamondHis(int dH_Count,int dH_SProf_ID,String dH_From,String dH_Date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long dHID = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DH_ID, dHID);
        contentValues.put(DH_FROM, dH_From);
        contentValues.put(DH_COUNT, dH_Count);
        contentValues.put(DH_SAVED_PROF_ID, dH_SProf_ID);
        contentValues.put(DH_DATE, dH_Date);
        sqLiteDatabase.insert(DH_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return dHID;
    }
    @SuppressLint("Range")
    public ArrayList<DiamondTransfer> getDiamondHisByProfID(int savedProfID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiamondTransfer> dataList = new ArrayList<DiamondTransfer>();
        String selection = DH_SAVED_PROF_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(savedProfID)};
        String[] columns = new String[]{DH_ID,DH_FROM,DH_COUNT,DH_SAVED_PROF_ID,DH_DATE};

        Cursor cursor = db.query(DH_TABLE, columns, selection,
                selectionArgs, null, null, null, null);

        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    DiamondTransfer diamondTransfer = new DiamondTransfer();

                    diamondTransfer.setdH_Count(cursor.getInt(4));
                    diamondTransfer.setdH_Date(cursor.getString(3));
                    diamondTransfer.setdH_From(cursor.getString(2));
                    dataList.add(diamondTransfer);
                } while (cursor.moveToNext());
            }

        }

        db.close();
        return dataList;

    }
}
