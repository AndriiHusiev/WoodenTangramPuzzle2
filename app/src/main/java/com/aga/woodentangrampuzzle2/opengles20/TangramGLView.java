package com.aga.woodentangrampuzzle2.opengles20;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;

/**
 *
 * Created by Andrii Husiev on 15.02.2016 for Wooden Tangram.
 *
 */
public class TangramGLView extends GLSurfaceView {
    //<editor-fold desc="Variables">
    public final TangramGLRenderer mRenderer;
//    public static float ASPECT_RATIO;
//    public TangramTimer universalTimer;
//    private Vibrator v;
    //</editor-fold>

    public TangramGLView(Context context, DisplayMetrics metrics){
        super(context);

        //TODO: This call crashes app on real devices.
//        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new TangramGLRenderer(context, metrics);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

//        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
//        universalTimer = new TangramTimer(this);
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
}
