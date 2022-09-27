package com.lahoriagency.cikolive;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.pay.PayClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.RedeemRequestAdapter;
import com.lahoriagency.cikolive.Classes.Constants;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.PaymentsUtil;
import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.RedeemRequestDAO;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class GooglePayCheckoutAct extends AppCompatActivity {
    private CheckoutViewModel model;

    private static final int ADD_TO_GOOGLE_WALLET_REQUEST_CODE = 999;

    private View googlePayButton;
    private ImageView addToGoogleWalletButtonContainer;
    private ImageView addToGoogleWalletButton;
    private Bundle gPayBundle;
    long amount;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    SharedPreferences userPreferences;
    private FloatingActionButton fab;

    DBHelper dbHelper;
    private RedeemRequestDAO redeemRequestDAO;
    private RecyclerView recyclerView;
    RedeemRequestAdapter hisAdapter;
    private ArrayList<RedeemRequest> redeemRequests;
    private SavedProfile savedProfile;
    private int savedProfID;

    SharedPreferences sharedPreferences;
    String SharedPrefUserPassword;
    String SharedPrefEmail;
    int SharedPrefProfileID;
    String SharedPrefSurName,SharedPrefUserName;
    String SharedPrefFirstName;
    int profileID,numberOfDiamonds;
    private QBUser qbUser;
    private int diamondCount;
    private PaymentsClient mPaymentsClient;
    private Diamond diamond;
    ActivityResultLauncher<IntentSenderRequest> resolvePaymentForResult = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            PaymentData paymentData = PaymentData.getFromIntent(result.getData());
                            if (paymentData != null) {
                                handlePaymentSuccess(paymentData);
                            }
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;
                }
            });
    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions =new Wallet.WalletOptions.Builder()
                .setEnvironment(Constants.PAYMENTS_ENVIRONMENT)
                .setTheme(WalletConstants.THEME_DARK)
                .build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_checkout);
        addToGoogleWalletButton = findViewById(R.id.addG);
        addToGoogleWalletButtonContainer = findViewById(R.id.buy_with_g);
        gPayBundle= new Bundle();
        mPaymentsClient = PaymentsUtil.createPaymentsClient(this);
        gson = new Gson();
        gson1 = new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        savedProfile= new SavedProfile();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPrefUserName=sharedPreferences.getString("SAVED_PROFILE_EMAIL", "");
        SharedPrefUserPassword=sharedPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);

        initializeUi();
        gPayBundle=getIntent().getExtras();
        if(gPayBundle !=null){
            amount=gPayBundle.getLong("Amount");
            savedProfile=gPayBundle.getParcelable("SavedProfile");
            qbUser=gPayBundle.getParcelable("QBUser");
            profileID=gPayBundle.getInt("SAVED_PROFILE_ID");

        }

        //model = new ViewModelProvider(this).get(CheckoutViewModel.class);
        model.canUseGooglePay.observe(this, this::setGooglePayAvailable);

        // Check out Google Wallet availability
        model.canAddPasses.observe(this, this::setAddToGoogleWalletAvailable);
    }
    private void initializeUi() {

        googlePayButton.setOnClickListener(this::requestPayment);

        addToGoogleWalletButton.setOnClickListener(v -> {
            addToGoogleWalletButton.setClickable(false);
            model.savePasses(model.genericObjectJwt, this, ADD_TO_GOOGLE_WALLET_REQUEST_CODE);
        });
    }
    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.google_pay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }




    public void requestPayment(View view) {
        gPayBundle= new Bundle();

        googlePayButton.setClickable(false);

        gPayBundle=getIntent().getExtras();
        if(gPayBundle !=null){
            amount=gPayBundle.getLong("Amount");
            savedProfile=gPayBundle.getParcelable("SavedProfile");
            qbUser=gPayBundle.getParcelable("QBUser");
            profileID=gPayBundle.getInt("SAVED_PROFILE_ID");

        }

        long totalPriceCents = amount;
        final Task<PaymentData> task = model.getLoadPaymentDataTask(totalPriceCents);

        task.addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                handlePaymentSuccess(completedTask.getResult());
            } else {
                Exception exception = completedTask.getException();
                if (exception instanceof ResolvableApiException) {
                    PendingIntent resolution = ((ResolvableApiException) exception).getResolution();
                    resolvePaymentForResult.launch(new IntentSenderRequest.Builder(resolution).build());

                } else if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    handleError(apiException.getStatusCode(), apiException.getMessage());
                    Intent dialogIntent = new Intent(GooglePayCheckoutAct.this, QuickTellerAct.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);

                } else {
                    handleError(CommonStatusCodes.INTERNAL_ERROR, "Unexpected non API" +
                            " exception when trying to deliver the task result to an activity!");
                    Intent dialogIntent = new Intent(GooglePayCheckoutAct.this, QuickTellerAct.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            }

            // Re-enables the Google Pay payment button.
            googlePayButton.setClickable(true);
        });
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        final String paymentInfo = paymentData.toJson();
        gPayBundle= new Bundle();
        diamond= new Diamond();

        gPayBundle=getIntent().getExtras();
        if(gPayBundle !=null){
            amount=gPayBundle.getLong("Amount");
            savedProfile=gPayBundle.getParcelable("SavedProfile");
            qbUser=gPayBundle.getParcelable("QBUser");
            profileID=gPayBundle.getInt("SAVED_PROFILE_ID");

        }
        if(savedProfile !=null){
            diamond=savedProfile.getSavedPDiamond();

        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
        }else {
            try {

                diamondCount=diamond.getDiamondCount();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");

            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();
            numberOfDiamonds = Math.toIntExact(amount / 5);

            diamondCount=diamondCount+numberOfDiamonds;

            diamond.setDiamondCount(diamondCount);
            savedProfile.setSavedPDiamond(diamond);

            // Logging token string.
            Log.d("Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token"));

        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e);
        }
    }

    private void handleError(int statusCode, @Nullable String message) {
        Log.e("loadPaymentData failed",
                String.format(Locale.getDefault(), "Error code: %d, Message: %s", statusCode, message));
    }


    private void setAddToGoogleWalletAvailable(boolean available) {
        if (available) {
            addToGoogleWalletButtonContainer.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(
                    this,
                    R.string.google_wallet_status_unavailable,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TO_GOOGLE_WALLET_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK: {
                    Toast
                            .makeText(this, getString(R.string.add_google_wallet_success), Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                case RESULT_CANCELED: {
                    // Save canceled
                    break;
                }

                case PayClient.SavePassesResult.SAVE_ERROR: {
                    if (data != null) {
                        String apiErrorMessage = data.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE);
                        handleError(resultCode, apiErrorMessage);
                    }
                    break;
                }

                default: handleError(
                        CommonStatusCodes.INTERNAL_ERROR, "Unexpected non API" +
                                " exception when trying to deliver the task result to an activity!"
                );
            }

            addToGoogleWalletButton.setClickable(true);
        }
    }
}