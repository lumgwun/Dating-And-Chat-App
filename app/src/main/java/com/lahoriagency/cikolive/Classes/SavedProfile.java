package com.lahoriagency.cikolive.Classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.users.model.QBUser;

import java.io.Serializable;

import static com.lahoriagency.cikolive.Classes.QBUser.QBUSER_ID;
import static com.lahoriagency.cikolive.Classes.QBUser.QBUSER_TABLE;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_ID;
import static com.lahoriagency.cikolive.Classes.UserProfileInfo.USER_PROF_INFO_TABLE;

public class SavedProfile implements Parcelable, Serializable {
    public static final String SAVED_PROFILE_NAME = "saved_p_name";
    public static final String SAVED_PROFILE_ID = "saved_p_id";
    public static final String SAVED_PROFILE_USERPROF_INFO_ID = "saved_prof_upi_id";
    public static final String SAVED_PROFILE_QBID = "saved_p_Qbid";
    public static final String SAVED_PROFILE_AGE = "saved_p_age";
    public static final String SAVED_PROFILE_DOB = "saved_p_dob";
    public static final String SAVED_PROFILE_LOC = "saved_p_loc";
    public static final String SAVED_PROFILE_PHOTO = "saved_p_photo";
    public static final String SAVED_PROFILE_GENDER = "saved_p_gender";
    public static final String SAVED_PROFILE_TABLE = "saved_p_Table";
    public static final String SAVED_PROFILE_PHONE = "saved_p_phone";
    public static final String SAVED_PROFILE_EMAIL = "saved_p_email";
    public static final String SAVED_PROFILE_PASSWORD = "saved_p_email";
    public static final String SAVED_PROFILE_COUNTRY = "saved_p_Country";
    public static final String SAVED_PROFILE_DEVICEID = "saved_p_device";
    public static final String SAVED_PROFILE_REFERRER = "saved_p_referrer";
    public static final String SAVED_PROFILE_ABOUT_ME = "saved_p_about_me";
    public static final String SAVED_PROFILE_MY_INT = "saved_p_my_int";
    public static final String SAVED_PROFILE_DATE_JOINED = "saved_p_JoinedDate";
    public static final String SAVED_PROFILE_LOOKING_GENDER = "saved_p_Looking_for";
    public static final String SAVED_PROFILE_LAST_SEEN = "saved_p_Last_seen";


    public static final String CREATE_SAVED_PROFILES_TABLE = "CREATE TABLE IF NOT EXISTS " + SAVED_PROFILE_TABLE + " (" + SAVED_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SAVED_PROFILE_NAME + " TEXT, " + SAVED_PROFILE_AGE + " TEXT, " + SAVED_PROFILE_LOC + " TEXT, " + SAVED_PROFILE_GENDER + " TEXT, " + SAVED_PROFILE_PHOTO + " TEXT, " + SAVED_PROFILE_PHONE + " TEXT, " +
            SAVED_PROFILE_EMAIL + " TEXT, " + SAVED_PROFILE_PASSWORD + " TEXT, " + SAVED_PROFILE_DEVICEID + " TEXT,"+ SAVED_PROFILE_DOB + " TEXT,"+ SAVED_PROFILE_COUNTRY + " TEXT,"+ SAVED_PROFILE_REFERRER + " TEXT,"+ SAVED_PROFILE_ABOUT_ME + " TEXT,"+ SAVED_PROFILE_MY_INT + " TEXT,"+ SAVED_PROFILE_LOOKING_GENDER + " TEXT,"+ SAVED_PROFILE_DATE_JOINED + " TEXT, "+ SAVED_PROFILE_USERPROF_INFO_ID + " TEXT,"+ SAVED_PROFILE_QBID + " TEXT,"+ SAVED_PROFILE_LAST_SEEN + " TEXT," + "FOREIGN KEY(" + SAVED_PROFILE_USERPROF_INFO_ID + ") REFERENCES " + USER_PROF_INFO_TABLE + "(" + USER_PROF_INFO_ID + "),"+ "FOREIGN KEY(" + SAVED_PROFILE_QBID + ") REFERENCES " + QBUSER_TABLE + "(" + QBUSER_ID + "),"+"PRIMARY KEY(" + SAVED_PROFILE_ID  + "))";






