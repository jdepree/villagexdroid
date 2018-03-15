package org.villagex.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.villagex.R;
import org.villagex.model.UpdatePhoto;

public class UpdatePhotoView extends LinearLayout {
    public UpdatePhotoView(Context context, UpdatePhoto photo) {
        super(context);
        init(context, photo);
    }

    private void init(Context context, UpdatePhoto photo) {
        setOrientation(VERTICAL);
        setPadding(25, 25, 25, 25);
        setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_transparent_background));

        inflate(context, R.layout.update_photo_layout, this);
        ((TextView) findViewById(R.id.update_photo_caption_view)).setText(
                ("0".equals(photo.getTitle()) ? "" : photo.getTitle() + "\n\n") + photo.getDate());
        Picasso.with(context).load(getResources().getString(R.string.base_url)
                + getResources().getString(R.string.pictures_dir)
                + photo.getImageUrl()).into((ImageView) findViewById(R.id.update_photo_image_view));
    }
}
