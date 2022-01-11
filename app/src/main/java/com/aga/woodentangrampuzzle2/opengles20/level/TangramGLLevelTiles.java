package com.aga.woodentangrampuzzle2.opengles20.level;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_MOVE;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.INGAME_TILE_ROTATION_MOVEMENT_THRESHOLD;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ONE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ROTATING_ANGLE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ZERO;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.woodentangrampuzzle2.common.MultiTouchGestures;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLTile;

/**
 *
 * Created by Andrii Husiev on 18.02.2016 for Wooden Tangram.
 * This class create and draw tiles and provide interaction reaction with tiles.
 *
 */
public class TangramGLLevelTiles {
    private static final String TAG = "TangramGLLevelTiles";
    private static int TILES_NUMBER;
    private enum Rotation {NO_ROTATION, CLOCKWISE, COUNTERCLOCKWISE}
    private int selectedTile;
    private TangramGLTile[] tile;
    private PointF prevTouch, prevTouch2Finger;

    public TangramGLLevelTiles(int numberOfTiles) {
        TILES_NUMBER = numberOfTiles;
        tile = new TangramGLTile[TILES_NUMBER];
        selectedTile = -1;
        prevTouch = new PointF(0, 0);
        prevTouch2Finger = new PointF(0, 0);
    }

    //<editor-fold desc="Common Function">

    /**
     * Creates new instance of tile object. Note, that first you must convert bitmap coordinates
     * into normalized device coordinates.
     * @param b The source bitmap.
     * @param x Array of X coordinates.
     * @param y Array of Y coordinates.
     */
    public void addTile(Bitmap b, float[] x, float[] y) {
        selectedTile++;
        tile[selectedTile] = new TangramGLTile(b, x, y, selectedTile);
    }

    public void drawFrames(int color, float width) {
        for (TangramGLTile t: tile)
            t.drawFrame(color, width);
    }

    public void moveTo(int index, float x, float y) {
        tile[index].moveTo(x, y);
    }

    public void scale(float scaleFactor) {
        for (TangramGLTile t: tile)
            t.scale(scaleFactor);
    }

    public void finalizeForDraw(TextureShaderProgram textureProgram) {
        for (TangramGLTile t: tile)
            t.finalizeForDraw(textureProgram);
    }
    //</editor-fold>

    //<editor-fold desc="Touch">
    public void touch(MultiTouchGestures multiTouch) {
        switch (multiTouch.getAction()) {
            case ACTION_DOWN:
                saveStartPoint(multiTouch.getNormalizedX(ZERO), multiTouch.getNormalizedY(ZERO));
                if (isAnyTileWasSelected(multiTouch.getNormalizedX(ZERO), multiTouch.getNormalizedY(ZERO)))
                    reorderTiles();
                break;
            case ACTION_POINTER_DOWN:
                saveStartPoint2Finger(multiTouch.getNormalizedX(ONE), multiTouch.getNormalizedY(ONE));
                break;
            case ACTION_MOVE:
                if (selectedTile >= 0){
                    rotateTile(checkForRotationDirection(multiTouch));
                    updateTileDrag(multiTouch.getNormalizedX(ZERO), multiTouch.getNormalizedY(ZERO));
                    update2FingerMove(multiTouch.getNormalizedX(ONE), multiTouch.getNormalizedY(ONE));
                }
                break;
            case ACTION_UP:
                selectedTile = -1;
                // TODO: place here implementation of calculation level progress
//                if (selectedTile >= 0)
//                    calcLevelProgress();
                break;
            case ACTION_POINTER_UP:
                break;
        }
    }

    private boolean isAnyTileWasSelected(float normalizedX, float normalizedY) {
        // Чтобы сбросить выделение плитки, если ни одна не выбрана.
        selectedTile = -1;
        for (int i = 0; i < TILES_NUMBER; i++) {
            if (tile[i].pointInPolygon(normalizedX, normalizedY)) {
                selectedTile = i;
                return true;
            }
        }
        return false;
    }

