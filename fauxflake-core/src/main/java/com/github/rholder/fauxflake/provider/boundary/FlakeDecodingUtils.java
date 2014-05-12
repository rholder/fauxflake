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

package com.github.rholder.fauxflake.provider.boundary;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * This class adds a collection of functionality for working with and
 * manipulating 128-bit Flake id's.
 */
public class FlakeDecodingUtils {

    /**
     * Return the Date from the given encoded Flake id.
     *
     * @param flakeBytes the id to decode from
     */
    public static Date decodeDate(byte[] flakeBytes) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(flakeBytes);
        buffer.flip();
        return new Date(buffer.getLong());
    }

    /**
     * Return the machine id from the given encoded Snowflake id.
     *
     * @param flakeBytes the id to decode from
     */
    public static long decodeMachineId(byte[] flakeBytes) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(flakeBytes);
        buffer.flip();
        buffer.getLong();
        return buffer.getLong() >>> 16;
    }

    /**
     * Return the sequence number from the given encoded Snowflake id.
     *
     * @param flakeBytes the id to decode from
     */
    public static int decodeSequence(byte[] flakeBytes) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(flakeBytes);
        buffer.flip();
        buffer.getLong();
        return (int)(buffer.getLong() & 0x000000000000FFFF);
    }
}
