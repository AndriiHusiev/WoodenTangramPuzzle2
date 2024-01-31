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
    private final PointF prevTouch, prevTouch2Finger;
    public PointF[] levelDots;

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

    public void setLevelPoints(float[] px, float[] py) {
        levelDots = new PointF[px.length];
        for (int i = 0; i < px.length; i++)
            levelDots[i] = new PointF(px[i], py[i]);
    }
    //</editor-fold>

    //<editor-fold desc="Touch">
    public boolean touch(MultiTouchGestures multiTouch) {
        boolean result = false;

        switch (multiTouch.getAction()) {
            case ACTION_DOWN:
//                logDebugOut(TAG, "touch coords", multiTouch.getNormalizedX(ZERO) + " " + multiTouch.getNormalizedY(ZERO));
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
                if (selectedTile >= 0)
                    result = magnetizeTile(tile[selectedTile]);
                selectedTile = -1;
                break;
            case ACTION_POINTER_UP:
                break;
        }

        return result;
    }

    private boolean isAnyTileWasSelected(float normalizedX, float normalizedY) {
        selectedTile = isPointInTile(normalizedX, normalizedY, false);
        return selectedTile != -1;
    }

    public int isPointInTile(float normalizedX, float normalizedY, boolean magnet) {
        for (int i = 0; i < TILES_NUMBER; i++) {
            if (tile[i].pointInPolygon(normalizedX, normalizedY) && (!magnet || tile[i].isMagnetized()))
                return i;
        }
        return -1;
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

    /**
     * Calculate the distance from the level points to the corners of the dropped tile.
     * And magnetize tile to the first point, which is within the sensitivity zone.
     * @param t Just dropped tile
     */
    private boolean magnetizeTile(TangramGLTile t) {
        float TOO_FAR = 1000;
        float minDist = TOO_FAR;
        PointF result = new PointF();

        // Min distance to level dots
        for (PointF levelDot : levelDots) {
            PointF offset = t.isPointNearPolygon(levelDot.x, levelDot.y);
            if (offset != null) {
                float dist = (float) Math.hypot(offset.x, offset.y);
                if (dist < minDist) {
                    minDist = dist;
                    result.set(offset.x, offset.y);
                }
            }
        }

        // Min distance to other tiles dots
        for (TangramGLTile tt: tile) {
            if (tt.getIndex() != t.getIndex()) {
                for (PointF d: tt.dots) {
                    PointF offset = t.isPointNearPolygon(d.x, d.y);
                    if (offset != null) {
                        float dist = (float) Math.hypot(offset.x, offset.y);
                        if (dist < minDist) {
                            minDist = dist;
                            result.set(offset.x, offset.y);
                        }
                    }
                }
            }
        }

        if (minDist == TOO_FAR) {
            t.setMagnetized(false);
            return false;
        } else {
            t.offset(result.x, result.y);
            t.setMagnetized(true);
            return true;
        }
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix) {
        for(int i = TILES_NUMBER - 1; i >= 0; i--) {
            tile[i].draw(projectionMatrix);
        }

    }
}
