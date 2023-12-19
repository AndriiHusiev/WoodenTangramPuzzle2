package com.aga.woodentangrampuzzle2.opengles20.screens;

import static android.graphics.Bitmap.createBitmap;
import static com.aga.android.util.ObjectBuildHelper.createTiledBitmap;
import static com.aga.android.util.ObjectBuildHelper.getSizeAndPositionRectangle;
import static com.aga.android.util.ObjectBuildHelper.getWoodShader;
import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.loadBitmap;
import static com.aga.android.util.ObjectBuildHelper.pixelsToDeviceCoords;
import static com.aga.android.util.ObjectBuildHelper.setPaint;
import static com.aga.android.util.ObjectBuildHelper.setTextWithShader;
import static com.aga.android.util.ObjectBuildHelper.velocityToDeviceCoords;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.BRONZE_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_LEVEL_BG;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_SHADOW;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.GOLDEN_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INSENSITIVE_BACKLASH_ON_SCROLL;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVELS_IN_THE_ROW;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LEVELS_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_OFFSET_FROM_TOP_DC;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_TIMER_HOFFSET;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_TIMER_SCALE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTONS_TIMER_VOFFSET;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTON_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_BUTTON_WIDTH;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_CUP_OFFSET;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_CUP_SCALE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_GRADIENT_HEADER_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_GRADIENT_HEADER_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_LOCK_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_PREVIEW_BITMAP_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_PREVIEW_PATH_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.LS_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.SCROLLING_ANIMATION_DURATION;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.SILVER_CUP;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.ASPECT_RATIO;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.BASE_SCREEN_DIMENSION;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.aGradientProgram;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.desaturationProgram;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.reuse;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.textureProgram;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.Mode;
import static com.aga.woodentangrampuzzle2.opengles20.screens.TangramGLLevelScreen.loadLevelPathByNumber;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.common.TangramAnimator;
import com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLButton;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLButtonExt;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelBackground;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelTimer;

public class TangramGLLevelSelectionScreen {
    private static final String TAG = "TangramGLLevelSelectionScreen";
    private static final String LEVEL_SET = "levels_set_";
    private static final String LEVEL_CUP = "_level_cup_";
    private static final String LEVEL_TIME = "_level_time_";
    private static final String SET = "set";
    private static final String DEF_TYPE_STRING = "string";

    private Context context;
    private RectF screenRect;
    private float prevTouch;
    private boolean isStartScrolling;
    private int selectedLevelSet, selectedLevel;
    private TangramAnimator animator;
    private TangramGLSquare imageMenuBackground;
    private TangramGLSquare imageMenuHeader;
    private TangramGLSquare imageLockScreen;
    private TangramGLButtonExt[] button;

    public TangramGLLevelSelectionScreen(Context context, RectF screenRect, int selectedLevelSet) {
        setLocalVariables(context, screenRect, selectedLevelSet);
        setBackground();
        setHeader();
        setButtons();
        setLockScreen();
    }

    //<editor-fold desc="Initialization">
    private void setLocalVariables(Context context, RectF scr, int selectedLevelSet) {
        this.context = context;
        this.screenRect = new RectF(scr.left, scr.top, scr.right, scr.bottom);
        this.selectedLevelSet = selectedLevelSet;
        animator = new TangramAnimator();
    }

    private void setBackground() {
        Bitmap b = createTiledBitmap(context, screenRect, reuse.bitmapMaple);

        imageMenuBackground = new TangramGLSquare(b, ASPECT_RATIO, 1.0f);
        imageMenuBackground.castObjectSizeAutomatically();
        imageMenuBackground.setShader(textureProgram);
        imageMenuBackground.bitmapToTexture();
        imageMenuBackground.recycleBitmap();
    }

    private void setHeader() {
        PointF textPos = new PointF();
        String text = getLSHeaderText(context, selectedLevelSet);
        Paint textPaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, true, Typeface.DEFAULT_BOLD);

