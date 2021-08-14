package org.villagex.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.vipulasri.timelineview.TimelineView;
import com.squareup.picasso.Picasso;

import org.villagex.R;
import org.villagex.model.UpdatePhoto;

import java.util.HashMap;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> implements View.OnClickListener {
    private Context mContext;
    private UpdatePhoto[] mData;
    private HashMap<View, UpdatePhoto> mPhotoMap;

    public PhotoAdapter(UpdatePhoto[] data) {
        mData = data;
        mPhotoMap = new HashMap<>();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ImageView view = new ImageView(mContext);
        view.setOnClickListener(this);

        view.setLayoutParams(new ViewGroup.LayoutParams(parent.getHeight(),
                parent.getHeight()));
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Picasso.with(mContext).load(holder.getView().getResources().getString(R.string.base_url)
                        + holder.getView().getResources().getString(R.string.pictures_dir)
                + mData[position].getImageUrl()).into(holder.getView());
        mPhotoMap.put(holder.getView(), mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    @Override
    public void onClick(View view) {
        UpdatePhoto photo = mPhotoMap.get(view);
        Context context = view.getContext();
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(new UpdatePhotoView(context, photo), false).build();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0);
        dialog.show();
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
