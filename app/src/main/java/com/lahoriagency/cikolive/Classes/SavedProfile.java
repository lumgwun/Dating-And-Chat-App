package com.lahoriagency.cikolive.Classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.users.model.QBUser;

import java.io.Serializable;

public class SavedProfile implements Parcelable, Serializable {
    public static final String SAVED_PROFILE_NAME = "saved_p_name";
    public static final String SAVED_PROFILE_ID = "saved_p_id";
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

    public static final String CREATE_SAVED_PROFILES_TABLE = "CREATE TABLE " + SAVED_PROFILE_TABLE + " (" + SAVED_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SAVED_PROFILE_NAME + " TEXT, " + SAVED_PROFILE_AGE + " TEXT, " + SAVED_PROFILE_LOC + " TEXT, " + SAVED_PROFILE_GENDER + " TEXT, " + SAVED_PROFILE_PHOTO + " TEXT, " + SAVED_PROFILE_PHONE + " TEXT, " +
            SAVED_PROFILE_EMAIL + " TEXT, " + SAVED_PROFILE_PASSWORD + " TEXT, " + SAVED_PROFILE_DEVICEID + " TEXT,"+ SAVED_PROFILE_DOB + " TEXT,"+ SAVED_PROFILE_COUNTRY + " TEXT,"+ SAVED_PROFILE_REFERRER + " TEXT)";
    private int savedProfID;
    private String name;
    private String age;
    private String location;
    private String gender;
    private String phone;
    private String email;
    private String dob;
    private String country;
    private String referrer;
    private String password;
    private String lastSeen;
    private String status;
    private String deviceID;
    private Uri image;
    private QBUser qbUser;
    private UserProfileInfo userProfileInfo;
    private UserProfileInfoModel userProfileInfoModel;
    private QBUserCustomData qbUserCustomData;

    public SavedProfile() {
        super();
    }

    public SavedProfile(Parcel in) {
        savedProfID = in.readInt();
        name = in.readString();
        age = in.readString();
        location = in.readString();
        gender = in.readString();
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

    public SavedProfile(String surname, String uFirstName, String emailStrg, String passwordStg, String dateOfBirth, String gender, String phoneNO, String country, String cityStrg, Uri mImageUri) {

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


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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
        parcel.writeString(gender);
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
}
