package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.pixelsToDeviceCoords;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.aga.android.programs.TextureShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Andrii Husiev on 17.12.2023 for Wooden Tangram.
 *
 */
public class TangramGLButtonExt {
    private static final float SCALE_FACTOR = 0.9f;
    private static final int OFFS = 0;
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private boolean isPressed, isLocked;
    private final RectF screenRect;
    private final List<RectF> dst = new ArrayList<>();
    private final List<PointF> centerOfScaling = new ArrayList<>();
    private int selCup;
    private final List<TangramGLSquare> textures = new ArrayList<>();
    private TangramGLSquare enabledTexture, disabledTexture;
    private RectF dstEnabled, dstDisabled, dstCup;
    private PointF centerEnabled, centerDisabled, centerCup;
    private TangramGLSquare[] digits, cups;
    private final RectF[] dstDigits = new RectF[4];
    private final PointF[] centerDigits = new PointF[4];
    private final int[] selDigits = new int[] {0, 0, 0, 0};
    private TextureShaderProgram enabledShaderProgram, disabledShaderProgram;

    /**
     * Creates new instance of Extended TangramGLButton.
     * @param scr The screen rectangle.
     */
    public TangramGLButtonExt(RectF scr) {
        screenRect = new RectF(scr.left, scr.top, scr.right, scr.bottom);
        isLocked = false;
        isPressed = false;
    }

    /**
     * Add new texture placed in the given position.
     * @param b The bitmap for new texture.
     * @param dstPos The rectangle with position in pixels.
     */
    public void addTexture(Bitmap b, RectF dstPos) {
        RectF pos = pixelsToDeviceCoords(dstPos, screenRect);
        dst.add(pos);
        textures.add(createTexture(b, pos, true));
        centerOfScaling.add(setCenterOfScaling(pos));
    }

    /**
     * Add new texture with offset from first one. This texture will not be added if there is
     * no previously added another texture.
     * @param b The bitmap for new texture.
     * @param xOffset The X offset from first texture.
     * @param yOffset The Y offset from first texture.
     * @param scaleFactor Scale factor for both dimensions.
     */
    public void addTexture(Bitmap b, float xOffset, float yOffset, float scaleFactor) {
        if (dst.size() == 0) return;

		RectF pos = convertOffsetToRect(b.getWidth(), b.getHeight(), xOffset, yOffset, scaleFactor);
        dst.add(pos);
        textures.add(createTexture(b, pos, true));
        centerOfScaling.add(setCenterOfScaling(pos));
    }

    /**
     * Add a special texture which is shown only if button is not locked.
     * This texture will not be added if there is no previously added another texture.
     * @param b The bitmap for new texture.
     * @param xOffset The X offset from first texture.
     * @param yOffset The Y offset from first texture.
     * @param scaleFactor Scale factor for both dimensions.
     */
    public void addEnabledTexture(Bitmap b, float xOffset, float yOffset, float scaleFactor) {
        if (dst.size() == 0) return;

        dstEnabled = convertOffsetToRect(b.getWidth(), b.getHeight(), xOffset, yOffset, scaleFactor);
        enabledTexture = createTexture(b, dstEnabled, true);
        centerEnabled = setCenterOfScaling(dstEnabled);
    }

    /**
     * Add a special texture which is shown only if button is locked.
     * This texture will not be added if there is no previously added another texture.
     * @param b The bitmap for new texture.
     * @param xOffset The X offset from first texture.
     * @param yOffset The Y offset from first texture.
     * @param scaleFactor Scale factor for both dimensions.
     */
    public void addDisabledTexture(Bitmap b, float xOffset, float yOffset, float scaleFactor) {
        if (dst.size() == 0) return;

        dstDisabled = convertOffsetToRect(b.getWidth(), b.getHeight(), xOffset, yOffset, scaleFactor);
        disabledTexture = createTexture(b, dstDisabled, true);
        centerDisabled = setCenterOfScaling(dstDisabled);
    }

