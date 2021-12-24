package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLTile;

/**
 *
 * Created by Andrii Husiev on 18.02.2016 for Wooden Tangram.
 * Этот класс отвечает за отображение плиток танграма.
 *
 */
public class TangramGLLevelTiles {
    private static int TILES_NUMBER;
    private int selectedTile;
    private TangramGLTile[] tile;
    private PointF prevTouch;
    private float dx, dy;
    private int i;

    public TangramGLLevelTiles(int numberOfTiles) {
        TILES_NUMBER = numberOfTiles;
        tile = new TangramGLTile[TILES_NUMBER];
        selectedTile = -1;
        prevTouch = new PointF(0, 0);
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
    public void touch(float normalizedX, float normalizedY, int motionEvent) {
        switch (motionEvent) {
            case MotionEvent.ACTION_DOWN:
                saveStartPoint(normalizedX, normalizedY);
                if (isAnyTileWasSelected(normalizedX, normalizedY))
                    reorderTiles();
                break;
            case MotionEvent.ACTION_MOVE:
                if (selectedTile >= 0)
                    updateTileDrag(normalizedX, normalizedY);
                break;
            case MotionEvent.ACTION_UP:
                selectedTile = -1;
//                if (selectedTile >= 0)
//                    calcLevelProgress();
                break;
        }
    }

    private boolean isAnyTileWasSelected(float normalizedX, float normalizedY) {
        // Чтобы сбросить выделение плитки, если ни одна не выбрана.
        selectedTile = -1;
        for (i = 0; i < TILES_NUMBER; i++) {
            if (tile[i].pointInPolygon(normalizedX, normalizedY)) {
                selectedTile = i;
                return true;
            }
        }
        return false;
    }

    private void reorderTiles() {
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

    private void updateTileDrag(float normalizedX, float normalizedY) {
        dx = normalizedX - prevTouch.x;
        dy = normalizedY - prevTouch.y;
        prevTouch.x = normalizedX;
        prevTouch.y = normalizedY;

        tile[selectedTile].offset(dx, dy);
//        tile[selectedTile].moveTo(normalizedX, normalizedY);
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix) {
        for(i = TILES_NUMBER - 1; i >= 0; i--){
            tile[i].draw(projectionMatrix);
//            tile[i].drawInversedScaling(projectionMatrix);
//            tile[i].drawNoScale(projectionMatrix);
        }

    }
}
