package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.Matrix;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.common.TangramCommonTimer;
import com.aga.woodentangrampuzzle2.common.TangramCommonTimer.mode;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;

/**
 *
 * Created by Andrii Husiev on 27.11.2016.
 *
 */

public class TangramGLLevelTimer {
    private static final String TAG = "TangramGLLevelTimer";

    //<editor-fold desc="Variables">
    private RectF firstDigitPosition;
    private float digitWidthDC;  // DC means "Device Coordinates"
    private float colonWidthDC;  // DC means "Device Coordinates"
    private float gapWidthDC;    // DC means "Device Coordinates"
    private float dxSecondDigit;
    private float dxThirdDigit;
    private float dxFourthDigit;
    private TangramGLSquare[] digits;
    private TangramGLSquare colon;
    private TangramCommonTimer timer;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    //</editor-fold>

    public TangramGLLevelTimer() {
        timer = new TangramCommonTimer();
        digits = new TangramGLSquare[10];
//        elapsedTimeDigits = new int[4];
    }

    //<editor-fold desc="Math">
    public static int[] convertElapsedTime(long elapsedTime) {
        int[] elapsedTimeDigits= new int[4];
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
//        int hours = minutes / 60;
        seconds = seconds % 60;

        // TODO: не забыть что-то сделать, если время больше одного часа. Хоть это и маловероятное событие.
        elapsedTimeDigits[0] = minutes / 10;
        elapsedTimeDigits[1] = minutes % 10;
        elapsedTimeDigits[2] = seconds / 10;
        elapsedTimeDigits[3] = seconds % 10;

        return elapsedTimeDigits;
    }

    private int calcFirstDigit(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;

        return (minutes / 10);
    }

    private int calcSecondDigit(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;

        return (minutes % 10);
    }

    private int calcThirdDigit(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000);
        seconds = seconds % 60;

        return (seconds / 10);
    }

    private int calcFourthDigit(long elapsedTime) {
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
            digits[index].setShader(textureProgram);
            digits[index].bitmapToTexture();
            digits[index].recycleBitmap();
        }
        catch (Exception ex) {
            logDebugOut(TAG, "addDigit","catch an Exception: " + ex.getMessage());
        }
    }

    public void addColon(Bitmap bitmap, RectF dst, TextureShaderProgram textureProgram) {
        try {
            colon = new TangramGLSquare(bitmap, ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect));
            colon.castObjectSizeAutomatically();
            colon.setShader(textureProgram);
            colon.bitmapToTexture();
            colon.recycleBitmap();

            colonWidthDC = ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect).width();
        }
        catch (Exception ex) {
            logDebugOut(TAG, "addColon","catch an Exception: " + ex.getMessage());
        }
    }

    public void finalizeConfiguring() {
        dxSecondDigit = digitWidthDC + gapWidthDC;
        dxThirdDigit = dxSecondDigit * 2 + colonWidthDC;
        dxFourthDigit = dxSecondDigit + dxThirdDigit;
    }
    //</editor-fold>

    //<editor-fold desc="Timer Control">
    public void resume() {
        timer.resume();
    }

    public void addTimePeriod(long millis) {
        timer.addTimePeriod(millis);
    }

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix) {
        long elapsedTime = 0;
        if (timer.getTimerMode() == mode.RUN)
            elapsedTime = timer.getElapsedTime();

        // FirstDigit
        digits[calcFirstDigit(elapsedTime)].draw(projectionMatrix);

        // SecondDigit
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dxSecondDigit, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        digits[calcSecondDigit(elapsedTime)].draw(modelViewProjectionMatrix);

        // Colon
        colon.draw(projectionMatrix);

        // ThirdDigit
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dxThirdDigit, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        digits[calcThirdDigit(elapsedTime)].draw(modelViewProjectionMatrix);

        // FourthDigit
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, dxFourthDigit, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        digits[calcFourthDigit(elapsedTime)].draw(modelViewProjectionMatrix);
    }
}
