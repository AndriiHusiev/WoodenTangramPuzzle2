package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ROTATING_ANIMATION_DURATION;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.woodentangrampuzzle2.common.TangramAnimator;
import com.aga.woodentangrampuzzle2.common.TangramLevelPath;

/**
 *
 * Created by Andrii Husiev on 04.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLTile{
    private static final int SQUARE = 2;
    private static final int TRIANGLE = 3;
    private static final int QUADRANGLE = 4;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final int INDEX;

    private float scaleFactor;
    private float accumulatedAngle, savedAngle, increasingAngle;
    private PointF offsetFromStartPos, realPosition, startPosition;
    private BaseGLObject tile;
    private TangramAnimator animator;
    private int numberOfVertices, i;
    private float[] polyX, polyY;
    private TangramLevelPath shadowTilePath;

    //<editor-fold desc="Constructor">
    public TangramGLTile(Bitmap b, float[] x, float[] y, int index) {
        INDEX = index;
        int arrayLength = x.length;
        switch (arrayLength) {
            case SQUARE:
                tile = new TangramGLSquare(b, x, y);
                // Assign vertices clockwise, start from left top corner
                polyX = new float[]{x[0], x[1], x[1], x[0]};
                polyY = new float[]{y[0], y[0], y[1], y[1]};
                numberOfVertices = 4;
                break;
            case TRIANGLE:
                tile = new TangramGLTriangle(b, x, y);
                polyX = x;
                polyY = y;
                numberOfVertices = 3;
                break;
            case QUADRANGLE:
                tile = new TangramGLQuadrangle(b, x, y);
                polyX = x;
                polyY = y;
                numberOfVertices = 4;
        }
        init();
    }

    private void init() {
        shadowTilePath = new TangramLevelPath(polyX, polyY);
        shadowTilePath.assignPivotPoint(tile.getPivotPoint());

        // Содержит реальные координаты опорной точки (Pivot Point)
        realPosition = new PointF(tile.getPivotPoint().x, tile.getPivotPoint().y);

        // Содержит смещение опорной точки (Pivot Point) от начальной позиции, которая
        // определяется в xml и передается в базовый класс при создании.
        // Сейчас, при инициализации смещение отсутствует, т.е. равно нулю.
        offsetFromStartPos = new PointF(0, 0);

        startPosition = new PointF(tile.getPivotPoint().x, tile.getPivotPoint().y);

        animator = new TangramAnimator();
        accumulatedAngle = savedAngle = increasingAngle = 0;
    }
    //</editor-fold>

    //<editor-fold desc="Movement">
    public void offset(float dx, float dy) {
        realPosition.offset(dx, dy);
        offsetFromStartPos.offset(dx, dy);

        for (i = 0; i < polyX.length; i++) {
            polyX[i] += dx;
            polyY[i] += dy;
        }
    }

    public void moveTo(float x, float y) {
        float dx = x - realPosition.x;
        float dy = y - realPosition.y;

        for (i = 0; i < polyX.length; i++) {
            polyX[i] += dx;
            polyY[i] += dy;
        }

        offsetFromStartPos.x = x - startPosition.x;
        offsetFromStartPos.y = y - startPosition.y;

        realPosition.x = x;
        realPosition.y = y;
    }

    public boolean pointInPolygon(float x, float y) {
        int i, j=numberOfVertices-1;
        boolean result = false;

        for (i=0; i<numberOfVertices; i++) {
            if ((polyY[i] < y && polyY[j] >= y || polyY[j] < y && polyY[i] >= y) && (polyX[i] <= x || polyX[j] <= x))
                if (polyX[i] + (y - polyY[i]) / (polyY[j] - polyY[i]) * (polyX[j] - polyX[i]) < x)
                    result = !result;
            j = i;
        }

        return result;
    }
    //</editor-fold>

    //<editor-fold desc="Rotate">
    public void rotate(float angle) {
        if (animator.isAnimationStarted())
            return;

        savedAngle += increasingAngle;
        animator.setStartValue(angle);
        animator.setAnimationType(TangramAnimator.ANIM_TYPE.INV_PARABOLIC);
        animator.setDuration(ROTATING_ANIMATION_DURATION);
        animator.start();
    }

    private void rotateTile() {
        if (animator.isAnimationStarted()) {
            increasingAngle = animator.getAnimatedValue();
            accumulatedAngle = savedAngle + increasingAngle;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Scale">
    public void scale(float scaleFactor) {
        this.scaleFactor = scaleFactor;
        shadowTilePath.scale(scaleFactor);
    }
    //</editor-fold>

    public int getIndex() {
        return INDEX;
    }

    public void drawFrame(int color, float width) {
        tile.drawFrame(color, width);
    }

    public void finalizeForDraw(TextureShaderProgram textureProgram) {
        tile.castObjectSizeAutomatically();
        tile.bitmapToTexture(textureProgram);
        tile.recycleBitmap();
    }

    public void draw(float[] projectionMatrix) {
        rotateTile();
        matrixMathUtilities(projectionMatrix);
        tile.draw(modelViewProjectionMatrix);
    }

    private void matrixMathUtilities(float[] projectionMatrix) {
        android.opengl.Matrix.setIdentityM(modelMatrix, 0);
        // Rotate
        android.opengl.Matrix.translateM(modelMatrix, 0, realPosition.x, realPosition.y, 0);
        android.opengl.Matrix.rotateM(modelMatrix, 0, accumulatedAngle, 0, 0, 1);
        android.opengl.Matrix.translateM(modelMatrix, 0, -realPosition.x, -realPosition.y, 0);
        // Scale
        android.opengl.Matrix.translateM(modelMatrix, 0, realPosition.x, realPosition.y, 0);
        android.opengl.Matrix.scaleM(modelMatrix, 0, scaleFactor, scaleFactor, 1);
        android.opengl.Matrix.translateM(modelMatrix, 0, -realPosition.x, -realPosition.y, 0);
        // Move
        android.opengl.Matrix.translateM(modelMatrix, 0, offsetFromStartPos.x, offsetFromStartPos.y, 0);
        // Multiply all changes
        android.opengl.Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
    }
}
