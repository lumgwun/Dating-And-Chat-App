package com.lahoriagency.cikolive.NewPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.SavedProfileDAO;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRefReceiver extends BroadcastReceiver {
    private static final String TAG = "AppInstallRefReceiver";

    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();

    private SavedProfileDAO profDAO;
    private DBHelper dbHelper;
    String referrer = "";
    //private PrefManager prefManager;
    private String strName;
    String strCode;
    private int refCount=0;
    private int refNewCount=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper= new DBHelper(context.getApplicationContext());
        profDAO= new SavedProfileDAO(context.getApplicationContext());
        final PendingResult pendingResult = goAsync();

        Bundle extras = intent.getExtras();
        //prefManager= new PrefManager();
        referrer = intent.getStringExtra("referrer");
        backgroundExecutor.execute(() -> {
            try {
                // Do some background work
                StringBuilder sb = new StringBuilder();
                sb.append("Action: ").append(intent.getAction()).append("\n");
                sb.append("URI: ").append(intent.toUri(Intent.URI_INTENT_SCHEME)).append("\n");
                String log = sb.toString();

                if (extras != null) {
                    referrer = extras.getString("referrer");

                    Log.e("Receiver Referral", "===>" + referrer);

                    if (referrer != null) {
                        String[] referrerParts = referrer.split("(?<=\\D)(?=\\d)");
                        strName = referrerParts[0];
                        strCode = referrerParts[1];

                        Log.e("Receiver Referral Code", "===>" + strCode);
                        Log.e("Receiver Referral Name", "===>" + strName);
                    }
                    if(dbHelper !=null){
                        dbHelper.openDataBase();
                        refCount = dbHelper.getReferrerCount(referrer);

                    }
                    refNewCount += refCount + 1;
                    if(dbHelper !=null){
                        dbHelper.openDataBase();
                        profDAO.updateRefCount(referrer, refNewCount);

                    }



                    //prefManager.saveAppReferrer(referrer, strName, strCode);
                }
                Log.d(TAG, log);
            } finally {
                // Must call finish() so the BroadcastReceiver can be recycled
                pendingResult.finish();
            }
            throw new UnsupportedOperationException("Not yet implemented");
        });

    }
}