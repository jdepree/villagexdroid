package org.villagex.model;

import android.content.ContentValues;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("dates")
    private String mDates;

    @SerializedName("event_labels")
    private String mEventLabels;

    @SerializedName("donor_count")
    private int mDonorCount;

    private Village mVillage;

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

    public String[] getDates() {
        if (mDates == null) {
            return null;
        }
        return mDates.split(",");
    }

    public String[] getEventLabels() {
        if (mEventLabels == null) {
            return null;
        }
        return mDates.split(",");
    }

    public UpdatePhoto[] getPhotos() {
        return new UpdatePhoto[] {};
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
