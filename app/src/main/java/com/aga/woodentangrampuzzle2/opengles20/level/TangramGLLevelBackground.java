package com.aga.woodentangrampuzzle2.opengles20.level;

import static com.aga.android.util.ObjectBuildHelper.xPixelsToDeviceCoords;
import static com.aga.android.util.ObjectBuildHelper.yPixelsToDeviceCoords;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.ASPECT_RATIO;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
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

    public PointF[] progressDots;

    public TangramGLLevelBackground(Bitmap b, RectF dst) {
        super(b, Math.abs(dst.width()/2f), Math.abs(dst.height()/2f));
    }

    public TangramGLLevelBackground(Bitmap b) {
        super(b, ASPECT_RATIO, 1.0f);
    }

    /**
     * Create path
     * @param x X-axis coordinates in pixels.
     * @param y Y-axis coordinates in pixels.
     */
    public void setLevelPath(int[] x, int[] y, int[] d) {
        levelPath = new TangramLevelPath(x, y);

        progressDots = new PointF[d.length / 2];
        for (int i = 0; i < d.length; i+=2)
            progressDots[i/2] = new PointF(d[i], d[i + 1]);
    }

    public float[] getNormalizedPointsX() {
        float[] x = levelPath.getPathX();
        for (int i = 0; i < x.length; i++) {
            x[i] = xPixelsToDeviceCoords(x[i], screenRect);
        }
        return x;
    }

    public float[] getNormalizedPointsY() {
        float[] y = levelPath.getPathY();
        for (int i = 0; i < y.length; i++) {
            y[i] = yPixelsToDeviceCoords(y[i], screenRect);
        }
        return y;
    }

    public PointF[] getProgressDots() {
        PointF[] pd = new PointF[progressDots.length];
        for (int i = 0; i < pd.length; i++) {
            pd[i] = new PointF(
                    xPixelsToDeviceCoords(progressDots[i].x, screenRect),
                    yPixelsToDeviceCoords(progressDots[i].y, screenRect)
            );
        }
        return pd;
    }

    public void resizeLevelPath(float size, boolean independentResize) {
        levelPath.resize(size, independentResize);
    }

    public void scaleLevelPath(float scaleFactor) {
        levelPath.scale(scaleFactor);
        scaleProgressDots(scaleFactor);
    }

    private void scaleProgressDots(float scaleFactor) {
        float dx, dy;
        for (PointF progressDot : progressDots) {
            dx = progressDot.x - levelPath.getPathBounds().centerX();
            dy = progressDot.y - levelPath.getPathBounds().centerY();
            dx *= scaleFactor;
            dy *= scaleFactor;
            progressDot.x = dx + levelPath.getPathBounds().centerX();
            progressDot.y = dy + levelPath.getPathBounds().centerY();
        }
    }

    /**
     * Move path to center of bounding rectangle.
     * @param bounds Bounding rectangle in pixels coordinates.
     */
    public void setLevelPathToCenter(RectF bounds) {
        float offsetX = bounds.centerX() - levelPath.getPathBounds().centerX();
        float offsetY = bounds.centerY() - levelPath.getPathBounds().centerY();
        for (PointF progressDot : progressDots)
            progressDot.offset(offsetX, offsetY);

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
