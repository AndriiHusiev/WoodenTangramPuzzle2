package com.aga.woodentangrampuzzle2;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;
import static com.aga.android.util.ObjectBuildHelper.pixelsToDeviceCoords;

import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.FLING_MAX_OFF_PATH;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.FLING_MIN_DISTANCE;
import static com.aga.woodentangrampuzzle2.common.TangramGlobalConstants.FLING_THRESHOLD_VELOCITY;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.aga.woodentangrampuzzle2.opengles20.TangramGLView;

public class FullscreenActivity extends AppCompatActivity {
    private static final String TAG = "FullscreenActivity";
    private static float ASPECT_RATIO;
    private TangramGLView mGLView;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new TangramGLView(this, setDisplayMetrics());
        mDetector = new GestureDetectorCompat(this, new TangramGestureListener());
        setOnTouchListener();
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();

        logDebugOut(TAG, "onResume","resumed.");
        hideUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logDebugOut(TAG, "onStop","stopped.");
    }

    public void onBackPressed () {
        mGLView.onBackPressed();
    }

    private DisplayMetrics setDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ASPECT_RATIO = (float) metrics.widthPixels / metrics.heightPixels;

        return metrics;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListener() {
        mGLView.setOnTouchListener((v, event) -> {
            // Convert touch coordinates into normalized device coordinates,
            // keeping in mind that Android's Y coordinates are inverted.
            final float normalizedX = pixelsToDeviceCoords(event.getX(), v.getWidth()) * ASPECT_RATIO;
            final float normalizedY = -pixelsToDeviceCoords(event.getY(), v.getHeight());

            mDetector.onTouchEvent(event);
            mGLView.queueEvent(() -> mGLView.mRenderer.handleTouch(normalizedX, normalizedY, event.getAction()));
            mGLView.queueEvent(() -> mGLView.mRenderer.onTouch(event, normalizedX, normalizedY));
            return true;
        });
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
                mGLView.queueEvent(() -> mGLView.mRenderer.onFling(velocityY));
            }
            return false;
//            return true;
        }
    }

    private void hideUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ((ActionBar) actionBar).hide();
        }

        if (mGLView != null ) {
            mGLView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }
}