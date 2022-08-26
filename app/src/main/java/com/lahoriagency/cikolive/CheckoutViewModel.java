package com.lahoriagency.cikolive;


import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.pay.Pay;
import com.google.android.gms.pay.PayApiAvailabilityStatus;
import com.google.android.gms.pay.PayClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.lahoriagency.cikolive.Classes.PaymentsUtil;

import org.json.JSONObject;

public class CheckoutViewModel extends AndroidViewModel {

    private final PaymentsClient paymentsClient;

    private final PayClient walletClient;

    private final MutableLiveData<Boolean> _canUseGooglePay = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _canAddPasses = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        paymentsClient = PaymentsUtil.createPaymentsClient(application);
        walletClient = Pay.getClient(application);

        fetchCanUseGooglePay();
        fetchCanAddPassesToGoogleWallet();
    }

    public final LiveData<Boolean> canUseGooglePay = _canUseGooglePay;
    public final LiveData<Boolean> canAddPasses = _canAddPasses;


    private void fetchCanUseGooglePay() {
        final JSONObject isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (isReadyToPayJson == null) {
            _canUseGooglePay.setValue(false);
            return;
        }


        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                completedTask -> {
                    if (completedTask.isSuccessful()) {
                        _canUseGooglePay.setValue(completedTask.getResult());
                    } else {
                        Log.w("isReadyToPay failed", completedTask.getException());
                        _canUseGooglePay.setValue(false);
                    }
                });
    }

    public Task<PaymentData> getLoadPaymentDataTask(final long priceCents) {
        JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
        if (paymentDataRequestJson == null) {
            return null;
        }

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        return paymentsClient.loadPaymentData(request);
    }

    /**
     * Determine whether the API to save passes to Google Pay is available on the device.
     */
    private void fetchCanAddPassesToGoogleWallet() {
        walletClient
                .getPayApiAvailabilityStatus(PayClient.RequestType.SAVE_PASSES)
                .addOnSuccessListener(
                        status -> _canAddPasses.setValue(status == PayApiAvailabilityStatus.AVAILABLE))

                .addOnFailureListener(exception -> _canAddPasses.setValue(false));
    }


    public void savePassesJwt(String jwtString, Activity activity, int requestCode) {
        walletClient.savePassesJwt(jwtString, activity, requestCode);
    }


    public void savePasses(String objectString, Activity activity, int requestCode) {
        walletClient.savePassesJwt(objectString, activity, requestCode);
    }

    public final String genericObjectJwt = "eyJ0eXAiOiAiSldUIiwgImFsZyI6ICJSUzI1NiIsICJraWQiOiAiMTY4M2VjZDA1MmU5NTgyZWZhNGU5YTQxNjVmYzE5N2JjNmJlYTJhMCJ9.eyJpc3MiOiAid2FsbGV0LWxhYi10b29sc0BhcHBzcG90LmdzZXJ2aWNlYWNjb3VudC5jb20iLCAiYXVkIjogImdvb2dsZSIsICJ0eXAiOiAic2F2ZXRvd2FsbGV0IiwgImlhdCI6IDE2NTA1MzI2MjMsICJwYXlsb2FkIjogeyJnZW5lcmljT2JqZWN0cyI6IFt7ImlkIjogIjMzODgwMDAwMDAwMjIwOTUxNzcuZjUyZDRhZjYtMjQxMS00ZDU5LWFlNDktNzg2ZDY3N2FkOTJiIn1dfX0.fYKw6fpLfwwNMi5OGr4ybO3ybuCU7RYjQhw-QM_Z71mfOyv2wFUzf6dKgpspJKQmkiaBWBr1L9n8jq8ZMfj6iOA_9_njfUe9GepCwVLC0nZBDd2EqS3UrBYT7tEmk7W2-Cpy5FJFTt_eiqXBZgwa6vMw6e6mMp-GzSD5_ls39fjOPziboLyG-GDmph3f6UhBkjnUjYyY_FoYdlqkTkCWM7AFPcy-FbRyVDpIaHfVk4eYQi4Vzk0fwxaWWTfP3gSXXT6UJ9aFvaPYs0gnlV2WPVgGGKCMtYHFRGYX1t0WRpN2kbxfO5VuMKWJlz3TCnxp-9Axz-enuCgnq2cLvCk6Tw";

}
