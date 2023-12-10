package com.aga.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_CLEANUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_SHADOW;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LOADSCREEN_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LOADSCREEN_TEXT_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.SHADOW_LAYER_OFFSET;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.ASPECT_RATIO;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.BASE_SCREEN_DIMENSION;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.textureProgram;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;

import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

/**
 *
 * Created by Andrii Husiev on 18.02.2016.
 *
 */
public class ObjectBuildHelper {

    //<editor-fold desc="Bitmap Manipulations">
    public static Bitmap loadBitmap(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inScaled = false;
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }

    public static Bitmap createTiledBitmap(Context context, RectF rect, int id) {
        Bitmap b = Bitmap.createBitmap((int)rect.width(), (int)rect.height(), Bitmap.Config.ARGB_8888);
        BitmapDrawable bitmapBack = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), id));
        bitmapBack.setTileModeX(Shader.TileMode.REPEAT);
        bitmapBack.setTileModeY(Shader.TileMode.REPEAT);
        bitmapBack.setBounds(0, 0, (int)rect.width(), (int)rect.height());
        Canvas c = new Canvas(b);
        bitmapBack.draw(c);

        return b;
    }
    //</editor-fold>

    //<editor-fold desc="Text Manipulations">
    public static void setTextWithShader(String text, PointF textPos, Paint textPaint, Bitmap b, Shader shader) {
        Canvas canvas = new Canvas(b);

        canvas.drawText(text, textPos.x, textPos.y, textPaint);
        textPaint.setShadowLayer(0, 0, 0, 0);
        textPaint.setShader(shader);
        canvas.drawText(text, textPos.x, textPos.y, textPaint);
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
    //</editor-fold>

    //<editor-fold desc="PixelsToDeviceCoords">
    public static RectF pixelsToDeviceCoords(RectF src, RectF screenRect) {
        final float ASPECT_RATIO = screenRect.width() / screenRect.height();
        RectF dst = new RectF();

        dst.left = ((src.left / screenRect.width()) * 2 - 1) * ASPECT_RATIO;
        dst.right = ((src.right / screenRect.width()) * 2 - 1) * ASPECT_RATIO;
        dst.top = -((src.top / screenRect.height()) * 2 - 1);
        dst.bottom = -((src.bottom / screenRect.height()) * 2 - 1);

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

    public static float pixelsToDeviceCoords(float size, float screenSize) {
        return ((size / screenSize) * 2 - 1);
    }

    public static float velocityToDeviceCoords(float size, float screenSize) {
        return (size / screenSize) * 2;
    }
    //</editor-fold>

    //<editor-fold desc="Mix">
    public static BitmapShader getWoodShader(Context context) {
        Resources res = context.getResources();
        Bitmap bitmapWood = BitmapFactory.decodeResource(res, R.drawable.woodentexture);
        return new  BitmapShader(bitmapWood, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    public static boolean rectContainsPoint(RectF rect, float x, float y) {
        return x >= rect.left && x < rect.right && y <= rect.top && y > rect.bottom;
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

    public static Paint setPaint(float textSize, int color, boolean setShadow, Typeface typeface) {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setFilterBitmap(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setColor(color);
        if (setShadow)
            textPaint.setShadowLayer(2, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
        textPaint.setTypeface(typeface);

        return textPaint;
    }

    /**
     * Рассчитывает координаты прямоугольника, по данным параметрам.
     * @param base Параметр, в котором указываются опорные стороны экрана, чтобы можно было верно рассчитать
     *             необходимые данные. Может принимать следующие значения: <ul>
     *             <li>"topleft" - опорными сторонами являются верхний и левый край экрана.</li>
     *             <li>"topright" - опорными сторонами являются верхний и правый край экрана.</li>
     *             <li>"bottomleft" - опорными сторонами являются нижний и левый край экрана.</li>
     *             <li>"bottomright" - опорными сторонами являются нижний и правый край экрана.</li>
     *             <li>"centerheight" - прямоугольник находится по ширине в центре экрана, а для расчета
     *             отступа от верхнего края используется distanceFactor1. Если distanceFactor2 равен нулю,
     *             то будет использоваться ширина экрана.</li>
     *             <li>"centerwidth" - прямоугольник находится по высоте в центре экрана, а для расчета
     *             отступа от левого края используется distanceFactor1. При этом параметры должны передавать иные значения: <ul>
     *                  <li>distanceFactor1 - расстояние от левого края экрана в процентах от ширины.</li>
     *                  <li>distanceFactor2 - высота, центр которой используется в вычислениях. Если равно нулю,
     *                          то будет использоватся высота экрана.</li>
     *                  <li>heightFactor - процент от ширины экрана.</li>
     *                  <li>aspectRatio - соотношение сторон, должно быть вычислено по формуле: height / width.</li></ul></li></ul>
     * @param distanceFactor1 Первый коэффициент из параметра "base", измеряется в процентах от высоты экрана.
     * @param distanceFactor2 Второй коэффициент из параметра "base", измеряется в процентах от высоты экрана.
     * @param heightFactor Высота прямоугольника, измеряется в процентах от высоты экрана.
     * @param aspectRatio Соотношение сторон, должно быть вычислено по формуле: width / height.
     * @return Прямоугольник, содержащий рассчитанные координаты.
     */
    public static RectF getSizeAndPositionRectangle(String base, float distanceFactor1, float distanceFactor2, float heightFactor, float aspectRatio) {
        RectF calcRectF = new RectF();
        switch (base) {
            case "topleft":
                calcRectF.top = BASE_SCREEN_DIMENSION * distanceFactor1;
                calcRectF.bottom = BASE_SCREEN_DIMENSION * (distanceFactor1 + heightFactor);
                calcRectF.left = BASE_SCREEN_DIMENSION * distanceFactor2;
                calcRectF.right = calcRectF.left + BASE_SCREEN_DIMENSION * heightFactor * aspectRatio;
                break;
            case "topright":
                calcRectF.top = BASE_SCREEN_DIMENSION * distanceFactor1;
                calcRectF.bottom = BASE_SCREEN_DIMENSION * (distanceFactor1 + heightFactor);
                calcRectF.right = screenRect.width() - BASE_SCREEN_DIMENSION * distanceFactor2;
                calcRectF.left = calcRectF.right - BASE_SCREEN_DIMENSION * heightFactor * aspectRatio;
                break;
            case "bottomleft":
                calcRectF.bottom = BASE_SCREEN_DIMENSION * (1 - distanceFactor1);
                calcRectF.top = BASE_SCREEN_DIMENSION * (1 - distanceFactor1 - heightFactor);
                calcRectF.left = BASE_SCREEN_DIMENSION * distanceFactor2;
                calcRectF.right = calcRectF.left + BASE_SCREEN_DIMENSION * heightFactor * aspectRatio;
                break;
            case "bottomright":
                calcRectF.bottom = BASE_SCREEN_DIMENSION * (1 - distanceFactor1);
                calcRectF.top = BASE_SCREEN_DIMENSION * (1 - distanceFactor1 - heightFactor);
                calcRectF.right = screenRect.width() - BASE_SCREEN_DIMENSION * distanceFactor2;
                calcRectF.left = calcRectF.right - BASE_SCREEN_DIMENSION * heightFactor * aspectRatio;
                break;
            case "centerheight":
                calcRectF.top = BASE_SCREEN_DIMENSION * distanceFactor1;
                calcRectF.bottom = BASE_SCREEN_DIMENSION * (distanceFactor1 + heightFactor);
                if (distanceFactor2 == 0)
                    distanceFactor2 = screenRect.width() / BASE_SCREEN_DIMENSION;
                calcRectF.left = BASE_SCREEN_DIMENSION * (distanceFactor2 - heightFactor * aspectRatio) / 2f;
                calcRectF.right = calcRectF.left + BASE_SCREEN_DIMENSION * heightFactor * aspectRatio;
                break;
            case "centerwidth":
                calcRectF.left = screenRect.width() * distanceFactor1;
                calcRectF.right = screenRect.width() * (distanceFactor1 + heightFactor);
                if (distanceFactor2 == 0)
                    distanceFactor2 = BASE_SCREEN_DIMENSION / screenRect.width();
                calcRectF.top = screenRect.width() * (distanceFactor2 - heightFactor * aspectRatio) / 2f;
                calcRectF.bottom = calcRectF.top + screenRect.width() * heightFactor * aspectRatio;
        }
        return calcRectF;
    }

    public static TangramGLSquare setLoadScreen(Context context) {
        Bitmap b = Bitmap.createBitmap((int)screenRect.width(), (int)screenRect.height(), Bitmap.Config.ARGB_8888);
        String text = context.getResources().getString(R.string.app_name);//"LoadScreen";//
        PointF textPos = new PointF();

        Paint textPaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, false, Typeface.DEFAULT_BOLD);
        textPaint.setShader(getWoodShader(context));
        Bitmap textBitmap = createBitmapSizeFromText(text, textPaint, textPos, true);
        RectF textRectF = getSizeAndPositionRectangle("centerheight", LOADSCREEN_TEXT_OFFSET_FROM_TOP, 0, LOADSCREEN_TEXT_HEIGHT, (float) textBitmap.getWidth() / textBitmap.getHeight());
        Canvas canvas = new Canvas(textBitmap);
        canvas.drawText(text, textPos.x, textPos.y, textPaint);

        TangramGLSquare imageLoadingBg = new TangramGLSquare(b, ASPECT_RATIO, 1.0f);
        imageLoadingBg.drawColor(Color.BLACK);
        imageLoadingBg.addBitmap(textBitmap, textRectF);
        imageLoadingBg.castObjectSizeAutomatically();
        imageLoadingBg.setShader(textureProgram);
        imageLoadingBg.bitmapToTexture();
        imageLoadingBg.recycleBitmap();

        return imageLoadingBg;
    }
    //</editor-fold>

    //<editor-fold desc="Log Debug Out">
    private static final String APP_TAG = "Tangram: ";
    private static final String dot = ". ";
    private static final String colon = ": ";
//        private static final boolean SHOW_LOG = true;
    private static final boolean SHOW_LOG = true;

    public static void logDebugOut(String object, String message, int param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }

    public static void logDebugOut(String object, String message, float param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }

    public static void logDebugOut(String object, String message, boolean param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }

    public static void logDebugOut(String object, String message, String param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }
    //</editor-fold>
}
