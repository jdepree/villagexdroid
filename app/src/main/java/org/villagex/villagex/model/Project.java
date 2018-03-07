package org.villagex.villagex.model;

import android.content.ContentValues;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

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
