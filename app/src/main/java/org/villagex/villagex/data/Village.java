package org.villagex.villagex.data;

import com.google.gson.annotations.SerializedName;

public class Village {
    @SerializedName("name")
    private String mName;

    @SerializedName("lat")
    private float mLat;

    @SerializedName("lng")
    private float mLng;

    public String getName() {
        return mName;
    }

    public float getLat() {
        return mLat;
    }

    public float getLng() {
        return mLng;
    }
}
