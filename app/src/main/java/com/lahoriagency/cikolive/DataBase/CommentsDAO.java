package com.lahoriagency.cikolive.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTIONS_TABLE;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_AMOUNT;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_CURRENCY;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_DATE;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_METHOD_OF_PAYMENT;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_NO_OF_DIAMOND;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_QBUSER_ID;
import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_SAVEDPROF_ID;

public class CommentsDAO extends DBHelperDAO{
    public CommentsDAO(Context context) {
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
}
