package com.aga.woodentangrampuzzle2.opengles20.screens;

import static com.aga.android.util.ObjectBuildHelper.createTiledBitmap;
import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.setPaint;
import static com.aga.android.util.ObjectBuildHelper.xPixelsToDeviceCoords;
import static com.aga.android.util.ObjectBuildHelper.yPixelsToDeviceCoords;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.BRONZE_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_LEVEL_BG;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_INGAME_HEADER;
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
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.NO_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.SILVER_CUP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TILES_NUMBER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ZERO;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.digitalTF;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.BASE_SCREEN_DIMENSION;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.Mode;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.reuse;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.textureProgram;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.common.MultiTouchGestures;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelBackground;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelButtons;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelCup;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelForeground;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelTiles;
import com.aga.woodentangrampuzzle2.opengles20.level.TangramGLLevelTimer;

/**
 *
 * Created by Andrii Husiev on 02.12.2021.
 * Этот объект отвечает за отображение и взаимодействие всех элементов игрового уровня.
 *
 */

public class TangramGLLevelScreen {
    private static final String TAG = "TangramGLLevelScreen";
    private static final String LEVEL_CUP = "_level_cup_";
    private static final String LEVEL_TIME = "_level_time_";
    private static final String SET = "set";
    private static final String TILE = "tile";
    private static final String COORD_X = "_x";
    private static final String COORD_Y = "_y";
    private static final String DEF_TYPE_ARRAY = "array";
    private static final String DEF_TYPE_STRING = "string";
    private static final float FRAME_WIDTH = 2;

    public TangramGLLevelForeground foreground;
    public TangramGLLevelTiles tiles;
    public TangramGLLevelBackground background;
    public TangramGLLevelCup cup;
    public TangramGLLevelButtons buttons;
    public TangramGLLevelTimer timer;

    private final Context context;
    private final RectF screenRect;
    private int selectedLevel, selectedLevelSet;
    private float tilesScaleFactor;

    public TangramGLLevelScreen(Context context, RectF screenRect) {
        this.context = context;
        this.screenRect = new RectF(screenRect);
    }

    //<editor-fold desc="Initialization">

    public void initInnerObjects(int selectedLevel, int selectedLevelSet, float tilesScaleFactor) {
        this.selectedLevel = selectedLevel;
        this.selectedLevelSet = selectedLevelSet;
        this.tilesScaleFactor = tilesScaleFactor;

        loadLevelBackground();
        loadLevelTiles();
        loadLevelForeground();
        loadLevelCups();
        loadLevelButtons();
        loadLevelTimer();
    }

    //<editor-fold desc="Level Background">

    /**
     * Инициализация заднего фона, в него входят: фоновая текстура и фигура,
     * с которой надо совмещать плитки.
     */
    private void loadLevelBackground() {
        background = setLevelBackground(context, screenRect);
        loadLevelPathByNumber(background, context, selectedLevelSet, selectedLevel);
        background.setLevelPathColors(COLOR_LEVEL_BG, Color.BLACK);
        background.scaleLevelPath(tilesScaleFactor);
        background.setLevelPathToCenter(getIngamePlayableRect());
        background.addPathToBackground();
        background.castObjectSizeAutomatically();
        background.setShader(textureProgram);
        background.bitmapToTexture();
        background.recycleBitmap();
    }

    private RectF getIngamePlayableRect() {
        RectF ingamePlayableRect = new RectF();

        ingamePlayableRect.left = 0;
        ingamePlayableRect.top = screenRect.height() * INGAME_HEADER_HEIGHT;
        ingamePlayableRect.right = screenRect.width();
        ingamePlayableRect.bottom = screenRect.height();

        return ingamePlayableRect;
    }
    //</editor-fold>

