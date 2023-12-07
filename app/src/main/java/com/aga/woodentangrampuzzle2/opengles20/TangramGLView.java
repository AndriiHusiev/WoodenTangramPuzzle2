package com.aga.woodentangrampuzzle2.opengles20;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.FLING_MAX_OFF_PATH;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.FLING_MIN_DISTANCE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.FLING_THRESHOLD_VELOCITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.core.view.GestureDetectorCompat;

/**
 *
 * Created by Andrii Husiev on 15.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLView extends GLSurfaceView {
    public final TangramGLRenderer mRenderer;
    private GestureDetectorCompat mDetector;

    public TangramGLView(Context context, DisplayMetrics metrics){
        super(context);

        //This call crashes app on real devices.
//        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new TangramGLRenderer(context, metrics);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mDetector = new GestureDetectorCompat(context.getApplicationContext(), new TangramGestureListener());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        mRenderer.onTouch(event);
        return true;
    }

    public TangramGLView(Context context) {
        super(context);
        mRenderer = new TangramGLRenderer(context, null);
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(mRenderer);
    }

    public void onBackPressed() {
        queueEvent(mRenderer::onBackPressed);
    }

    class TangramGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) > FLING_MAX_OFF_PATH)
                return false;
            if(Math.abs(e1.getY() - e2.getY()) > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_THRESHOLD_VELOCITY) {
                mRenderer.onFling(velocityY);
            }
            return false;
//            return true;
        }
    }
}
