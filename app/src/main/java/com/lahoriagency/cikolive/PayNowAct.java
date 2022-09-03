package com.lahoriagency.cikolive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.quickblox.users.model.QBUser;

public class PayNowAct extends AppCompatActivity {
    WebView mywebView;
    private String weblink;
    private Bundle bundle;
    int savedProfileID,qbUserID;
    private double amount;
    private String linkToPay;
    private  int numberOfDiamond;
    private SavedProfile savedProfile;
    private QBUser qbUser;
    private QBUserCustomData qbUserCustomData;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pay_now);
        savedProfile= new SavedProfile();
        qbUser= new QBUser();
        bundle= getIntent().getExtras();
        if(bundle !=null){
            savedProfileID=bundle.getInt("SavedProfileID");
            numberOfDiamond=bundle.getInt("NoOfDiamond");
            savedProfile=bundle.getParcelable("SavedProfile");
            qbUserID=bundle.getInt("QBUserID");
            linkToPay=bundle.getString("Link");

        }
        mywebView = (WebView) findViewById(R.id.webView_payNow);
        mywebView.setWebViewClient(new WebViewClient());
        WebSettings webViewSettings = mywebView.getSettings();
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        mywebView.loadUrl(linkToPay);
        mywebView.setWebViewClient(new WebViewClient());
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