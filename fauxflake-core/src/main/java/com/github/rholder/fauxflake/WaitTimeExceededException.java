package com.github.rholder.fauxflake;

/**
 * This is thrown when we've waited beyond the specified wait time to generate
 * an identifier.
 */
public class WaitTimeExceededException extends RuntimeException {

    /**
     * Construct a new {@code WaitTimeExceededException}.
     *
     * @param message a more detailed description of what happened
     */
    public WaitTimeExceededException(String message) {
        super(message);
    }
}
