package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.Classes.TimeLine;

import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_AMOUNT;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_COUNT;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_DATE;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_ID;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_PROF_ID;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_TABLE;
import static com.lahoriagency.cikolive.Classes.RedeemRequest.REDEEM_REQUEST_TYPE;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_DETAILS;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_QU_ID;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_SAVED_PROF_ID;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_STATUS;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TABLE;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TIME;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TYPE;
import static java.lang.String.valueOf;

public class RedeemRequestDAO extends DBHelperDAO{
    public RedeemRequestDAO(Context context) {
        super(context);
    }
    public long insertNewRedeemReq(int rr_SProf_ID,int rr_Count,String rr_Amount,String rr_Date,String rr_Type,String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long drID = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(REDEEM_REQUEST_ID, drID);
        contentValues.put(REDEEM_REQUEST_TYPE, rr_Type);
        contentValues.put(REDEEM_REQUEST_DATE, rr_Date);
        contentValues.put(REDEEM_REQUEST_COUNT, rr_Count);
        contentValues.put(REDEEM_REQUEST_AMOUNT, rr_Amount);
        contentValues.put(REDEEM_REQUEST_PROF_ID, rr_SProf_ID);
        sqLiteDatabase.insert(REDEEM_REQUEST_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return drID;
    }
    @SuppressLint("Range")
    public ArrayList<RedeemRequest> getRedeemReqByProfID(int savedProfID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<RedeemRequest> dataList = new ArrayList<RedeemRequest>();
        String selection = REDEEM_REQUEST_PROF_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(savedProfID)};
        String[] columns = new String[]{REDEEM_REQUEST_ID,REDEEM_REQUEST_TYPE,REDEEM_REQUEST_DATE,REDEEM_REQUEST_COUNT,REDEEM_REQUEST_AMOUNT};

        Cursor cursor = db.query(REDEEM_REQUEST_TABLE, columns, selection,
                selectionArgs, null, null, null, null);

        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    RedeemRequest redeemRequest = new RedeemRequest();

                    redeemRequest.setRr_Id(cursor.getInt(0));
                    redeemRequest.setRr_SProf_ID(cursor.getInt(1));
                    redeemRequest.setRr_Type(cursor.getInt(3));
                    redeemRequest.setRr_Amount(cursor.getString(5));
                    redeemRequest.setRr_Count(cursor.getInt(4));
                    redeemRequest.setRr_Date(cursor.getString(2));
                    dataList.add(redeemRequest);
                } while (cursor.moveToNext());
            }

        }

        db.close();
        return dataList;

    }
}
