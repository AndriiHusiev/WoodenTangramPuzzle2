package com.aga.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 *
 * Created by Andrii Husiev on 18.02.2016.
 *
 */
public class TextureHelper {
    private static final int COLOR_SHADOW = 0x80242424;
    private static final int COLOR_CLEANUP = 0x00ffffff;

    public static Bitmap loadBitmap(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inMutable = true;
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }

    public static Bitmap textToBitmap(String text, Paint textPaint) {
        Bitmap b;
        Canvas c;
        Rect textBounds = new Rect();
        float x, y;

        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        b = Bitmap.createBitmap(textBounds.width(), textBounds.height(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        c.drawColor(Color.TRANSPARENT);
        x = textBounds.width() / 2;
        y = textBounds.height() / 2 - textBounds.exactCenterY();
        c.drawText(text, x, y, textPaint);

        return b;
    }

    public static RectF pixelsToDeviceCoords(RectF src, RectF screenRect) {
        final float ASPECT_RATIO = screenRect.width() / screenRect.height();
        RectF dst = new RectF();

        dst.left = ((src.left / screenRect.width()) * 2 - 1) * ASPECT_RATIO;
        dst.right = ((src.right / screenRect.width()) * 2 - 1) * ASPECT_RATIO;
        dst.top = -((src.top / screenRect.height()) * 2 - 1);
        dst.bottom = -((src.bottom / screenRect.height()) * 2 - 1);
//        Log.d("debug","pixelsToDeviceCoords. dst == " + dst.left + " - " + dst.top + " - " + dst.right + " - " + dst.bottom);
//        float height = -((src.height() / screenRect.height()) * 2 - 1);
//        Log.d("debug","pixelsToDeviceCoords. width == " + width + ". height == " + height);

        return dst;
    }

    public static Bitmap bitmapToShadow(Bitmap srcBitmap) {
        int color;
        Bitmap shadowButtonInLSS = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(shadowButtonInLSS);
        c.drawColor(Color.TRANSPARENT);

        for (int i = 0; i < srcBitmap.getWidth(); i++)
            for (int j = 0; j < srcBitmap.getHeight(); j++) {
                color = srcBitmap.getPixel(i, j);
                color |= COLOR_CLEANUP;
                color &= COLOR_SHADOW;
                shadowButtonInLSS.setPixel(i, j, color);
            }

        return shadowButtonInLSS;
    }

    public static int loadTexture(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];

        GLES20.glGenTextures(1, textureObjectIds, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
