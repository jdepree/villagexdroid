package org.villagex.model;

import android.content.ContentValues;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Project {
    @SerializedName("project_id")
    private int mId;

    @SerializedName("project_name")
    private String mName;

    @SerializedName("project_lat")
    private float mLat;

    @SerializedName("project_lng")
    private float mLng;

    @SerializedName("project_village_id")
    private int mVillageId;

    @SerializedName("project_type")
    private String mType;

    @SerializedName("project_budget")
    private int mBudget;

    @SerializedName("project_funded")
    private int mFunded;

    @SerializedName("picture_filename")
    private String mPicture;

    @SerializedName("banner_filename")
    private String mBanner;

    @SerializedName("project_summary")
    private String mSummary;

    @SerializedName("project_community_problem")
    private String mCommunityProblem;

    @SerializedName("project_community_solution")
    private String mCommunitySolution;

    @SerializedName("project_impact")
    private String mProjectImpact;

    @SerializedName("project_community_partners")
    private String mCommunityPartners;

    @SerializedName("event_dates")
    private String mDates;

    @SerializedName("event_labels")
    private String mEventLabels;

    @SerializedName("donor_count")
    private int mDonorCount;

    @SerializedName("update_pictures")
    private String mUpdatePhotos;

    @SerializedName("update_dates")
    private String mUpdateDates;

    @SerializedName("update_descriptions")
    private String mUpdateDescriptions;

    private Village mVillage;
    private TimeLineModel[] mEventArray;
    private UpdatePhoto[] mPhotoArray;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public float getLat() {
        return mLat;
    }

    public void setLat(float lat) {
        mLat = lat;
    }

    public float getLng() {
        return mLng;
    }

    public void setLng(float lng) {
        mLng = lng;
    }

    public int getVillageId() {
        return mVillageId;
    }

    public void setVillageId(int villageId) {
        mVillageId = villageId;
    }

    public Village getVillage() {
        return mVillage;
    }

    public void setVillage(Village village) {
        mVillage = village;
    }

    public int getBudget() {
        return mBudget;
    }

    public void setBudget(int budget) {
        mBudget = budget;
    }

    public int getFunded() {
        return mFunded;
    }

    public void setFunded(int funded) {
        mFunded = funded;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public String getBanner() {
        return mBanner;
    }

    public void setBanner(String banner) {
        mBanner = banner;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getCommunityProblem() {
        return mCommunityProblem;
    }

    public String getCommunitySolution() {
        return mCommunitySolution;
    }

    public String getProjectImpact() {
        return mProjectImpact;
    }

    public String getCommunityPartners() {
        return mCommunityPartners;
    }

    public TimeLineModel[] getEvents() {
        if (mEventArray != null) {
            return mEventArray;
        }

        if (mDates == null || mEventLabels == null) {
            return null;
        }
        String[] dates = mDates.split(",");
        String[] eventLabels = mEventLabels.split(",");
        List<TimeLineModel> result = new ArrayList<>();
        boolean isFirst = true;
        for (int i = 0; i < dates.length; i++) {
            result.add(new TimeLineModel(eventLabels[i], dates[i],
                    dates[i] == null
                            ? (isFirst ? TimeLineModel.TimeLineStatus.IN_PROGRESS : TimeLineModel.TimeLineStatus.UNSTARTED)
                            : TimeLineModel.TimeLineStatus.COMPLETED));
            if (dates[i] == null) {
                isFirst = false;
            }
        }

        return mEventArray = result.toArray(new TimeLineModel[] {});
    }

    public UpdatePhoto[] getPhotos() {
        if (mPhotoArray != null) {
            return mPhotoArray;
        }

        if (mUpdateDates == null || mUpdateDescriptions == null || mUpdatePhotos == null) {
            return null;
        }
        String[] dates = mUpdateDates.split(",");
        String[] descriptions = mUpdateDescriptions.split("~");
        String[] photos = mUpdatePhotos.split(",");
        List<UpdatePhoto> result = new ArrayList<>();
        for (int i = 0; i < dates.length; i++) {
            result.add(new UpdatePhoto(descriptions[i], photos[i], dates[i]));
        }
        return mPhotoArray = result.toArray(new UpdatePhoto[] {});
    }

    public int getDonorCount() {
        return mDonorCount;
    }

    public String getTitle() {
        return mName;
    }

    public LatLng getPosition() {
        return new LatLng(mLat, mLng);
    }

    public String getSnippet() {
        return mName;
    }
}
