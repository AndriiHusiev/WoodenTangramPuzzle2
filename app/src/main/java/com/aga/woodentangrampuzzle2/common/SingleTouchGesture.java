package com.aga.woodentangrampuzzle2.common;

import android.view.MotionEvent;

public class SingleTouchGesture {
    private float x, y;
    private int id, action;

    public SingleTouchGesture() {
        id = MotionEvent.INVALID_POINTER_ID;
    }

    public void setTouchData(float x, float y, int pointerId, int action) {
        this.x = x;
        this.y = y;
        this.action = action;
        id = pointerId;
    }

    public void setTouchData(float x, float y, int action) {
        this.x = x;
        this.y = y;
        this.action = action;
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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
