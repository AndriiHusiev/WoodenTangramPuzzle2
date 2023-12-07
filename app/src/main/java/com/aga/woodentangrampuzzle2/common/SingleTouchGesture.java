package com.aga.woodentangrampuzzle2.common;

public class SingleTouchGesture {
    private float x, y;
    private int id;

    public SingleTouchGesture() {
        id = -1;
    }

    public void setTouchData(float x, float y, int pointerId) {
        this.x = x;
        this.y = y;
        id = pointerId;
    }

    public void setTouchData(float x, float y) {
        this.x = x;
        this.y = y;
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
}