    /**
     * Инициализация плиток, которые и надо двигать и поворачивать, чтобы пройти уровень игры.
     */
    private void loadLevelTiles() {
        tiles = setLevelTiles(context, screenRect);
        tiles.drawFrames(Color.BLACK, FRAME_WIDTH);
        tiles.scale(tilesScaleFactor);
        tiles.finalizeForDraw(textureProgram);
        tiles.setLevelPoints(background.getNormalizedPointsX(), background.getNormalizedPointsY());
    }

    /**
     * Инициализация переднего плана: заголовок с названием уровня и боковые расширители заголовка
     * для таймера и прогресса уровня.
     */
    private void loadLevelForeground() {
        foreground = setLevelForeground(screenRect);
        setLevelHeader(context, foreground, screenRect);
        setAngularHeaders(context, foreground, screenRect);
        setLevelTitle(context, foreground, screenRect, selectedLevelSet, selectedLevel);
        foreground.castObjectSizeAutomatically();
        foreground.setShader(textureProgram);
        foreground.bitmapToTexture();
        foreground.recycleBitmap();
    }

    /**
     * Инициализация объекта, отвечающего за отображение кубка в правом AngularHeaders.
     * Осуществляется последовательная загрузка всех вариантов кубков, а затем устанавливается
     * на данный момент реально достигнутый кубок.
     */
    private void loadLevelCups() {
        cup = new TangramGLLevelCup();
        RectF cupDstRect = getIngameCupPosition(reuse.cups[0], screenRect);
        cup.addCup(reuse.cups[0], cupDstRect, NO_CUP, textureProgram);
        cup.addCup(reuse.cups[1], cupDstRect, BRONZE_CUP, textureProgram);
        cup.addCup(reuse.cups[2], cupDstRect, SILVER_CUP, textureProgram);
        cup.addCup(reuse.cups[3], cupDstRect, GOLDEN_CUP, textureProgram);
        // чисто для тестов, потом надо убрать
//        cup.setReachedCup(GOLDEN_CUP);
        cup.setReachedCup(loadDataCup(context, selectedLevelSet, selectedLevel));
    }

    /**
     * Инициализация угловых кнопок: назад в меню и сброс уровня,
     * т.е. возврат плиток на начальные позиции.
     */
    private void loadLevelButtons() {
        buttons = new TangramGLLevelButtons();

        setIngameButtonBackToMenuBitmap(context, buttons);
        setIngameButtonResetLevelBitmap(context, buttons, screenRect);
    }

    /**
     * Инициализация объекта, отвечающего за отображение таймера и подсчет времени.
     * Если в уровень уже заходили, то время продолжится с предыдущего значения.
     */
    private void loadLevelTimer() {
        timer = new TangramGLLevelTimer();
        setIngameTimerSymbols(timer);
        timer.addTimePeriod(loadDataTimer(context, selectedLevelSet, selectedLevel));
        timer.finalizeConfiguring();
    }
    //</editor-fold>

    //<editor-fold desc="Touch">
    public Mode touch(MultiTouchGestures multiTouch) {
        // Reaction if tile is touched
        tiles.touch(multiTouch);

        // Reaction if button is touched
        return touchButtons(multiTouch.getNormalizedX(ZERO), multiTouch.getNormalizedY(ZERO), multiTouch.getAction());
    }

    public Mode touchButtons(float normalizedX, float normalizedY, int motionEvent) {
        switch (buttons.touch(normalizedX, normalizedY, motionEvent)) {
            case BACK:
                return Mode.LEVEL_SELECTION;
            case RESET:
                tiles.resetTilesOrder();
                shiftTilesToStartPosition();
                break;
        }

        return Mode.LEVEL;
    }
    //</editor-fold>

    //<editor-fold desc="Static">
    private static TangramGLLevelBackground setLevelBackground(Context context, RectF screenRect) {
        Bitmap b = createTiledBitmap(context, screenRect, reuse.bitmapMaple);

        return new TangramGLLevelBackground(b);
    }

