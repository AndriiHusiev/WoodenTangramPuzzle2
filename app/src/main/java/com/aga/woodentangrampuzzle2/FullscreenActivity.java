package com.aga.woodentangrampuzzle2;

import static com.aga.android.util.ObjectBuildHelper.pixelsToDeviceCoords;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.aga.woodentangrampuzzle2.opengles20.TangramGLView;

public class FullscreenActivity extends AppCompatActivity {
    private static float ASPECT_RATIO;
    private TangramGLView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new TangramGLView(this, setDisplayMetrics());
        setOnTouchListener();
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();

        Log.d("debug","onResume.");
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
        Log.d("debug","onStop.");
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

            mGLView.queueEvent(() -> mGLView.mRenderer.handleTouch(normalizedX, normalizedY, event.getAction()));
            return true;
        });
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