package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lahoriagency.cikolive.Classes.TimeLine;
import com.lahoriagency.cikolive.Classes.Transaction;

import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_DETAILS;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_QU_ID;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_SAVED_PROF_ID;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_STATUS;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TABLE;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TIME;
import static com.lahoriagency.cikolive.Classes.TimeLine.TIMELINE_TYPE;

import static java.lang.String.valueOf;

public class TimeLineDAO extends DBHelperDAO{
    public TimeLineDAO(Context context) {
        super(context);
    }
    public long insertNewTimeLine(int timeLineSavedProfID,int timelineQbUserID,String type,String timelineDetails,String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long timeLineID = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMELINE_SAVED_PROF_ID, timeLineSavedProfID);
        contentValues.put(TIMELINE_QU_ID, timelineQbUserID);
        contentValues.put(TIMELINE_TYPE, type);
        contentValues.put(TIMELINE_DETAILS, timelineDetails);
        contentValues.put(TIMELINE_TIME, time);
        sqLiteDatabase.insert(TIMELINE_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return timeLineID;
    }
    @SuppressLint("Range")
    public ArrayList<TimeLine> getTimeLineByProfID(int savedProfID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TimeLine> dataList = new ArrayList<TimeLine>();
        String selection = TIMELINE_SAVED_PROF_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(savedProfID)};
        String[] columns = new String[]{TIMELINE_TYPE,TIMELINE_DETAILS,TIMELINE_TIME,TIMELINE_STATUS};

        Cursor cursor = db.query(TIMELINE_TABLE, columns, selection,
                selectionArgs, null, null, null, null);

        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TimeLine timeLine = new TimeLine();

                    timeLine.setTimelineID(cursor.getInt(0));
                    timeLine.setTimelineDetails(cursor.getString(4));
                    timeLine.setTimelineType(cursor.getString(2));
                    timeLine.setTimelineTime(cursor.getString(5));
                    timeLine.setTimelineStatus(cursor.getString(6));

                    dataList.add(timeLine);
                } while (cursor.moveToNext());
            }

        }

        db.close();
        return dataList;

    }
}
