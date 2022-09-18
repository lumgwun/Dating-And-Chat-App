package com.lahoriagency.cikolive.Classes;

import android.app.Activity;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

public class ActivityLifecycleE implements MultiDexApplication.ActivityLifecycleCallbacks {

    private static ActivityLifecycleE instance;

    private boolean foreground = false;

    public static void init(MultiDexApplication app) {
        if (instance == null) {
            instance = new ActivityLifecycleE();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    private ActivityLifecycleE() {
    }

    public static synchronized ActivityLifecycleE getInstance() {
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

    }

    @Override
    public void onActivityStarted(Activity activity) {

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

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
