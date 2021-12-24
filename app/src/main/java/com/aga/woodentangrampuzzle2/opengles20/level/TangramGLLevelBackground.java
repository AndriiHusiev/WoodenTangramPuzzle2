package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;

import com.aga.woodentangrampuzzle2.common.TangramLevelPath;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

/**
 *
 * Created by Andrii Husiev on 18.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLLevelBackground extends TangramGLSquare {
    private TangramLevelPath levelPath;
    private Paint paintPathFill, paintPathStroke;

    public TangramGLLevelBackground(Bitmap b, RectF dst) {
        super(b, Math.abs(dst.width()/2f), Math.abs(dst.height()/2f));
    }

    /**
     * Create path
     * @param x X-axis coordinates in pixels.
     * @param y Y-axis coordinates in pixels.
     */
    public void setLevelPath(int[] x, int[] y) {
        levelPath = new TangramLevelPath(x, y);
    }

    public void resizeLevelPath(float size, boolean independentResize) {
        levelPath.resize(size, independentResize);
    }

    public void scaleLevelPath(float scaleFactor) {
        levelPath.scale(scaleFactor);
    }

    /**
     * Move path to center of bounding rectangle.
     * @param bounds Bounding rectangle in pixels coordinates.
     */
    public void setLevelPathToCenter(RectF bounds) {
        levelPath.setToCenter(bounds);
    }

    public void setLevelPathColors(int fillColor, int strokeColor) {
        paintPathFill = new Paint();
        paintPathFill.setAntiAlias(true);
        paintPathFill.setDither(true);
        paintPathFill.setColor(fillColor);
        paintPathFill.setStyle(Paint.Style.FILL);

        paintPathStroke = new Paint();
        paintPathStroke.setAntiAlias(true);
        paintPathStroke.setDither(true);
        paintPathStroke.setStrokeWidth(3);
        paintPathStroke.setColor(strokeColor);
        paintPathStroke.setStyle(Paint.Style.STROKE);
    }

    public void addPathToBackground() {
        super.drawPath(levelPath.getPath(), levelPath.getPathHole(), paintPathFill, paintPathStroke);
    }

    @Override
    public void draw(float[] projectionMatrix) {
        super.draw(projectionMatrix);
    }
}