    private int savedProfID;
    private int savedProfQBID;
    private int savedProfUserProfInfoID;
    private String name;
    private String age;
    private String location;
    private int gender;
    private String phone;
    private String email;
    private String dob;
    private String country;
    private String referrer;
    private String password;
    private String lastSeen;
    private String status;
    private String dateJoined;
    private String aboutMe;
    private String myInterest;
    private String lookingFor;
    private String deviceID;
    private Uri image;
    private QBUser qbUser;
    private ModelItem modelItem;
    private UserProfileInfo userProfileInfo;
    private UserProfileInfoModel userProfileInfoModel;
    private QBUserCustomData qbUserCustomData;

    public SavedProfile() {
        super();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + savedProfID;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SavedProfile other = (SavedProfile) obj;
        if (savedProfID != other.savedProfID)
            return false;
        return true;
    }

    public SavedProfile(Parcel in) {
        savedProfID = in.readInt();
        name = in.readString();
        age = in.readString();
        location = in.readString();
        gender = in.readInt();
        phone = in.readString();
        email = in.readString();
        password = in.readString();
        deviceID = in.readString();
        image = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<SavedProfile> CREATOR = new Creator<SavedProfile>() {
        @Override
        public SavedProfile createFromParcel(Parcel in) {
            return new SavedProfile(in);
        }

        @Override
        public SavedProfile[] newArray(int size) {
            return new SavedProfile[size];
        }
    };

    public SavedProfile(String surname, String emailStrg, String passwordStg, String aboutMe, String myInterest, String age, int myGender, String lookingFor, String dateJoined, String country, String cityStrg, Uri mImageUri) {
     this.name=surname;
        this.name=surname;
        this.email=emailStrg;
        this.password=passwordStg;
        this.age=age;
        this.age=aboutMe;
        this.age=myInterest;
        this.gender=myGender;
        this.lookingFor=lookingFor;
        this.dateJoined=dateJoined;
        this.country=country;
        this.location=cityStrg;
        this.image=mImageUri;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getSavedProfID() {
        return savedProfID;
    }

    public void setSavedProfID(int savedProfID) {
        this.savedProfID = savedProfID;
    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(savedProfID);
        parcel.writeString(name);
        parcel.writeString(age);
        parcel.writeString(location);
        parcel.writeInt(gender);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(deviceID);
        parcel.writeParcelable(image, i);
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public QBUser getQbUser() {
        return qbUser;
    }

    public void setQbUser(QBUser qbUser) {
        this.qbUser = qbUser;
    }

    public UserProfileInfo getUserProfileInfo() {
        return userProfileInfo;
    }

    public void setUserProfileInfo(UserProfileInfo userProfileInfo) {
        this.userProfileInfo = userProfileInfo;
    }

    public QBUserCustomData getQbUserCustomData() {
        return qbUserCustomData;
    }

    public void setQbUserCustomData(QBUserCustomData qbUserCustomData) {
        this.qbUserCustomData = qbUserCustomData;
    }

    public UserProfileInfoModel getUserProfileInfoModel() {
        return userProfileInfoModel;
    }

    public void setUserProfileInfoModel(UserProfileInfoModel userProfileInfoModel) {
        this.userProfileInfoModel = userProfileInfoModel;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getMyInterest() {
        return myInterest;
    }

    public void setMyInterest(String myInterest) {
        this.myInterest = myInterest;
    }

    public int getSavedProfQBID() {
        return savedProfQBID;
    }

    public void setSavedProfQBID(int savedProfQBID) {
        this.savedProfQBID = savedProfQBID;
    }

    public int getSavedProfUserProfInfoID() {
        return savedProfUserProfInfoID;
    }

    public void setSavedProfUserProfInfoID(int savedProfUserProfInfoID) {
        this.savedProfUserProfInfoID = savedProfUserProfInfoID;
    }

    public ModelItem getModelItem() {
        return modelItem;
    }

    public void setModelItem(ModelItem modelItem) {
        this.modelItem = modelItem;
    }
}
