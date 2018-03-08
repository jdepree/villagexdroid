package org.villagex.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeLineModel implements Parcelable {
    public enum TimeLineStatus {
        UNSTARTED,
        IN_PROGRESS,
        COMPLETED
    };

    private String mMessage;
    private String mDate;
    private TimeLineStatus mStatus;

    public TimeLineModel() {
    }

    public TimeLineModel(String mMessage, String mDate, TimeLineStatus status) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getDate() {
        return mDate;
    }

    public TimeLineStatus getStatus() {
        return mStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMessage);
        dest.writeString(this.mDate);
    }

    protected TimeLineModel(Parcel in) {
        this.mMessage = in.readString();
        this.mDate = in.readString();
    }

    public static final Creator<TimeLineModel> CREATOR = new Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };
}