package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

import androidx.annotation.NonNull;

import com.aga.android.util.VertexArray;

/**
 *
 * Created by Andrii Husiev on 30.12.2016.
 *
 */

public class TangramGLSquare extends BaseGLObject{

    private final static float HOR_INDENT = 0.075f;
    private final static float VERT_INDENT = 0.1f;
    private static float bitmapWidth;
    private static float bitmapHeight;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint;
    private final PointF pivotPoint;

    public TangramGLSquare(Bitmap b, RectF dst) {
        super(b);

        float[] vertex_data = {
//              Order of coordinates: X, Y, S, T
//              Triangle Fan
                dst.centerX(),  dst.centerY(), 0.5f,   0.5f,
                dst.left,       dst.bottom,      0f,     1f,
                dst.right,      dst.bottom,      1f,     1f,
                dst.right,      dst.top,         1f,     0f,
                dst.left,       dst.top,         0f,     0f,
                dst.left,       dst.bottom,      0f,     1f };
        super.setVertexArray(new VertexArray(vertex_data));

        pivotPoint = new PointF(dst.centerX(), dst.centerY());
        super.setPivotPoint(pivotPoint);

        init(b);
    }

    public TangramGLSquare(Bitmap b, float[] x, float[] y) {
        super(b);

        pivotPoint = calcPivotPoint(x, y);
        float[] vertex_data = {
//              Order of coordinates: X, Y, S, T
//              Triangle Fan
                pivotPoint.x,   pivotPoint.y,      0.5f,   0.5f,
                x[0],   y[1],      0f,     1f,
                x[1],   y[1],      1f,     1f,
                x[1],   y[0],      1f,     0f,
                x[0],   y[0],      0f,     0f,
                x[0],   y[1],      0f,     1f };
        super.setVertexArray(new VertexArray(vertex_data));

        super.setPivotPoint(pivotPoint);

        init(b);
    }

    public TangramGLSquare(Bitmap b, float width, float height) {
        super(b);
        float[] vertex_data = {
//              Order of coordinates: X, Y, S, T
//              Triangle Fan
                0f,     0f,     0.5f,   0.5f,
                -width, -height,0f,     1f,
                width,  -height,1f,     1f,
                width,  height, 1f,     0f,
                -width, height, 0f,     0f,
                -width, -height,0f,     1f };
        super.setVertexArray(new VertexArray(vertex_data));

        pivotPoint = new PointF(0, 0);
        super.setPivotPoint(pivotPoint);

        init(b);
    }

    private void init(Bitmap b) {
        bitmap = b;
        canvas = new Canvas(bitmap);
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setDither(true);
        bitmapPaint.setFilterBitmap(true);
        bitmapWidth = (float) bitmap.getWidth();
        bitmapHeight = (float) bitmap.getHeight();
//        Log.d("debug","ExtendedGLSquare.init b height == " + bitmapHeight);
    }

    private PointF calcPivotPoint(float[] x, float[] y) {
        PointF XY = new PointF();
        XY.x = x[0] + (x[1] - x[0]) / 2f;
        XY.y = y[0] + (y[1] - y[0]) / 2f;

        return XY;
    }

    public PointF getPivotPoint() {
        return pivotPoint;
    }

    public void recycleBitmap() {
        super.recycleBitmap();
        canvas = null;
        bitmapPaint = null;
    }

    //<editor-fold desc="Bitmap">
    /**
     * Draw the specified bitmap, using standard canvas' function.
     * @param b The bitmap to draw.
     * @param dst The rectangle that the bitmap will be scaled/translated to fit into.
     */
    public void addBitmap(Bitmap b, @NonNull RectF dst) {
        canvas.drawBitmap(b, null, dst, bitmapPaint);
    }

    /**
     * Draw the specified bitmap, using standard canvas' function.
     * @param b The bitmap to draw.
     * @param dst The rectangle that the bitmap will be scaled/translated to fit into.
     */
    public void addBitmap(Bitmap b, @NonNull Rect dst) {
        canvas.drawBitmap(b, null, dst, bitmapPaint);
    }

    /**
     * Draw the specified bitmap, using standard canvas' function.
     * Bitmap will be drawn without scaling in the left top corner.
     * @param b The bitmap to draw.
     */
    public void addBitmap(Bitmap b) {
        canvas.drawBitmap(b, 0, 0, bitmapPaint);
    }
    //</editor-fold>

    //<editor-fold desc="Text">
    public void addText(String text, PointF pos, Paint paint) {
        canvas.drawText(text, pos.x, pos.y, paint);
    }

    public void addText(String text, float x, float y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }

