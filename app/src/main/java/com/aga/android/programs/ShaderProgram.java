package com.aga.android.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.aga.android.util.ShaderHelper;
import com.aga.android.util.TextResourceReader;

/**
 *
 * Created by Andrii Husiev on 18.02.2016.
 *
 */
public class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_RESOLUTION = "u_resolution";
    protected static final String U_OFFSET_FROM_TOP = "u_offsetFromTop";
    protected static final String U_GRADIENT_HEIGHT = "u_gradientHeight";
    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    // Shader program
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program);
    }
}
