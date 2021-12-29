package com.aga.woodentangrampuzzle2.common;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

/**
 *
 * Created by Andrii Husiev on 18.11.2016.
 *
 */

public class TangramLevelPath {
    private float[] x;
    private float[] y;
    private Path path;
    private Path pathHole;
    private RectF pathBounds;
    private PointF pivotPoint;
    private boolean hasHole;
    private Region tileRegion, clipRegion;

    //<editor-fold desc="Public Functions">
    public TangramLevelPath(int[] x, int[] y) {
        int n = x.length;
        this.x = new float[n];
        this.y = new float[n];
        hasHole = false;

        for (int i = 0; i < n; i++) {
            this.x[i] = (float) x[i];
            this.y[i] = (float) y[i];
            if (x[i] == 0)
                hasHole = true;
        }

        init();
    }

    public TangramLevelPath(float[] x, float[] y) {
        this.x = x;
        this.y = y;
        hasHole = false;

        init();
    }

    private void init() {
        tileRegion = new Region();
        clipRegion = new Region();
        calcBounds();
        setPivotPoint();
        xyToPath();
    }

    public void scale(float scaleFactor) {
        float xx, yy;

        for (int i = 0; i < x.length; i++) {
            if (x[i] == 0) continue;
            xx = x[i] - pivotPoint.x;
            yy = y[i] - pivotPoint.y;
            xx *= scaleFactor;
            yy *= scaleFactor;
            x[i] = xx + pivotPoint.x;
            y[i] = yy + pivotPoint.y;
        }

        calcBounds();
        xyToPath();
    }

    /**
     * Centers the polygon to given rectangle.
     *
     * @param bounds Bounds of work area.
     */
    public void setToCenter(RectF bounds) {
        // Offset between pivot point and center.
        float offsetX = bounds.centerX() - pivotPoint.x;
        float offsetY = bounds.centerY() - pivotPoint.y;

        offset(offsetX, offsetY);
        calcBounds();
        setPivotPoint();
        xyToPath();
    }

    /**
     * Change size of polygon to the given size.
     *
     * @param size Bounding value for height and width of polygon.
     * @param independentResize If true both parameters (width and height) will be scaled to fit to square with side equals "size".
     *                          If false they will be scaled to fit to given size, saving aspect ratio.
     */
    public void resize(float size, boolean independentResize) {
        float dx, dy;
        float scaleFactorX, scaleFactorY;
        if (independentResize) {
            scaleFactorX = size / pathBounds.width();
            scaleFactorY = size / pathBounds.height();
        }
        else {
            if (pathBounds.width() > pathBounds.height())
                scaleFactorX = scaleFactorY = size / pathBounds.width();
            else
                scaleFactorX = scaleFactorY = size / pathBounds.height();
        }

        for (int i = 0; i < x.length; i++) {
            if (x[i] == 0) continue;
            dx = x[i] - pivotPoint.x;
            dy = y[i] - pivotPoint.y;
            dx *= scaleFactorX;
            dy *= scaleFactorY;
            x[i] = dx + pivotPoint.x;
            y[i] = dy + pivotPoint.y;
        }

        calcBounds();
        xyToPath();
    }

    public Path getPath() {
        return path;
    }

    public Path getPathHole() {
        return pathHole;
    }

    public RectF getPathBounds() {
        return pathBounds;
    }

    public boolean hasHole() {
        return hasHole;
    }

    /**
     * Assign pivot point from outer source. Overrides initial inner calculations.
     * Be aware: it can be overwritten by the setToCenter() function.
     * @param pivotPoint Contains new values of pivot point.
     */
    public void assignPivotPoint(PointF pivotPoint) {
        this.pivotPoint = new PointF(pivotPoint.x, pivotPoint.y);
    }
    //</editor-fold>

    //<editor-fold desc="Auxiliary private functions">
    private void offset(float dx, float dy) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 0) continue;
            x[i] += dx;
            y[i] += dy;
        }
    }

    private void calcBounds() {
        pathBounds = new RectF(x[0], y[0], x[0], y[0]);

        for (int i = 1; i < x.length; i++) {
            if (x[i] == 0) continue;
            pathBounds.left = Math.min(pathBounds.left, x[i]);
            pathBounds.top = Math.min(pathBounds.top, y[i]);
            pathBounds.right = Math.max(pathBounds.right, x[i]);
            pathBounds.bottom = Math.max(pathBounds.bottom, y[i]);
        }
    }

    private void setPivotPoint() {
        pivotPoint = new PointF(0, 0);

        pivotPoint.x = pathBounds.left + pathBounds.width() / 2;
        pivotPoint.y = pathBounds.top + pathBounds.height() / 2;

    }

    private void xyToPath() {
        boolean initPathHole = false;

        pathHole = null;
        path = null;
        path = new Path();
        path.moveTo(x[0],y[0]);
        int n = x.length;

        for (int i = 1; i < n; i++) {
            if (x[i] == 0) {
                path.close();
                i++;
                pathHole = new Path();
                pathHole.moveTo(x[i],y[i]);
                initPathHole = true;
            }
            if (!initPathHole)
                path.lineTo(x[i], y[i]);
            else
                pathHole.lineTo(x[i], y[i]);
        }

        if (pathHole == null)
            path.close();
        else
            pathHole.close();
    }
    //</editor-fold>
}
