/*
 * Copyright 2012-2013 Ray Holder
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

package com.github.rholder.fauxflake.api;

/**
 * Implementations of this interface are the primary entry points for generating
 * unique identifiers.
 */
public interface IdGenerator {

    /**
     * Return a unique identifier. This should be resilient enough to handle
     * leap seconds, however, backwards time that occurs when the system time is
     * updated, as from NTP, will result in an {@link InterruptedException}.
     *
     * NOTE: Generating more id's at a time than can be handled may result in
     * the current thread blocking for maxWait ms to ensure a new timestamp is
     * available.
     *
     * @param maxWait
     *          the maximum amount of time to wait in ms before giving up
     * @throws InterruptedException
     *          thrown when thread is interrupted, the maximum amount of time
     *          to wait for a new timestamp to become available has expired, or
     *          backwards time has been detected
     */
    public Id generateId(int maxWait) throws InterruptedException;
}
