package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.aga.android.util.VertexArray;

/**
 *
 * Created by Andrii Husiev on 30.12.2016.
 *
 */

public class TangramGLTriangle extends BaseGLObject{
    private Bitmap bitmap;
    private Canvas canvas;
    private PointF pivotPoint;

    public TangramGLTriangle(Bitmap b, float[] x, float[] y) {
        super(b);

        float[] s = {0, 1, 1};
        float[] t = {0, 1, 0};

        PointF XY = calcCentroidOfTriangle(x, y);
        PointF ST = calcCentroidOfTriangle(s, t);

        float[] vertex_data = {
//              Order of coordinates: X, Y, S, T
//              Triangle Fan
                XY.x,   XY.y,   ST.x,   ST.y,
                x[0],   y[0],   s[0],   t[0],
                x[1],   y[1],   s[1],   t[1],
                x[2],   y[2],   s[2],   t[2],
                x[0],   y[0],   s[0],   t[0]};
        super.setVertexArray(new VertexArray(vertex_data));

        pivotPoint = new PointF(XY.x, XY.y);
        super.setPivotPoint(pivotPoint);

        init(b);
    }

    private void init(Bitmap b) {
        bitmap = b;
        canvas = new Canvas(bitmap);
    }

    private PointF calcCentroidOfTriangle(float[] x, float[] y) {
        PointF centroid = new PointF();
        centroid.x = (x[0] + x[1] + x[2]) / 3f;
        centroid.y = (y[0] + y[1] + y[2]) / 3f;
        return centroid;
    }

    public PointF getPivotPoint() {
        return pivotPoint;
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void drawFrame(int color, float width) {
        int[] x = {1, bitmap.getWidth()-1, bitmap.getWidth()-1};
        int[] y = {1, bitmap.getHeight()-1, 1};
        Paint paintTileFrame = new Paint();
        paintTileFrame.setAntiAlias(true);
        paintTileFrame.setDither(true);
        paintTileFrame.setStrokeWidth(width);
        paintTileFrame.setColor(color);
        paintTileFrame.setStyle(Paint.Style.STROKE);

        canvas.drawLine(x[0], y[0], x[1], y[1], paintTileFrame);
        canvas.drawLine(x[1], y[1], x[2], y[2], paintTileFrame);
        canvas.drawLine(x[2], y[2], x[0], y[0], paintTileFrame);
    }

    public void recycleBitmap() {
        super.recycleBitmap();
        canvas = null;
    }
}
