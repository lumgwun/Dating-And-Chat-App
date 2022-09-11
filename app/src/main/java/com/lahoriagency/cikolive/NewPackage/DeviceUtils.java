package com.lahoriagency.cikolive.NewPackage;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceUtils {
    @SuppressLint("HardwareIds")
    public static String getDeviceUid() {
        Context context = CoreApp.getInstance();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds") String uniqueDeviceId = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(uniqueDeviceId)) {
            // for tablets
            ContentResolver cr = context.getContentResolver();
            uniqueDeviceId = Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
        }

        return uniqueDeviceId;
    }
}
