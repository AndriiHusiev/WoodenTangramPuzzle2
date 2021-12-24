package com.aga.woodentangrampuzzle2.opengles20.screens;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_SHADOW;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_ON_BUTTONS;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INSENSITIVE_BACKLASH_ON_SCROLL;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVELS_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVEL_SET_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_BUTTON_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_GRADIENT_HEADER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_GRADIENT_HEADER_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_LOCK_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_OFFSET_FROM_TOP_DC;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LSS_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramObjectBuilder.createTiledBitmap;
import static com.aga.woodentangrampuzzle2.common.TangramObjectBuilder.getSizeAndPositionRectangle;
import static com.aga.woodentangrampuzzle2.common.TangramObjectBuilder.getWoodShader;
import static com.aga.woodentangrampuzzle2.common.TangramObjectBuilder.setMenusHeaderTextWithShader;
import static com.aga.woodentangrampuzzle2.common.TangramObjectBuilder.setPaint;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.ASPECT_RATIO;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.INSTANTIATED_LEVEL_SET_NUMBER;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.textureProgram;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.Mode;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLButton;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

/**
 *
 * Created by Andrii Husiev on 24.11.2021.
 * Этот объект отвечает за отображение и взаимодействие всех элементов
 * меню выбора набора уровней (Level Set Selection - LSS).
 *
 */

public class TangramGLLevelSetSelectionScreen {
    private static final String LEVEL_SET = "levels_set_";

    private Context context;
    private RectF screenRect;
    private PointF prevTouch;
    private boolean isStartScrolling;
    private int selectedLevelSet;
    private TangramGLSquare imageMenuBackground;
    private TangramGLSquare imageMenuHeader;
    private TangramGLSquare imageLockScreen;
    private TangramGLButton[] button;

    public TangramGLLevelSetSelectionScreen(Context context, RectF screenRect) {
        setLocalVariables(context, screenRect);
        setBackground();
        setHeader();
        setButtons();
        setLockScreen();
    }

    //<editor-fold desc="Initialization">
    private void setLocalVariables(Context context, RectF screenRect) {
        this.context = context;
        this.screenRect = new RectF();
        this.screenRect.left = screenRect.left;
        this.screenRect.top = screenRect.top;
        this.screenRect.right = screenRect.right;
        this.screenRect.bottom = screenRect.bottom;
        prevTouch = new PointF();
    }

    private void setBackground() {
        Bitmap b = createTiledBitmap(context, screenRect, R.drawable.maple_full);

        imageMenuBackground = new TangramGLSquare(b, ASPECT_RATIO, 1.0f);
        imageMenuBackground.castObjectSizeAutomatically();
        imageMenuBackground.bitmapToTexture(textureProgram);
        imageMenuBackground.recycleBitmap();
    }