    /**
     * Рисование текста на основном битмаме. Но не стандартной функцией canvas, а сначала
     * нарисовав его на отдельном битмапе, а потом масштабируя его переносим на основной битмап.
     * Только так удалось решить вопрос с разными экранами на разных устройствах. Всё еще продолжаю
     * изобретать велосипед. Может уже пора б перейти на libGDX?
     * @param text Текст, который будет рисоваться.
     * @param heightFactor Коэффициент масштаба, меряется от высоты объекта.
     * @param offsetFromTop Расстояние по оси Y от верхнего края объекта до верхнего края текста.
     * @param textPaint Параметры рисования текста.
     */
    public void drawText(String text, float heightFactor, float offsetFromTop, Paint textPaint) {
        PointF textPos = new PointF();
        Bitmap textBitmap = createBitmapSizeFromText(text, textPaint, textPos, true);

        Canvas canvas = new Canvas(textBitmap);
        canvas.drawText(text, textPos.x, textPos.y, textPaint);

        float ratio = (float) textBitmap.getWidth() / textBitmap.getHeight();
        RectF calcRectF = getSizeAndPositionRectangle(offsetFromTop, heightFactor, ratio);

        addBitmap(textBitmap, calcRectF);
    }

    /**
     * Рисование текста на основном битмаме. Текст будет выровнен по вертикали по центру основного
     * битмапа.
     * @param text Текст, который будет рисоваться.
     * @param heightFactor Коэффициент масштаба, меряется от высоты объекта.
     * @param textPaint Параметры рисования текста.
     */
    public void drawText(String text, float heightFactor, Paint textPaint) {
        PointF textPos = new PointF();
        Bitmap textBitmap = createBitmapSizeFromText(text, textPaint, textPos, true);

        Canvas canvas = new Canvas(textBitmap);
        canvas.drawText(text, textPos.x, textPos.y, textPaint);

        float ratio = (float) textBitmap.getWidth() / textBitmap.getHeight();
        RectF calcRectF = getSizeAndPositionRectangle(-1, heightFactor, ratio);

        addBitmap(textBitmap, calcRectF);
    }

    /**
     * Создает битмап на основании параметров текста - строго по размерам.
     * Цвет - прозрачный (TRANSPARENT).
     * @param text Текст, на основании которого создается битмап.
     * @param textPaint Параметры текста, на основании которого создается битмап.
     * @param textPos Возвращает позицию для последующего рисования текста.
     * @param needScaleMarks Нужно ли использовать дополнительные средства чтобы гарантировать
     *                       верный масштаб. Если false, то в большинстве
     *                       текстов будет неверное масштабирование текста. Если true, то масштаб
     *                       будет верным, но при этом будут добавлены широкие пустые поля по бокам.
     * @return Битмап с нарисованной подложкой заголовка.
     */
    public static Bitmap createBitmapSizeFromText(String text, Paint textPaint, PointF textPos, boolean needScaleMarks) {
        Rect bounds = new Rect();
        if (needScaleMarks)
            text = "¯" + text + "_";
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        bounds.right += bounds.width() * HOR_INDENT;
        bounds.bottom += bounds.height() * VERT_INDENT;
        // Координаты центра битмапа - для будущего рисования текста.
        textPos.x = (float) bounds.width() / 2f;
        textPos.y = (float) bounds.height() / 2f - bounds.exactCenterY();
        return Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
    }

    /**
     *  Рассчитывает координаты прямоугольника, по данным параметрам. Прямоугольник находится по ширине в центре объекта.
     * @param offsetFromTop Расстояние по оси Y от верхнего края объекта до верхнего края текста.
     * @param heightFactor Коэффициент масштаба, меряется от высоты объекта.
     * @param aspectRatio Соотношение сторон, должно быть вычислено по формуле: width / height.
     * @return Прямоугольник с координатами.
     */
    private RectF getSizeAndPositionRectangle(float offsetFromTop, float heightFactor, float aspectRatio) {
        RectF calcRectF = new RectF();

        float rectHeight = bitmapHeight * heightFactor;
        if (offsetFromTop < 0)
            calcRectF.top = (bitmapHeight - rectHeight) / 2f;
        else
            calcRectF.top = bitmapHeight * offsetFromTop;
        calcRectF.bottom = calcRectF.top + rectHeight;

        float rectWidth = calcRectF.height() * aspectRatio;
        calcRectF.left = (bitmapWidth - rectWidth) / 2f;
        calcRectF.right = calcRectF.left + rectWidth;
        return calcRectF;
    }
    //</editor-fold>

    //<editor-fold desc="Draw Other Elements">
    public void drawColor(int color) {
        canvas.drawColor(color);
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * Draw the specified path, using standard canvas' function.
     * @param mainPath Main path, is drawing using paintFill.
     * @param holePath If figure has hole inside send it in this argument. If not - send null.
     * @param paintFill The paint used to draw filled figure.
     * @param paintStroke The paint used to draw line of figure.
     */
    public void drawPath(Path mainPath, Path holePath, Paint paintFill, Paint paintStroke) {
        if (holePath != null) {
            canvas.save();
            canvas.clipPath(holePath, Region.Op.DIFFERENCE);
            canvas.drawPath(mainPath, paintFill);
            canvas.restore();
        }
        else
            canvas.drawPath(mainPath, paintFill);

        canvas.drawPath(mainPath, paintStroke);
        if (holePath != null)
            canvas.drawPath(holePath, paintStroke);
    }

    public void drawFrame(int color, float width) {
        int[] x = {1, bitmap.getWidth()-1, bitmap.getWidth()-1, 1};
        int[] y = {1, 1, bitmap.getHeight()-1, bitmap.getHeight()-1};
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
    //</editor-fold>
}
