package com.aga.woodentangrampuzzle2.opengles20;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.aga.android.programs.AlphaGradientShaderProgram;
import com.aga.android.programs.TextureShaderProgram;
import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.common.MultiTouchGestures;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;
import com.aga.woodentangrampuzzle2.opengles20.screens.TangramGLLevelScreen;
import com.aga.woodentangrampuzzle2.opengles20.screens.TangramGLLevelSelectionScreen;
import com.aga.woodentangrampuzzle2.opengles20.screens.TangramGLLevelSetSelectionScreen;
import com.aga.woodentangrampuzzle2.opengles20.screens.TangramGLMainMenuScreen;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.setLoadScreen;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ZERO;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.digitalTF;
import static com.aga.woodentangrampuzzle2.opengles20.screens.TangramGLLevelSelectionScreen.saveData;

import androidx.core.content.res.ResourcesCompat;

/**
 *
 * Created by Andrii Husiev on 15.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLRenderer implements GLSurfaceView.Renderer {

    //<editor-fold desc="Constants">
    private static final String TAG = "TangramGLRenderer";
    private static float TILES_SCALE_FACTOR;
    public static final int INSTANTIATED_LEVEL_SET_NUMBER = 2;
    //</editor-fold>

    //<editor-fold desc="Variables">
    public enum Mode {LOADING_SCREEN, MAIN_MENU, LEVELS_SET_SELECTION, LEVEL_SELECTION, LEVEL, LOCK_LSS_TOUCH, LOCK_LS_TOUCH}
    public Mode playMode;
    private boolean additionalDrawCycleEnds;
    private int selectedLevelSet;
    private int selectedLevel;
    private final Context context;
    public static RectF screenRect;
    public static float ASPECT_RATIO;
    public static float BASE_SCREEN_DIMENSION;
    public static boolean isLoadingEnds;

    private TangramGLSquare imageLoadingBg;
    private TangramGLMainMenuScreen screenMainMenu;
    private TangramGLLevelSetSelectionScreen screenLSS;
    private TangramGLLevelSelectionScreen screenLS;
    private TangramGLLevelScreen levelScreen;
    private MultiTouchGestures multiTouchEvent;

    public static TextureShaderProgram textureProgram;
    public static TextureShaderProgram desaturationProgram;
    public static AlphaGradientShaderProgram aGradientProgram;
    private final float[] projectionMatrix = new float[16];
//    private float testX=0, testY=0;
    //</editor-fold>

    TangramGLRenderer(Context context, DisplayMetrics metrics) {
        this.context = context;
        setScreenRect(metrics);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        setGLES20();
        doTheVeryFirstInitializations();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        switch (playMode) {
            case LOADING_SCREEN:
                imageLoadingBg.draw(projectionMatrix);
                if (additionalDrawCycleEnds) {
                    setObjects();
                }
                additionalDrawCycleEnds = true;
                if (isLoadingEnds)
                    playMode = Mode.MAIN_MENU;
                break;
            case MAIN_MENU:
                screenMainMenu.draw(projectionMatrix);
                break;
            case LOCK_LSS_TOUCH:
            case LEVELS_SET_SELECTION:
                screenLSS.draw(projectionMatrix, playMode);
                if (playMode == Mode.LOCK_LSS_TOUCH) {
                    if (additionalDrawCycleEnds) {
                        screenLS = new TangramGLLevelSelectionScreen(context, screenRect, selectedLevelSet);
                        playMode = Mode.LEVEL_SELECTION;
                    }
                    additionalDrawCycleEnds = true;
                }
                break;
            case LOCK_LS_TOUCH:
            case LEVEL_SELECTION:
                screenLS.draw(projectionMatrix, playMode);
                if (playMode == Mode.LOCK_LS_TOUCH) {
                    if (additionalDrawCycleEnds){
                        loadLevel();
                        levelScreen.timer.resume();
                        levelScreen.shiftTilesToStartPosition();
                        playMode = Mode.LEVEL;
                    }
                    additionalDrawCycleEnds = true;
                }
                break;
            case LEVEL:
                levelScreen.draw(projectionMatrix);
                break;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        final float aspectRatio =  (float) width / (float) height;

        GLES20.glViewport(0, 0, width, height);
        Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
    }

    //<editor-fold desc="All about screen interaction">
    public void onFling(float velocity) {
        switch (playMode) {
            case LEVELS_SET_SELECTION:
                screenLSS.onFling(velocity);
                break;
            case LEVEL_SELECTION:
                screenLS.onFling(velocity);
                break;
        }
    }

    public void onTouch(MotionEvent event) {
        multiTouchEvent.onTouchEvent(event);
        handleTouch(multiTouchEvent.getNormalizedX(ZERO), multiTouchEvent.getNormalizedY(ZERO), multiTouchEvent.getAction());
    }

    private void handleTouch(float normalizedX, float normalizedY, int motionEvent) {
        switch (playMode) {
            case LOADING_SCREEN:
                break;
            case MAIN_MENU:
                playMode = screenMainMenu.touch(normalizedX, normalizedY, motionEvent);
                break;
            case LEVELS_SET_SELECTION:
                playMode = screenLSS.touch(normalizedX, normalizedY, motionEvent);
                selectedLevelSet = screenLSS.getSelectedLevelSet();
                if (playMode == Mode.LOCK_LSS_TOUCH)
                    additionalDrawCycleEnds = false;
                break;
            case LEVEL_SELECTION:
                playMode = screenLS.touch(normalizedX, normalizedY, motionEvent);
                selectedLevel = screenLS.getSelectedLevel();
//                if (selectedLevel > 0) {
//                    if (screenLS.isSelectedLevelLocked()) {
//                        logDebugOut(TAG, "handleTouch.LEVEL_SELECTION","Place here MsgBox");
//                    }
//                }
                if (playMode == Mode.LOCK_LS_TOUCH)
                    additionalDrawCycleEnds = false;
                break;
            case LEVEL:
                if (levelScreen.touch(multiTouchEvent) == Mode.LEVEL_SELECTION)
                    onBackPressed();
                break;
        }
    }

    void onBackPressed() {
        logDebugOut(TAG, "onBackPressed","pressed.");

        switch (playMode) {
            case MAIN_MENU:
                exitApplication();
            case LEVELS_SET_SELECTION:
                playMode = Mode.MAIN_MENU;
                break;
            case LEVEL_SELECTION:
                playMode = Mode.LEVELS_SET_SELECTION;
                break;
            case LEVEL:
                saveData(context, selectedLevelSet, selectedLevel, levelScreen.timer.getElapsedTime(), levelScreen.cup.getReachedCup());
                screenLS.updateButton(levelScreen.timer.getElapsedTime(), levelScreen.cup.getReachedCup());
                levelScreen = null;
                playMode = Mode.LEVEL_SELECTION;
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Initializing resources">

    //<editor-fold desc="Game Start Actions">
    private void setGLES20() {
        // Set the background frame color
//        GLES20.glClearColor(0.0f, 0.5f, 0.05f, 1.0f); // Dark green for tests
        GLES20.glClearColor(0, 0, 0, 1);
        // Enable face culling feature
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // Specify which faces to not draw
        GLES20.glCullFace(GLES20.GL_BACK);
        // Turn on usage of alpha-component
        GLES20.glEnable(GLES20.GL_BLEND);
        // Интересные ссылки по типам смешивания:
        // http://www.gamedev.ru/code/forum/?id=60501&page=2
//        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void doTheVeryFirstInitializations() {
        isLoadingEnds = false;
        // Эта переменная используется для создания дополнительного цикла прорисовки,
        // чтобы сначала отобразить загрузочный экран, а потом во втором цикле уже
        // и загружать необходимые ресурсы. Так мы избегаем создание отдельного потока.
        additionalDrawCycleEnds = false;

        ASPECT_RATIO = screenRect.width() / screenRect.height();
        BASE_SCREEN_DIMENSION = screenRect.height();
        TILES_SCALE_FACTOR = BASE_SCREEN_DIMENSION / ((float) context.getResources().getInteger(R.integer.level_scale_factor));
        digitalTF = ResourcesCompat.getFont(context, R.font.digitaldismay);

        textureProgram = new TextureShaderProgram(context);
        desaturationProgram = new TextureShaderProgram(context, R.raw.desaturation_shader);
        aGradientProgram = new AlphaGradientShaderProgram(context);
        imageLoadingBg = setLoadScreen(context);
        playMode = Mode.LOADING_SCREEN;
    }

    private void setObjects() {
        screenMainMenu = new TangramGLMainMenuScreen(context, screenRect);
        screenLSS = new TangramGLLevelSetSelectionScreen(context, screenRect);
        multiTouchEvent = new MultiTouchGestures(screenRect);

        isLoadingEnds = true;
    }
    //</editor-fold>

    //<editor-fold desc="Mix">
    private void setScreenRect(DisplayMetrics metrics) {
        screenRect = new RectF();
        screenRect.left = 0;
        screenRect.top = 0;
        screenRect.right = metrics.widthPixels;
        screenRect.bottom = metrics.heightPixels;
    }

    private void exitApplication() {
        Activity activity = (Activity) context;
        activity.finish();
        System.exit(0);
    }

    private void loadLevel() {
        levelScreen = new TangramGLLevelScreen(context, screenRect);
        levelScreen.initInnerObjects(selectedLevel, selectedLevelSet, TILES_SCALE_FACTOR);
        playMode = Mode.LEVEL;
    }
    //</editor-fold>

    //</editor-fold>
}
