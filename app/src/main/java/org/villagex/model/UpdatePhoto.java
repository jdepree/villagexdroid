package org.villagex.model;

public class UpdatePhoto {
    private String mTitle;
    private String mImageUrl;
    private String mDate;

    public UpdatePhoto(String title, String imageUrl, String date) {
        mTitle = title;
        mImageUrl = imageUrl;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDate() {
        return mDate;
    }
}
