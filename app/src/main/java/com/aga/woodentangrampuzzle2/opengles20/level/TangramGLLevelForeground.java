package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

/**
 *
 * Created by Andrii Husiev on 18.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLLevelForeground extends TangramGLSquare {
//    private String levelTitle;
//    private int levelProgress;
//    private Bitmap bitmap;

    public TangramGLLevelForeground(Bitmap b, RectF dst) {
        super(b, Math.abs(dst.width()/2f), Math.abs(dst.height()/2f));

//        bitmap = b;
//        levelProgress = 0;
    }

    //<editor-fold desc="Common Function">
//    public void setLevelTitle(String levelTitle, Paint textPaint, float offsetFromTop) {
//        float x, y;
//        Rect textBounds = new Rect();
//        textPaint.getTextBounds(levelTitle, 0, levelTitle.length(), textBounds);
//        x = (float) bitmap.getWidth() / 2;
//        y = (float) bitmap.getHeight() * offsetFromTop - textBounds.exactCenterY();
//        super.addText(levelTitle, x, y, textPaint);
//    }
//
//    public void setLevelProgress(int progress) {
//        levelProgress = progress;
//    }
//
//    public int getLevelProgress() {
//        return levelProgress;
//    }
    //</editor-fold>

    @Override
    public void draw(float[] projectionMatrix) {
        super.draw(projectionMatrix);
    }
}