    private void setHeader() {
        PointF textPos = new PointF();
        String text = context.getResources().getString(R.string.levels_set_title);

        Paint textPaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, true, Typeface.DEFAULT_BOLD);
        Bitmap textBitmap = createBitmapSizeFromText(text, textPaint, textPos, true);
        float aspectRatio = (float) textBitmap.getWidth() / textBitmap.getHeight();
        setMenusHeaderTextWithShader(text, textPos, textPaint, textBitmap, getWoodShader(context));
        RectF textRect = getSizeAndPositionRectangle("centerheight", LSS_TITLE_OFFSET_FROM_TOP, 0, LSS_TITLE_HEIGHT, aspectRatio);
        imageMenuHeader = setLSSHeader_Bg();
        imageMenuHeader.addBitmap(textBitmap, textRect);
        imageMenuHeader.castObjectSizeAutomatically();
        imageMenuHeader.bitmapToTexture(textureProgram);
        imageMenuHeader.recycleBitmap();
    }

    private TangramGLSquare setLSSHeader_Bg() {
        Resources res = context.getResources();
        float baseScreenDimension = screenRect.height();
        Rect bounds = new Rect();

        bounds.left = 0;
        bounds.right = (int)screenRect.width();
        bounds.top =  0;
        bounds.bottom = (int)(screenRect.height() * (LSS_GRADIENT_HEADER_OFFSET_FROM_TOP + LSS_GRADIENT_HEADER_HEIGHT));
        RectF rect = new RectF(bounds);
        Bitmap srcBitmap = ObjectBuildHelper.loadBitmap(context, R.drawable.maple_full);
        Bitmap gradientHeaderLSS = ObjectBuildHelper.createGradientBitmap(srcBitmap, res, baseScreenDimension, LSS_GRADIENT_HEADER_OFFSET_FROM_TOP, LSS_GRADIENT_HEADER_HEIGHT, bounds);

        return new TangramGLSquare(gradientHeaderLSS, ObjectBuildHelper.pixelsToDeviceCoords(rect, screenRect));
    }

    private void setButtons() {
        RectF buttonRect;
        RectF lockRect = new RectF();
        Paint textPaint;
        Resources res = context.getResources();
        Bitmap b = ObjectBuildHelper.loadBitmap(context, R.drawable.button02);
        Rect bitmapRect = new Rect(0, 0, b.getWidth(), b.getHeight());
        float aspectRatio, lockSize;
        // В целях сокращения объема конечного файла apk мы вместо добавления в ресурсы
        // файлов с затемнением кнопок просто вручную создаем эти изображения.
        Bitmap shadowBitmap = ObjectBuildHelper.bitmapToShadow(b);
        Bitmap lockBitmap = BitmapFactory.decodeResource(res, R.drawable.lock);
        String title;

        // General settings for all LSS buttons.
        textPaint = setPaint(ALL_FONTS_SIZE, COLOR_TEXT_ON_BUTTONS, true, Typeface.DEFAULT_BOLD);
        lockSize = b.getHeight() * LSS_LOCK_SIZE;
        lockRect.left = (float) b.getWidth() / 2f - lockSize / 2f;
        lockRect.right = lockRect.left + lockSize;
        lockRect.top = (float) b.getHeight() / 2f - lockSize / 2f;
        lockRect.bottom = lockRect.top + lockSize;

        button = new TangramGLButton[LEVEL_SET_NUMBER];
        // Creating each LSS button.
        aspectRatio = (float) b.getWidth()/b.getHeight();
        buttonRect = getSizeAndPositionRectangle("centerheight", LSS_BUTTON_OFFSET_FROM_TOP, 0, LSS_BUTTON_HEIGHT, aspectRatio);
        button[0] = new TangramGLButton(Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ARGB_8888), ObjectBuildHelper.pixelsToDeviceCoords(buttonRect, screenRect));
        button[0].addBitmap(b, bitmapRect);
        button[0].setLocked(false);
        // Text Bitmap
        title = getButtonTitle(context, 0);
        button[0].drawText(title, LSS_BUTTON_TEXT_HEIGHT,textPaint);
        // Finalize button
        button[0].castObjectSizeAutomatically();
        button[0].bitmapToTexture(textureProgram);
        button[0].recycleBitmap();

        for (int i = 1; i < LEVEL_SET_NUMBER; i++) {
//            aspectRatio = (float) b.getWidth()/b.getHeight();
            buttonRect = getSizeAndPositionRectangle("centerheight", LSS_BUTTON_OFFSET_FROM_TOP + LSS_BUTTON_HEIGHT * i + LSS_BUTTON_GAP * i, 0, LSS_BUTTON_HEIGHT, aspectRatio);
            button[i] = new TangramGLButton(Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ARGB_8888), ObjectBuildHelper.pixelsToDeviceCoords(buttonRect, screenRect));
            button[i].addBitmap(b, bitmapRect);

            // Text Bitmap
            title = getButtonTitle(context, i);
            button[i].drawText(title, LSS_BUTTON_TEXT_HEIGHT, textPaint);

            if (isPrevLevelsSetSolved(context, i))
                button[i].setLocked(false);
            else {
                button[i].setLocked(true);
                button[i].addBitmap(shadowBitmap, bitmapRect);
                button[i].addBitmap(lockBitmap, lockRect);
            }

            button[i].castObjectSizeAutomatically();
            button[i].bitmapToTexture(textureProgram);
            button[i].recycleBitmap();
        }

        b.recycle();
        lockBitmap.recycle();
        shadowBitmap.recycle();
    }

    private static String getButtonTitle(Context context, int i) {
        String str = LEVEL_SET + i;
        int id = context.getResources().getIdentifier(str, "string", context.getPackageName());
        return context.getResources().getString(id);
    }

    private void setLockScreen() {
        Paint textPaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, false, Typeface.DEFAULT_BOLD);
        PointF textPos = new PointF();
        String text = context.getResources().getString(R.string.ls_lockscreen_text);

        imageLockScreen = setLockScreen_Bg(text, textPaint, textPos, screenRect);
        imageLockScreen.addText(text, textPos.x, textPos.y, textPaint);
        imageLockScreen.castObjectSizeAutomatically();
        imageLockScreen.bitmapToTexture(textureProgram);
        imageLockScreen.recycleBitmap();
    }

    private static TangramGLSquare setLockScreen_Bg(String text, Paint textPaint, PointF textPos, RectF screenRect) {
        Bitmap b = Bitmap.createBitmap((int) screenRect.width(), (int) screenRect.height(), Bitmap.Config.ARGB_8888);
        Rect bounds = new Rect();
        // Координаты центра битмапа - для будущего рисования текста.
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        textPos.x = screenRect.width() / 2f;
        textPos.y = screenRect.height() / 2f - bounds.exactCenterY();

        TangramGLSquare imageLockScreen = new TangramGLSquare(b, ASPECT_RATIO, 1.0f);
        imageLockScreen.drawColor(COLOR_SHADOW);

        return imageLockScreen;
    }
    //</editor-fold>

    //<editor-fold desc="Touch">
    public Mode touch(float normalizedX, float normalizedY, int motionEvent) {
        Mode playMode = Mode.LEVELS_SET_SELECTION;
        switch (motionEvent) {
            case MotionEvent.ACTION_DOWN:
                pressSelectedButton(normalizedX, normalizedY);
                startScroll(normalizedY);
                return playMode;
            case MotionEvent.ACTION_MOVE:
                updateScroll(normalizedY);
                return playMode;
            case MotionEvent.ACTION_UP:
                unpressAllButtons();
                // Какой-то непонятный баг - если быстро скроллить, то почему-то после выхода из функции по break
                // мы снова заходим в нее и проходим сквозь всю ветку ACTION_UP.
                if (isStartScrolling){
                    isStartScrolling = false;
                    break;
                }
                if (normalizedY > LSS_OFFSET_FROM_TOP_DC)
                    break;
                for (int i = 0; i < INSTANTIATED_LEVEL_SET_NUMBER; i++) {
                    if (ObjectBuildHelper.rectContainsPoint(button[i].getDstRect(), normalizedX, normalizedY)) {
                        if (isPrevLevelsSetSolved(context, i)) {
                            playMode = Mode.LOCK_LSS_TOUCH;
                            selectedLevelSet = i;
                        }
                        break;
                    }
                }
        }
        return playMode;
    }

    private void pressSelectedButton(float normalizedX, float normalizedY) {
        for (int i = 0; i < LEVEL_SET_NUMBER; i++) {
            if (ObjectBuildHelper.rectContainsPoint(button[i].getDstRect(), normalizedX, normalizedY)) {
                button[i].setPressed(true);
                break;
            }
        }
    }

    private void unpressAllButtons() {
        for (int i = 0; i < LEVEL_SET_NUMBER; i++)
            button[i].setPressed(false);
    }

    private void startScroll(float normalizedY) {
        prevTouch.y = normalizedY;
    }

    private void updateScroll(float normalizedY) {
        float dy = normalizedY - prevTouch.y;
        prevTouch.y = normalizedY;

        // Limitation for scrolling
        if (Math.abs(dy) < INSENSITIVE_BACKLASH_ON_SCROLL )
            return;
        dy = upperBoundOfScroll(dy);
        dy = lowerBoundOfScroll(dy);

        for (TangramGLButton t: button)
            t.offset(0, dy);

        isStartScrolling = true;
    }

    private float upperBoundOfScroll(float dy) {
        if ((button[0].getDstRect().top + dy) < LSS_OFFSET_FROM_TOP_DC)
            return LSS_OFFSET_FROM_TOP_DC - button[0].getDstRect().top;
        return dy;
    }

    private float lowerBoundOfScroll(float dy) {
        if ((button[LEVEL_SET_NUMBER-1].getDstRect().bottom + dy) > (LSS_BUTTON_GAP - 1))
            return (LSS_BUTTON_GAP - 1) - button[LEVEL_SET_NUMBER-1].getDstRect().bottom;
        return dy;
    }

    public int getSelectedLevelSet() {
        return selectedLevelSet;
    }

    private static boolean isPrevLevelsSetSolved(Context context, int selectedLevelSet) {
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

    private static void loadData(Context context, int selectedLevelSet, long[] timer, int[] cup) {
        int id;
        String str;
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
            }
        }
        catch (Exception ex) {
            Log.d("debug","loadData. Exception: " + ex.getMessage());
        }
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix, Mode playMode) {
        imageMenuBackground.draw(projectionMatrix);
        for (TangramGLButton t: button)
            t.draw(projectionMatrix);
        imageMenuHeader.draw(projectionMatrix);
        if (playMode == Mode.LOCK_LSS_TOUCH)
            imageLockScreen.draw(projectionMatrix);

    }

}
