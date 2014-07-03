package com.github.rholder.fauxflake;

/**
 * {@code BackwardsTimeException} is thrown when backwards time is detected.
 * The offset should indicate how many milliseconds to wait before trying again
 * in the present.
 */
public class BackwardsTimeException extends RuntimeException {

    private long offset;

    /**
     * Constructor for propagating a detailed message and time offset.
     *
     * @param message description detailing possible time travel
     * @param offset  how many milliseconds we are from the present
     */
    public BackwardsTimeException(String message, long offset) {
        super(message);
        this.offset = offset;
    }

    /**
     * Return the number of milliseconds from the time the Exception occurred
     * and the present time.
     *
     * @return milliseconds our clock is now from what should be the present
     */
    public long getOffset() {
        return offset;
    }
}
