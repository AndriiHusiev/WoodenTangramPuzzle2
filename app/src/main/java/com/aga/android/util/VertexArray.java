package com.aga.android.util;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 *
 * Created by Andrii Husiev on 18.02.2016 for Wooden Tangram.
 *
 */
public class VertexArray {
    public static final int BYTES_PER_FLOAT = 4;
    public static final int COORD_PER_VERTEX = 4;

    private final FloatBuffer floatBuffer;
    private final int vertexCount;

    public VertexArray(float[] vertexData) {
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexCount = vertexData.length / COORD_PER_VERTEX;
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int stride) {
        floatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, floatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
