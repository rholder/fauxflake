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

import com.github.rholder.fauxflake.api.EncodingProvider;
import com.github.rholder.fauxflake.api.Id;
import com.github.rholder.fauxflake.api.IdGenerator;
import com.github.rholder.fauxflake.api.TimeProvider;

/**
 * This is the default reference implementation of the {@link IdGenerator}
 * interface that is resilient enough to handle leap seconds, however, backwards
 * time that occurs when the system time is updated, as from NTP, will result in
 * an {@link InterruptedException}.
 */
public class DefaultIdGenerator implements IdGenerator {

    // lock for modifying shared state
    private final Object LOCK = new Object();

    // rolling sequence number, 0 to encodingProvider.maxSequenceNumbers()
    private volatile int sequence;

    // last time we checked the current time
    private volatile long lastTime;

    private final TimeProvider timeProvider;
    private final EncodingProvider encodingProvider;
    private final int startingSequenceNumber;

    public DefaultIdGenerator(TimeProvider timeProvider,
                              EncodingProvider encodingProvider) {
        this(timeProvider, encodingProvider, 0);
    }

    public DefaultIdGenerator(TimeProvider timeProvider,
                              EncodingProvider encodingProvider,
                              int startingSequenceNumber) {
        this.timeProvider = timeProvider;
        this.encodingProvider = encodingProvider;

        this.lastTime = timeProvider.getCurrentTime();
        this.startingSequenceNumber = startingSequenceNumber;
        this.sequence = 0;
    }

    private Values generateValues(int maxWait) throws InterruptedException {
        long currentTime;
        int currentSequence;
        synchronized (LOCK) {
            currentTime = timeProvider.getCurrentTime();

            // backwards time, likely due to NTP updates for clock drift
            if(currentTime < lastTime) {
                long diff = lastTime - currentTime;
                throw new BackwardsTimeException("Backwards time detected, try again in " + diff + " ms", diff);
            }

            if(sequence == encodingProvider.maxSequenceNumbers()) {
                // out of sequence numbers for this clock tick, be evil and hold the lock until the time changes
                int currentWait = 0;
                while(currentTime <= lastTime) {
                    if(currentWait > maxWait) {
                        throw new WaitTimeExceededException("The maximum time to wait to generate an id has been exceeded") ;
                    }
                    Thread.sleep(1);
                    currentTime = timeProvider.getCurrentTime();
                    currentWait++;
                }
            }

            if(currentTime != lastTime) {
                lastTime = currentTime;
                sequence = 0;
            }
            currentSequence = sequence;
            sequence++;
        }
        return new Values(currentTime, (currentSequence + startingSequenceNumber) % encodingProvider.maxSequenceNumbers());
    }

    @Override
    public Id generateId(int maxWait) throws InterruptedException {
        Values values = generateValues(maxWait);
        return new EncodedId(encodingProvider, values.currentTime, values.currentSequence);
    }

    /**
     * This is a simple tuple-y pair class.
     */
    public static class Values {
        public Values(long currentTime, int currentSequence) {
            this.currentTime = currentTime;
            this.currentSequence = currentSequence;
        }
        public long currentTime;
        public int currentSequence;
    }
}
