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
 * Implementations of this interface are responsible for converting time and
 * sequence values into valid encoded identifiers.
 */
public interface EncodingProvider {

    /**
     * Return a raw byte encoding of the given time and sequence number.
     *
     * @param time      a time value to encode
     * @param sequence  a sequence value to encode
     */
    public byte[] encodeAsBytes(long time, int sequence);

    /**
     * Return a Long encoding of the given the time and sequence number.
     *
     * @param time      a time value to encode
     * @param sequence  a sequence value to encode
     */
    public long encodeAsLong(long time, int sequence);

    /**
     * Return a String encoding of the given the time and sequence number.
     *
     * @param time      a time value to encode
     * @param sequence  a sequence value to encode
     */
    public String encodeAsString(long time, int sequence);

    /**
     * Return the maximum number of sequence numbers that can be encoded for a
     * given time by this implementation.
     */
    public int maxSequenceNumbers();
}