        Bitmap textBitmap = createBitmapSizeFromText(text, textPaint, textPos, true);
        float aspectRatio = (float) textBitmap.getWidth() / textBitmap.getHeight();
        setTextWithShader(text, textPos, textPaint, textBitmap, getWoodShader());
        RectF textRectF = getSizeAndPositionRectangle("centerheight", LS_TITLE_OFFSET_FROM_TOP, 0, LS_TITLE_HEIGHT, aspectRatio);
        imageMenuHeader = setLSHeader_Bg(context, screenRect);
        imageMenuHeader.addBitmap(textBitmap, textRectF);
        imageMenuHeader.castObjectSizeAutomatically();
        imageMenuHeader.bitmapToTexture();
        imageMenuHeader.recycleBitmap();
    }

    private void setButtons() {
        Bitmap b = loadBitmap(context, R.drawable.button03);
        Bitmap lockBitmap = ObjectBuildHelper.loadBitmap(context, R.drawable.lock);
        RectF[] rectButtons = new RectF[LEVELS_NUMBER];
        float buttonRatio = (float) b.getHeight() / b.getWidth();
        float yOffsetCup = BASE_SCREEN_DIMENSION * LS_CUP_OFFSET;
        float yScaleCup = BASE_SCREEN_DIMENSION * LS_CUP_SCALE;
        int[] cup = new int[LEVELS_NUMBER];
        long[] timer = new long[LEVELS_NUMBER];
        loadData(context, selectedLevelSet, timer, cup);

        // Creating each LS button.
        // Последовательно определяем все необходимые данные кнопок LS.
        // ------ Первая кнопка ------
        // Сложность в том, что первая кнопка находится не в центре по горизонтали (как в LSS),
        // а левее на (2 * LS_BUTTON_SIZE) + (LS_BUTTON_GAP * 1.5).
        rectButtons[0] = new RectF();
        rectButtons[0].left = screenRect.width()/2 - BASE_SCREEN_DIMENSION * (LS_BUTTON_WIDTH * 2f + LS_BUTTON_GAP * 1.5f);
        rectButtons[0].right = rectButtons[0].left + BASE_SCREEN_DIMENSION * LS_BUTTON_WIDTH;
        rectButtons[0].top = LS_BUTTONS_OFFSET_FROM_TOP * BASE_SCREEN_DIMENSION;
        rectButtons[0].bottom = rectButtons[0].top + BASE_SCREEN_DIMENSION * LS_BUTTON_WIDTH * buttonRatio;

        button = new TangramGLButtonExt[LEVELS_NUMBER];
        button[0] = new TangramGLButtonExt(screenRect);
        button[0].setLocked(false);
        button[0].addTexture(b, rectButtons[0], false);
        setButtonTimer(button[0], timer[0]);
        button[0].addTexture(setButtonCup(cup[0]), 0, yOffsetCup, yScaleCup, true);
        button[0].addEnabledTexture(setButtonPreviews(context, selectedLevelSet, 0, b, screenRect), 0, 0, 1, true);
        button[0].setShaders(textureProgram, desaturationProgram);

        // Первый ряд кнопок. Ориентиром является первая кнопка.
        for (int i = 1; i < LEVELS_IN_THE_ROW; i++) {
            rectButtons[i] = setButtonPositionFirstRow(rectButtons, i);
            button[i] = new TangramGLButtonExt(screenRect);
            button[i].setLocked(false);
            button[i].addTexture(b, rectButtons[i], false);
            setButtonTimer(button[i], timer[i]);
            button[i].addTexture(setButtonCup(cup[i]), 0, yOffsetCup, yScaleCup, true);
            button[i].addEnabledTexture(setButtonPreviews(context, selectedLevelSet, i, b, screenRect), 0, 0, 1, true);
            button[i].setShaders(textureProgram, desaturationProgram);
        }

        // Все последующие кнопки. Ориентиром является первый ряд.
        for (int i = LEVELS_IN_THE_ROW; i < LEVELS_NUMBER; i++) {
            rectButtons[i] = setButtonPositionOtherRows(rectButtons, buttonRatio, i);
            button[i] = new TangramGLButtonExt(screenRect);
            button[i].setLocked(true);
            button[i].addTexture(b, rectButtons[i], false);
            setButtonTimer(button[i], timer[i]);
            button[i].addTexture(setButtonCup(cup[i]), 0, yOffsetCup, yScaleCup, true);
            button[i].addEnabledTexture(setButtonPreviews(context, selectedLevelSet, i, b, screenRect), 0, 0, 1, true);
            button[i].addDisabledTexture(lockBitmap, 0, 0, LS_LOCK_SIZE * BASE_SCREEN_DIMENSION, false);
            button[i].setShaders(textureProgram, desaturationProgram);
        }

        b.recycle();
        lockBitmap.recycle();
    }

    /**
     * При каждом выходе из игрового уровня необходимо обновить данные, отображаемые на кнопке.
     * Это происходит путем повторного создания объекта кнопки с нуля.
     * @param timer Время, проведенное на уровне.
     * @param cup Номер полученного кубка.
     */
    public void updateButton(long timer, int cup) {
        int[] t = TangramGLLevelTimer.convertElapsedTime(timer);
        button[selectedLevel].replaceTexture(1, reuse.digits[t[0]]);
        button[selectedLevel].replaceTexture(2, reuse.digits[t[1]]);
        button[selectedLevel].replaceTexture(3, reuse.digits[t[2]]);
        button[selectedLevel].replaceTexture(4, reuse.digits[t[3]]);

        button[selectedLevel].replaceTexture(6, setButtonCup(cup));
    }

    private void setLockScreen() {
        Paint textPaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, false, Typeface.DEFAULT_BOLD);
        PointF textPos = new PointF();
        String text = context.getResources().getString(R.string.ls_lockscreen_text);

        imageLockScreen = setLockScreen_Bg(text, textPaint, textPos, screenRect);
        imageLockScreen.addText(text, textPos.x, textPos.y, textPaint);
        imageLockScreen.castObjectSizeAutomatically();
        imageLockScreen.setShader(textureProgram);
        imageLockScreen.bitmapToTexture();
        imageLockScreen.recycleBitmap();
    }
    //</editor-fold>

    //<editor-fold desc="Touch">
    public Mode touch(float normalizedX, float normalizedY, int motionEvent) {
        Mode playMode = Mode.LEVEL_SELECTION;
        switch (motionEvent) {
            case MotionEvent.ACTION_DOWN:
                if (animator.isAnimationStarted()) animator.stop();
                pressSelectedButton(normalizedX, normalizedY);
                startScroll(normalizedY);
                return playMode;
            case MotionEvent.ACTION_MOVE:
                updateScroll(normalizedY);
                return playMode;
            case MotionEvent.ACTION_UP:
                if (isStartScrolling){
                    finishScroll();
                    unpressAllButtons();
                    selectedLevel = -1;
                    break;
                }
                for (int i = 0; i < LEVELS_NUMBER; i++) {
                    if (ObjectBuildHelper.rectContainsPoint(button[i].getDstRect(), normalizedX, normalizedY) && button[i].isPressed()) {
                        if (allLevelsInThePrevRowSolved(i, button)) {
                            selectedLevel = i;
                            playMode = Mode.LOCK_LS_TOUCH;
                        }
                        else {
                            selectedLevel = i;
                            logDebugOut(TAG, "onTouchEvent.ACTION_UP","Selected level is still locked.");
                        }
                        break;
                    }
                }
                unpressAllButtons();
        }
        return playMode;
    }

    private void pressSelectedButton(float normalizedX, float normalizedY) {
        for (int i = 0; i < LEVELS_NUMBER; i++) {
            if (ObjectBuildHelper.rectContainsPoint(button[i].getDstRect(), normalizedX, normalizedY)) {
                button[i].setPressed(true);
                break;
            }
        }
    }

    private void startScroll(float normalizedY) {
        prevTouch = normalizedY;
    }

    private void unpressAllButtons() {
        for (int i = 0; i < LEVELS_NUMBER; i++)
            button[i].setPressed(false);
    }

    private void updateScroll(float normalizedY) {
        float dy = normalizedY - prevTouch;
        prevTouch = normalizedY;

        // Limitation for scrolling
        if (Math.abs(dy) < INSENSITIVE_BACKLASH_ON_SCROLL )
            return;
        dy = upperBoundOfScroll(dy);
        dy = lowerBoundOfScroll(dy);

        for (TangramGLButtonExt t: button)
            t.offset(0, dy);

        isStartScrolling = true;
    }

    private void finishScroll() {
        isStartScrolling = false;
    }

    private float upperBoundOfScroll(float dy) {
        if ((button[0].getDstRect().top + dy) < LS_BUTTONS_OFFSET_FROM_TOP_DC)
            return LS_BUTTONS_OFFSET_FROM_TOP_DC - button[0].getDstRect().top;
        return dy;
    }

    private float lowerBoundOfScroll(float dy) {
        if ((button[LEVELS_NUMBER-1].getDstRect().bottom + dy) > (LS_BUTTON_GAP - 1))
            return (LS_BUTTON_GAP - 1) - button[LEVELS_NUMBER-1].getDstRect().bottom;
        return dy;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public boolean isSelectedLevelLocked() {
        return button[selectedLevel].isLocked();
    }
    //</editor-fold>

    //<editor-fold desc="Scrolling on Flinging">
    public void onFling(float velocity) {
        animator.stop();
        animator.setStartValue(velocityToDeviceCoords(velocity, screenRect.height()));
        animator.setAnimationType(TangramAnimator.ANIM_TYPE.PARABOLIC);
        animator.setDuration(SCROLLING_ANIMATION_DURATION);
        animator.start();
        startScroll(animator.getAnimatedValue());
    }

    private void scrolling() {
        if (animator.isAnimationStarted())
            updateScroll(animator.getAnimatedValue());
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix, TangramGLRenderer.Mode playMode) {
        imageMenuBackground.draw(projectionMatrix);
        for (TangramGLButtonExt t: button)
            t.draw(projectionMatrix);

        imageMenuHeader.useGradientShader(aGradientProgram, projectionMatrix, screenRect.width(), screenRect.height(),
                LS_GRADIENT_HEADER_OFFSET_FROM_TOP-LS_GRADIENT_HEADER_HEIGHT, LS_GRADIENT_HEADER_OFFSET_FROM_TOP);
        imageMenuHeader.draw(projectionMatrix);
        if (playMode == TangramGLRenderer.Mode.LOCK_LS_TOUCH)
            imageLockScreen.draw(projectionMatrix);

        scrolling();
    }

    //<editor-fold desc="Static Auxiliary Functions">
    private static String getLSHeaderText( Context context, int selectedLevelSet) {
        Resources res = context.getResources();
        String str = LEVEL_SET + selectedLevelSet;
        int id = res.getIdentifier(str, "string", context.getPackageName());
        return context.getResources().getString(id);
    }

    private static TangramGLSquare setLSHeader_Bg(Context context, RectF screenRect) {
        RectF bounds = new RectF();
        bounds.left = 0;
        bounds.right = screenRect.width();
        bounds.top =  0;
        bounds.bottom = screenRect.height() * (1f - LS_GRADIENT_HEADER_OFFSET_FROM_TOP + LS_GRADIENT_HEADER_HEIGHT);
        Bitmap gradientHeaderLSS = ObjectBuildHelper.createTiledBitmap(context, bounds, reuse.bitmapMaple);

        return new TangramGLSquare(gradientHeaderLSS, pixelsToDeviceCoords(bounds, screenRect));
    }

    private static TangramGLSquare setLockScreen_Bg(String text, Paint textPaint, PointF textPos, RectF screenRect) {
        Bitmap b = createBitmap((int) screenRect.width(), (int) screenRect.height(), Bitmap.Config.ARGB_8888);
        Rect bounds = new Rect();
        // Координаты центра битмапа - для будущего рисования текста.
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        textPos.x = screenRect.width() / 2f;
        textPos.y = screenRect.height() / 2f - bounds.exactCenterY();

        TangramGLSquare imageLSLockScreen = new TangramGLSquare(b, ASPECT_RATIO, 1.0f);
        imageLSLockScreen.drawColor(COLOR_SHADOW);

        return imageLSLockScreen;
    }

    private static RectF setButtonPositionFirstRow(RectF[] rectButtons, int index) {
        RectF rectButton = new RectF();
        rectButton.top = rectButtons[0].top;
        rectButton.bottom = rectButtons[0].bottom;
        rectButton.left = rectButtons[index-1].right + BASE_SCREEN_DIMENSION * LS_BUTTON_GAP;
        rectButton.right = rectButton.left + BASE_SCREEN_DIMENSION * LS_BUTTON_WIDTH;

        return rectButton;
    }

    private static RectF setButtonPositionOtherRows(RectF[] rectButtons, float buttonRatio, int index) {
        RectF rectButton = new RectF();
        rectButton.left = rectButtons[index-LEVELS_IN_THE_ROW].left;
        rectButton.right = rectButtons[index-LEVELS_IN_THE_ROW].right;
        rectButton.top = rectButtons[index - LEVELS_IN_THE_ROW].bottom + BASE_SCREEN_DIMENSION * LS_BUTTON_GAP;
        rectButton.bottom = rectButton.top + BASE_SCREEN_DIMENSION * LS_BUTTON_WIDTH * buttonRatio;

        return rectButton;
    }

    private static void setButtonTimer(TangramGLButtonExt buttonLS, long timer) {
        int[] t = TangramGLLevelTimer.convertElapsedTime(timer);
        float xOffset = BASE_SCREEN_DIMENSION * LS_BUTTONS_TIMER_HOFFSET;
        float yOffset = BASE_SCREEN_DIMENSION * LS_BUTTONS_TIMER_VOFFSET;
        float scaleFactor = BASE_SCREEN_DIMENSION * LS_BUTTONS_TIMER_SCALE;

        buttonLS.addTexture(reuse.digits[t[0]], -2*xOffset, yOffset, scaleFactor, false);
        buttonLS.addTexture(reuse.digits[t[1]], -xOffset, yOffset, scaleFactor, false);
        buttonLS.addTexture(reuse.digits[t[2]], xOffset, yOffset, scaleFactor, false);
        buttonLS.addTexture(reuse.digits[t[3]], 2*xOffset, yOffset, scaleFactor, false);
        buttonLS.addTexture(reuse.colon, 0, yOffset, scaleFactor, false);
    }

    private static Bitmap setButtonPreviews(Context context, int selectedLevelSet, int levelNumber, Bitmap b, RectF screenRect) {
        float previewSize = (float) b.getHeight() * LS_PREVIEW_BITMAP_SIZE * BASE_SCREEN_DIMENSION;
        Bitmap bitmap = createBitmap((int) previewSize, (int) previewSize, Bitmap.Config.ARGB_8888);
        RectF rect = new RectF(0, 0, previewSize, previewSize);

        TangramGLLevelBackground tempBg = new TangramGLLevelBackground(bitmap, pixelsToDeviceCoords(rect, screenRect));
        tempBg.drawColor(Color.TRANSPARENT);
        tempBg.setLevelPathColors(COLOR_LEVEL_BG, Color.BLACK);
        loadLevelPathByNumber(tempBg, context, selectedLevelSet, levelNumber);
        tempBg.resizeLevelPath(previewSize * LS_PREVIEW_PATH_SIZE, false);
        tempBg.setLevelPathToCenter(new RectF(0, 0, previewSize, previewSize));
        tempBg.addPathToBackground();

        return bitmap;
    }

    private static Bitmap setButtonCup(int cup) {
        switch (cup)
        {
            case BRONZE_CUP:
                return reuse.cupBronze;
            case SILVER_CUP:
                return reuse.cupSilver;
            case GOLDEN_CUP:
                return reuse.cupGold;
            default:
                return reuse.cupNo;
        }
    }

    public static boolean allLevelsInThePrevRowSolved(int indexOfButton, TangramGLButtonExt[] buttonLS) {
        // Define is the levels unlocked in the current row.
        // Current row unlocked when all levels in the previous row get at least bronze cup.
        // But note, first 5 levels are always unlocked.
        boolean result = true;
        if (indexOfButton >= LEVELS_IN_THE_ROW) {
            int rowOfSelectedLevel = (int) Math.floor((double) indexOfButton / LEVELS_IN_THE_ROW);
            for(int j = (rowOfSelectedLevel * LEVELS_IN_THE_ROW - LEVELS_IN_THE_ROW); j < (rowOfSelectedLevel * LEVELS_IN_THE_ROW); j++) {
                result &= (buttonLS[j].getCup() > 0);
            }
        }
//        logDebugOut(TAG, "onTouchEvent.ACTION_UP.allLevelsInThePrevRowSolved", result);

        return result;
    }
    //</editor-fold>

    //<editor-fold desc="Save-Load Data">
    private static void loadData(Context context, int selectedLevelSet, long[] timer, int[] cup) {
        int id;
        String resName;
        Resources res = context.getResources();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            logDebugOut(TAG, "loadData","Data is loading.");

            for (int i = 0; i < LEVELS_NUMBER; i++) {
                resName = SET + selectedLevelSet + LEVEL_CUP + i;
                id = res.getIdentifier(resName, DEF_TYPE_STRING, context.getPackageName());
                cup[i] = sharedPref.getInt(res.getString(id), 0);

                resName = SET + selectedLevelSet + LEVEL_TIME + i;
                id = res.getIdentifier(resName, DEF_TYPE_STRING, context.getPackageName());
                timer[i] = sharedPref.getLong(res.getString(id), 0);
            }
        }
        catch (Exception ex) {
            logDebugOut(TAG, "loadData","Exception: " + ex.getMessage());
        }
    }

    public static void saveData(Context context, int selectedLevelSet, int level, long timer, int cup) {
        int id;
        Resources res = context.getResources();
        SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        id = res.getIdentifier(SET + selectedLevelSet + LEVEL_CUP + level, DEF_TYPE_STRING, context.getPackageName());
        editor.putInt(res.getString(id), cup);
        id = res.getIdentifier(SET + selectedLevelSet + LEVEL_TIME + level, DEF_TYPE_STRING, context.getPackageName());
        editor.putLong(res.getString(id), timer);
        editor.apply();
        logDebugOut(TAG, "saveData","Save data of level No" + level);
    }
    //</editor-fold>

}
