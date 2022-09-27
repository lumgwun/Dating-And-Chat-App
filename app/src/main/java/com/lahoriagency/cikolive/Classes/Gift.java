package com.lahoriagency.cikolive.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Gift implements Parcelable, Serializable {

    private String image;
    private String giftType;
    private int count;
    public Gift() {
        super();
    }

    protected Gift(Parcel in) {
        image = in.readString();
        giftType = in.readString();
        count = in.readInt();
    }

    public static final Creator<Gift> CREATOR = new Creator<Gift>() {
        @Override
        public Gift createFromParcel(Parcel in) {
            return new Gift(in);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGiftType() {
        return giftType;
    }

    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(giftType);
        parcel.writeInt(count);
    }
}
