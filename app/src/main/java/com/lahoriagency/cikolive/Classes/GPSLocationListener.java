package com.lahoriagency.cikolive.Classes;

import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;

public class GPSLocationListener implements LocationListener {
    PreferencesManager preferencesManager;
    MyPreferences myPreferences;
    public GPSLocationListener() {
        preferencesManager = App.getPreferencesManager();
        myPreferences = App.getPreferences();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        myPreferences.setLatitude(latitude);
        myPreferences.setLongitude(longitude);
        preferencesManager.savePreferences();
        if(myPreferences.getFbId() != null && myPreferences.getUserId() != null) {
            LocationUpdatedRequest locationUpdatedRequest = new LocationUpdatedRequest(myPreferences.getUserId(),
                    longitude, latitude);
            BaseAsyncTask<LocationUpdatedRequest> updateLocation = new BaseAsyncTask<>(ServerMethodsConsts.LOCATION, locationUpdatedRequest);
            updateLocation.setHttpMethod("POST");
            updateLocation.execute();
        }
    }
}
