package org.villagex.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.vipulasri.timelineview.TimelineView;
import com.squareup.picasso.Picasso;

import org.villagex.model.UpdatePhoto;

import java.util.HashMap;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<UpdatePhoto> mData;
    private HashMap<View, UpdatePhoto> mPhotoMap;

    public PhotoAdapter(List<UpdatePhoto> data) {
        mData = data;
        mPhotoMap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ImageView view = new ImageView(mContext);
        view.setOnClickListener(this);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Picasso.with(mContext).load(mData.get(position).getThumbUrl()).into(holder.getView());
        mPhotoMap.put(holder.getView(), mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View view) {
        UpdatePhoto photo = mPhotoMap.get(view);
        Context context = view.getContext();
        new MaterialDialog.Builder(context)
                .customView(new UpdatePhotoView(context, photo), false)
                .backgroundColorRes(android.R.color.transparent)
                .show();
    }
}

class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView mView;
    public PhotoViewHolder(ImageView view) {
        super(view);
        mView = view;
    }

    public ImageView getView() {
        return mView;
    }
}
