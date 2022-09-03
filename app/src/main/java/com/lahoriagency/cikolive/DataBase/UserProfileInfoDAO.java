package com.lahoriagency.cikolive.DataBase;

import android.content.Context;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;

public class UserProfileInfoDAO extends DBHelperDAO{
    private static final String WHERE_ID_EQUALS = SAVED_PROFILE_ID
            + " =?";
    public UserProfileInfoDAO(Context context) {
        super(context);
    }
}
