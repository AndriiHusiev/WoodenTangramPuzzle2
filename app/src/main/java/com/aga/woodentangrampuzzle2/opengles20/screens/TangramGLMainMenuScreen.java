package com.aga.woodentangrampuzzle2.opengles20.screens;

import static com.aga.android.util.ObjectBuildHelper.createTiledBitmap;
import static com.aga.android.util.ObjectBuildHelper.getSizeAndPositionRectangle;
import static com.aga.android.util.ObjectBuildHelper.getWoodShader;
import static com.aga.android.util.ObjectBuildHelper.rectContainsPoint;
import static com.aga.android.util.ObjectBuildHelper.setPaint;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ALL_FONTS_SIZE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_INGAME_HEADER;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.COLOR_TEXT_ON_BUTTONS;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_GAP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_BUTTON_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_TITLE_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_TITLE_OFFSET_FROM_TOP;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_VERSION_TEXT_HEIGHT;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.MM_VERSION_TEXT_OFFSET;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.ASPECT_RATIO;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.textureProgram;
import static com.aga.woodentangrampuzzle2.opengles20.TangramGLRenderer.Mode;
import static com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare.createBitmapSizeFromText;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.aga.android.util.ObjectBuildHelper;
import com.aga.woodentangrampuzzle2.R;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLButton;
import com.aga.woodentangrampuzzle2.opengles20.baseobjects.TangramGLSquare;

/**
 *
 * Created by Andrii Husiev on 23.11.2021.
 * Этот объект отвечает за отображение и взаимодействие всех элементов главного меню.
 *
 */

public class TangramGLMainMenuScreen {
    private Context context;
    private RectF screenRect;
    private TangramGLSquare imageMenuBackground;
    private TangramGLSquare imageMenuHeader;
    private TangramGLButton buttonStart, buttonCredits, buttonExit;

    public TangramGLMainMenuScreen(Context context, RectF screenRect) {
        setLocalVariables(context, screenRect);
        setBackground();
        setHeader();
        setButtonStart();
        setButtonCredits();
        setButtonExit();
    }

    //<editor-fold desc="Initialization">
    private void setLocalVariables(Context context, RectF screenRect) {
        this.context = context;
        this.screenRect = new RectF();
        this.screenRect.left = screenRect.left;
        this.screenRect.top = screenRect.top;
        this.screenRect.right = screenRect.right;
        this.screenRect.bottom = screenRect.bottom;
    }

    private void setBackground() {
        Bitmap bitmapBackground = createTiledBitmap(context, screenRect, R.drawable.maple_full);
        Bitmap bitmapVersion = getVersion();

        imageMenuBackground = new TangramGLSquare(bitmapBackground, ASPECT_RATIO, 1.0f);
        if (bitmapVersion != null) {
            RectF bitmapVersionCoords = getSizeAndPositionRectangle("bottomright", MM_VERSION_TEXT_OFFSET, MM_VERSION_TEXT_OFFSET, MM_VERSION_TEXT_HEIGHT, (float) bitmapVersion.getWidth() / bitmapVersion.getHeight());
            imageMenuBackground.addBitmap(bitmapVersion, bitmapVersionCoords);
        }
        imageMenuBackground.castObjectSizeAutomatically();
        imageMenuBackground.bitmapToTexture(textureProgram);
        imageMenuBackground.recycleBitmap();
    }

    private void setHeader() {
        Paint textPaint = setPaint(ALL_FONTS_SIZE, Color.BLACK, true, Typeface.DEFAULT_BOLD);
        PointF textPos = new PointF();
        String text = context.getResources().getString(R.string.app_name);
        Bitmap b = createBitmapSizeFromText(text, textPaint, textPos, true);
        RectF bitmapCoords = getSizeAndPositionRectangle("centerheight", MM_TITLE_OFFSET_FROM_TOP, 0, MM_TITLE_HEIGHT, (float) b.getWidth() / b.getHeight());

        imageMenuHeader = new TangramGLSquare(b, ObjectBuildHelper.pixelsToDeviceCoords(bitmapCoords, screenRect));
        imageMenuHeader.drawColor(Color.TRANSPARENT);
        imageMenuHeader.addText(text, textPos.x, textPos.y, textPaint);
        textPaint.setShadowLayer(0, 0, 0, 0);
        textPaint.setShader(getWoodShader(context));
        imageMenuHeader.addText(text, textPos.x, textPos.y, textPaint);
        imageMenuHeader.castObjectSizeAutomatically();
        imageMenuHeader.bitmapToTexture(textureProgram);
        imageMenuHeader.recycleBitmap();
    }

    private void setButtonStart() {
        buttonStart = createButtonWithBackground(context, screenRect, R.drawable.button01, MM_BUTTON_OFFSET_FROM_TOP);
        setButtonTitle(context, buttonStart, R.string.button_MM_start);
        buttonStart.castObjectSizeAutomatically();
        buttonStart.bitmapToTexture(textureProgram);
        buttonStart.recycleBitmap();
    }

