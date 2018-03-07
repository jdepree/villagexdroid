package org.villagex.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;


public class Village implements ClusterItem {
    @SerializedName("village_id")
    private int mId;

    @SerializedName("village_name")
    private String mName;

    @SerializedName("village_lat")
    private float mLat;

    @SerializedName("village_lng")
    private float mLng;

    private List<Project> mProjects;

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

    public String getTitle() {
        return String.valueOf(mId);
    }

    public LatLng getPosition() {
        return new LatLng(mLat, mLng);
    }

    public String getSnippet() {
        return mName;
    }

    public void setProjects(List<Project> projects) {
        mProjects = projects;
    }

    public List<Project> getProjects() {
        return mProjects;
    }
}
