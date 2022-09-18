package com.lahoriagency.cikolive.Classes;

import android.app.Activity;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

public class ActivityLifecyclePush implements MultiDexApplication.ActivityLifecycleCallbacks {
    private static ActivityLifecyclePush instance;

    private boolean foreground = false;

    public static void init() {
        if (instance == null) {
            instance = new ActivityLifecyclePush();
            AppPush.getInstance().registerActivityLifecycleCallbacks(instance);
        }
    }

    public static synchronized ActivityLifecyclePush getInstance() {
        return instance;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // empty
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // empty
    }

    @Override
    public void onActivityResumed(Activity activity) {
        foreground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        foreground = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // empty
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // empty
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // empty
    }
}