    private void reorderTiles() {
        logDebugOut(TAG, "reorderTiles","Another tile was selected.");
        // Sequence of tiles in array is changed so that selected tile always be on top
        TangramGLTile[] reorderedTiles = new TangramGLTile[TILES_NUMBER];
        int shift = 1;
        reorderedTiles[0] = tile[selectedTile];
        for (int i = 0; i < TILES_NUMBER; i++) {
            if (i == selectedTile){
                shift = 0;
                continue;
            }
            reorderedTiles[i+shift] = tile[i];
        }
        tile = reorderedTiles;
        selectedTile = 0;
    }

    public void resetTilesOrder() {
        TangramGLTile[] reorderedTiles = new TangramGLTile[TILES_NUMBER];
        for (int i=0; i<TILES_NUMBER; i++) {
            reorderedTiles[tile[i].getIndex()] = tile[i];
        }
        tile = reorderedTiles;
    }

    private void saveStartPoint(float normalizedX, float normalizedY) {
        prevTouch.x = normalizedX;
        prevTouch.y = normalizedY;
    }

    private void saveStartPoint2Finger(float normalizedX, float normalizedY) {
        prevTouch2Finger.x = normalizedX;
        prevTouch2Finger.y = normalizedY;
    }

    private void updateTileDrag(float normalizedX, float normalizedY) {
        float dx = normalizedX - prevTouch.x;
        float dy = normalizedY - prevTouch.y;
        prevTouch.x = normalizedX;
        prevTouch.y = normalizedY;

        tile[selectedTile].offset(dx, dy);
//        tile[selectedTile].moveTo(normalizedX, normalizedY);
    }

    private void update2FingerMove(float normalizedX, float normalizedY) {
        prevTouch2Finger.x = normalizedX;
        prevTouch2Finger.y = normalizedY;
    }

    /**
     * Determines if a list of triangle points are in clockwise order.<ul>
     * <li>First point - previous position of finger;</li>
     * <li>Second point - current position of finger;</li>
     * <li>Third point - current position of other finger.</li></ul>
     * If the result is negative, then the triangle is oriented clockwise,
     * if it's positive, the triangle is oriented counterclockwise.
     * @param multiTouch Object that contains current position of two fingers.
     */
    private Rotation checkForRotationDirection(MultiTouchGestures multiTouch) {
        if (!multiTouch.isMultiTouch())
            return Rotation.NO_ROTATION;
        float dx = multiTouch.getNormalizedX(ONE) - prevTouch2Finger.x;
        float dy = multiTouch.getNormalizedY(ONE) - prevTouch2Finger.y;
        float x1,x2,x3,y1,y2,y3;
        x1 = prevTouch2Finger.x;
        y1 = prevTouch2Finger.y;
        x2 = multiTouch.getNormalizedX(ONE);
        y2 = multiTouch.getNormalizedY(ONE);
        x3 = multiTouch.getNormalizedX(ZERO);
        y3 = multiTouch.getNormalizedY(ZERO);
        if (movementLongEnough(lengthOfMovement(dx, dy))) {
            float sumEdges = x2*y3 - y2*x3 - x1*y3 + y1*x3 + x1*y2 - y1*x2;
            if (sumEdges < 0)
                return Rotation.CLOCKWISE;
            else
                return Rotation.COUNTERCLOCKWISE;
        }

        return Rotation.NO_ROTATION;
    }

    private static boolean movementLongEnough(float absLength) {
        return absLength > INGAME_TILE_ROTATION_MOVEMENT_THRESHOLD;
    }

    private static float lengthOfMovement(float dx, float dy) {
        return (float) Math.pow(Math.pow(dx,2)+Math.pow(dy,2), 0.5);
    }

    private void rotateTile(Rotation direction) {
        switch (direction) {
            case NO_ROTATION:
                return;
            case CLOCKWISE:
                tile[selectedTile].rotate(-ROTATING_ANGLE);
                break;
            case COUNTERCLOCKWISE:
                tile[selectedTile].rotate(ROTATING_ANGLE);
                break;
        }
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix) {
        for(int i = TILES_NUMBER - 1; i >= 0; i--) {
            tile[i].draw(projectionMatrix);
        }

    }
}
