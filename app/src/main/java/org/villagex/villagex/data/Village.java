package org.villagex.villagex.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;


public class Village implements ClusterItem {
    @SerializedName("village_id")
    private int mId;

    @SerializedName("village_name")
    private String mName;

    @SerializedName("village_lat")
    private float mLat;

    @SerializedName("village_lng")
    private float mLng;

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
        return String.valueOf(mId);
    }

    public LatLng getPosition() {
        return new LatLng(mLat, mLng);
    }

    public String getSnippet() {
        return mName;
    }
}