    public static boolean loadLevelPathByNumber(TangramGLLevelBackground level, Context context, int selectedLevelSet, int levelNumber){
        int id;
        int[] x, y;
        boolean result = true;
        Resources res = context.getResources();

        try {
            id = res.getIdentifier("ls" + selectedLevelSet + "_level_" + levelNumber + "_x", "array", context.getPackageName());
            x = res.getIntArray(id);
            id = res.getIdentifier("ls" + selectedLevelSet + "_level_" + levelNumber + "_y", "array", context.getPackageName());
            y = res.getIntArray(id);

            level.setLevelPath(x, y);
        }
        catch (Exception ex) {
            result = false;
            logDebugOut(TAG, "loadLevelPathByNumber","loadLevelByNumber(" + levelNumber + "). Exception: " + ex.getMessage());
        }

        return result;
    }

    private static TangramGLLevelTiles setLevelTiles(Context context, RectF screenRect) {
        Resources res = context.getResources();
        float[] x, y;
        int[] intX, intY;
        int id;
        Bitmap bitmapWood;

        TangramGLLevelTiles tiles = new TangramGLLevelTiles(TILES_NUMBER);

        for (int i = 0; i < TILES_NUMBER; i++) {
            id = res.getIdentifier(TILE + i + COORD_X, DEF_TYPE_ARRAY, context.getPackageName());
            intX = res.getIntArray(id);
            id = res.getIdentifier(TILE + i + COORD_Y, DEF_TYPE_ARRAY, context.getPackageName());
            intY = res.getIntArray(id);
            x = xPixelsToDeviceCoords(intX, screenRect);
            y = ObjectBuildHelper.yPixelsToDeviceCoords(intY, screenRect);
            bitmapWood = ObjectBuildHelper.loadBitmap(context, R.drawable.woodentexture);
            tiles.addTile(bitmapWood, x, y);
        }

        return tiles;
    }

    private static TangramGLLevelForeground setLevelForeground(RectF screenRect) {
        TangramGLLevelForeground fg = new TangramGLLevelForeground(Bitmap.createBitmap((int) screenRect.width(), (int) screenRect.height(), Bitmap.Config.ARGB_8888), ObjectBuildHelper.pixelsToDeviceCoords(screenRect, screenRect));
        fg.drawColor(Color.TRANSPARENT);

        return fg;
    }

