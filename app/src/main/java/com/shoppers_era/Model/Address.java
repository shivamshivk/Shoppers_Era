package com.shoppers_era.Model;


public class Address {


    private String mFirstName;
    private String mPhoneNumber;
    private String mAddress;
    private String mAddress2;
    private String mPin;
    private String mLandmark;
    private String mCity;
    private String mState;
    private String mPos;


    public Address(String mFirstName,  String mPhoneNumber, String mAddress, String mAddress2,  String mLandmark,String mPin, String mCity, String mState,String mPos) {
        this.mFirstName = mFirstName;
        this.mPhoneNumber = mPhoneNumber;
        this.mAddress = mAddress;
        this.mAddress2 = mAddress2;
        this.mLandmark = mLandmark;
        this.mPin = mPin;
        this.mCity = mCity;
        this.mState = mState;
        this.mPos = mPos;
    }


    public String getFirstName() {
        return mFirstName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getLandmark() {
        return mLandmark;
    }

    public String getPin() {
        return mPin;
    }

    public String getCity() {
        return mCity;
    }

    public String getmAddress2() {
        return mAddress2;
    }

    public String getmState() {
        return mState;
    }

    public String getmPos() {
        return mPos;
    }

}
