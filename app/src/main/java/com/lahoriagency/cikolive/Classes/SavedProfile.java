package com.lahoriagency.cikolive.Classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.io.Serializable;
import java.util.ArrayList;

import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_ID;
import static com.lahoriagency.cikolive.Classes.AppServerUser.QBUSER_TABLE;
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
    public static final String SAVED_PROFILE_PASSWORD = "saved_p_Password";
    public static final String SAVED_PROFILE_COUNTRY = "saved_p_Country";
    public static final String SAVED_PROFILE_DEVICEID = "saved_p_device";
    public static final String SAVED_PROFILE_REFERRER = "saved_p_referrer";
    public static final String SAVED_PROFILE_ABOUT_ME = "saved_p_about_me";
    public static final String SAVED_PROFILE_MY_INT = "saved_p_my_int";
    public static final String SAVED_PROFILE_DATE_JOINED = "saved_p_JoinedDate";
    public static final String SAVED_PROFILE_LOOKING_GENDER = "saved_p_Looking_for";
    public static final String SAVED_PROFILE_LAST_SEEN = "saved_p_Last_seen";
    public static final String SAVED_PROFILE_STATUS = "saved_p_Status";
    public static final String SAVED_PROFILE_DBID = "saved_p_DBID";
    public static final String SAVED_PROFILE_DIAMOND = "saved_p_Diamond";
    public static final String SAVED_PROF_REF_REWARD_COUNT = "saved_p_Reward_Count";




    public static final String CREATE_SAVED_PROFILES_TABLE = "CREATE TABLE IF NOT EXISTS " + SAVED_PROFILE_TABLE + " (" + SAVED_PROFILE_ID + " INTEGER , " + SAVED_PROFILE_NAME + " TEXT, " + SAVED_PROFILE_AGE + " TEXT, " + SAVED_PROFILE_LOC + " TEXT, " + SAVED_PROFILE_GENDER + " TEXT, " + SAVED_PROFILE_PHOTO + " BLOB, " + SAVED_PROFILE_PHONE + " TEXT, " +
            SAVED_PROFILE_EMAIL + " TEXT, " + SAVED_PROFILE_PASSWORD + " TEXT, " + SAVED_PROFILE_DEVICEID + " TEXT,"+ SAVED_PROFILE_DOB + " TEXT,"+ SAVED_PROFILE_COUNTRY + " TEXT,"+ SAVED_PROFILE_REFERRER + " TEXT,"+ SAVED_PROFILE_ABOUT_ME + " TEXT,"+ SAVED_PROFILE_MY_INT + " TEXT,"+ SAVED_PROFILE_LOOKING_GENDER + " TEXT,"+ SAVED_PROFILE_DATE_JOINED + " TEXT, "+ SAVED_PROFILE_USERPROF_INFO_ID + " TEXT,"+ SAVED_PROFILE_QBID + " TEXT,"+ SAVED_PROFILE_LAST_SEEN + " TEXT," + SAVED_PROFILE_STATUS + " TEXT," + SAVED_PROFILE_DBID + " INTEGER,"+ SAVED_PROFILE_DIAMOND + " INTEGER,"+ SAVED_PROF_REF_REWARD_COUNT + " INTEGER,"+ "FOREIGN KEY(" + SAVED_PROFILE_USERPROF_INFO_ID + ") REFERENCES " + USER_PROF_INFO_TABLE + "(" + USER_PROF_INFO_ID + "),"+ "FOREIGN KEY(" + SAVED_PROFILE_QBID + ") REFERENCES " + QBUSER_TABLE + "(" + QBUSER_ID + "),"+"PRIMARY KEY(" + SAVED_PROFILE_DBID  + "))";


    public static final String PICTURE_TABLE = "pictureTable";
    //@Ignore
    public static final String PICTURE_URI = "picture_uri";
    public static final String PROFILE_PIC_ID = "picture_id";
    public static final String PROFILE_PIC_SAVEDPROF_ID = "picture_SPid";

    public static final String CREATE_PIXTURE_TABLE = "CREATE TABLE " + PICTURE_TABLE + " (" + PROFILE_PIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PROFILE_PIC_SAVEDPROF_ID + " INTEGER, " + PICTURE_URI + " BLOB,"+"FOREIGN KEY(" + PROFILE_PIC_SAVEDPROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";


    private int savedProfID;
    private int savedProfQBID;
    private int savedProfUserProfInfoID;
    private String savedPName;
    private String savedPAge;
    private String savedPLocation;
    private String savedPGender;
    private String savedPPhone;
    private String savedPEmail;
    private String savedPDob;
    private String savedPCountry;
    private String savedPReferrer;
    private String savedPPassword;
    private String savedPLastSeen;
    private String savedPStatus;
    private String savedPDateJoined;
    private String savedPAboutMe;
    private String savedPMyInterest;
    private String savedPLookingFor;
    private String savedPDeviceID;
    private Uri savedPImage;
    private QBUser savedPQbUser;
    private ModelItem savedPModelItem;
    private UserProfileInfo savedPUserProfileInfo;
    private UserProfileInfoModel userProfileInfoModel;
    private QBUserCustomData qbUserCustomData;
    private ArrayList<PurchaseDiamond> purchaseDiamonds;
    private ArrayList<RedeemRequest> redeemRequests;
    private ArrayList<DiamondTransfer> diamondHistories;
    private ArrayList<QBChatDialog> qbChatDialogArrayList;

    private Diamond savedPDiamond;
    private LoginReply loginReply;
    private int defaultDiamond;
    private int savedPRefRewardCount;

    public SavedProfile() {
        super();
    }

    public SavedProfile(int profileID, String profileName, String emailStrg, String passwordStg, String aboutMe, String myIntrest, String myAge, String userGender, String gender, String joinedDate, String country, String cityStrg, Uri mImageUri) {
        this.savedProfID =profileID;
        this.savedPName =profileName;
        this.savedPEmail =emailStrg;
        this.savedPPassword =passwordStg;
        this.savedPAge = myAge;
        this.savedPAboutMe = aboutMe;
        this.savedPMyInterest = myIntrest;
        this.savedPGender =userGender;
        this.savedPLookingFor = gender;
        this.savedPDateJoined = joinedDate;
        this.savedPCountry = country;
        this.savedPLocation =cityStrg;
        this.savedPImage =mImageUri;
    }


    public void addQbChatDialog(QBChatDialog chatDialog) {
        qbChatDialogArrayList = new ArrayList<>();
        qbChatDialogArrayList.add(chatDialog);
    }
    public void addRedeemRequest(int id, String date, int type, int diamondCount, String amount) {
        redeemRequests = new ArrayList<>();
        RedeemRequest redeemRequest = new RedeemRequest(id,date, type, diamondCount,amount);
        redeemRequests.add(redeemRequest);
    }
    public void addPurchasedDiamond(int count, String price) {
        purchaseDiamonds = new ArrayList<>();
        PurchaseDiamond purchaseDiamond = new PurchaseDiamond(count,price);
        purchaseDiamonds.add(purchaseDiamond);
    }
    public void addDiamondHistory(String from, String date,int count) {
        diamondHistories = new ArrayList<>();
        DiamondTransfer diamondTransfer = new DiamondTransfer(from,date,count);
        diamondHistories.add(diamondTransfer);
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
        savedPName = in.readString();
        savedPAge = in.readString();
        savedPLocation = in.readString();
        savedPGender = in.readString();
        savedPPhone = in.readString();
        savedPEmail = in.readString();
        savedPPassword = in.readString();
        savedPDeviceID = in.readString();
        savedPImage = in.readParcelable(Uri.class.getClassLoader());
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

    public SavedProfile(String surname, String emailStrg, String passwordStg, String savedPAboutMe, String savedPMyInterest, String savedPAge, String myGender, String savedPLookingFor, String savedPDateJoined, String savedPCountry, String cityStrg, Uri mImageUri) {
     this.savedPName =surname;
        this.savedPEmail =emailStrg;
        this.savedPPassword =passwordStg;
        this.savedPAge = savedPAge;
        this.savedPEmail =emailStrg;
        this.savedPPassword =passwordStg;
        this.savedPAboutMe = savedPAboutMe;
        this.savedPMyInterest = savedPMyInterest;

        this.savedPGender =myGender;
        this.savedPLookingFor = savedPLookingFor;
        this.savedPDateJoined = savedPDateJoined;
        this.savedPCountry = savedPCountry;
        this.savedPLocation =cityStrg;
        this.savedPImage =mImageUri;

    }

    public String getSavedPName() {
        return savedPName;
    }

    public void setSavedPName(String savedPName) {
        this.savedPName = savedPName;
    }

    public String getSavedPAge() {
        return savedPAge;
    }

    public void setSavedPAge(String savedPAge) {
        this.savedPAge = savedPAge;
    }

    public String getSavedPLocation() {
        return savedPLocation;
    }

    public void setSavedPLocation(String savedPLocation) {
        this.savedPLocation = savedPLocation;
    }

    public Uri getSavedPImage() {
        return savedPImage;
    }

    public void setSavedPImage(Uri savedPImage) {
        this.savedPImage = savedPImage;
    }

    public int getSavedProfID() {
        return savedProfID;
    }

    public void setSavedProfID(int savedProfID) {
        this.savedProfID = savedProfID;
    }


    public String getSavedPGender() {
        return savedPGender;
    }

    public void setSavedPGender(String savedPGender) {
        this.savedPGender = savedPGender;
    }

    public String getSavedPPhone() {
        return savedPPhone;
    }

    public void setSavedPPhone(String savedPPhone) {
        this.savedPPhone = savedPPhone;
    }

    public String getSavedPEmail() {
        return savedPEmail;
    }

    public void setSavedPEmail(String savedPEmail) {
        this.savedPEmail = savedPEmail;
    }

    public String getSavedPPassword() {
        return savedPPassword;
    }

    public void setSavedPPassword(String savedPPassword) {
        this.savedPPassword = savedPPassword;
    }

    public String getSavedPDeviceID() {
        return savedPDeviceID;
    }

    public void setSavedPDeviceID(String savedPDeviceID) {
        this.savedPDeviceID = savedPDeviceID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(savedProfID);
        parcel.writeString(savedPName);
        parcel.writeString(savedPAge);
        parcel.writeString(savedPLocation);
        parcel.writeString(savedPGender);
        parcel.writeString(savedPPhone);
        parcel.writeString(savedPEmail);
        parcel.writeString(savedPPassword);
        parcel.writeString(savedPDeviceID);
        parcel.writeParcelable(savedPImage, i);
    }

    public String getSavedPDob() {
        return savedPDob;
    }

    public void setSavedPDob(String savedPDob) {
        this.savedPDob = savedPDob;
    }

    public String getSavedPCountry() {
        return savedPCountry;
    }

    public void setSavedPCountry(String savedPCountry) {
        this.savedPCountry = savedPCountry;
    }

    public String getSavedPReferrer() {
        return savedPReferrer;
    }

    public void setSavedPReferrer(String savedPReferrer) {
        this.savedPReferrer = savedPReferrer;
    }

    public String getSavedPLastSeen() {
        return savedPLastSeen;
    }

    public void setSavedPLastSeen(String savedPLastSeen) {
        this.savedPLastSeen = savedPLastSeen;
    }

    public String getSavedPStatus() {
        return savedPStatus;
    }

    public void setSavedPStatus(String savedPStatus) {
        this.savedPStatus = savedPStatus;
    }

    public QBUser getSavedPQbUser() {
        return savedPQbUser;
    }

    public void setSavedPQbUser(QBUser savedPQbUser) {
        this.savedPQbUser = savedPQbUser;
    }

    public UserProfileInfo getSavedPUserProfileInfo() {
        return savedPUserProfileInfo;
    }

    public void setSavedPUserProfileInfo(UserProfileInfo savedPUserProfileInfo) {
        this.savedPUserProfileInfo = savedPUserProfileInfo;
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

    public String getSavedPLookingFor() {
        return savedPLookingFor;
    }

    public void setSavedPLookingFor(String savedPLookingFor) {
        this.savedPLookingFor = savedPLookingFor;
    }

    public String getSavedPDateJoined() {
        return savedPDateJoined;
    }

    public void setSavedPDateJoined(String savedPDateJoined) {
        this.savedPDateJoined = savedPDateJoined;
    }

    public String getSavedPAboutMe() {
        return savedPAboutMe;
    }

    public void setSavedPAboutMe(String savedPAboutMe) {
        this.savedPAboutMe = savedPAboutMe;
    }

    public String getSavedPMyInterest() {
        return savedPMyInterest;
    }

    public void setSavedPMyInterest(String savedPMyInterest) {
        this.savedPMyInterest = savedPMyInterest;
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

    public ModelItem getSavedPModelItem() {
        return savedPModelItem;
    }

    public void setSavedPModelItem(ModelItem savedPModelItem) {
        this.savedPModelItem = savedPModelItem;
    }

    public ArrayList<PurchaseDiamond> getPurchaseDiamonds() {
        return purchaseDiamonds;
    }

    public void setPurchaseDiamonds(ArrayList<PurchaseDiamond> purchaseDiamonds) {
        this.purchaseDiamonds = purchaseDiamonds;
    }

    public ArrayList<RedeemRequest> getRedeemRequests() {
        return redeemRequests;
    }

    public void setRedeemRequests(ArrayList<RedeemRequest> redeemRequests) {
        this.redeemRequests = redeemRequests;
    }

    public ArrayList<DiamondTransfer> getDiamondHistories() {
        return diamondHistories;
    }

    public void setDiamondHistories(ArrayList<DiamondTransfer> diamondHistories) {
        this.diamondHistories = diamondHistories;
    }

    public Diamond getSavedPDiamond() {
        return savedPDiamond;
    }

    public void setSavedPDiamond(Diamond savedPDiamond) {
        this.savedPDiamond = savedPDiamond;
    }

    public void setLoginReply(LoginReply loginReply) {
        this.loginReply = loginReply;
    }

    public LoginReply getLoginReply() {
        return loginReply;
    }

    public int getDefaultDiamond() {
        return defaultDiamond;
    }

    public void setDefaultDiamond(int defaultDiamond) {
        this.defaultDiamond = defaultDiamond;
    }

    public ArrayList<QBChatDialog> getQbChatDialogArrayList() {
        return qbChatDialogArrayList;
    }

    public void setQbChatDialogArrayList(ArrayList<QBChatDialog> qbChatDialogArrayList) {
        this.qbChatDialogArrayList = qbChatDialogArrayList;
    }

    public int getSavedPRefRewardCount() {
        return savedPRefRewardCount;
    }

    public void setSavedPRefRewardCount(int savedPRefRewardCount) {
        this.savedPRefRewardCount = savedPRefRewardCount;
    }
}
