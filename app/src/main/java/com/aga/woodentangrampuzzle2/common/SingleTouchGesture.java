package com.aga.woodentangrampuzzle2.common;

import android.graphics.PointF;
import android.view.MotionEvent;

public class SingleTouchGesture {
    private float x, y;
    private int id;

    public SingleTouchGesture() {
        id = MotionEvent.INVALID_POINTER_ID;
    }

    public void setTouchData(float x, float y, int pointerId) {
        this.x = x;
        this.y = y;
        id = pointerId;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
