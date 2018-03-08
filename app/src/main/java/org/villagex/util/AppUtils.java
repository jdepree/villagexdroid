package org.villagex.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class AppUtils {
    public static BitmapDescriptor buildLabeledIcon(Context context, int baseIconRes, String labelText) {
        Bitmap base = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), baseIconRes));
        Bitmap result = base.copy(base.getConfig(), true);
        Canvas canvas = new Canvas(result);

        TextPaint paint = new TextPaint();

        Rect bounds = new Rect();
        float initialTextSize = 32f;
        paint.setTextSize(initialTextSize);
        paint.getTextBounds(labelText, 0, labelText.length(), bounds);
        paint.setTextSize(initialTextSize * Math.max(1, canvas.getWidth() / bounds.width()));

        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelText, canvas.getWidth() / 2, canvas.getHeight(), paint);
        canvas.save();
        return BitmapDescriptorFactory.fromBitmap(result);
    }
}
