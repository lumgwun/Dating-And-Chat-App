package com.lahoriagency.cikolive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lahoriagency.cikolive.Classes.QBUserCustomData;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.SuperAdmin.SuperAdminOffice;
import com.quickblox.users.model.QBUser;


/*import com.interswitchng.sdk.auth.Passport;
import com.interswitchng.sdk.model.RequestOptions;
import com.interswitchng.sdk.payment.IswCallback;
import com.interswitchng.sdk.payment.Payment;
import com.interswitchng.sdk.payment.android.WalletSDK;
import com.interswitchng.sdk.payment.android.util.Util;
import com.interswitchng.sdk.payment.android.util.Validation;
import com.interswitchng.sdk.payment.model.AuthorizeOtpRequest;
import com.interswitchng.sdk.payment.model.AuthorizeOtpResponse;
import com.interswitchng.sdk.payment.model.PaymentMethod;
import com.interswitchng.sdk.payment.model.PurchaseRequest;
import com.interswitchng.sdk.payment.model.PurchaseResponse;
import com.interswitchng.sdk.payment.model.WalletRequest;
import com.interswitchng.sdk.payment.model.WalletResponse;
import com.interswitchng.sdk.util.RandomString;
import com.interswitchng.sdk.util.StringUtils;*/

public class PurchaseDiamondActivity extends AppCompatActivity  {
//implements Util.PromptResponseHandler
    private DBHelper dbHelper;
    private FloatingActionButton fab;
    private Bundle paymentBundle;
    private double amount;
    private String currency;
    private  int numberOfDiamond;
    private SavedProfile savedProfile;
    private QBUser qbUser;
    private QBUserCustomData qbUserCustomData;
    private int userID;

    private Context context;

    private Button payCusAmount,pay5Diamond,pay10Diamond,pay100Diamond;
    private String otpTransactionIdentifier;
    private String transactionIdentifier,linkToPay;
    int savedProfileID,noOfDiamond;
    private Bundle payBundle;

    SharedPreferences sharedPref;

    private static final String PREF_NAME = "Ciko";
    private static final String FIVE_DIAMOND = "https://business.quickteller.com/link/pay/Lumgwunqrcmh";
    private static final String ANY_AMOUNT = "https://business.quickteller.com/link/pay/CIKO_LIVE";
    private static final String TEN_DIAMONDS = "https://business.quickteller.com/link/pay/LumgwunAy5sP";
    private static final String HUNDRED_DIAMONDS = "https://business.quickteller.com/link/pay/LumgwunsMOBr";

    String json, json1, json2;
    private String CIKO_PAYMENT_LINK="https://business.quickteller.com/link/pay/CIKO_LIVE";
    //private RequestOptions options = RequestOptions.builder().setClientId("IKIA3E267D5C80A52167A581BBA04980CA64E7B2E70E").setClientSecret("SagfgnYsmvAdmFuR24sKzMg7HWPmeh67phDNIiZxpIY=").build();


    ActivityResultLauncher<Intent> mGetPurchaseDetails = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            Intent data = result.getData();
                            if (data != null) {
                                //pictureUri = data.getData();
                            }
                            Toast.makeText(PurchaseDiamondActivity.this, "Diamond purchase successful ", Toast.LENGTH_SHORT).show();
                            //doProcessing();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(PurchaseDiamondActivity.this, "Diamond purchase failed", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_purchase_diamond);
        dbHelper = new DBHelper(this);
        savedProfile= new SavedProfile();
        qbUser= new QBUser();
        payBundle= new Bundle();
        qbUserCustomData= new QBUserCustomData();
        setTitle("Pay for Diamond");
        paymentBundle=getIntent().getExtras();
        if(paymentBundle !=null){
            amount=paymentBundle.getDouble("Amount");
            currency=paymentBundle.getString("Currency");
            numberOfDiamond=paymentBundle.getInt("Number");
            savedProfile=paymentBundle.getParcelable("SavedProfile");
            qbUser=paymentBundle.getParcelable("QBUser");
            qbUserCustomData=paymentBundle.getParcelable("");
            userID=paymentBundle.getInt("QBUserID");
            savedProfileID=paymentBundle.getInt("SavedProfileID");


        }
        context = this;

        payCusAmount = (Button) findViewById(R.id.payButton);
        pay5Diamond = (Button) findViewById(R.id.pay5Button);
        pay10Diamond = (Button) findViewById(R.id.pay10Button);
        pay100Diamond = (Button) findViewById(R.id.payButton100);


        payCusAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBundle.putParcelable("QBUser", (Parcelable) qbUser);
                payBundle.putInt("QBUserID",userID);
                payBundle.putParcelable("SavedProfile",savedProfile);
                payBundle.putInt("SavedProfileID",savedProfileID);
                payBundle.putInt("NoOfDiamond",noOfDiamond);
                payBundle.putString("Link",ANY_AMOUNT);

