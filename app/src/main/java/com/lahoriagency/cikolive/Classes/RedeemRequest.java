package com.lahoriagency.cikolive.Classes;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class RedeemRequest {

    private int rr_Id;
    private String rr_Date;
    private int rr_Type;
    private int rr_Count;
    private String rr_Amount;
    private int rr_SProf_ID;

    public static final String REDEEM_REQUEST_ID = "rr_id";
    public static final String REDEEM_REQUEST_DATE = "rr_date";
    public static final String REDEEM_REQUEST_TYPE = "rr_type";
    public static final String REDEEM_REQUEST_COUNT = "rr_count";
    public static final String REDEEM_REQUEST_AMOUNT = "rr_Amount";
    public static final String REDEEM_REQUEST_PROF_ID = "rr_sProfId";
    public static final String REDEEM_REQUEST_TABLE = "rr_Table";

    public static final String CREATE_REDEEM_REQUEST_TABLE = "CREATE TABLE " + REDEEM_REQUEST_TABLE + " (" + REDEEM_REQUEST_ID + " INTEGER, "+ REDEEM_REQUEST_PROF_ID + " INTEGER , "  +
            REDEEM_REQUEST_DATE + " TEXT, " + REDEEM_REQUEST_TYPE + " TEXT , " + REDEEM_REQUEST_COUNT + " INTEGER , " + REDEEM_REQUEST_AMOUNT + " REAL , " +
            "PRIMARY KEY(" + REDEEM_REQUEST_ID + "), " + "FOREIGN KEY(" + REDEEM_REQUEST_PROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";

    public RedeemRequest() {
        super();

    }

    public RedeemRequest(int rr_Id, String rr_Date, int rr_Type, int rr_Count, String rr_Amount) {
        this.rr_Id = rr_Id;
        this.rr_Date = rr_Date;
        this.rr_Type = rr_Type;
        this.rr_Count = rr_Count;
        this.rr_Amount = rr_Amount;
    }

    public int getRr_Id() {
        return rr_Id;
    }

    public void setRr_Id(int rr_Id) {
        this.rr_Id = rr_Id;
    }

    public String getRr_Date() {
        return rr_Date;
    }

    public void setRr_Date(String rr_Date) {
        this.rr_Date = rr_Date;
    }

    public int getRr_Type() {
        return rr_Type;
    }

    public void setRr_Type(int rr_Type) {
        this.rr_Type = rr_Type;
    }

    public int getRr_Count() {
        return rr_Count;
    }

    public void setRr_Count(int rr_Count) {
        this.rr_Count = rr_Count;
    }

    public String getRr_Amount() {
        return rr_Amount;
    }

    public void setRr_Amount(String rr_Amount) {
        this.rr_Amount = rr_Amount;
    }

    public int getRr_SProf_ID() {
        return rr_SProf_ID;
    }

    public void setRr_SProf_ID(int rr_SProf_ID) {
        this.rr_SProf_ID = rr_SProf_ID;
    }
}
