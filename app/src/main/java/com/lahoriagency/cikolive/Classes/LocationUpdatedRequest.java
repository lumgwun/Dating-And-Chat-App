package com.lahoriagency.cikolive.Classes;

public class LocationUpdatedRequest  extends BaseUserRequest {
    private double longitude;
    private double latitude;

    public LocationUpdatedRequest(Long userId, double longitude, double latitude) {
        super(userId);
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
