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

public class TangramGLQuadrangle extends BaseGLObject {
    private Bitmap bitmap;
    private Canvas canvas;
    private PointF pivotPoint;

    public TangramGLQuadrangle(Bitmap b, float[] x, float[] y) {
        super(b);

        float[] s = {0.5f,   0,      0.333f, 1, 0.667f, 0};
        float[] t = {0.167f, 0.333f, 0,      0, 0.333f, 0.333f};
        PointF XY = new PointF();
        XY.x = x[0] + (x[2] - x[0]) / 2f;
        XY.y = y[0] + (y[2] - y[0]) / 2f;

        float[] vertex_data = {
//              Order of coordinates: X, Y, S, T
//              Triangle Fan
                XY.x,   XY.y,   s[0],   t[0],
                x[0],   y[0],   s[1],   t[1],
                x[1],   y[1],   s[2],   t[2],
                x[2],   y[2],   s[3],   t[3],
                x[3],   y[3],   s[4],   t[4],
                x[0],   y[0],   s[5],   t[5]};
        super.setVertexArray(new VertexArray(vertex_data));

        pivotPoint = XY;
        super.setPivotPoint(pivotPoint);

        init(b);
    }

    private void init(Bitmap b) {
        bitmap = b;
        canvas = new Canvas(bitmap);
    }

    public PointF getPivotPoint() {
        return pivotPoint;
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void drawFrame(int color, float width) {
        int[] x = {0, 2*bitmap.getWidth()/3, bitmap.getWidth(), bitmap.getWidth()/3};
        int[] y = {bitmap.getHeight()/3, bitmap.getHeight()/3, 0, 0};
        Paint paintTileFrame = new Paint();
        paintTileFrame.setAntiAlias(true);
        paintTileFrame.setDither(true);
        paintTileFrame.setStrokeWidth(width);
        paintTileFrame.setColor(color);
        paintTileFrame.setStyle(Paint.Style.STROKE);

        canvas.drawLine(x[0], y[0], x[1], y[1], paintTileFrame);
        canvas.drawLine(x[1], y[1], x[2], y[2], paintTileFrame);
        canvas.drawLine(x[2], y[2], x[3], y[3], paintTileFrame);
        canvas.drawLine(x[3], y[3], x[0], y[0], paintTileFrame);
    }

    public void recycleBitmap() {
        super.recycleBitmap();
        canvas = null;
    }
}
