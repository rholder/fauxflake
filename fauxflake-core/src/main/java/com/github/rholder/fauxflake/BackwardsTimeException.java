/*
 * Copyright 2012-2014 Ray Holder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