    private static void setLevelHeader(Context context, TangramGLLevelForeground fg, RectF screenRect) {
        RectF rectF = new RectF(0, 0, screenRect.width(), screenRect.height() * INGAME_HEADER_HEIGHT);
        Bitmap b = createTiledBitmap(context, rectF, reuse.bitmapOak);
        fg.addBitmap(b, rectF);

        // Draw shadow under header.
        float y;
        int shadowHeight = (int) (INGAME_HEADER_SHADOW_HEIGHT * BASE_SCREEN_DIMENSION);
        int color = Color.BLACK; //== 0xff000000;
        int colorOffset = ((int) Math.floor(255f / shadowHeight)) * 0x1000000;
        Paint linePaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, false, Typeface.DEFAULT);
        for (int i = 0; i < shadowHeight; i++) {
            color -= colorOffset;
            linePaint.setColor(color);
            y = screenRect.height() * INGAME_HEADER_HEIGHT + i;
            fg.drawLine(0, y, screenRect.width(), y, linePaint);
        }
    }

    private static void setAngularHeaders(Context context, TangramGLLevelForeground fg, RectF screenRect) {
        Bitmap b0 = BitmapFactory.decodeResource(context.getResources(), R.drawable.angular_header);
        float buttonRatio = (float) b0.getWidth() / b0.getHeight();
        RectF rect = new RectF();
        Matrix m = new Matrix();

        rect.left = 0;
        rect.top = 0;
        rect.right = BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio;
        rect.bottom = BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT;
        fg.addBitmap(b0, rect);

        m.preScale(-1, 1);
        Bitmap b1 = Bitmap.createBitmap(b0, 0, 0, b0.getWidth(), b0.getHeight(), m, true);
        rect.right = screenRect.width();
        rect.left = rect.right - BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio;
        fg.addBitmap(b1, rect);
    }

    private static void setLevelTitle(Context context, TangramGLLevelForeground fg, RectF screenRect, int selectedLevelSet, int levelNumber) {
        PointF textPos = new PointF();
        Resources res = context.getResources();
        int id = res.getIdentifier("ls" + selectedLevelSet + "_level_" + levelNumber, "string", context.getPackageName());
        String title = String.format(res.getString(R.string.level_title), (levelNumber+1)) + res.getString(id);
        Paint textPaint = setPaint(ALL_FONTS_SIZE, COLOR_TEXT_INGAME_HEADER, false, Typeface.DEFAULT_BOLD);
        Bitmap b = createBitmapSizeFromText(title, textPaint, textPos, true);
        RectF bitmapCoord = getIngameTitlePos(b, screenRect);
        Canvas canvas = new Canvas(b);
        canvas.drawText(title, textPos.x, textPos.y, textPaint);
        fg.addBitmap(b, bitmapCoord);
    }

    private static RectF getIngameTitlePos(Bitmap b, RectF screenRect) {
        RectF bitmapCoord = new RectF();
        float aspectRatio = (float) b.getWidth() / b.getHeight();
        // Определяем координаты битмапа.
        // Отталкиваемся от двух параметров - смещение центра текста от верхнего края экрана и
        // высота (жестко заданный коэффициент от высоты экрана), от которой мы и узнаем ширину.
        bitmapCoord.top = screenRect.height() * (INGAME_TITLE_OFFSET_FROM_TOP - INGAME_TITLE_HEIGHT);
        bitmapCoord.bottom = bitmapCoord.top + screenRect.height() * INGAME_TITLE_HEIGHT;
        bitmapCoord.left = (screenRect.width() - bitmapCoord.height() * aspectRatio) / 2f;
        bitmapCoord.right = bitmapCoord.left + bitmapCoord.height() * aspectRatio;

        return bitmapCoord;
    }

    private static RectF getIngameCupPosition(Bitmap bitmap, RectF screenRect) {
        RectF cupDstRect = new RectF();
        float aspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();

        cupDstRect.top = 0;
        cupDstRect.bottom = BASE_SCREEN_DIMENSION * INGAME_CUP_HEIGHT;
        cupDstRect.right = screenRect.width() - BASE_SCREEN_DIMENSION * INGAME_CUP_OFFSET_FROM_RIGHT;
        cupDstRect.left = cupDstRect.right - BASE_SCREEN_DIMENSION * INGAME_CUP_HEIGHT * aspectRatio;

        return cupDstRect;
    }

    //<editor-fold desc="Set Buttons">
    private static void setIngameButtonBackToMenuBitmap(Context context, TangramGLLevelButtons buttons) {
        Bitmap bitmap = ObjectBuildHelper.loadBitmap(context, R.drawable.backtomenu);
        RectF buttonRect = getIngameButtonBackToMenuPosition(bitmap);
        buttons.setButtonBackToMenu(bitmap, buttonRect, textureProgram);
    }

    private static RectF getIngameButtonBackToMenuPosition(Bitmap bitmap) {
        RectF buttonRect = new RectF();
        float buttonRatio = (float) bitmap.getWidth() / bitmap.getHeight();

        // Высота кнопки такая же, как и у ANGULAR_HEADER.
        buttonRect.left = 0;
        buttonRect.right = BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio;
        buttonRect.bottom = BASE_SCREEN_DIMENSION;
        buttonRect.top = buttonRect.bottom - BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT;

        return buttonRect;
    }

    private static void setIngameButtonResetLevelBitmap(Context context, TangramGLLevelButtons buttons, RectF screenRect) {
        Bitmap bitmap = ObjectBuildHelper.loadBitmap(context, R.drawable.reset_level);
        RectF buttonRect = getIngameButtonResetLevelPosition(bitmap, screenRect);
        buttons.setButtonResetLevel(bitmap, buttonRect, textureProgram);
    }

    private static RectF getIngameButtonResetLevelPosition(Bitmap bitmap, RectF screenRect) {
        RectF buttonRect = new RectF();
        float buttonRatio = (float) bitmap.getWidth() / bitmap.getHeight();

        // Высота кнопки такая же, как и у ANGULAR_HEADER.
        buttonRect.right = screenRect.width();
        buttonRect.left = buttonRect.right - BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio;
        buttonRect.bottom = BASE_SCREEN_DIMENSION;
        buttonRect.top = buttonRect.bottom - BASE_SCREEN_DIMENSION * INGAME_ANGULAR_HEADER_HEIGHT;

        return buttonRect;
    }
    //</editor-fold>

    //<editor-fold desc="Set Timer">
    /**
     * Функция для создания битмапов для каждой цифры и для двоеточия. Пришлось применить своего
     * рода костыль - все символы в drawText передаются не по одиночке, а обрамленные справа и слева
     * дополнительными символами. Это связано с тем, что символы ":" и "1" слишком тонкие
     * и поэтому иначе неверно масштабируются.
     * @param timer Объект, в который будут передаваться загруженные и сконфигурированные данные.
     */
    private static void setIngameTimerSymbols(TangramGLLevelTimer timer) {
        Bitmap bitmap;
        Canvas canvas;
        String digit;
        String sample = "0";
        String colon = "0:0";
        RectF colonRectF;
        final int NUMBER_OF_DIGITS = 900;
//        final float INDENT = HOR_INDENT * 1.5f; // доп. отступ по горизонтали, чтоб цифры не обрезались.
        PointF textPos = new PointF();
        Paint timerPaint = setPaint(ALL_FONTS_SIZE, COLOR_TEXT_INGAME_HEADER, false, digitalTF);
        RectF firstDigitRectF = getIngameFirstDigitPosition(createBitmapSizeFromText(sample, timerPaint, textPos, false));
        RectF interSymbolGap = getIngameInterSymbolGapPosition(firstDigitRectF);
        timer.setTimerPosition(firstDigitRectF, interSymbolGap);

        for (Integer index = 800, i = 0; index < NUMBER_OF_DIGITS; index+=10, i++) {
            digit = index.toString();
            bitmap = createBitmapSizeFromText(sample, timerPaint, textPos, false);
            canvas = new Canvas(bitmap);
            canvas.drawText(digit, textPos.x, textPos.y, timerPaint);
            timer.addDigit(bitmap, i, textureProgram);
        }

        bitmap = createBitmapSizeFromText(sample, timerPaint, textPos, false);
        colonRectF = getIngameColonPosition(bitmap, firstDigitRectF.width(), interSymbolGap.width());
        canvas = new Canvas(bitmap);
        canvas.drawText(colon, textPos.x, textPos.y, timerPaint);
        timer.addColon(bitmap, colonRectF, textureProgram);
    }

    private static RectF getIngameFirstDigitPosition(Bitmap bitmap) {
        RectF timerRectF = new RectF();
        float aspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();

        timerRectF.bottom = BASE_SCREEN_DIMENSION * INGAME_TIMER_OFFSET_FROM_TOP;
        timerRectF.top = timerRectF.bottom - BASE_SCREEN_DIMENSION * INGAME_TIMER_HEIGHT;
        timerRectF.left = BASE_SCREEN_DIMENSION * INGAME_TIMER_OFFSET_FROM_LEFT;
        timerRectF.right = timerRectF.left + timerRectF.height() * aspectRatio;

        return timerRectF;
    }

    private static RectF getIngameColonPosition(Bitmap bitmap, float digitWidth, float gapWidth) {
        RectF timerRectF = new RectF();
        float aspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();

        timerRectF.bottom = BASE_SCREEN_DIMENSION * INGAME_TIMER_OFFSET_FROM_TOP;
        timerRectF.top = timerRectF.bottom - BASE_SCREEN_DIMENSION * INGAME_TIMER_HEIGHT;
        timerRectF.left = BASE_SCREEN_DIMENSION * INGAME_TIMER_OFFSET_FROM_LEFT + (digitWidth + gapWidth) * 2f;
        timerRectF.right = timerRectF.left + timerRectF.height() * aspectRatio;

        return timerRectF;
    }

    private static RectF getIngameInterSymbolGapPosition(RectF timerRectF) {
        RectF interSymbolGap = new RectF();

        interSymbolGap.left = timerRectF.right;
        interSymbolGap.right = interSymbolGap.left + BASE_SCREEN_DIMENSION * INGAME_TIMER_DIGITS_GAP;
        interSymbolGap.top = timerRectF.top;
        interSymbolGap.bottom = timerRectF.bottom;

        return interSymbolGap;
    }

    //</editor-fold>

    //<editor-fold desc="Save-Load Data">
    private static long loadDataTimer(Context context, int selectedLevelSet, int level) {
        int id;
        long timer = 0;
        String str;
        Resources res = context.getResources();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            logDebugOut(TAG, "loadDataTimer","Timer data is begin to load.");

            str = SET + selectedLevelSet + LEVEL_TIME + level;
            id = res.getIdentifier(str, DEF_TYPE_STRING, context.getPackageName());
            timer = sharedPref.getLong(res.getString(id), 0);
        }
        catch (Exception ex) {
            logDebugOut(TAG, "loadDataTimer","Exception: " + ex.getMessage());
        }

        return timer;
    }

    private static int loadDataCup(Context context, int selectedLevelSet, int level) {
        int id;
        int cup = 0;
        String str;
        Resources res = context.getResources();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            logDebugOut(TAG, "loadDataCup","Cup data is begin to load.");

            str = SET + selectedLevelSet + LEVEL_CUP + level;
            id = res.getIdentifier(str, DEF_TYPE_STRING, context.getPackageName());
            cup = sharedPref.getInt(res.getString(id), 0);
        }
        catch (Exception ex) {
            logDebugOut(TAG, "loadDataCup","Exception: " + ex.getMessage());
        }

        return cup;
    }
    //</editor-fold>

    //</editor-fold>

    public void shiftTilesToStartPosition() {
        float x, y;

        x = xPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE0_OFFSET_X, screenRect);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE0_OFFSET_Y, screenRect);
        tiles.moveTo(0, x, y);
        x = xPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE1_OFFSET_X, screenRect);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE1_OFFSET_Y, screenRect);
        tiles.moveTo(1, x, y);
        x = xPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE2_OFFSET_X, screenRect);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE2_OFFSET_Y, screenRect);
        tiles.moveTo(2, x, y);
        x = xPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE3_OFFSET_X, screenRect);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE3_OFFSET_Y, screenRect);
        tiles.moveTo(3, x, y);
        x = xPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE4_OFFSET_X, screenRect);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE4_OFFSET_Y, screenRect);
        tiles.moveTo(4, x, y);
        x = xPixelsToDeviceCoords(screenRect.width() - BASE_SCREEN_DIMENSION * INGAME_TILES_OFFSET_FROM_RIGHT, screenRect);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE5_OFFSET_Y, screenRect);
        tiles.moveTo(5, x, y);
        y = yPixelsToDeviceCoords(BASE_SCREEN_DIMENSION * INGAME_TILE6_OFFSET_Y, screenRect);
        tiles.moveTo(6, x, y);
    }

    public void draw(float[] projectionMatrix) {
        background.draw(projectionMatrix);
        tiles.draw(projectionMatrix);
        foreground.draw(projectionMatrix);
        cup.draw(projectionMatrix);
        buttons.draw(projectionMatrix);
        // TODO: реализовать остановку таймера при выключении экрана
        timer.draw(projectionMatrix);
    }
}
