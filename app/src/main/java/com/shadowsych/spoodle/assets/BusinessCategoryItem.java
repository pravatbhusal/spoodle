package com.shadowsych.spoodle.assets;

//create and get information for specific business category items
public class BusinessCategoryItem {
    private String mCategoryText;
    private String mItemText;
    private double mPrice;
    private int mQuantity;
    private String mExternalities;

    public BusinessCategoryItem(String categoryText, String categoryItemText, double price, int quantity, String externalities) {
        mCategoryText = categoryText;
        mItemText = categoryItemText;
        mPrice = price;
        mQuantity = quantity;
        mExternalities = externalities;
    }

    public String getCategory() {
        return mCategoryText;
    }
    public String getCategoryItem() {
        return mItemText;
    }
    public double getCategoryItemPrice() {
        return mPrice;
    }
    public int getQuantity() {
        return mQuantity;
    }
    public String getExternalities() {
        return mExternalities;
    }
}
