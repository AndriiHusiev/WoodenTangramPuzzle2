package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import static android.graphics.Bitmap.createBitmap;
import static com.aga.android.util.ObjectBuildHelper.setPaint;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_INGAME_HEADER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.digitalTF;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;

import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.R;

public class TangramGLReusable {

    public final Bitmap bitmapMaple;
    public final Bitmap bitmapOak;
    public final Bitmap bitmapWooden;
    public final Bitmap[] digits = new Bitmap[10];
    public final Bitmap colon;
    public final Bitmap[] cups;

    public TangramGLReusable(Context context) {
        // Common bitmaps for backgrounds
        bitmapMaple = BitmapFactory.decodeResource(context.getResources(), R.drawable.maple_full);
        bitmapOak = BitmapFactory.decodeResource(context.getResources(), R.drawable.oak_full);
        bitmapWooden = BitmapFactory.decodeResource(context.getResources(), R.drawable.woodentexture);

        // Digits and colon
        String digit;
        String sample = "0";
        String strColon = " : ";
        PointF textPos = new PointF();
        Paint timerPaint = setPaint(ALL_FONTS_SIZE, COLOR_TEXT_INGAME_HEADER, false, digitalTF);
        Bitmap bitmap = createBitmapSizeFromText(sample, timerPaint, textPos, false);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < 10; i++) {
            digit = "_" + i + "_";
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawText(digit, textPos.x, textPos.y, timerPaint);
            digits[i] = createBitmap(bitmap);
        }

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawText(strColon, textPos.x, textPos.y, timerPaint);
        colon = createBitmap(bitmap);

        // Cups
        cups = new Bitmap[] {
                ObjectBuildHelper.loadBitmap(context, R.drawable.no_cup),
                ObjectBuildHelper.loadBitmap(context, R.drawable.bronze_cup),
                ObjectBuildHelper.loadBitmap(context, R.drawable.silver_cup),
                ObjectBuildHelper.loadBitmap(context, R.drawable.gold_cup)
        };
    }

}
