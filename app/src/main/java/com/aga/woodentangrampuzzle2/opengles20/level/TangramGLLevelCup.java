package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;

/**
 *
 * Created by Andrii Husiev on 26.11.2016.
 *
 */

public class TangramGLLevelCup {
    private static final int TOTAL_CUPS_AMOUNT = 4;

    private int reachedCup;
    private TangramGLSquare[] cup;

    public TangramGLLevelCup() {
        reachedCup = 0;
        cup = new TangramGLSquare[TOTAL_CUPS_AMOUNT];
    }

    public void setReachedCup(int value) {
        reachedCup = value;
    }

    public int getReachedCup() {
        return reachedCup;
    }

    public void addCup(Bitmap bitmap, RectF dst, int index, TextureShaderProgram textureProgram) {
        try {
            cup[index] = new TangramGLSquare(bitmap, ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect));
            cup[index].castObjectSizeAutomatically();
            cup[index].bitmapToTexture(textureProgram);
            cup[index].recycleBitmap();
        }
        catch (Exception ex) {
            Log.d("debug","TangramGLLevelCup catch an Exception: " + ex.getMessage());
        }
    }

    public void draw(float[] projectionMatrix) {
        cup[reachedCup].draw(projectionMatrix);
    }
}
