package com.lahoriagency.cikolive.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lahoriagency.cikolive.Classes.Transaction;

import java.util.ArrayList;
import java.util.List;

import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTIONS_TABLE;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_AMOUNT;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_APPROVAL_DATE;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_CURRENCY;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_DATE;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_ID;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_METHOD_OF_PAYMENT;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_NO_OF_DIAMOND;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_QBUSER_ID;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_SAVEDPROF_ID;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_STATUS;
import static java.lang.String.valueOf;

public class TransactionDAO extends DBHelperDAO{
    private static final String WHERE_ID_EQUALS = TRANSACTION_ID
            + " =?";
    public TransactionDAO(Context context) {
        super(context);
    }
    public long insertNewTranx(int tranSenderSavedProfID,int tranQbUserID,String tranMethodOfPayment, int tranNoOfDiamond,  double tranAmount,String tranCurrency,String tranDate) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int tranXID = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_SAVEDPROF_ID, tranSenderSavedProfID);
        contentValues.put(TRANSACTION_QBUSER_ID, tranQbUserID);
        contentValues.put(TRANSACTION_METHOD_OF_PAYMENT, tranMethodOfPayment);
        contentValues.put(TRANSACTION_NO_OF_DIAMOND, tranNoOfDiamond);
        contentValues.put(TRANSACTION_AMOUNT, tranAmount);
        contentValues.put(TRANSACTION_CURRENCY, tranCurrency);
        contentValues.put(TRANSACTION_DATE, tranDate);
        sqLiteDatabase.insert(TRANSACTIONS_TABLE,null,contentValues);
        sqLiteDatabase.close();
        return tranXID;
    }
    public void updateTranXDiamond(int tranXID,int noOfDiamond,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String selection = TRANSACTION_ID + "=?";
        String[] selectionArgs = new String[]{valueOf(tranXID)};
        contentValues.put(TRANSACTION_NO_OF_DIAMOND, noOfDiamond);
        contentValues.put(TRANSACTION_STATUS, status);
        db.update(TRANSACTIONS_TABLE, contentValues, selection, selectionArgs);
        //db.close();


    }
    public ArrayList<Transaction> getAllTranX() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TRANSACTIONS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction();

                    transaction.setTranID(Integer.parseInt(cursor.getString(0)));
                    transaction.setTranSenderSavedProfID(cursor.getInt(1));
                    transaction.setTranQbUserID(cursor.getInt(2));
                    transaction.setTranDate(cursor.getString(3));
                    transaction.setTranAmount(cursor.getDouble(8));
                    transaction.setTranMethodOfPayment(cursor.getString(10));
                    transaction.setTranApprovalDate(cursor.getString(12));
                    transaction.setTranStatus(cursor.getString(13));
                    transaction.setTranNoOfDiamond(cursor.getInt(14));
                    transaction.setTranCurrency(cursor.getString(15));

                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        }
        return transactions;
    }
    @SuppressLint("Range")
    public ArrayList<Transaction> getTranxByMonths(String monthAndYear){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Transaction> dataList = new ArrayList<Transaction>();
        String selection = "substr(" + TRANSACTION_DATE + ",4)";
        String[] selectionArgs = new String[]{valueOf(monthAndYear)};
        String[] columns = new String[]{TRANSACTION_DATE,TRANSACTION_AMOUNT,TRANSACTION_CURRENCY,TRANSACTION_METHOD_OF_PAYMENT,TRANSACTION_NO_OF_DIAMOND,TRANSACTION_QBUSER_ID,TRANSACTION_SAVEDPROF_ID,TRANSACTION_STATUS,TRANSACTION_APPROVAL_DATE};
        //String groupbyclause = "substr(" + TRANSACTION_DATE + ",4)";
        String orderbyclause = "substr(" + TRANSACTION_DATE + ",7,2)||substr(" + TRANSACTION_DATE + ",4,2)";
        Cursor cursor = db.query(TRANSACTIONS_TABLE, null, selection,
                selectionArgs, null, null, null, null);

        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction();

                    transaction.setTranID(Integer.parseInt(cursor.getString(0)));
                    transaction.setTranSenderSavedProfID(cursor.getInt(1));
                    transaction.setTranQbUserID(cursor.getInt(2));
                    transaction.setTranDate(cursor.getString(3));
                    transaction.setTranAmount(cursor.getDouble(8));
                    transaction.setTranMethodOfPayment(cursor.getString(10));
                    transaction.setTranApprovalDate(cursor.getString(12));
                    transaction.setTranStatus(cursor.getString(13));
                    transaction.setTranNoOfDiamond(cursor.getInt(14));
                    transaction.setTranCurrency(cursor.getString(15));

                    dataList.add(transaction);
                } while (cursor.moveToNext());
            }

        }

        db.close();
        return dataList;

    }

    @SuppressLint("Range")
    public ArrayList<Transaction> getTranxByMonthForAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Transaction> dataList = new ArrayList<Transaction>();
        String tmpcol_monthly_total = "Monthly_Total";
        String tmpcol_month_year = "Month_and_Year";
        String[] columns = new String[]{"sum(" + TRANSACTION_AMOUNT + ") AS " + tmpcol_monthly_total, "substr(" + TRANSACTION_DATE + ",4) AS " + tmpcol_month_year};
        String groupbyclause = "substr(" + TRANSACTION_DATE + ",4)";
        String orderbyclause = "substr(" + TRANSACTION_DATE + ",7,2)||substr(" + TRANSACTION_DATE + ",4,2)";
        Cursor cursor = db.query(TRANSACTIONS_TABLE, columns, null,
                null, groupbyclause, null, orderbyclause, null);
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();

                transaction.setTranID(Integer.parseInt(cursor.getString(0)));
                transaction.setTranSenderSavedProfID(cursor.getInt(1));
                transaction.setTranQbUserID(cursor.getInt(2));
                transaction.setTranDate(cursor.getString(3));
                transaction.setTranAmount(cursor.getDouble(8));
                transaction.setTranMethodOfPayment(cursor.getString(10));
                transaction.setTranApprovalDate(cursor.getString(12));
                transaction.setTranStatus(cursor.getString(13));
                transaction.setTranNoOfDiamond(cursor.getInt(14));
                transaction.setTranCurrency(cursor.getString(15));

                dataList.add(transaction);
            } while (cursor.moveToNext());
        }
        return dataList;

    }
    @SuppressLint("Range")
    public ArrayList<Transaction> getTranxForSpecMonth(String monthYear){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Transaction> dataList = new ArrayList<Transaction>();
        String selection = "substr(" + TRANSACTION_DATE + ",4)" + "=?";
        String selArgsSubStr = "substr(" + monthYear + ",4)";
        String[] selectionArgs = new String[]{selArgsSubStr};
        String[] columns = new String[]{TRANSACTION_DATE,TRANSACTION_AMOUNT,TRANSACTION_CURRENCY,TRANSACTION_METHOD_OF_PAYMENT,TRANSACTION_NO_OF_DIAMOND,TRANSACTION_QBUSER_ID,TRANSACTION_SAVEDPROF_ID,TRANSACTION_STATUS,TRANSACTION_APPROVAL_DATE};
        Cursor cursor = db.query(TRANSACTIONS_TABLE, columns, selection,
                selectionArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();

                transaction.setTranID(Integer.parseInt(cursor.getString(0)));
                transaction.setTranSenderSavedProfID(cursor.getInt(1));
                transaction.setTranQbUserID(cursor.getInt(2));
                transaction.setTranDate(cursor.getString(3));
                transaction.setTranAmount(cursor.getDouble(8));
                transaction.setTranMethodOfPayment(cursor.getString(10));
                transaction.setTranApprovalDate(cursor.getString(12));
                transaction.setTranStatus(cursor.getString(13));
                transaction.setTranNoOfDiamond(cursor.getInt(14));
                transaction.setTranCurrency(cursor.getString(15));

                dataList.add(transaction);
            } while (cursor.moveToNext());
        }
        return dataList;

    }
    public int getAllAdminDepositCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT (*) FROM " + TRANSACTIONS_TABLE + " WHERE " + null,
                null
        );
        int count = 0;
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    count=cursor.getColumnIndex(TRANSACTION_ID);
                } while (cursor.moveToNext());
                cursor.close();
            }

        }

        db.close();
        return count;
    }
    public int getAllAdminDepositCountDate(String date) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selection = TRANSACTION_DATE + "=?";
        String[] selectionArgs = new String[]{valueOf(date)};
        Cursor cursor = db.rawQuery(
                "SELECT COUNT (*) FROM " + TRANSACTIONS_TABLE + " WHERE " + selection,
                selectionArgs
        );
        int count = 0;
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    count=cursor.getColumnIndex(TRANSACTION_ID);
                } while (cursor.moveToNext());
                cursor.close();
            }

        }

        db.close();
        return count;

    }

}
