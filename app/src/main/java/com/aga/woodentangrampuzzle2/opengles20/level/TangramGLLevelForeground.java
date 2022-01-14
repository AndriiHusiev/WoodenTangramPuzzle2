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

    public TangramGLLevelForeground(Bitmap b, RectF dst) {
        super(b, Math.abs(dst.width()/2f), Math.abs(dst.height()/2f));
    }

    @Override
    public void draw(float[] projectionMatrix) {
        super.draw(projectionMatrix);
    }
}
