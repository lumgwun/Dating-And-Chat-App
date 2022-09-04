package com.lahoriagency.cikolive.Classes;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class TimeLine {
    private int timelineID;
    private int timelineSavedProfID;
    private int timelineQBUserID;
    private String timelineTime;
    private String timelineDetails;
    private String timelineType;
    private String timelineStatus;

    public static final String TIMELINE_TABLE = "timeline_table";
    public static final String TIMELINE_TYPE = "timeline_type";
    public static final String TIMELINE_DETAILS = "timeline_details";
    public static final String TIMELINE_ID = "timeline_ID";
    public static final String TIMELINE_TIME = "timeline_time";
    public static final String TIMELINE_STATUS = "timeline_status";
    public static final String TIMELINE_SAVED_PROF_ID = "timeline_prof_id";
    public static final String TIMELINE_QU_ID = "timeline_qu_id";

    public static final String CREATE_ACCOUNT_TIMELINE_TABLE = "CREATE TABLE " + TIMELINE_TABLE + " (" + TIMELINE_ID + " INTEGER, " +
            TIMELINE_SAVED_PROF_ID + " INTEGER, " + TIMELINE_TYPE + " TEXT , " + TIMELINE_QU_ID + " INTEGER , " + TIMELINE_DETAILS + " TEXT , "+ TIMELINE_TIME + " TEXT , "+ TIMELINE_STATUS + " TEXT , "+
            "PRIMARY KEY(" + TIMELINE_ID + "), " + "FOREIGN KEY(" + TIMELINE_SAVED_PROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";


    public TimeLine(){
        super();

    }

    public int getTimelineID() {
        return timelineID;
    }

    public void setTimelineID(int timelineID) {
        this.timelineID = timelineID;
    }

    public int getTimelineSavedProfID() {
        return timelineSavedProfID;
    }

    public void setTimelineSavedProfID(int timelineSavedProfID) {
        this.timelineSavedProfID = timelineSavedProfID;
    }

    public int getTimelineQBUserID() {
        return timelineQBUserID;
    }

    public void setTimelineQBUserID(int timelineQBUserID) {
        this.timelineQBUserID = timelineQBUserID;
    }

    public String getTimelineTime() {
        return timelineTime;
    }

    public void setTimelineTime(String timelineTime) {
        this.timelineTime = timelineTime;
    }

    public String getTimelineDetails() {
        return timelineDetails;
    }

    public void setTimelineDetails(String timelineDetails) {
        this.timelineDetails = timelineDetails;
    }

    public String getTimelineType() {
        return timelineType;
    }

    public void setTimelineType(String timelineType) {
        this.timelineType = timelineType;
    }

    public String getTimelineStatus() {
        return timelineStatus;
    }

    public void setTimelineStatus(String timelineStatus) {
        this.timelineStatus = timelineStatus;
    }
}
