package com.lahoriagency.cikolive.Classes;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

@SuppressWarnings("deprecation")
public class BackgroundListener implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onBackground() {
        QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                QBChatService.getInstance().destroy();
            }

            @Override
            public void onError(QBResponseException exception) {

            }
        });
        ChatHelper.getInstance().destroy();
        Log.d("BackgroundListener", "Background Successful");
    }
}
