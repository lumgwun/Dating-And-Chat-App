package com.lahoriagency.cikolive.DataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.lahoriagency.cikolive.DataBase.DBHelper;

public class DBHelperDAO {
    protected SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context mContext;
    private SQLiteDatabase writableDatabase;

    public DBHelperDAO(Context context) {
        this.mContext = context;
        dbHelper = DBHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DBHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database = null;
    }

    public SQLiteDatabase getWritableDatabase() {
        return writableDatabase;
    }

    public void setWritableDatabase(SQLiteDatabase writableDatabase) {
        this.writableDatabase = writableDatabase;
    }
}
