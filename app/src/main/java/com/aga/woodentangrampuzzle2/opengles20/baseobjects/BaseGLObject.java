package com.aga.woodentangrampuzzle2.opengles20.baseobjects;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;

import com.aga.android.programs.TextureShaderProgram;
import com.aga.android.util.ObjectBuildHelper;
import com.aga.android.util.VertexArray;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TEXTURE_SIZE_LARGE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TEXTURE_SIZE_MEDIUM;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TEXTURE_SIZE_SMALL;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TEXTURE_SIZE_XLARGE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.TEXTURE_SIZE_XSMALL;

/**
 *
 * Created by Andrii Husiev on 30.12.2016.
 *
 */

public class BaseGLObject {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * VertexArray.BYTES_PER_FLOAT;

    private VertexArray vertexArray;
    private TextureShaderProgram textureProgram;
    private int texture;
    private Bitmap bitmap;
    private PointF pivotPoint;

    public BaseGLObject(Bitmap b) {
        bitmap = b;
    }

    public void setVertexArray(VertexArray vertexArray) {
        this.vertexArray = vertexArray;
    }

    //<editor-fold desc="Cast Bitmap">
    /**
     * Cast object width and height automatically to nearest
     * bigger POT (power of two). The reason for this is that
     * non-POT textures are very restricted in where they can be used,
     * while POT textures are fine for all uses.
     */
    public void castObjectSizeAutomatically() {
        float width, height;
        width = (bitmap.getWidth() < TEXTURE_SIZE_XSMALL ? TEXTURE_SIZE_XSMALL : (bitmap.getWidth() < TEXTURE_SIZE_SMALL ? TEXTURE_SIZE_SMALL : (bitmap.getWidth() < TEXTURE_SIZE_MEDIUM ? TEXTURE_SIZE_MEDIUM : (bitmap.getWidth() < TEXTURE_SIZE_LARGE ? TEXTURE_SIZE_LARGE : TEXTURE_SIZE_XLARGE))));
        height = (bitmap.getHeight() < TEXTURE_SIZE_XSMALL ? TEXTURE_SIZE_XSMALL : (bitmap.getHeight() < TEXTURE_SIZE_SMALL ? TEXTURE_SIZE_SMALL : (bitmap.getHeight() < TEXTURE_SIZE_MEDIUM ? TEXTURE_SIZE_MEDIUM : (bitmap.getHeight() < TEXTURE_SIZE_LARGE ? TEXTURE_SIZE_LARGE : TEXTURE_SIZE_XLARGE))));
        castObjectToSize(width, height);
    }

    /**
     * Cast object width and height to specified values. It is better if it
     * POW (power of two). The reason for this is that
     * non-POT textures are very restricted in where they can be used,
     * while POT textures are fine for all uses.
     * <p>It is recommended to use castObjectSizeAutomatically() function instead for better results.</p>
     * @param width Function cast object width to this value.
     * @param height Function cast object height to this value.
     */
    public void castObjectToSize(float width, float height) {
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
    }
    //</editor-fold>

    //<editor-fold desc="Pivot Point">
    public PointF getPivotPoint() {
        return pivotPoint;
    }

    public void setPivotPoint(PointF pivotPoint) {
        this.pivotPoint = pivotPoint;
    }
    //</editor-fold>

    public void drawFrame(int color, float width) {
    }

    public void bitmapToTexture(TextureShaderProgram textureProgram) {
        this.textureProgram = textureProgram;
        texture = ObjectBuildHelper.loadTexture(bitmap);
    }

    /**
     * Now that the data’s been loaded into OpenGL, we no longer need to keep the
     * Android bitmap around. Under normal circumstances, it might take a few
     * garbage collection cycles for Dalvik to release this bitmap data, so we should
     * call recycle() on the bitmap object to release the data immediately.
     */
    public void recycleBitmap() {
        bitmap.recycle();
    }

    public void draw(float[] projectionMatrix) {
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        bindData(textureProgram);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexArray.getVertexCount());
    }

    private void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }
}
