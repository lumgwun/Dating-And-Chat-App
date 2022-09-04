package com.lahoriagency.cikolive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.Transaction;
import com.lahoriagency.cikolive.DataBase.TransactionDAO;
import com.quickblox.users.model.QBUser;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PayNowAct extends AppCompatActivity {
    WebView mywebView;
    private String weblink;
    private Bundle bundle;
    int savedProfileID,qbUserID;
    private double amount;
    private String linkToPay,tranDate,tranCurrency,tranMethodOfPayment;
    private  int numberOfDiamond;
    private SavedProfile savedProfile;
    private QBUser qbUser;
    private QBUserCustomData qbUserCustomData;
    private Transaction transaction;
    private int txID;
    private double tranAmount;
    private TransactionDAO transactionDAO;
    private long tranxID;
    private ArrayList<Transaction> transactionArrayList;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pay_now);
        savedProfile= new SavedProfile();
        transactionArrayList =new ArrayList<>();
        transactionDAO=new TransactionDAO(this);
        qbUser= new QBUser();
        try {
            txID= SecureRandom.getInstanceStrong().nextInt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        transaction= new Transaction();
        bundle= getIntent().getExtras();
        if(bundle !=null){
            savedProfileID=bundle.getInt("SavedProfileID");
            numberOfDiamond=bundle.getInt("NoOfDiamond");
            savedProfile=bundle.getParcelable("SavedProfile");
            qbUserID=bundle.getInt("QBUserID");
            linkToPay=bundle.getString("Link");
            tranCurrency=bundle.getString("tranCurrency");
            tranAmount=bundle.getDouble("tranAmount");
            tranMethodOfPayment=bundle.getString("tranMethodOfPayment");

        }
        if(tranMethodOfPayment.isEmpty()){
            tranMethodOfPayment="QuickTeller";

        }

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.add(Calendar.DAY_OF_YEAR, 31);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = calendar.getTime();
        tranDate = sdf.format(newDate);
        mywebView = (WebView) findViewById(R.id.webView_payNow);
        mywebView.setWebViewClient(new WebViewClient());
        WebSettings webViewSettings = mywebView.getSettings();
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        mywebView.loadUrl(linkToPay);
        mywebView.setWebViewClient(new WebViewClient());
        transaction=new Transaction(savedProfileID,qbUserID,tranMethodOfPayment,numberOfDiamond,tranAmount,tranCurrency,tranDate);

        try {
            for (int i = 0; i < transactionArrayList.size(); i++) {
                numberOfDiamond = transactionArrayList.get(i).getTranNoOfDiamond();
                if (transactionArrayList.get(i).getTranNoOfDiamond()==numberOfDiamond && transactionArrayList.get(i).getTranDate().equalsIgnoreCase(tranDate)||transactionArrayList.get(i).getTranQbUserID()==qbUserID) {
                    Toast.makeText(PayNowAct.this, "There is a similar Transaction" , Toast.LENGTH_LONG).show();
                    return;

                }else {
                    tranxID=transactionDAO.insertNewTranx(savedProfileID,qbUserID,tranMethodOfPayment,numberOfDiamond,tranAmount,tranCurrency,tranDate);
                    if(tranxID>0){
                        Toast.makeText(PayNowAct.this, "Report submission was successful" , Toast.LENGTH_LONG).show();


                    }


                }
            }

        } catch (NullPointerException e) {
            System.out.println("Oops!");
        }


        if(tranxID>0){
            mywebView.goBack();

        }
    }
    @Override
    public void onBackPressed() {
        if(mywebView.canGoBack())
        {
            mywebView.goBack();
        }

        else
        {
            super.onBackPressed();
        }

    }
}