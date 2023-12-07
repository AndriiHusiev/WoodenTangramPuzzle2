package com.aga.woodentangrampuzzle2.common;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.INVALID_POINTER_ID;
import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.pixelsToDeviceCoords;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ONE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.ZERO;

import android.graphics.RectF;
import android.view.MotionEvent;

public class MultiTouchGestures {
    private static final String TAG = "MultiTouchGestures";
    private static float ASPECT_RATIO;

    private final RectF screenRect;
    private final SingleTouchGesture[] singleTouch;
    private boolean isMultiTouch;
    private int action;

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
        isMultiTouch = false;
    }

    public void onTouchEvent(MotionEvent event) {
        isMultiTouch = event.getPointerCount() > 1;
        action = event.getActionMasked();
        try {
            switch (action) {
                case ACTION_DOWN:
                case ACTION_UP:
                    singleTouch[ZERO].setTouchData(event.getX(), event.getY(), event.getPointerId(ZERO));
                    break;
                case ACTION_POINTER_DOWN:
                    singleTouch[ONE].setTouchData(event.getX(ONE), event.getY(ONE), event.getPointerId(ONE));
                    break;
                case ACTION_POINTER_UP:
                    singleTouch[ONE].setTouchData(event.getX(ONE), event.getY(ONE));
                    break;
                case ACTION_MOVE:
                    int p0 = event.findPointerIndex(singleTouch[ZERO].getId());
                    int p1 = event.findPointerIndex(singleTouch[ONE].getId());

                    if (p0 != INVALID_POINTER_ID)
                        singleTouch[ZERO].setTouchData(event.getX(p0), event.getY(p0), event.getPointerId(p0));
                    if (p1 != INVALID_POINTER_ID && isMultiTouch)
                        singleTouch[ONE].setTouchData(event.getX(p1), event.getY(p1), event.getPointerId(p1));
                    break;
            }
        }
        catch (Exception ex) {
            logDebugOut(TAG, "onTouchEvent","catch an Exception: " + ex.getMessage());
        }
    }

    public int getAction() {
        return action;
    }

    /**
     * Convert touch coordinates into normalized device coordinates.
     * @param pointerIndex Raw index of pointer to retrieve.
     * @return Touch coordinates which converted into normalized device coordinates.
     */
    public float getNormalizedX(int pointerIndex) {
        return pixelsToDeviceCoords(singleTouch[pointerIndex].getX(), screenRect.width()) * ASPECT_RATIO;
    }

    /**
     * Convert touch coordinates into normalized device coordinates,
     * keeping in mind that Android's Y coordinates are inverted.
     * @param pointerIndex Raw index of pointer to retrieve.
     * @return Touch coordinates which converted into normalized device coordinates.
     */
    public float getNormalizedY(int pointerIndex) {
        return -pixelsToDeviceCoords(singleTouch[pointerIndex].getY(), screenRect.height());
    }

    public boolean isMultiTouch() {
//        logDebugOut(TAG, "isMultiTouch",isMultiTouch);
        return isMultiTouch;
    }
}