    private void setButtonCredits() {
        buttonCredits = createButtonWithBackground(context, screenRect, R.drawable.button01, MM_BUTTON_OFFSET_FROM_TOP + MM_BUTTON_HEIGHT + MM_BUTTON_GAP);
        setButtonTitle(context, buttonCredits, R.string.button_MM_credits);
        buttonCredits.castObjectSizeAutomatically();
        buttonCredits.bitmapToTexture(textureProgram);
        buttonCredits.recycleBitmap();
    }

    private void setButtonExit() {
        buttonExit = createButtonWithBackground(context, screenRect, R.drawable.button01, MM_BUTTON_OFFSET_FROM_TOP + MM_BUTTON_HEIGHT * 2 + MM_BUTTON_GAP * 2);
        setButtonTitle(context, buttonExit, R.string.button_MM_exit);
        buttonExit.castObjectSizeAutomatically();
        buttonExit.bitmapToTexture(textureProgram);
        buttonExit.recycleBitmap();
    }

    private Bitmap getVersion() {
        String version = getApplicationVersion();
        if (version == null)
            return null;
        Paint textPaint = setPaint(ALL_FONTS_SIZE, COLOR_TEXT_INGAME_HEADER, true, Typeface.DEFAULT_BOLD);
        PointF textPos = new PointF();
        Bitmap b = createBitmapSizeFromText(version, textPaint, textPos, true);
        Canvas canvas = new Canvas(b);
        canvas.drawText(version, textPos.x, textPos.y, textPaint);
        return b;
    }

    private String getApplicationVersion() {
        String version = null;
        try {
            Activity activity = (Activity) context;
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            version = "v" + pInfo.versionName + " (" + pInfo.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    //</editor-fold>

    public Mode touch(float normalizedX, float normalizedY, int motionEvent) {
        Mode playMode = Mode.MAIN_MENU;
        switch (motionEvent) {
            case MotionEvent.ACTION_DOWN:
                if (rectContainsPoint(buttonStart.getDstRect(), normalizedX, normalizedY)) {
                    buttonStart.setPressed(true);
                }
                else if (rectContainsPoint(buttonCredits.getDstRect(), normalizedX, normalizedY)) {
                    buttonCredits.setPressed(true);
                }
                else if (rectContainsPoint(buttonExit.getDstRect(), normalizedX, normalizedY)) {
                    buttonExit.setPressed(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (rectContainsPoint(buttonStart.getDstRect(), normalizedX, normalizedY) && buttonStart.isPressed()) {
//                    gapAfterChangeMode = System.currentTimeMillis();
                    playMode = Mode.LEVELS_SET_SELECTION;
                }
                else if (rectContainsPoint(buttonCredits.getDstRect(), normalizedX, normalizedY) && buttonCredits.isPressed()) {
                }
                else if (rectContainsPoint(buttonExit.getDstRect(), normalizedX, normalizedY) && buttonExit.isPressed()) {
                    exitApplication();
                }
                buttonStart.setPressed(false);
                buttonCredits.setPressed(false);
                buttonExit.setPressed(false);
                break;
        }
        return playMode;
    }

    public void draw(float[] projectionMatrix) {
        imageMenuBackground.draw(projectionMatrix);
        imageMenuHeader.draw(projectionMatrix);
        buttonStart.draw(projectionMatrix);
        buttonCredits.draw(projectionMatrix);
        buttonExit.draw(projectionMatrix);
    }

    private void exitApplication() {
        Activity activity = (Activity) context;
        activity.finish();
        System.exit(0);
    }

    //<editor-fold desc="Static">
    private static TangramGLButton createButtonWithBackground(Context context, RectF screenRect, int drawable_id, float distanceFactor) {
        Bitmap b = ObjectBuildHelper.loadBitmap(context, drawable_id);
        Rect bitmapRect = new Rect(0, 0, b.getWidth(), b.getHeight());
        float ratio = (float) b.getWidth()/b.getHeight();
        RectF buttonRect = getSizeAndPositionRectangle("centerheight", distanceFactor, 0, MM_BUTTON_HEIGHT, ratio);
        TangramGLButton button = new TangramGLButton(Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ARGB_8888), ObjectBuildHelper.pixelsToDeviceCoords(buttonRect, screenRect));
        button.addBitmap(b, bitmapRect);

        return button;
    }

    private static void setButtonTitle(Context context, TangramGLButton button, int title_id) {
        String title = context.getResources().getString(title_id);
//        String title = "¯Start_";
        Paint textPaint = setPaint(ALL_FONTS_SIZE, COLOR_TEXT_ON_BUTTONS, true, Typeface.DEFAULT_BOLD);
        button.drawText(title, MM_BUTTON_TEXT_HEIGHT, textPaint);
    }
    //</editor-fold>
}
