package com.aga.woodentangrampuzzle2.opengles20.level;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLButton;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.rectContainsPoint;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.screenRect;

/**
 *
 * Created by Andrii Husiev on 26.11.2016.
 *
 */

public class TangramGLLevelButtons {
    private static final String TAG = "TangramGLLevelButtons";
    private TangramGLButton buttonBackToMenu;
    private TangramGLButton buttonResetLevel;
    public enum SelectedButton {NOTHING, BACK, RESET}

    public TangramGLLevelButtons() {}

    //<editor-fold desc="Get-Set">
    public void setButtonBackToMenu(Bitmap bitmap, RectF dst, TextureShaderProgram textureProgram) {
        try {
            buttonBackToMenu = new TangramGLButton(bitmap, ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect));
            buttonBackToMenu.setCenterOfScaling("bottomleft");
            buttonBackToMenu.castObjectSizeAutomatically();
            buttonBackToMenu.bitmapToTexture(textureProgram);
            buttonBackToMenu.recycleBitmap();
        }
        catch (Exception ex) {
            logDebugOut(TAG, "buttonBackToMenu","catch an Exception: " + ex.getMessage());
        }
    }

    public void setButtonResetLevel(Bitmap bitmap, RectF dst, TextureShaderProgram textureProgram) {
        try {
            buttonResetLevel = new TangramGLButton(bitmap, ObjectBuildHelper.pixelsToDeviceCoords(dst, screenRect));
            buttonResetLevel.setCenterOfScaling("bottomright");
            buttonResetLevel.castObjectSizeAutomatically();
            buttonResetLevel.bitmapToTexture(textureProgram);
            buttonResetLevel.recycleBitmap();
        }
        catch (Exception ex) {
            logDebugOut(TAG, "setButtonResetLevel","catch an Exception: " + ex.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Press Buttons">
    public SelectedButton touch(float normalizedX, float normalizedY, int motionEvent) {
        SelectedButton selectedButton = SelectedButton.NOTHING;
        switch (motionEvent) {
            case MotionEvent.ACTION_DOWN:
                if (rectContainsPoint(buttonBackToMenu.getDstRect(), normalizedX, normalizedY))
                    buttonBackToMenu.setPressed(true);
                else if (rectContainsPoint(buttonResetLevel.getDstRect(), normalizedX, normalizedY))
                    buttonResetLevel.setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
                if (isPressed(buttonBackToMenu, normalizedX, normalizedY))
                    selectedButton = SelectedButton.BACK;
                else if (isPressed(buttonResetLevel, normalizedX, normalizedY))
                    selectedButton =  SelectedButton.RESET;
                unpressAllButtons();
                break;
        }
        return selectedButton;
    }

    private boolean isPressed(TangramGLButton button, float normalizedX, float normalizedY) {
        return rectContainsPoint(button.getDstRect(), normalizedX, normalizedY) && button.isPressed();
    }

    public void unpressAllButtons() {
        buttonBackToMenu.setPressed(false);
        buttonResetLevel.setPressed(false);
    }
    //</editor-fold>

    public void draw(float[] projectionMatrix) {
        buttonBackToMenu.draw(projectionMatrix);
        buttonResetLevel.draw(projectionMatrix);
    }
}
