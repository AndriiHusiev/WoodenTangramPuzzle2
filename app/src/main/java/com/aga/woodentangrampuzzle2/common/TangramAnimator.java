package com.aga.woodentangrampuzzle2.common;

import static com.aga.android.util.ObjectBuildHelper.logDebugOut;

import com.aga.woodentangrampuzzle2.common.TangramCommonTimer.mode;

/**
 *
 * Created by Andrii Husiev on 26.12.2021.
 * This class provides a simple timing engine for running animations
 * which calculate animated values and set them on target objects.
 *
 */

public class TangramAnimator {
    private static final String TAG = "TangramAnimator";

    public enum ANIM_TYPE {LINEAR, PARABOLIC, INV_PARABOLIC}
    private ANIM_TYPE animType;
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
     * Sets float value that will be animated from.
     * @param initialVelocity Initial velocity which will decay at the end of duration time.
     */
    public void setStartValue(float initialVelocity) {
        velocity = 2 * initialVelocity / screenSize;
        logDebugOut(TAG, "setStartValue velocity", velocity);
    }

    /**
     * Sets the desired animation formula to be used in this animator:
     * <ul>
     *     <li>LINEAR - linear decay formula from StartValue to zero;</li>
     *     <li>PARABOLIC - parabolic decay formula from StartValue to zero;</li>
     *     <li>INV_PARABOLIC - parabolic formula from zero to StartValue and again to zero.</li>
     * </ul>
     * @param type Desired animation formula; select from internal enum.
     */
    public void setAnimationType(ANIM_TYPE type) {
        animType = type;
    }

    public float getAnimatedValue() {
        long elapsedTime = checkEndOfAnimation();
        return calcAnimation(elapsedTime);
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

    private long checkEndOfAnimation() {
        long elapsedTime = timer.getElapsedTime();
        float percent = (float) elapsedTime / duration;

        if (percent >= 1f)
            timer.stop();

        return elapsedTime;
    }

    private float calcAnimation(long elapsedTime) {
        switch (animType) {
            case LINEAR:
                return calcLinearDecay(elapsedTime);
            case PARABOLIC:
                return calcParabolicDecay(elapsedTime);
            case INV_PARABOLIC:
                return calcInvertedParabolic(elapsedTime);
        }
        return 0;
    }

    /**
     * This interpolator used in calculating the elapsed fraction of this animation.
     * The current interpolator use linear curve formula:
     *          y = velocity - k * elapsedTime;
     * @param elapsedTime Time depending on which the last value will be calculated by this Animator.
     * @return The value most recently calculated by this ValueAnimator.
     */
    private float calcLinearDecay(float elapsedTime) {
        return velocity * (1 - elapsedTime / duration);
    }

    /**
     * This interpolator used in calculating the elapsed fraction of this animation.
     * The current interpolator use parabolic curve formula:
     *          y = velocity * sqr(1 - elapsedTime / animation_duration)
     * @param elapsedTime Time depending on which the last value will be calculated by this Animator.
     * @return The value most recently calculated by this ValueAnimator.
     */
    private float calcParabolicDecay(float elapsedTime) {
        return velocity * (float) Math.pow(1 - elapsedTime / duration, 2);
    }

    /**
     * This interpolator used in calculating the elapsed fraction of this animation.
     * The current interpolator use parabolic curve formula:
     *          y = velocity * (1 - sqr(1 - 2 * elapsedTime / animation_duration));
     * @param elapsedTime Time depending on which the last value will be calculated by this Animator.
     * @return The value most recently calculated by this ValueAnimator.
     */
    private float calcInvertedParabolic(float elapsedTime) {
        return velocity * (1 - (float) Math.pow(1 - 2 * elapsedTime / duration, 2));
    }

}
