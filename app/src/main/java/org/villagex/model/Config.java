package org.villagex.model;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("villages_path")
    private String mVillagesPath;

    @SerializedName("villages_version")
    private int mVillagesVersion;

    @SerializedName("projects_path")
    private String mProjectsPath;

    @SerializedName("projects_version")
    private int mProjectsVersion;

    public String getVillagesPath() {
        return mVillagesPath;
    }

    public int getVillagesVersion() {
        return mVillagesVersion;
    }

    public void setVillagesVersion(int version) {
        mVillagesVersion = version;
    }

    public String getProjectsPath() {
        return mProjectsPath;
    }

    public int getProjectsVersion() {
        return mProjectsVersion;
    }

    public void setProjectsVersion(int version) {
        mProjectsVersion = version;
    }

}
