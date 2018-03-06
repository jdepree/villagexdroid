package org.villagex.villagex.data;

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

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public float getLat() {
        return mLat;
    }

    public float getLng() {
        return mLng;
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

    public int getVillageId() {
        return mVillageId;
    }

    public String getType() {
        return mType;
    }
}
