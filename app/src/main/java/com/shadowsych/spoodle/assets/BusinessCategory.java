package com.shadowsych.spoodle.assets;

//create and get information for specific business categories
public class BusinessCategory {
    private String mCheckoutKey;
    private String mCategoryText;

    public BusinessCategory(String checkoutKey, String categoryText) {
        mCheckoutKey = checkoutKey;
        mCategoryText = categoryText;
    }

    public String getCheckoutKey() {
        return mCheckoutKey;
    }
    public String getCategoryText() {
        return mCategoryText;
    }
}