    public void addDigitsTexture(Bitmap[] b, float xOffset, float yOffset, float scaleFactor) {
        RectF rect = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), 0, 0, scaleFactor);
        digits = new TangramGLSquare[b.length];

        for (int i = 0; i < b.length; i++)
            digits[i] = createTexture(b[i], rect, true);

        dstDigits[0] = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), -2*xOffset, yOffset, scaleFactor);
        dstDigits[1] = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), -xOffset, yOffset, scaleFactor);
        dstDigits[2] = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), xOffset, yOffset, scaleFactor);
        dstDigits[3] = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), 2*xOffset, yOffset, scaleFactor);

        for (int i = 0; i < dstDigits.length; i++)
            centerDigits[i] = setCenterOfScaling(dstDigits[i]);
    }

    public void addCupsTexture(Bitmap[] b, float xOffset, float yOffset, float scaleFactor) {
        RectF rect = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), 0, 0, scaleFactor);
        cups = new TangramGLSquare[b.length];

        for (int i = 0; i < b.length; i++)
            cups[i] = createTexture(b[i], rect, true);

        dstCup = convertOffsetToRect(b[0].getWidth(), b[0].getHeight(), xOffset, yOffset, scaleFactor);
        centerCup = setCenterOfScaling(dstCup);
    }

    /**
     * Convert pixel offset to normalized absolute position of texture.
     * @param width Bitmap width.
     * @param height Bitmap height.
     * @param xOffset Horizontal offset in pixels.
     * @param yOffset Vertical offset in pixels.
     * @return Rectangle with normalized absolute coordinates for texture.
     */
    private RectF convertOffsetToRect(float width, float height, float xOffset, float yOffset, float scaleFactor) {
        RectF rect = new RectF(0, 0, width*scaleFactor, height*scaleFactor);
        rect.offset(
                screenRect.width()/2f - width*scaleFactor/2f + xOffset,
                screenRect.height()/2f - height*scaleFactor/2f + yOffset
        );
        RectF pos = pixelsToDeviceCoords(rect, screenRect);
        pos.offset(dst.get(0).centerX(), dst.get(0).centerY());
        return pos;
    }

    private PointF setCenterOfScaling(RectF pos) {
        if (dst.size() != 0 && centerOfScaling.size() != 0) {
            PointF center = new PointF(centerOfScaling.get(0).x, centerOfScaling.get(0).y);
            center.offset(dst.get(0).centerX() - pos.centerX(), dst.get(0).centerY() - pos.centerY());
            return center;
        } else
            return new PointF(0, 0);
    }

    /**
     * Create texture from bitmap.
     * @param b The bitmap for new texture.
     * @param dst The rectangle with normalized position.
     * @param recycle Will the bitmap be recycled.
     * @return Created texture.
     */
    public static TangramGLSquare createTexture(Bitmap b, RectF dst, boolean recycle) {
        TangramGLSquare tex = new TangramGLSquare(b, Math.abs(dst.width()/2f), Math.abs(dst.height()/2f));
        tex.castObjectSizeAutomatically();
        tex.bitmapToTexture();
        if (recycle) tex.recycleBitmap();
        return tex;
    }

    public void setShaders(TextureShaderProgram enabled, TextureShaderProgram disabled) {
        enabledShaderProgram = enabled;
        disabledShaderProgram = disabled;
        setActualShaderProgram();
    }

    public void replaceTexture(int i, Bitmap b) {
        textures.get(i).replaceTexture(b);
    }

    public void setDigits(int[] t) {
        System.arraycopy(t, 0, selDigits, 0, selDigits.length);
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
        setActualShaderProgram();
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setCup(int value) {
        selCup = value;
    }

    public int getCup() {
        return selCup;
    }

    public RectF getDstRect() {
        if (dst.size() == 0)
            return null;
        else
            return dst.get(0);
    }

    public void offset(float dx, float dy) {
        for (RectF rect: dst) rect.offset(dx, dy);
        if (dstEnabled != null) dstEnabled.offset(dx, dy);
        if (dstDisabled != null) dstDisabled.offset(dx, dy);
        if (dstCup != null) dstCup.offset(dx, dy);
        for (RectF rect: dstDigits) if (rect != null) rect.offset(dx, dy);
    }

    private void setActualShaderProgram() {
        TextureShaderProgram curShader = isLocked ? disabledShaderProgram : enabledShaderProgram;
        for (TangramGLSquare tex: textures)
            tex.setShader(curShader);
        if (disabledTexture != null)
            disabledTexture.setShader(curShader);
        if (enabledTexture != null)
            enabledTexture.setShader(curShader);
        if (enabledTexture != null)
            enabledTexture.setShader(curShader);
        if (cups != null)
            for (TangramGLSquare tex: cups)
                tex.setShader(curShader);
        if (digits != null)
            for (TangramGLSquare tex: digits)
                tex.setShader(curShader);
    }

    public void draw(float[] projectionMatrix) {
        // Use special blend mode for translucent textures
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        for (int i=0; i<textures.size(); i++) {
            texDraw(projectionMatrix, textures.get(i), centerOfScaling.get(i), dst.get(i).centerX(), dst.get(i).centerY());
        }
        if (isLocked && disabledTexture != null)
            texDraw(projectionMatrix, disabledTexture, centerDisabled, dstDisabled.centerX(), dstDisabled.centerY());
        else if (!isLocked && enabledTexture != null)
            texDraw(projectionMatrix, enabledTexture, centerEnabled, dstEnabled.centerX(), dstEnabled.centerY());

        if (cups != null)
            texDraw(projectionMatrix, cups[selCup], centerCup, dstCup.centerX(), dstCup.centerY());

        if (digits != null)
            for (int i = 0; i < selDigits.length; i++)
                texDraw(projectionMatrix, digits[selDigits[i]], centerDigits[i], dstDigits[i].centerX(), dstDigits[i].centerY());

        // Return to normal blend mode
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void texDraw(float[] projectionMatrix, TangramGLSquare texture, PointF centerOfScale, float centerX, float centerY) {
        Matrix.setIdentityM(modelMatrix, OFFS);

        // Move to position
        Matrix.translateM(modelMatrix, OFFS, centerX, centerY, 0);

        // Scale
        Matrix.translateM(modelMatrix, OFFS, centerOfScale.x, centerOfScale.y, 0);
        if (isPressed)
            Matrix.scaleM(modelMatrix, OFFS, SCALE_FACTOR, SCALE_FACTOR, 1);
        Matrix.translateM(modelMatrix, OFFS, -centerOfScale.x, -centerOfScale.y, 0);

        // Multiply all changes
        Matrix.multiplyMM(modelViewProjectionMatrix, OFFS, projectionMatrix, OFFS, modelMatrix, OFFS);

        texture.draw(modelViewProjectionMatrix);
    }
}
