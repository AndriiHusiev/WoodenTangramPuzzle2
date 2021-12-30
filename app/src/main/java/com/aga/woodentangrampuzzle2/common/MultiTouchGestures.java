package com.aga.woodentangrampuzzle2.common;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.INVALID_POINTER_ID;
import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.pixelsToDeviceCoords;

import android.graphics.RectF;
import android.view.MotionEvent;

public class MultiTouchGestures {
    private static final String TAG = "MultiTouchGestures";
    private static float ASPECT_RATIO;

    private final RectF screenRect;
    private final SingleTouchGesture[] singleTouch;
    private float normalizedX, normalizedY;

    public MultiTouchGestures(RectF screenRectangle) {
        singleTouch = new SingleTouchGesture[2];
        singleTouch[0] = new SingleTouchGesture();
        singleTouch[1] = new SingleTouchGesture();
        screenRect = new RectF();
        screenRect.left = 0;
        screenRect.top = 0;
        screenRect.right = screenRectangle.width();
        screenRect.bottom = screenRectangle.height();
        ASPECT_RATIO = screenRectangle.width() / screenRectangle.height();
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case ACTION_DOWN:
                singleTouch[0].setTouchData(event.getX(), event.getY(), event.getPointerId(0), ACTION_DOWN);
                break;
            case ACTION_POINTER_DOWN:
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()","ACTION_POINTER_DOWN");
                singleTouch[1].setTouchData(event.getX(1), event.getY(1), event.getPointerId(1), ACTION_POINTER_DOWN);
                break;
            case ACTION_UP:
                singleTouch[0].setTouchData(event.getX(), event.getY(), ACTION_UP);
                break;
            case ACTION_POINTER_UP:
                break;
            case ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(singleTouch[0].getId());
                if (pointerIndex != INVALID_POINTER_ID) {
                    singleTouch[0].setAction(ACTION_MOVE);
                    singleTouch[0].setTouchData(event.getX(), event.getY(), ACTION_MOVE);
                }
                break;
        }
        normalizeCoordinates();
    }

    private void normalizeCoordinates() {
        // Convert touch coordinates into normalized device coordinates,
        // keeping in mind that Android's Y coordinates are inverted.
        normalizedX = pixelsToDeviceCoords(singleTouch[0].getX(), screenRect.width()) * ASPECT_RATIO;
        normalizedY = -pixelsToDeviceCoords(singleTouch[0].getY(), screenRect.height());
    }

    public int getAction() {
        return singleTouch[0].getAction();
    }

    public float getNormalizedX() {
        return normalizedX;
    }

    public float getNormalizedY() {
        return normalizedY;
    }
}
