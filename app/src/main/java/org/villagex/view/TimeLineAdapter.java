package org.villagex.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import org.villagex.R;
import org.villagex.model.TimeLineModel;
import org.villagex.util.DateTimeUtils;
import org.villagex.util.VectorDrawableUtils;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {
    private Context mContext;
    private TimeLineModel[] mData;

    public TimeLineAdapter(TimeLineModel[] data) {
        mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_timeline, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        TimeLineModel timeLineModel = mData[position];


        if(timeLineModel.getStatus() == TimeLineModel.TimeLineStatus.UNSTARTED) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.timeline_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getStatus() == TimeLineModel.TimeLineStatus.IN_PROGRESS) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.timeline_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.timeline_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

        if(timeLineModel.getDate() != null) {
            holder.mDateView.setVisibility(View.VISIBLE);
            holder.mDateView.setText(timeLineModel.getDate());
        } else {
            holder.mDateView.setVisibility(View.GONE);
        }

        holder.mMessageView.setText(timeLineModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }
}

class TimeLineViewHolder extends RecyclerView.ViewHolder {
    TextView mDateView;
    TextView mMessageView;
    public TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        mTimelineView = itemView.findViewById(R.id.time_marker);
        mDateView = itemView.findViewById(R.id.text_timeline_date);
        mMessageView = itemView.findViewById(R.id.text_timeline_title);
        mTimelineView.initLine(viewType);
    }

}
