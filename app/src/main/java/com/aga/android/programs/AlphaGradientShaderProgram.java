package com.aga.android.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.aga.woodentangrampuzzle2.R;

/**
 *
 * Created by Andrii Husiev on 10.12.2023.
 *
 */
public class AlphaGradientShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uResolutionLocation;
    private final int uTextureUnitLocation;
    private final int uOffsetFromTop;
    private final int uGradientHeight;
    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public AlphaGradientShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.alpha_gradient_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uResolutionLocation = GLES20.glGetUniformLocation(program, U_RESOLUTION);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
        uOffsetFromTop = GLES20.glGetUniformLocation(program, U_OFFSET_FROM_TOP);
        uGradientHeight = GLES20.glGetUniformLocation(program, U_GRADIENT_HEIGHT);
        // Retrieve attribute locations for the shader program.
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, float width, float height, float offsetFromTop, float gradientHeight, int textureId) {
        // Pass data into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        GLES20.glUniform2f(uResolutionLocation, width, height);
        GLES20.glUniform1f(uOffsetFromTop, offsetFromTop);
        GLES20.glUniform1f(uGradientHeight, gradientHeight);
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
