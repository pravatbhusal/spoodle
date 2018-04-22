package com.shadowsych.spoodle.assets;

//create and get information for specific business cards
public class BusinessCard {

    private String mBusinessId;
    private String mBusinessImage;
    private String mTimeText;
    private String mTitleText;
    private boolean mIsPromoted;

    public BusinessCard(String businessId, String businessImage, String timeText, String titleText, boolean isPromoted) {
        mBusinessId = businessId;
        mBusinessImage = businessImage;
        mTimeText = timeText;
        mTitleText = titleText;
        mIsPromoted = isPromoted;
    }

    public String getBusinessImage() {
        return mBusinessImage;
    }

    public String getTimeText() {
        return mTimeText;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public String getBusinessId() {
        return mBusinessId;
    }

    public boolean getIsPromoted() {
        return mIsPromoted;
    }
}
