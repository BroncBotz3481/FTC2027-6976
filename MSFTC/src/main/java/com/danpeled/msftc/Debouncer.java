package com.danpeled.msftc;

public class Debouncer {
    private final long debounceTimeNanos;
    private long startTime = -1;

    public Debouncer(double seconds) {
        this.debounceTimeNanos = (long) (seconds * 1e9);
    }

    public boolean calculate(boolean input) {
        if (!input) {
            startTime = -1;
            return false;
        }

        if (startTime == -1) {
            startTime = System.nanoTime();
            return false;
        }

        return System.nanoTime() - startTime >= debounceTimeNanos;
    }
}
