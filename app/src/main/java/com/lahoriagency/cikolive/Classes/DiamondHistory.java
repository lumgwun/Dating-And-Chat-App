package com.lahoriagency.cikolive.Classes;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class DiamondHistory {
    private String dH_From;
    private String dH_Date;
    private int dH_Count;
    private int dH_SProf_ID;

    public static final String DH_FROM = "dh_from";
    public static final String DH_DATE = "dh_date";
    public static final String DH_COUNT = "dh_count";
    public static final String DH_ID = "dh_date";
    public static final String DH_TABLE = "dh_Table";
    public static final String DH_SAVED_PROF_ID = "dh_SProf_ID";

    public static final String CREATE_DIAMOND_HISTORY_TABLE = "CREATE TABLE " + DH_TABLE + " (" + DH_ID + " INTEGER, " +
            DH_SAVED_PROF_ID + " INTEGER, " + DH_FROM + " TEXT , " + DH_DATE + " TEXT , " + DH_COUNT + " INTEGER , "+
            "PRIMARY KEY(" + DH_ID + "), " + "FOREIGN KEY(" + DH_SAVED_PROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";


    public DiamondHistory() {
        super();

    }

    public DiamondHistory(String dH_From, String dH_Date, int dH_Count) {
        this.dH_From = dH_From;
        this.dH_Date = dH_Date;
        this.dH_Count = dH_Count;
    }

    public String getdH_From() {
        return dH_From;
    }

    public void setdH_From(String dH_From) {
        this.dH_From = dH_From;
    }

    public String getdH_Date() {
        return dH_Date;
    }

    public void setdH_Date(String dH_Date) {
        this.dH_Date = dH_Date;
    }

    public int getdH_Count() {
        return dH_Count;
    }

    public void setdH_Count(int dH_Count) {
        this.dH_Count = dH_Count;
    }

    public int getdH_SProf_ID() {
        return dH_SProf_ID;
    }

    public void setdH_SProf_ID(int dH_SProf_ID) {
        this.dH_SProf_ID = dH_SProf_ID;
    }
}
