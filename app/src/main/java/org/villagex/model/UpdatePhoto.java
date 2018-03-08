package org.villagex.model;

public class UpdatePhoto {
    private String mTitle;
    private String mThumbUrl;
    private String mLargeUrl;
    private String mDate;

    public UpdatePhoto(String title, String thumbUrl, String largeUrl, String date) {
        mTitle = title;
        mThumbUrl = thumbUrl;
        mLargeUrl = largeUrl;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public String getLargeUrl() {
        return mLargeUrl;
    }

    public String getDate() {
        return mDate;
    }
}
