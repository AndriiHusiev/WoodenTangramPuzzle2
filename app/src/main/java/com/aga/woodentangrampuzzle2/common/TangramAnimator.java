package com.aga.woodentangrampuzzle2.common;

import com.aga.woodentangrampuzzle2.common.TangramCommonTimer.mode;

/**
 *
 * Created by Andrii Husiev on 26.12.2021.
 * This class provides a simple timing engine for running animations
 * which calculate animated values and set them on target objects.
 *
 */

public class TangramAnimator {
    private long duration;
    private float velocity, screenSize;
    private TangramCommonTimer timer;

    public TangramAnimator(float screenSize) {
        duration = 300;
        this.screenSize = screenSize;
        timer = new TangramCommonTimer();
    }

    /**
     * Sets the length of the animation. The default duration is 300 milliseconds.
     * @param duration The length of the animation, in milliseconds. This value cannot be negative.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Sets float values that will be animated between.
     * @param initialVelocity Initial velocity which will decay at the end of duration time.
     */
    public void setAnimatedValues(float initialVelocity) {
        velocity = initialVelocity / screenSize;
    }

    public float getAnimatedValue() {
        return calcAnimation();
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public boolean isAnimationStarted () {
        return timer.getTimerMode() == mode.RUN;
    }

    private float calcAnimation() {
        long elapsedTime = timer.getElapsedTime();
        float percent = (float) elapsedTime / duration;

        if (percent >= 1f)
            timer.stop();

        return calcParabolicDecay(elapsedTime);
    }

    private static final float VELOCITY_COEFFICIENT = 0.0001f;

    /**
     * The pctElapsedTime interpolator used in calculating the elapsed fraction of this animation.
     * The current interpolator use parabolic curve formula:
     *          y = k * velocity * sqr(elapsedTime - animation_duration)
     * @param elapsedTime Time depending on which the last value will be calculated by this Animator.
     * @return The value most recently calculated by this ValueAnimator
     */
    private float calcParabolicDecay(float elapsedTime) {
        return (VELOCITY_COEFFICIENT * duration * velocity * (float) Math.pow((elapsedTime - duration) / 1000f, 2));
    }

}
