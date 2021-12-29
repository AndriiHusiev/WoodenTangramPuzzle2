package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.Matrix;

/**
 *
 * Created by Andrii Husiev on 24.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLButton extends TangramGLSquare {
//    private static final float SCALE_FACTOR = 0.975f;
    private static final float SCALE_FACTOR = 0.95f;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private boolean isPressed, isLocked;
    private RectF dst;
    private PointF centerOfScaling;
    private int cup;

    /**
     * Creates new instance of ExtendedGLSquare. Note, that first you must convert bitmap coordinates
     * into normalized device coordinates. Use next formula:
     * <p align=center>(dst.width()/2 / yourGLSurfaceView.getWidth()) * 2 - 1;
     * @param b The source bitmap.
     * @param dst The rectangle that the bitmap will be scaled/translated to fit into.
     */
    public TangramGLButton(Bitmap b, RectF dst) {
        super(b, Math.abs(dst.width()/2f), Math.abs(dst.height()/2f));

        this.dst = new RectF(dst.left, dst.top, dst.right, dst.bottom);
        isPressed = false;
        cup = 0;
        centerOfScaling = new PointF(0, 0);
    }

    /**
     * Назначает точку, которая является центром, к которому сжимается копка при нажатии.
     * Возможные значения: topleft, topright, bottomleft, bottomright, center.
     * Если данная функция не будет вызвана, то по умолчанию принимается значение "center".
     * @param center Центр масштабирования, используемый при нажатии на кнопку.
     */
    public void setCenterOfScaling(String center) {
        switch (center) {
            case "topleft":
                centerOfScaling.x = dst.left - dst.centerX();
                centerOfScaling.y = dst.top - dst.centerY();
                break;
            case "topright":
                centerOfScaling.x = dst.right - dst.centerX();
                centerOfScaling.y = dst.top - dst.centerY();
                break;
            case "bottomleft":
                centerOfScaling.x = dst.left - dst.centerX();
                centerOfScaling.y = dst.bottom - dst.centerY();
                break;
            case "bottomright":
                centerOfScaling.x = dst.right - dst.centerX();
                centerOfScaling.y = dst.bottom - dst.centerY();
                break;
            case "center":
                centerOfScaling.x = 0;
                centerOfScaling.y = 0;
                break;
        }
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setCup(int value) {
        cup = value;
    }

    public int getCup() {
        return cup;
    }

    public RectF getDstRect() {
        return dst;
    }

    public void offset(float dx, float dy) {
        dst.left += dx;
        dst.top += dy;
        dst.right += dx;
        dst.bottom += dy;
//        logDebugOut(TAG, "offset","dst == " + dst.left + " - " + dst.top + " - " + dst.right + " - " + dst.bottom);
    }

    @Override
    public void draw(float[] projectionMatrix) {
        Matrix.setIdentityM(modelMatrix, 0);

        // Move to position
        Matrix.translateM(modelMatrix, 0, dst.centerX(), dst.centerY(), 0);

        // Scale
        Matrix.translateM(modelMatrix, 0, centerOfScaling.x, centerOfScaling.y, 0);
        if (isPressed)
            Matrix.scaleM(modelMatrix, 0, SCALE_FACTOR, SCALE_FACTOR, 1);
        Matrix.translateM(modelMatrix, 0, -centerOfScaling.x, -centerOfScaling.y, 0);

        // Multiply all changes
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);

        super.draw(modelViewProjectionMatrix);
    }
}
