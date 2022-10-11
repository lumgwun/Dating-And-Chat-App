package com.lahoriagency.cikolive.NewPackage;

import android.os.Parcel;
import android.os.Parcelable;

import com.lahoriagency.cikolive.Classes.DiamondTransfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionFreePaid implements Parcelable, Serializable {
    private int sessionID;
    private ArrayList<String> sessionPhotoLinkArray;
    private ArrayList<String> sessionVideoLinks;
    private List<Integer> sessionUserIDs;
    private ArrayList<Integer> sessionRoomIDs;
    private ArrayList<DiamondTransfer> sessionDiamondTransfers;
    private ArrayList<String> sessionMessages;
    private int sessionDiamondAmt;
    private int sessionProfID;
    private String sessionDate;
    private String sessionStartTime;
    private String sessionEndTime;
    private String sessionCreatedAt;
    private String sessionUpdatedAt;
    private String sessionType;
    private String sessionStatus;
    private String sessionMale;
    private String sessionFemale;
    private String sessionTittle;
    private String qbDialogID;
    private long eventId;
    public SessionFreePaid() {
        super();
    }
    public SessionFreePaid(int sessionID, int diamondAmt, String sessionMaleGender, String sessionFeMaleGender, String sessionTittle, String startTimeOfSession, String sessionEndTime, String status) {
     this.sessionID=sessionID;
        this.sessionDiamondAmt=diamondAmt;
        this.sessionMale=sessionMaleGender;
        this.sessionFemale=sessionFeMaleGender;
        this.sessionTittle=sessionTittle;
        this.sessionStartTime=startTimeOfSession;
        this.sessionEndTime=sessionEndTime;
        this.sessionStatus=status;
    }
    public SessionFreePaid(int sessionID, String qbDialogID, long eventId, List<Integer> occupantIDs, int diamondAmt, String sessionMaleGender, String sessionFeMaleGender, String sessionTittle, String startTimeOfSession, String sessionEndMinutes, String status) {
        this.sessionID=sessionID;
        this.sessionUserIDs=occupantIDs;
        this.eventId=eventId;
        this.qbDialogID=qbDialogID;

        this.sessionDiamondAmt=diamondAmt;
        this.sessionMale=sessionMaleGender;
        this.sessionFemale=sessionFeMaleGender;
        this.sessionTittle=sessionTittle;
        this.sessionStartTime=startTimeOfSession;
        this.sessionEndTime=sessionEndMinutes;
        this.sessionStatus=status;
    }

    protected SessionFreePaid(Parcel in) {
        sessionID = in.readInt();
        sessionPhotoLinkArray = in.createStringArrayList();
        sessionVideoLinks = in.createStringArrayList();
        sessionMessages = in.createStringArrayList();
        sessionDiamondAmt = in.readInt();
        sessionProfID = in.readInt();
        sessionDate = in.readString();
        sessionStartTime = in.readString();
        sessionEndTime = in.readString();
        sessionCreatedAt = in.readString();
        sessionUpdatedAt = in.readString();
        sessionType = in.readString();
        sessionStatus = in.readString();
    }

    public static final Creator<SessionFreePaid> CREATOR = new Creator<SessionFreePaid>() {
        @Override
        public SessionFreePaid createFromParcel(Parcel in) {
            return new SessionFreePaid(in);
        }

        @Override
        public SessionFreePaid[] newArray(int size) {
            return new SessionFreePaid[size];
        }
    };




    private static List<String> splitLinks(String links) {
        if (links != null) {
            String[] list = links.split(";");
            return Arrays.asList(list);
        }
        return null;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public ArrayList<String> getSessionPhotoLinkArray() {
        return sessionPhotoLinkArray;
    }

    public void setSessionPhotoLinkArray(ArrayList<String> sessionPhotoLinkArray) {
        this.sessionPhotoLinkArray = sessionPhotoLinkArray;
    }

    public ArrayList<String> getSessionVideoLinks() {
        return sessionVideoLinks;
    }

    public void setSessionVideoLinks(ArrayList<String> sessionVideoLinks) {
        this.sessionVideoLinks = sessionVideoLinks;
    }

    public List<Integer> getSessionUserIDs() {
        return sessionUserIDs;
    }

    public void setSessionUserIDs(ArrayList<Integer> sessionUserIDs) {
        this.sessionUserIDs = sessionUserIDs;
    }

    public ArrayList<Integer> getSessionRoomIDs() {
        return sessionRoomIDs;
    }

    public void setSessionRoomIDs(ArrayList<Integer> sessionRoomIDs) {
        this.sessionRoomIDs = sessionRoomIDs;
    }

    public ArrayList<DiamondTransfer> getSessionDiamondTransfers() {
        return sessionDiamondTransfers;
    }

    public void setSessionDiamondTransfers(ArrayList<DiamondTransfer> sessionDiamondTransfers) {
        this.sessionDiamondTransfers = sessionDiamondTransfers;
    }

    public int getSessionDiamondAmt() {
        return sessionDiamondAmt;
    }

    public void setSessionDiamondAmt(int sessionDiamondAmt) {
        this.sessionDiamondAmt = sessionDiamondAmt;
    }

    public int getSessionProfID() {
        return sessionProfID;
    }

    public void setSessionProfID(int sessionProfID) {
        this.sessionProfID = sessionProfID;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getSessionCreatedAt() {
        return sessionCreatedAt;
    }

    public void setSessionCreatedAt(String sessionCreatedAt) {
        this.sessionCreatedAt = sessionCreatedAt;
    }

    public String getSessionUpdatedAt() {
        return sessionUpdatedAt;
    }

    public void setSessionUpdatedAt(String sessionUpdatedAt) {
        this.sessionUpdatedAt = sessionUpdatedAt;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public ArrayList<String> getSessionMessages() {
        return sessionMessages;
    }

    public void setSessionMessages(ArrayList<String> sessionMessages) {
        this.sessionMessages = sessionMessages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sessionID);
        parcel.writeStringList(sessionPhotoLinkArray);
        parcel.writeStringList(sessionVideoLinks);
        parcel.writeStringList(sessionMessages);
        parcel.writeInt(sessionDiamondAmt);
        parcel.writeInt(sessionProfID);
        parcel.writeString(sessionDate);
        parcel.writeString(sessionStartTime);
        parcel.writeString(sessionEndTime);
        parcel.writeString(sessionCreatedAt);
        parcel.writeString(sessionUpdatedAt);
        parcel.writeString(sessionType);
        parcel.writeString(sessionStatus);
    }

    public String getSessionMale() {
        return sessionMale;
    }

    public void setSessionMale(String sessionMale) {
        this.sessionMale = sessionMale;
    }

    public String getSessionFemale() {
        return sessionFemale;
    }

    public void setSessionFemale(String sessionFemale) {
        this.sessionFemale = sessionFemale;
    }

    public String getSessionTittle() {
        return sessionTittle;
    }

    public void setSessionTittle(String sessionTittle) {
        this.sessionTittle = sessionTittle;
    }

    public String getQbDialogID() {
        return qbDialogID;
    }

    public void setQbDialogID(String qbDialogID) {
        this.qbDialogID = qbDialogID;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
