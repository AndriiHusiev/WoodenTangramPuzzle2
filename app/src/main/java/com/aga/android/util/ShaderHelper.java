package com.aga.android.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 *
 * Created by Andrii Husiev on 16.02.2016.
 *
 */
public class ShaderHelper {
    private static final String TAG = "debug";

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;
        // Compile the shaders.
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
        // Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader);
        //validateProgram(program);

        return program;
    }

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);
        final int[] compileStatus = new int[1];

        if (shaderObjectId == 0) {
            Log.d(TAG, "Could not create new shader.");
            return 0;
        }
        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        // Print the shader info log to the Android log output.
        //Log.d(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            GLES20.glDeleteShader(shaderObjectId);
            //Log.d(TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        final int[] linkStatus = new int[1];

        if (programObjectId == 0) {
            Log.d(TAG, "Could not create new program");
            return 0;
        }
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        GLES20.glLinkProgram(programObjectId);
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        // Print the program info log to the Android log output.
        //Log.d(TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            GLES20.glDeleteProgram(programObjectId);
            Log.d(TAG, "Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        //Log.d(TAG, "Results of validating program: " + validateStatus[0] + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}
