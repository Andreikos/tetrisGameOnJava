package tetris;

/**
 * The class is responsible for tracking the number of cycles that have elapsed.
 */
public class Time {
    private float perCycle;
    private long last;
    private int endCycle;
    private float normalCycles;
    private boolean pause;

    /**
     * Creates a new clock and sets it's cycles-per-second.
     *
     * @param cycles The number of cycles that elapse per second.
     */
    public Time(float cycles) {
        setCycles(cycles);
        setTime();
    }

    /**
     * Number of cycles that elapse per second.
     *
     * @param cycles The number of cycles per second.
     */
    public void setCycles(float cycles) {
        this.perCycle = (1 / cycles) * 1000;
    }

    /**
     * Resets the timer stats.
     */
    public void setTime() {
        this.endCycle = 0;
        this.normalCycles = 0.0f;
        this.last = getCurrentTime();
        this.pause = false;
    }

    /**
     * Updates the clock stats. Calculated only if the timer is not paused.
     */
    public void update() {
        long curr = getCurrentTime();
        float difference = (float) (curr - last) + normalCycles;
        if (pause == false) {
            this.endCycle += difference / perCycle;
            this.normalCycles = difference % perCycle;
        }
        this.last = curr;
    }

    private final long getCurrentTime() {
        return (System.currentTimeMillis());
    }

    /**
     * Pauses or unpauses the timer.
     *
     * @param paused Whether or not to pause this clock.
     */
    public void setPause(boolean paused) {
        this.pause = paused;
    }

    /**
     * See if the clock is currently paused.
     *
     * @return Whether or not this clock is paused.
     */
    public boolean pause() {
        return pause;
    }

    /**
     * Checks to see if a cycle has elapsed for this timer.
     *
     * @return Whether or not a cycle has elapsed.
     */
    public boolean elapsedCycle() {
        if (endCycle > 0) {
            this.endCycle--;
            return true;
        }
        return false;
    }
}
