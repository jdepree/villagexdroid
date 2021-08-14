package org.villagex.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Vipul on 28/12/16.
 */

public class VectorDrawableUtils {

    public static Drawable getDrawable(Context context, int drawableResId) {
        Drawable drawable;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            drawable = context.getResources().getDrawable(drawableResId, context.getTheme());
        } else {
            drawable = VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        }

        return drawable;
    }

    public static Drawable getDrawable(Context context, int drawableResId, int colorFilter) {
        Drawable drawable = getDrawable(context, drawableResId);
        drawable.setColorFilter(ContextCompat.getColor(context, colorFilter), PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}