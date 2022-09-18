package com.lahoriagency.cikolive.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class ModelItem implements Serializable, Parcelable {

    public static final String MODEL_ITEM_TABLE = "model_table";
    public static final String MODEL_ACTOR_NAME = "model_actor_name";
    public static final String MODEL_PROF_ID = "model_Prof_ID";
    public static final String MODEL_ITEM_ID = "model_Item_ID";
    public static final String MODEL_ACTOR_IMAGE = "model_actor_image";
    public static final String MODEL_LOCATION = "model_loc";
    public static final String MODEL_DIAMOND = "model_diamond_count";
    public static final String MODEL_AGE = "model_age";
    public static final String MODEL_IS_SHORT = "model_is_short";
    public static final String MODEL_STATUS = "model_Status";
    public static final String MODEL_QBUser_ID = "model_QbUser_ID";

    public static final String CREATE_MODEL_TYPE_TABLE = "CREATE TABLE " + MODEL_ITEM_TABLE + " (" + MODEL_ITEM_ID + " INTEGER, " +
            MODEL_ACTOR_NAME + " TEXT, " + MODEL_AGE + " INTEGER , "+ MODEL_DIAMOND + " INTEGER , "+ MODEL_IS_SHORT + " INTEGER , "+ MODEL_ACTOR_IMAGE + " BLOB , " + MODEL_LOCATION + " REAL , " + MODEL_STATUS + " REAL , "+ MODEL_PROF_ID + " INTEGER , "+ MODEL_QBUser_ID + " INTEGER , "+
            "PRIMARY KEY(" + MODEL_ITEM_ID + "), " + "FOREIGN KEY(" + MODEL_PROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";

    private String actorName;
    private String actorImage;
    private String location;
    private int diamond;
    private int modelProfID;
    private int modelID;
    private int modelQBUserID;

    private int age;
    private String modelItemStatus;

    public ModelItem() {
        super();
    }

    protected ModelItem(Parcel in) {
        actorName = in.readString();
        actorImage = in.readString();
        location = in.readString();
        diamond = in.readInt();
        modelProfID = in.readInt();
        modelID = in.readInt();
        age = in.readInt();
        modelItemStatus = in.readString();
        isShort = in.readByte() != 0;
    }

    public static final Creator<ModelItem> CREATOR = new Creator<ModelItem>() {
        @Override
        public ModelItem createFromParcel(Parcel in) {
            return new ModelItem(in);
        }

        @Override
        public ModelItem[] newArray(int size) {
            return new ModelItem[size];
        }
    };

    public ModelItem(int modelItemID, String itemName, int age, int diamondNo, String isShort, String img, String location,String status) {
        this.modelID=modelItemID;
        this.actorName=itemName;
        this.age=age;
        this.diamond=diamondNo;
        this.isShort= Boolean.parseBoolean(isShort);
        this.actorImage=img;
        this.location=location;
        this.modelItemStatus=status;

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private boolean isShort;

    public boolean isShort() {
        return isShort;
    }

    public void setShort(boolean aShort) {
        isShort = aShort;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorImage() {
        return actorImage;
    }

    public void setActorImage(String actorImage) {
        this.actorImage = actorImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getModelProfID() {
        return modelProfID;
    }

    public void setModelProfID(int modelProfID) {
        this.modelProfID = modelProfID;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public String getModelItemStatus() {
        return modelItemStatus;
    }

    public void setModelItemStatus(String modelItemStatus) {
        this.modelItemStatus = modelItemStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(actorName);
        parcel.writeString(actorImage);
        parcel.writeString(location);
        parcel.writeInt(diamond);
        parcel.writeInt(modelProfID);
        parcel.writeInt(modelID);
        parcel.writeInt(age);
        parcel.writeString(modelItemStatus);
        parcel.writeByte((byte) (isShort ? 1 : 0));
    }

    public int getModelQBUserID() {
        return modelQBUserID;
    }

    public void setModelQBUserID(int modelQBUserID) {
        this.modelQBUserID = modelQBUserID;
    }
}
