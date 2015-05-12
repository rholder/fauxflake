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

package com.github.rholder.fauxflake.provider.twitter;

import com.github.rholder.fauxflake.api.EncodingProvider;

import static com.github.rholder.fauxflake.util.StringUtils.leftPad;
import static java.lang.Long.toHexString;

/**
 * This class provides functionality for working with Snowflake-style unique
 * identifiers, defined as being a 64-bit unsigned long value where the first
 * 41 bits store the number of milliseconds from the epoch, the next 10 bits
 * are the machine id, and the final 12 bits are a sequence number.  The
 * beginning of time (epoch) is measured as the number of milliseconds from
 * Wed Nov 03 20:42:54 CDT 2010, the date used by Twitter's Snowflake unique id
 * generating solution.
 *
 * NOTE: Since there are only 10 bits used to uniquely identify a machine, some
 * coordination might be necessary to ensure unique machine id's are
 * sufficiently distributed and recycled.  The full Snowflake service uses a
 * Zookeeper cluster for centralizing this functionality.
 */
public class SnowflakeEncodingProvider implements EncodingProvider {

    /**
     * Wed Nov 03 20:42:54 CDT 2010, when time stamps begin for Twitter's epoch
     */
    public static final long EPOCH = 1288834974657L;

    /**
     * Total sequence numbers available within a single ms = 2^12
     */
    public static final int MAX_SEQUENCE_NUMBERS = 4096;

    /**
     * Number of bits to shift the time over
     */
    public static final int SHIFT_TIME_BITS = 22;

    /**
     * Number of bits to shift the machine code over
     */
    public static final int SHIFT_MACHINE_CODE_BITS = 12;

    /**
     * Total unique machine codes = 2^10
     */
    public static final int MACHINE_CODES = 1024;

    private long shiftedMachineId;

    public SnowflakeEncodingProvider(long machineId) {
        this.shiftedMachineId = ((Math.abs(machineId) % MACHINE_CODES) << SHIFT_MACHINE_CODE_BITS);
    }

    @Override
    public byte[] encodeAsBytes(long time, int sequence) {
        long v =  ((time - EPOCH) << SHIFT_TIME_BITS) | shiftedMachineId | sequence;

        byte[] buffer = new byte[8];
        buffer[0] = (byte)(v >>> 56);
        buffer[1] = (byte)(v >>> 48);
        buffer[2] = (byte)(v >>> 40);
        buffer[3] = (byte)(v >>> 32);
        buffer[4] = (byte)(v >>> 24);
        buffer[5] = (byte)(v >>> 16);
        buffer[6] = (byte)(v >>>  8);
        buffer[7] = (byte)(v);

        return buffer;
    }

    @Override
    public long encodeAsLong(long time, int sequence) {
        return ((time - EPOCH) << SHIFT_TIME_BITS) | shiftedMachineId | sequence;
    }

    /**
     * Return the 16 character left padded hex version of the given id. These
     * can be lexicographically sorted.
     */
    @Override
    public String encodeAsString(long time, int sequence) {
        return leftPad(toHexString(encodeAsLong(time, sequence)), 16, '0');
    }

    @Override
    public int maxSequenceNumbers() {
        return MAX_SEQUENCE_NUMBERS;
    }
}
