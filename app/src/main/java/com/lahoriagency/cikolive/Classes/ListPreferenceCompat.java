package com.lahoriagency.cikolive.Classes;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.preference.ListPreference;

public class ListPreferenceCompat extends ListPreference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListPreferenceCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListPreferenceCompat(Context context) {
        super(context);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }
}