                Intent myIntent = new Intent(PurchaseDiamondActivity.this, PayNowAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(payBundle);
                startActivity(myIntent);
            }
        });
        pay5Diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBundle.putParcelable("QBUser", (Parcelable) qbUser);
                payBundle.putInt("QBUserID",userID);
                payBundle.putInt("SavedProfileID",savedProfileID);
                payBundle.putInt("NoOfDiamond",noOfDiamond);
                payBundle.putString("Link",FIVE_DIAMOND);

                Intent myIntent = new Intent(PurchaseDiamondActivity.this, PayNowAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(payBundle);
                startActivity(myIntent);
            }
        });
        pay10Diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBundle.putParcelable("QBUser", (Parcelable) qbUser);
                payBundle.putInt("QBUserID",userID);
                payBundle.putInt("SavedProfileID",savedProfileID);
                payBundle.putInt("NoOfDiamond",noOfDiamond);
                payBundle.putString("Link",TEN_DIAMONDS);

                Intent myIntent = new Intent(PurchaseDiamondActivity.this, PayNowAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(payBundle);
                startActivity(myIntent);
            }
        });
        pay100Diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBundle.putParcelable("QBUser", (Parcelable) qbUser);
                payBundle.putInt("QBUserID",userID);
                payBundle.putInt("SavedProfileID",savedProfileID);
                payBundle.putInt("NoOfDiamond",noOfDiamond);
                payBundle.putString("Link",HUNDRED_DIAMONDS);

                Intent myIntent = new Intent(PurchaseDiamondActivity.this, PayNowAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(payBundle);
                startActivity(myIntent);
            }
        });
        payCusAmount.setOnClickListener(this::customAmt);
        pay5Diamond.setOnClickListener(this::fiveDiamond);
        pay10Diamond.setOnClickListener(this::tenDiamond);
        pay100Diamond.setOnClickListener(this::hundredDiamond);
        fab = findViewById(R.id.gFAB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void executePay() {
        /*Payment.overrideApiBase("https://qa.interswitchng.com"); // used to override the payment api base url.
        Passport.overrideApiBase("https://qa.interswitchng.com/passport"); //used to override the payment api base url.
        List<EditText> fields = new ArrayList<>();
        fields.clear();

        fields.add(customerID);
        fields.add(amount);
        fields.add(pan);
        fields.add(pin);
        fields.add(expiry);

        if (Validation.isValidEditboxes(fields)) {
            if (Util.isNetworkAvailable(this)) {
                final PurchaseRequest request = new PurchaseRequest();
                request.setCustomerId(customerID.getText().toString());
                request.setAmount(amount.getText().toString());
                request.setPan(pan.getText().toString());
                request.setPinData(pin.getText().toString());
                request.setExpiryDate(expiry.getText().toString());
                request.setRequestorId("11179920172");
                request.setCurrency("NGN");
                request.setTransactionRef(RandomString.numeric(12));
                Util.hide_keyboard(this);
                Util.showProgressDialog(context, "Sending Payment");
                new PaymentSDK(context, options).purchase(request, new IswCallback<PurchaseResponse>() {

                    @Override
                    public void onError(Exception error) {
                        Util.hideProgressDialog();
                        Util.notify(context, "Error", error.getLocalizedMessage(), "Close", false);
                    }

                    @Override
                    public void onSuccess(PurchaseResponse response) {
                        Util.hideProgressDialog();
                        transactionIdentifier = response.getTransactionIdentifier();
                        if (StringUtils.hasText(response.getOtpTransactionIdentifier())) {
                            otpTransactionIdentifier = response.getOtpTransactionIdentifier();
                            Util.prompt(SplashActivity.this, "OTP", response.getMessage(), "Close", "Continue", true, 1L);
                        } else {
                            Util.notify(context, "Success", "Ref: " + transactionIdentifier, "Close", false);
                        }
                    }
                });
            } else {
                Util.notifyNoNetwork(this);
            }
        }
    }

    @Override
    public void promptResponse(String response, long requestId) {
        if (requestId == 1 && StringUtils.hasText(response)) {
            AuthorizeOtpRequest request = new AuthorizeOtpRequest();
            request.setOtpTransactionIdentifier(otpTransactionIdentifier);
            request.setOtp(response);
            Util.hide_keyboard(this);
            Util.showProgressDialog(context, "Verifying OTP");
            new PaymentSDK(context, options).authorizeOtp(request, new IswCallback<AuthorizeOtpResponse>() {
                @Override
                public void onError(Exception error) {
                    Util.hideProgressDialog();
                    Util.notify(context, "Error", error.getLocalizedMessage(), "Close", false);
                }

                @Override
                public void onSuccess(AuthorizeOtpResponse otpResponse) {
                    Util.hideProgressDialog();
                    Util.notify(context, "Success", "Ref: " + transactionIdentifier, "Close", false);
                }
            });
        }*/

    }

    public void customAmt(View view) {
    }

    public void fiveDiamond(View view) {
    }

    public void tenDiamond(View view) {
    }

    public void hundredDiamond(View view) {
    }
}