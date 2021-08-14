package org.villagex.model;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("villages_version")
    private int mVillagesVersion;

    @SerializedName("projects_version")
    private int mProjectsVersion;

    public int getVillagesVersion() {
        return mVillagesVersion;
    }

    public void setVillagesVersion(int version) {
        mVillagesVersion = version;
    }

    public int getProjectsVersion() {
        return mProjectsVersion;
    }

    public void setProjectsVersion(int version) {
        mProjectsVersion = version;
    }

}
