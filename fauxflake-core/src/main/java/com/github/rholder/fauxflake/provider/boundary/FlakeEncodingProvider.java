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

package com.github.rholder.fauxflake.provider.boundary;

import com.github.rholder.fauxflake.api.EncodingProvider;

import java.nio.ByteBuffer;

import static com.github.rholder.fauxflake.util.StringUtils.leftPad;
import static java.lang.Long.toHexString;

/**
 * This class provides functionality for working with Flake-style unique
 * identifiers, defined as being a 128-bit unsigned long value where the first
 * 64 bits store the number of milliseconds from the epoch, the next 48 bits
 * are the machine id, and the final 16 bits are a sequence number.  The
 * beginning of time (epoch) is measured as the number of milliseconds from
 * the Unix epoch.
 * <p/>
 * See http://boundary.com/blog/2012/01/12/flake-a-decentralized-k-ordered-unique-id-generator-in-erlang/
 * for more about the Flake identifier specification developed by Boundary.
 */
public class FlakeEncodingProvider implements EncodingProvider {

    private long shiftedMachineId;

    public FlakeEncodingProvider(long machineId) {
        shiftedMachineId = (0x0000FFFFFFFFFFFFl & machineId) << 16;
    }

    /**
     * Return a 128-bit version of the given time and sequence numbers according
     * to the Flake specification.
     *
     * @param time     a time value to encode
     * @param sequence a sequence value to encode
     * @return the Flake id as bytes
     */
    @Override
    public byte[] encodeAsBytes(long time, int sequence) {
        byte[] buffer = new byte[16];
        buffer[0] = (byte) (time >>> 56);
        buffer[1] = (byte) (time >>> 48);
        buffer[2] = (byte) (time >>> 40);
        buffer[3] = (byte) (time >>> 32);
        buffer[4] = (byte) (time >>> 24);
        buffer[5] = (byte) (time >>> 16);
        buffer[6] = (byte) (time >>> 8);
        buffer[7] = (byte) (time);

        long rest = shiftedMachineId | (0x0000FFFF & sequence);
        buffer[8] = (byte) (rest >>> 56);
        buffer[9] = (byte) (rest >>> 48);
        buffer[10] = (byte) (rest >>> 40);
        buffer[11] = (byte) (rest >>> 32);
        buffer[12] = (byte) (rest >>> 24);
        buffer[13] = (byte) (rest >>> 16);
        buffer[14] = (byte) (rest >>> 8);
        buffer[15] = (byte) (rest);

        return buffer;
    }

    /**
     * This always throws an {@link UnsupportedOperationException} since the
     * output of this {@link EncodingProvider} doesn't fit in 4 bytes.
     *
     * @param time     a time value to encode
     * @param sequence a sequence value to encode
     * @return never returns
     */
    @Override
    public long encodeAsLong(long time, int sequence) {
        throw new UnsupportedOperationException("Long value not supported");
    }

    /**
     * Return the 32 character left padded hex version of the given id. These
     * can be lexicographically sorted.
     *
     * @param time     a time value to encode
     * @param sequence a sequence value to encode
     * @return 32 character left padded hex version of the given id
     */
    @Override
    public String encodeAsString(long time, int sequence) {
        StringBuilder s = new StringBuilder(32);
        ByteBuffer bb = ByteBuffer.wrap(encodeAsBytes(time, sequence));
        s.append(leftPad(toHexString(bb.getLong()), 16, '0'));
        s.append(leftPad(toHexString(bb.getLong()), 16, '0'));

        return s.toString();
    }

    @Override
    public int maxSequenceNumbers() {
        // 2^16
        return 65536;
    }
}
