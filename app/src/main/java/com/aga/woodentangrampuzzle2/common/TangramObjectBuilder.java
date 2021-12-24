package com.aga.woodentangrampuzzle2.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLButton;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelBackground;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelButtons;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelForeground;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelTiles;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelTimer;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.BRONZE_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_LEVEL_BG;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_SHADOW;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_INGAME_HEADER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_ON_BUTTONS;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.GOLDEN_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_ANGULAR_HEADER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_CUP_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_CUP_OFFSET_FROM_RIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_HEADER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_HEADER_SHADOW_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE0_OFFSET_X;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE0_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE1_OFFSET_X;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE1_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE2_OFFSET_X;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE2_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE3_OFFSET_X;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE3_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE4_OFFSET_X;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE4_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE5_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE6_OFFSET_Y;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILES_OFFSET_FROM_RIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TIMER_DIGITS_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TIMER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TIMER_OFFSET_FROM_LEFT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TIMER_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVELS_IN_THE_ROW;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVELS_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVEL_SET_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LOADSCREEN_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LOADSCREEN_TEXT_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_GRADIENT_HEADER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_GRADIENT_HEADER_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_LOCK_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_TIMER_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_TIMER_TEXT_OFFSET;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTON_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTON_WIDTH;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_GRADIENT_HEADER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_GRADIENT_HEADER_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_LOCK_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_PREVIEW_BITMAP_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_PREVIEW_PATH_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.SHADOW_LAYER_OFFSET;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.SILVER_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TILES_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.digitalTF;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.ASPECT_RATIO;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.BASE_SCREEN_DIMENSION;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.textureProgram;

/**
 *
 * Created by Andrii Husiev on 08.11.2016.
 *
 */

public class TangramObjectBuilder {

    //<editor-fold desc="Set Load Screen">
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
        imageLoadingBg.bitmapToTexture(textureProgram);
        imageLoadingBg.recycleBitmap();

        return imageLoadingBg;
    }
    //</editor-fold>

    //<editor-fold desc="Save-Load Data">
    private static void loadData(Context context, int selectedLevelSet, long[] timer, int[] cup) {
        int id;
        String str, strTime;
        Resources res = context.getResources();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            Log.d("debug","loadData.");

            for (int i = 0; i < LEVELS_NUMBER; i++) {
                str = "set" + selectedLevelSet + "_level_cup_" + i;
                id = res.getIdentifier(str, "string", context.getPackageName());
                cup[i] = sharedPref.getInt(res.getString(id), 0);

                str = "set" + selectedLevelSet + "_level_time_" + i;
                id = res.getIdentifier(str, "string", context.getPackageName());
                timer[i] = sharedPref.getLong(res.getString(id), 0);
//                strTime = sharedPref.getString(res.getString(id), "-:-");
//                timer[i] = strTime;
            }
        }
        catch (Exception ex) {
            Log.d("debug","loadData. Exception: " + ex.getMessage());
        }
    }

    public static long loadDataTimer(Context context, int selectedLevelSet, int level) {
        int id;
        long timer = 0;
        String str;
        Resources res = context.getResources();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            Log.d("debug","loadDataTimer.");

            str = "set" + selectedLevelSet + "_level_time_" + level;
            id = res.getIdentifier(str, "string", context.getPackageName());
            timer = sharedPref.getLong(res.getString(id), 0);
        }
        catch (Exception ex) {
            Log.d("debug","loadDataTimer. Exception: " + ex.getMessage());
        }

        return timer;
    }

    public static int loadDataCup(Context context, int selectedLevelSet, int level) {
        int id;
        int cup = 0;
        String str;
        Resources res = context.getResources();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            Log.d("debug","loadDataCup.");

            str = "set" + selectedLevelSet + "_level_cup_" + level;
            id = res.getIdentifier(str, "string", context.getPackageName());
            cup = sharedPref.getInt(res.getString(id), 0);
        }
        catch (Exception ex) {
            Log.d("debug","loadDataCup. Exception: " + ex.getMessage());
        }

        return cup;
    }

    /**
     * Saving data of all levels in selected set.
     * Recommend to use if app suddenly being destroyed by the system (I hope it helps :-)).
     */
    public static void saveData(Context context, int selectedLevelSet, long[] timer, int[] cup) {
        int id;
        Resources res = context.getResources();
        SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("debug","saveData.");

        try {
            for (int i = 0; i < LEVELS_NUMBER; i++) {
                id = res.getIdentifier("set" + selectedLevelSet + "_level_cup_" + i, "string", context.getPackageName());
                editor.putInt(res.getString(id), cup[i]);

                id = res.getIdentifier("set" + selectedLevelSet + "_level_time_" + i, "string", context.getPackageName());
                editor.putLong(res.getString(id), timer[i]);
            }
        }
        catch (Exception ex) {
            Log.d("debug","loadData. Exception: " + ex.getMessage());
        }

        editor.apply();
    }

    public static void saveData(Context context, int selectedLevelSet, int level, long timer, int cup) {
        int id;
        Resources res = context.getResources();
        SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        id = res.getIdentifier("set" + selectedLevelSet + "_level_cup_" + level, "string", context.getPackageName());
        editor.putInt(res.getString(id), cup);
        id = res.getIdentifier("set" + selectedLevelSet + "_level_time_" + level, "string", context.getPackageName());
        editor.putLong(res.getString(id), timer);
        editor.apply();
        Log.d("debug","saveData. Save data of level No" + level);
    }
    //</editor-fold>

    //<editor-fold desc="Mix">
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

    public static void setMenusHeaderTextWithShader(String text, PointF textPos, Paint textPaint, Bitmap b, Shader shader) {
        Canvas canvas = new Canvas(b);

        canvas.drawText(text, textPos.x, textPos.y, textPaint);
        textPaint.setShadowLayer(0, 0, 0, 0);
        textPaint.setShader(shader);
        canvas.drawText(text, textPos.x, textPos.y, textPaint);
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

    public static BitmapShader getWoodShader(Context context) {
        Resources res = context.getResources();
        Bitmap bitmapWood = BitmapFactory.decodeResource(res, R.drawable.woodentexture);
        return new  BitmapShader(bitmapWood, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
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

    public static boolean isPrevLevelsSetSolved(Context context, int selectedLevelSet) {
        if (selectedLevelSet == 0)
            return true;

        long[] timer = new long[LEVELS_NUMBER];
        int[] cup = new int[LEVELS_NUMBER];
        boolean result = true;

        loadData(context, selectedLevelSet, timer, cup);
        for (int i = 0; i < LEVELS_NUMBER; i++) {
            result &= (cup[i] > 0);
        }

        return result;
    }

    //</editor-fold>
}
