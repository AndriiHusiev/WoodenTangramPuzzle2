package com.aga.woodentangrampuzzle2.common;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;

import android.graphics.PointF;
import android.view.MotionEvent;

public class MultiTouchGestures {
    private static final String TAG = "MultiTouchGestures";

    private SingleTouchGesture[] singleTouch;

    public MultiTouchGestures() {
        singleTouch = new SingleTouchGesture[2];
        singleTouch[0] = new SingleTouchGesture();
        singleTouch[1] = new SingleTouchGesture();
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                singleTouch[0].setId(event.getPointerId(0));
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()","ACTION_DOWN");
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()",event.getActionIndex());
                logDebugOut(TAG, "onTouchEvent event.  getPointerId()",event.getPointerId(event.getActionIndex()));
                logDebugOut(TAG, "onTouchEvent event.getPointerId(getActionIndex)","--------------------------------------------------------------");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                singleTouch[1].setId(event.getPointerId(1));
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()","ACTION_POINTER_DOWN");
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()",event.getActionIndex());
                logDebugOut(TAG, "onTouchEvent event.  getPointerId()",event.getPointerId(event.getActionIndex()));
                logDebugOut(TAG, "onTouchEvent event.getPointerId(getActionIndex)","--------------------------------------------------------------");
                break;
            case MotionEvent.ACTION_UP:
                singleTouch[0].setId(MotionEvent.INVALID_POINTER_ID);
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()","ACTION_UP");
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()",event.getActionIndex());
                logDebugOut(TAG, "onTouchEvent event.  getPointerId()",event.getPointerId(event.getActionIndex()));
                logDebugOut(TAG, "onTouchEvent event.getPointerId(getActionIndex)","--------------------------------------------------------------");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                singleTouch[1].setId(MotionEvent.INVALID_POINTER_ID);
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()","ACTION_POINTER_UP");
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()",event.getActionIndex());
                logDebugOut(TAG, "onTouchEvent event.  getPointerId()",event.getPointerId(event.getActionIndex()));
                logDebugOut(TAG, "onTouchEvent event.getPointerId(getActionIndex)","--------------------------------------------------------------");
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(singleTouch[0].getId());
                logDebugOut(TAG, "onTouchEvent event.   getActionId()",singleTouch[0].getId());
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()",pointerIndex);
                logDebugOut(TAG, "onTouchEvent event.getPointerId(getActionIndex)","--------------------------------------------------------------");

                pointerIndex = event.findPointerIndex(singleTouch[1].getId());
                logDebugOut(TAG, "onTouchEvent event.   getActionId()",singleTouch[1].getId());
                logDebugOut(TAG, "onTouchEvent event.getActionIndex()",pointerIndex);
                logDebugOut(TAG, "onTouchEvent event.getPointerId(getActionIndex)","--------------------------------------------------------------");
                break;
        }

    }

}
