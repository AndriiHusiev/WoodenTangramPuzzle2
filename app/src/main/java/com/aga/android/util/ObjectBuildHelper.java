package com.aga.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_CLEANUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_SHADOW;

/**
 *
 * Created by Andrii Husiev on 18.02.2016.
 *
 */
public class ObjectBuildHelper {

    public static Bitmap loadBitmap(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inScaled = false;
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
        x = textBounds.width() / 2f;
        y = textBounds.height() / 2f - textBounds.exactCenterY();
        c.drawText(text, x, y, textPaint);

        return b;
    }

    //<editor-fold desc="PixelsToDeviceCoords">
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

    public static float xPixelsToDeviceCoords(float x, RectF screenRect) {
        final float ASPECT_RATIO = screenRect.width() / screenRect.height();
        return ((x / screenRect.width()) * 2 - 1) * ASPECT_RATIO;
    }

    public static float[] xPixelsToDeviceCoords(int[] x, RectF screenRect) {
        final float ASPECT_RATIO = screenRect.width() / screenRect.height();
        int n = x.length;
        float[] xOut = new float[n];

        for (int i = 0; i < n; i++) {
            xOut[i] = ((x[i] / screenRect.width()) * 2 - 1) * ASPECT_RATIO;
        }

        return xOut;
    }

    public static float yPixelsToDeviceCoords(float y, RectF screenRect) {
        return -((y / screenRect.height()) * 2 - 1);
    }

    public static float[] yPixelsToDeviceCoords(int[] y, RectF screenRect) {
        int n = y.length;
        float[] yOut = new float[n];

        for (int i = 0; i < n; i++) {
            yOut[i] = -((y[i] / screenRect.height()) * 2 - 1);
        }

        return yOut;
    }
    //</editor-fold>

    public static Bitmap bitmapToShadow(Bitmap srcBitmap) {
        int color;
        Bitmap shadowButtonInLSS = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < srcBitmap.getWidth(); i++)
            for (int j = 0; j < srcBitmap.getHeight(); j++) {
                color = srcBitmap.getPixel(i, j);
                color |= COLOR_CLEANUP;
                color &= COLOR_SHADOW;
                shadowButtonInLSS.setPixel(i, j, color);
            }

        return shadowButtonInLSS;
    }

    public static boolean rectContainsPoint(RectF rect, float x, float y) {
        return x >= rect.left && x < rect.right && y <= rect.top && y > rect.bottom;
    }

    public static Bitmap createGradientBitmap(Bitmap srcBitmap, Resources res, float baseScreenDimension, float gradientOffsetFromTop, float gradientHeight, Rect bounds) {
        Bitmap gradientHeaderLSS = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(gradientHeaderLSS);
        c.drawColor(Color.TRANSPARENT);
        BitmapDrawable gradientHeaderDrawable = new BitmapDrawable(res, srcBitmap);
        gradientHeaderDrawable.setTileModeX(Shader.TileMode.REPEAT);
        gradientHeaderDrawable.setTileModeY(Shader.TileMode.REPEAT);
        gradientHeaderDrawable.setBounds(bounds);
        gradientHeaderDrawable.draw(c);
        int color, currentColor = 0xffffffff;
        int step = ((int) Math.ceil(255f / (baseScreenDimension * gradientHeight))) * 0x1000000;
        for (int i = (int) (baseScreenDimension * gradientOffsetFromTop); i < gradientHeaderLSS.getHeight(); i++) {
            for (int j = 0; j < gradientHeaderLSS.getWidth(); j++) {
                color = gradientHeaderLSS.getPixel(j, i);
                color &= currentColor;
                gradientHeaderLSS.setPixel(j, i, color);
            }
            if (currentColor > 0 && (currentColor - step) < 0)
                currentColor = 0x00ffffff;
            else
                currentColor -= step;
        }

        return gradientHeaderLSS;
    }

    public static int loadTexture(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];

        GLES20.glGenTextures(1, textureObjectIds, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
//        bitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
