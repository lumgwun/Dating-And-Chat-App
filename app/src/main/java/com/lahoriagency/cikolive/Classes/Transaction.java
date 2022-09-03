package com.lahoriagency.cikolive.Classes;

import android.database.Observable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigInteger;


import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_ID;
import static com.lahoriagency.cikolive.Classes.SavedProfile.SAVED_PROFILE_TABLE;

public class Transaction extends Observable implements Parcelable, Serializable {
    private int tranID;
    private int tranSenderSavedProfID;
    private int tranReceiverSavedProfID;
    private String tranStatus;
    private double tranAmount;
    private String tranDate;
    private String tranApprover;
    private String tranApprovalDate;
    private String tranType;
    private String tranMethodOfPayment;

    public static final String TRANSACTIONS_TABLE = "transactions";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_SENDING_ACCT = "transaction_sending_acct";
    public static final String TRANSACTION_DEST_ACCT = "transaction_dest_acct";
    public static final String TRANSACTION_PAYEE = "transaction_payee";
    public static final String TRANSACTION_PAYER = "transaction_payer";
    public static final String TRANSACTION_STATUS = "transaction_status";
    public static final String TRANSACTIONS_TYPE = "transaction_type";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_APPROVER = "transaction_approver";
    public static final String TRANSACTION_APPROVAL_DATE = "transaction_approval_Date";
    public static final String TRANSACTION_METHOD_OF_PAYMENT = "transaction_method_of_payment";
    public static final String TRANSACTION_SAVEDPROF_ID = "transaction_Prof_ID";
    public static final String TRANSACTION_QBUSER_ID = "transaction_QBUser_ID";

    public static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TRANSACTIONS_TABLE + " (" + TRANSACTION_ID + " INTEGER, " + TRANSACTION_SAVEDPROF_ID + " INTEGER , " +
            TRANSACTION_QBUSER_ID + " INTEGER , "  + TRANSACTION_DATE + " TEXT, " + TRANSACTION_SENDING_ACCT + " TEXT, " +
            TRANSACTION_DEST_ACCT + " TEXT, " + TRANSACTION_PAYEE + " TEXT, " + TRANSACTION_PAYER + " TEXT, " + TRANSACTION_AMOUNT + " REAL, " +
            TRANSACTIONS_TYPE + " TEXT, " + TRANSACTION_METHOD_OF_PAYMENT + " TEXT, "+ TRANSACTION_APPROVER + TRANSACTION_APPROVAL_DATE + " TEXT, "+  TRANSACTION_STATUS + " TEXT, " + "PRIMARY KEY(" +TRANSACTION_ID + "), " +"FOREIGN KEY(" + TRANSACTION_SAVEDPROF_ID + ") REFERENCES " + SAVED_PROFILE_TABLE + "(" + SAVED_PROFILE_ID + "))";






    public Transaction() {
        super();

    }
    protected Transaction(Parcel in) {
        tranID = in.readInt();
        tranSenderSavedProfID = in.readInt();
        tranReceiverSavedProfID = in.readInt();
        tranStatus = in.readString();
        tranAmount = in.readDouble();
        tranDate = in.readString();
        tranApprover = in.readString();
        tranApprovalDate = in.readString();
        tranType = in.readString();
        tranMethodOfPayment = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public int getTranID() {
        return tranID;
    }

    public void setTranID(int tranID) {
        this.tranID = tranID;
    }

    public int getTranSenderSavedProfID() {
        return tranSenderSavedProfID;
    }

    public void setTranSenderSavedProfID(int tranSenderSavedProfID) {
        this.tranSenderSavedProfID = tranSenderSavedProfID;
    }

    public int getTranReceiverSavedProfID() {
        return tranReceiverSavedProfID;
    }

    public void setTranReceiverSavedProfID(int tranReceiverSavedProfID) {
        this.tranReceiverSavedProfID = tranReceiverSavedProfID;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public double getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(double tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTranApprover() {
        return tranApprover;
    }

    public void setTranApprover(String tranApprover) {
        this.tranApprover = tranApprover;
    }

    public String getTranApprovalDate() {
        return tranApprovalDate;
    }

    public void setTranApprovalDate(String tranApprovalDate) {
        this.tranApprovalDate = tranApprovalDate;
    }

    public String getTranMethodOfPayment() {
        return tranMethodOfPayment;
    }

    public void setTranMethodOfPayment(String tranMethodOfPayment) {
        this.tranMethodOfPayment = tranMethodOfPayment;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tranID);
        parcel.writeInt(tranSenderSavedProfID);
        parcel.writeInt(tranReceiverSavedProfID);
        parcel.writeString(tranStatus);
        parcel.writeDouble(tranAmount);
        parcel.writeString(tranDate);
        parcel.writeString(tranApprover);
        parcel.writeString(tranApprovalDate);
        parcel.writeString(tranType);
        parcel.writeString(tranMethodOfPayment);
    }
}
