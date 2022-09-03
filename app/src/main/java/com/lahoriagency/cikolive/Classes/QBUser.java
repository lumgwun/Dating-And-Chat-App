package com.lahoriagency.cikolive.Classes;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_TABLE;

public class QBUser extends com.quickblox.users.model.QBUser {
    private int qbUserId;
    private int qbUser_SPId;
    private int qbUser_USI_Id;
    private String qbUserDetails;
    public static final String QBUSER_ID = "qb_user_id";
    public static final String QBUSER_SPID = "qb_sp_id";
    public static final String QBUSER_UPI_ID = "qb_usi_id";
    public static final String QBUSER_DETAILS = "qb_user_Details";
    public static final String QBUSER_TABLE = "qb_user_table";

    public static final String CREATE_QB_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + QBUSER_TABLE + " ( " + QBUSER_ID + " INTEGER  , " + QBUSER_DETAILS + " TEXT , " + QBUSER_SPID + " INTEGER  , " +
            QBUSER_UPI_ID + " TEXT, " + "FOREIGN KEY(" + QBUSER_SPID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "),"+ "FOREIGN KEY(" + QBUSER_UPI_ID + ") REFERENCES " + USER_PROF_INFO_TABLE + "(" + USER_PROF_INFO_ID + "),"+"PRIMARY KEY(" + QBUSER_ID  + "))";

    public int getQbUserId() {
        return qbUserId;
    }

    public void setQbUserId(int qbUserId) {
        this.qbUserId = qbUserId;
    }

    public String getQbUserDetails() {
        return qbUserDetails;
    }

    public void setQbUserDetails(String qbUserDetails) {
        this.qbUserDetails = qbUserDetails;
    }

    public int getQbUser_SPId() {
        return qbUser_SPId;
    }

    public void setQbUser_SPId(int qbUser_SPId) {
        this.qbUser_SPId = qbUser_SPId;
    }

    public int getQbUser_USI_Id() {
        return qbUser_USI_Id;
    }

    public void setQbUser_USI_Id(int qbUser_USI_Id) {
        this.qbUser_USI_Id = qbUser_USI_Id;
    }
}
