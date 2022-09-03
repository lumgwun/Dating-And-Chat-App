package com.lahoriagency.cikolive.DataBase;

import android.content.Context;

import static com.lahoriagency.cikolive.Classes.Transaction.TRANSACTION_ID;

public class TransactionDAO extends DBHelperDAO{
    private static final String WHERE_ID_EQUALS = TRANSACTION_ID
            + " =?";
    public TransactionDAO(Context context) {
        super(context);
    }
}
