package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.Matrix;
import android.util.Log;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;

/**
 *
 * Created by Andrii Husiev on 27.11.2016.
 *
 */

public class TangramGLLevelTimer {
    //<editor-fold desc="Variables">
    public enum mode {STOP, RUN, PAUSE}
    private mode timerMode;
    private long startTime;
    private long pausedTime;
    private long elapsedTime;
//    private int[] elapsedTimeDigits;
    private boolean timerActivated;
    private RectF firstDigitPosition;
    private float digitWidthDC;  // DC means "Device Coordinates"
    private float colonWidthDC;  // DC means "Device Coordinates"
    private float gapWidthDC;  // DC means "Device Coordinates"
    private float dxSecondDigit;
    private float dxThirdDigit;
    private float dxFourthDigit;
    private TangramGLSquare[] digits;
    private TangramGLSquare colon;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    //</editor-fold>

    public TangramGLLevelTimer() {
        startTime = 0;
        pausedTime = 0;
        elapsedTime = 0;
        timerActivated = false;
        timerMode = mode.STOP;
        digits = new TangramGLSquare[10];
//        elapsedTimeDigits = new int[4];
    }

    //<editor-fold desc="Math">
    public static int[] convertElapsedTime(long elapsedTime) {
        int[] elapsedTimeDigits= new int[4];
        int seconds = (int) (elapsedTime / 1000);
//        Log.d("debug","TangramGLLevelTimer.convertElapsedTime seconds == " + seconds);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds = seconds % 60;

        // TODO: не забыть что-то сделать, если время больше одного часа. Хоть это и маловероятное событие.
        elapsedTimeDigits[0] = minutes / 10;
        elapsedTimeDigits[1] = minutes % 10;
        elapsedTimeDigits[2] = seconds / 10;
        elapsedTimeDigits[3] = seconds % 10;

        return elapsedTimeDigits;
    }

    private int calcFirstDigit() {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;

        return (minutes / 10);
    }

    private int calcSecondDigit() {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;

        return (minutes % 10);
    }

    private int calcThirdDigit() {
        int seconds = (int) (elapsedTime / 1000);
        seconds = seconds % 60;

        return (seconds / 10);
    }

    private int calcFourthDigit() {
        int seconds = (int) (elapsedTime / 1000);
        seconds = seconds % 60;

        return (seconds % 10);
    }
    //</editor-fold>

    //<editor-fold desc="Common">
    public void setTimerPosition(RectF firstDigit, RectF interSymbolGap) {
        firstDigitPosition = ObjectBuildHelper.pixelsToDeviceCoords(firstDigit, screenRect);

        digitWidthDC = firstDigitPosition.width();
        gapWidthDC = ObjectBuildHelper.pixelsToDeviceCoords(interSymbolGap, screenRect).width();
    }

    public void addDigit(Bitmap bitmap, int index, TextureShaderProgram textureProgram) {
        try {
            digits[index] = new TangramGLSquare(bitmap, firstDigitPosition);
            digits[index].castObjectSizeAutomatically();
            digits[index].bitmapToTexture(textureProgram);
            digits[index].recycleBitmap();
        }
        catch (Exception ex) {
            Log.d("debug","TangramGLLevelTimer.addDigit catch an Exception: " + ex.getMessage());
        }
    }

    public void addColon(Bitmap bitmap, RectF dst, TextureShaderProgram textureProgram) {
        try {
            colon = new TangramGLSquare(bitmap, ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect));
            colon.castObjectSizeAutomatically();
            colon.bitmapToTexture(textureProgram);
            colon.recycleBitmap();

            colonWidthDC = ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect).width();
        }
        catch (Exception ex) {
            Log.d("debug","TangramGLLevelTimer.addColon catch an Exception: " + ex.getMessage());
        }
    }

    public void finalizeConfiguring() {
        dxSecondDigit = digitWidthDC + gapWidthDC;
        dxThirdDigit = dxSecondDigit * 2 + colonWidthDC;
        dxFourthDigit = dxSecondDigit + dxThirdDigit;
    }
    //</editor-fold>

    //<editor-fold desc="Timer Control">
    /**
     * Start new timer. If timer is already activated it will be restarted.
     */
    public void start() {
        timerActivated = true;
        timerMode = mode.RUN;
        pausedTime = 0;
        startTime = System.currentTimeMillis();
    }

    /**
     * Resume previously started and then paused timer.
     */
    public void resume() {
        timerActivated = true;
        timerMode = mode.RUN;
        startTime = System.currentTimeMillis();
    }

    /**
     * Pause timer.
     */
    public void pause() {
        timerActivated = false;
        timerMode = mode.PAUSE;
        pausedTime += System.currentTimeMillis() - startTime;
//        Log.d("debug","TangramGLLevelTimer.pause pausedTime == " + pausedTime);
    }

    /**
     * Stop timer.
     */
    public void stop() {
        timerActivated = false;
        timerMode = mode.STOP;
        startTime = 0;
        pausedTime = 0;
    }

    public mode getTimerMode() {
        return timerMode;
    }

    public void addTimePeriod(long millis) {
        pausedTime += millis;
    }

    public long getElapsedTime() {
        if (timerMode == mode.RUN)
            return System.currentTimeMillis() - startTime + pausedTime;
        else if (timerMode == mode.PAUSE)
            return pausedTime;
        else
            return 0;
//        Log.d("debug","TangramGLLevelTimer.getElapsedTime elapsedTime == " + elapsedTime);
//        return elapsedTime;
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix) {
        if (timerActivated)
            elapsedTime = System.currentTimeMillis() - startTime + pausedTime;

        // FirstDigit
        digits[calcFirstDigit()].draw(projectionMatrix);

        // SecondDigit
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dxSecondDigit, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        digits[calcSecondDigit()].draw(modelViewProjectionMatrix);

        // Colon
        colon.draw(projectionMatrix);

        // ThirdDigit
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dxThirdDigit, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        digits[calcThirdDigit()].draw(modelViewProjectionMatrix);

        // FourthDigit
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dxFourthDigit, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        digits[calcFourthDigit()].draw(modelViewProjectionMatrix);
    }
}
