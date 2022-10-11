package com.lahoriagency.cikolive.Classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_ID;
import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_TABLE;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class UserProfileInfo implements Parcelable {
    private int userProfInfoID;
    private int userQbId;
    private int userSavedProfID;
    private String name;
    private List<String> photoLinks;
    private ArrayList<String> photoLinkArray;
    //private ArrayList<Uri> profileMediaFilesUri;
    private ArrayList<String> videoLinks;
    //private ArrayList<Uri> videoLinksUri;
    private String upiPhoto;
    private int age;
    private int matchValue;
    private int distance;
    private String description;
    private String upiAboutMe;
    private String upiInterest;
    private String upiMyCountry;
    private double chatDiamondAmount;
    private String upiLookingForGender;

    public static final String USER_PROF_INFO_ID = "upi_Id";
    public static final String USER_PROF_QB_ID = "upi_qbUser_ID";
    public static final String USER_PROF_INFO_SP_ID = "upi_saved_prof_id";
    public static final String USER_PROF_INFO_NAME = "upi_name";
    public static final String USER_PROF_INFO_AGE = "upi_Age";
    public static final String USER_PROF_INFO_MATCHED_VALUE = "upi_MatchedV";
    public static final String USER_PROF_INFO_DISTANCE = "upi_Distance";
    public static final String USER_PROF_INFO_DESC = "upi_Desc";
    public static final String USER_PROF_INFO_TABLE = "upi_table";
    public static final String USER_PROF_INFO_ABOUT_ME = "upi_aboutMe";
    public static final String USER_PROF_INTEREST20 = "upi_interest";
    public static final String USER_PROF_INFO_COUNTRY = "upi_Country";


    public static final String CREATE_USER_PROF_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_PROF_INFO_TABLE + " ( " + USER_PROF_INFO_ID + " INTEGER  , " + USER_PROF_QB_ID + " INTEGER , " + USER_PROF_INFO_SP_ID + " INTEGER  , " +
            USER_PROF_INFO_NAME + " TEXT, " + USER_PROF_INFO_DESC + " TEXT, " + USER_PROF_INFO_AGE + " TEXT, " + USER_PROF_INFO_MATCHED_VALUE + " TEXT, " + USER_PROF_INFO_DISTANCE + " TEXT, " + USER_PROF_INFO_ABOUT_ME + " TEXT, "+ USER_PROF_INTEREST20 + " TEXT, "+ USER_PROF_INFO_COUNTRY + " TEXT, "+
            "FOREIGN KEY(" + USER_PROF_QB_ID + ") REFERENCES " + QBUSER_TABLE + "(" + QBUSER_ID + "),"+ "FOREIGN KEY(" + USER_PROF_INFO_SP_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "),"+"PRIMARY KEY(" + USER_PROF_INFO_ID  + "))";


    public UserProfileInfo() {
        super();
    }

    public UserProfileInfo(int userQbId,int userSavedProfID,String name) {
        this.userProfInfoID = userSavedProfID;
        this.userQbId = userQbId;
        this.name = name;
    }
    public UserProfileInfo(int userQbId,int userSavedProfID,int age,String name, String upiAboutMe,String upiInterest,String upiMyCountry,String upiLookingForGender,String mImageUriString) {
        this.userProfInfoID = userSavedProfID;
        this.userQbId = userQbId;
        this.name = name;
        this.upiAboutMe = upiAboutMe;
        this.upiInterest = upiInterest;
        this.upiMyCountry = upiMyCountry;
        this.upiLookingForGender = upiLookingForGender;
        this.age = age;
        this.upiPhoto = mImageUriString;
    }
    public UserProfileInfo(int userQbId,String name) {
        this.userQbId = userQbId;
        this.name = name;
    }
    public UserProfileInfo(UserProfileInfoModel model) {
        this.userProfInfoID = model.getUserId();
        this.userQbId = model.getQuickbloxId();
        this.name = model.getName();
        this.age = model.getAge();
        this.distance = model.getDistance();
        this.description = model.getDescription();
        this.photoLinks = splitLinks(model.getPhotoLinks());
    }
    public void  addPhotoLink(String photoLinks){
        photoLinkArray = new ArrayList<String>();
        photoLinkArray.add(photoLinks);

    }

    public int getUserProfInfoID() {
        return userProfInfoID;
    }

    public void setUserProfInfoID(int userProfInfoID) {
        this.userProfInfoID = userProfInfoID;
    }

    public int getUserQbId() {
        return userQbId;
    }

    public void setUserQbId(int userQbId) {
        this.userQbId = userQbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(int matchValue) {
        this.matchValue = matchValue;
    }

    public List<String> getPhotoLinks() {
        return photoLinks;
    }

    public void setPhotoLinks(List<String> photoLinks) {
        this.photoLinks = photoLinks;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected UserProfileInfo(Parcel in) {
        userProfInfoID = in.readInt();
        userQbId = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {

            photoLinks = new ArrayList<String>();
            in.readList(photoLinks, String.class.getClassLoader());
        } else {
            photoLinks = null;
        }
        age = in.readInt();
        matchValue = in.readInt();
        distance = in.readInt();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userProfInfoID);
        dest.writeInt(userQbId);
        dest.writeString(name);
        if (photoLinks == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(photoLinks);
        }
        dest.writeInt(age);
        dest.writeInt(matchValue);
        dest.writeInt(distance);
        dest.writeString(description);
    }

    private static List<String> splitLinks(String links) {
        if (links != null) {
            String[] list = links.split(";");
            return Arrays.asList(list);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static final Creator<UserProfileInfo> CREATOR = new Creator<UserProfileInfo>() {
        @Override
        public UserProfileInfo createFromParcel(Parcel in) {
            return new UserProfileInfo(in);
        }

        @Override
        public UserProfileInfo[] newArray(int size) {
            return new UserProfileInfo[size];
        }
    };

    public int getUserSavedProfID() {
        return userSavedProfID;
    }

    public void setUserSavedProfID(int userSavedProfID) {
        this.userSavedProfID = userSavedProfID;
    }

    public String getUpiAboutMe() {
        return upiAboutMe;
    }

    public void setUpiAboutMe(String upiAboutMe) {
        this.upiAboutMe = upiAboutMe;
    }

    public String getUpiInterest() {
        return upiInterest;
    }

    public void setUpiInterest(String upiInterest) {
        this.upiInterest = upiInterest;
    }

    public String getUpiMyCountry() {
        return upiMyCountry;
    }

    public void setUpiMyCountry(String upiMyCountry) {
        this.upiMyCountry = upiMyCountry;
    }

    public String getUpiLookingForGender() {
        return upiLookingForGender;
    }

    public void setUpiLookingForGender(String upiLookingForGender) {
        this.upiLookingForGender = upiLookingForGender;
    }

    public String getUpiPhoto() {
        return upiPhoto;
    }


    public void setUpiPhoto(String upiPhoto) {
        this.upiPhoto = upiPhoto;
    }

   /* public ArrayList<Uri> getProfileMediaFilesUri() {
        return profileMediaFilesUri;
    }

    public void setProfileMediaFilesUri(ArrayList<Uri> profileMediaFilesUri) {
        this.profileMediaFilesUri = profileMediaFilesUri;
    }*/

    public ArrayList<String> getVideoLinks() {
        return videoLinks;
    }

    public void setVideoLinks(ArrayList<String> videoLinks) {
        this.videoLinks = videoLinks;
    }


    public double getChatDiamondAmount() {
        return chatDiamondAmount;
    }

    public void setChatDiamondAmount(double chatDiamondAmount) {
        this.chatDiamondAmount = chatDiamondAmount;
    }
}
