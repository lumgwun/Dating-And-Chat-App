package com.lahoriagency.cikolive.Classes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

public class QbEntityCallbackTwoTypeW<T, R> implements QBEntityCallback<T> {
    protected static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    protected QBEntityCallback<Void> callback;


    public QbEntityCallbackTwoTypeW(QBEntityCallback<Void> loginCallback) {
        this.callback = loginCallback;
    }

    @Override
    public void onSuccess(T t, Bundle bundle) {
    }

    @Override
    public void onError(QBResponseException error) {
        onErrorInMainThread(error);
    }

    protected void onSuccessInMainThread(final R result, final Bundle bundle) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess((Void) result, bundle);
            }
        });
    }

    protected void onErrorInMainThread(final QBResponseException error) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(error);
            }
        });
    }
}
