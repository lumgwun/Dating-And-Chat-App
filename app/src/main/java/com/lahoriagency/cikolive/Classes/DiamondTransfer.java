package com.lahoriagency.cikolive.Classes;

import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_ID;
import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class DiamondTransfer {
    private String dH_From;
    private String dH_Date;
    private int dH_Count;
    private int dH_SProf_ID;
    private int dH_From_SProf_ID;
    private int dH_To_SProf_ID;
    private long dH_Event_ID;
    private String dH_To;
    private int dH_QBUser_ID;
    private int dH_QBUser_From_ID;
    private String dH_Dialog_ID;
    private String eventTittle;
    private int sessionID;

    public static final String DH_FROM = "dh_from";
    public static final String DH_DATE = "dh_date";
    public static final String DH_COUNT = "dh_count";
    public static final String DH_ID = "dh_ID";
    public static final String DH_TABLE = "dh_Table";
    public static final String DH_SAVED_PROF_ID = "dh_SProf_ID";
    public static final String DH_SAVED_QBUSER_ID = "dh_QBUser_ID";
    public static final String DH_TO = "dh_To";
    public static final String DH_DIALOG_ID = "dh_Dialog_ID";

    public static final String DH_FROM_Prof_ID = "dh_from_P_ID";
    public static final String DH_TO_PROF_ID = "dh_from_Prof_ID";

    public static final String CREATE_DIAMOND_HISTORY_TABLE = "CREATE TABLE " + DH_TABLE + " (" + DH_ID + " INTEGER, " +
            DH_SAVED_PROF_ID + " INTEGER, " + DH_FROM + " TEXT , " + DH_DATE + " TEXT , " + DH_COUNT + " INTEGER , "+ DH_TO + " INTEGER , "+ DH_SAVED_QBUSER_ID + " INTEGER , "+ DH_DIALOG_ID + " TEXT , "+ DH_FROM_Prof_ID + " INTEGER , "+ DH_TO_PROF_ID + " INTEGER , "+
            "PRIMARY KEY(" + DH_ID + "), " +"FOREIGN KEY(" + DH_SAVED_QBUSER_ID  + ") REFERENCES " + QBUSER_TABLE + "(" + QBUSER_ID + "),"+ "FOREIGN KEY(" + DH_SAVED_PROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";
    private int dH_Balance;


    public DiamondTransfer() {
        super();

    }

    public DiamondTransfer(String dH_From, String dH_Date, int dH_Count) {
        this.dH_From = dH_From;
        this.dH_Date = dH_Date;
        this.dH_Count = dH_Count;
    }

    public DiamondTransfer(int sessionID, String dialogID, long eventID, int qbUserID, int savedProfID, int savedProfOfHostID, String name, String eventTittle, String diamondTime, int sessionDiamond) {
        this.dH_From_SProf_ID = savedProfID;
        this.dH_To_SProf_ID = savedProfOfHostID;
        this.dH_Dialog_ID = dialogID;
        this.dH_Date = diamondTime;
        this.dH_Event_ID = eventID;
        this.dH_Count = sessionDiamond;
        this.sessionID = sessionID;
        this.dH_From = name;
        this.dH_QBUser_From_ID = qbUserID;
        this.eventTittle = eventTittle;


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

    public String getdH_To() {
        return dH_To;
    }

    public void setdH_To(String dH_To) {
        this.dH_To = dH_To;
    }

    public int getdH_QBUser_ID() {
        return dH_QBUser_ID;
    }

    public void setdH_QBUser_ID(int dH_QBUser_ID) {
        this.dH_QBUser_ID = dH_QBUser_ID;
    }

    public void setdH_Balance(int dH_balance) {
        this.dH_Balance = dH_balance;
    }

    public int getdH_Balance() {
        return dH_Balance;
    }

    public String getdH_Dialog_ID() {
        return dH_Dialog_ID;
    }

    public void setdH_Dialog_ID(String dH_Dialog_ID) {
        this.dH_Dialog_ID = dH_Dialog_ID;
    }

    public int getdH_From_SProf_ID() {
        return dH_From_SProf_ID;
    }

    public void setdH_From_SProf_ID(int dH_From_SProf_ID) {
        this.dH_From_SProf_ID = dH_From_SProf_ID;
    }

    public int getdH_To_SProf_ID() {
        return dH_To_SProf_ID;
    }

    public void setdH_To_SProf_ID(int dH_To_SProf_ID) {
        this.dH_To_SProf_ID = dH_To_SProf_ID;
    }

    public long getdH_Event_ID() {
        return dH_Event_ID;
    }

    public void setdH_Event_ID(long dH_Event_ID) {
        this.dH_Event_ID = dH_Event_ID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getdH_QBUser_From_ID() {
        return dH_QBUser_From_ID;
    }

    public void setdH_QBUser_From_ID(int dH_QBUser_From_ID) {
        this.dH_QBUser_From_ID = dH_QBUser_From_ID;
    }

    public String getEventTittle() {
        return eventTittle;
    }

    public void setEventTittle(String eventTittle) {
        this.eventTittle = eventTittle;
    }
}
