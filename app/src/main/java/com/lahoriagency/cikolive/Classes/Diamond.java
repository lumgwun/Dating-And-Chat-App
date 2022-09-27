package com.lahoriagency.cikolive.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Diamond implements Parcelable, Serializable {
    private int diamondCount;
    private int savedUserProfID;
    private int diamondWalletID;
    private int diamondCollections;

    public Diamond() {
        super();
    }

    protected Diamond(Parcel in) {
        diamondCount = in.readInt();
        savedUserProfID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(diamondCount);
        dest.writeInt(savedUserProfID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Diamond> CREATOR = new Creator<Diamond>() {
        @Override
        public Diamond createFromParcel(Parcel in) {
            return new Diamond(in);
        }

        @Override
        public Diamond[] newArray(int size) {
            return new Diamond[size];
        }
    };

    public int getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(int diamondCount) {
        this.diamondCount = diamondCount;
    }

    public int getSavedUserProfID() {
        return savedUserProfID;
    }

    public void setSavedUserProfID(int savedUserProfID) {
        this.savedUserProfID = savedUserProfID;
    }

    public int getDiamondWalletID() {
        return diamondWalletID;
    }

    public void setDiamondWalletID(int diamondWalletID) {
        this.diamondWalletID = diamondWalletID;
    }

    public int getDiamondCollections() {
        return diamondCollections;
    }

    public void setDiamondCollections(int diamondCollections) {
        this.diamondCollections = diamondCollections;
    }
}
