package com.aga.woodentangrampuzzle2.common;

/**
 *
 * Created by Andrii Husiev on 26.12.2021.
 * This class serves as a timer.
 *
 */

public class TangramCommonTimer {
    public enum mode {STOP, RUN, PAUSE}
    private mode timerMode;
    private long startTime;
    private long pausedTime;

    public TangramCommonTimer() {
        startTime = 0;
        pausedTime = 0;
        timerMode = mode.STOP;
    }

    /**
     * Start new timer. If timer is already activated it will be restarted.
     */
    public void start() {
        timerMode = mode.RUN;
        pausedTime = 0;
        startTime = System.currentTimeMillis();
    }

    /**
     * Resume previously started and then paused timer.
     */
    public void resume() {
        timerMode = mode.RUN;
        startTime = System.currentTimeMillis();
    }

    /**
     * Pause timer.
     */
    public void pause() {
        timerMode = mode.PAUSE;
        pausedTime += System.currentTimeMillis() - startTime;
    }

    /**
     * Stop timer.
     */
    public void stop() {
        timerMode = mode.STOP;
        startTime = 0;
        pausedTime = 0;
    }

    public void addTimePeriod(long millis) {
        pausedTime += millis;
    }

    public mode getTimerMode() {
        return timerMode;
    }

    public long getElapsedTime() {
        if (timerMode == mode.RUN)
            return System.currentTimeMillis() - startTime + pausedTime;
        else if (timerMode == mode.PAUSE)
            return pausedTime;
        else
            return 0;
    }
}
