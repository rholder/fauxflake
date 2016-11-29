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

import java.util.Date;

/**
 * This class adds a collection of functionality for working with and
 * manipulating 64-bit Snowflake id's.
 */
public abstract class SnowflakeDecodingUtils {

    /**
     * Return the Date from the given encoded Snowflake id.
     *
     * @param id the id to decode from
     */
    public static Date decodeDate(long id) {
        return new Date((id >> SnowflakeEncodingProvider.SHIFT_TIME_BITS) + SnowflakeEncodingProvider.EPOCH);
    }

    /**
     * Return the machine id from the given encoded Snowflake id.
     *
     * @param id the id to decode from
     */
    public static long decodeMachineId(long id) {
        return (int)((id & 0x00000000003FF000) >> SnowflakeEncodingProvider.SHIFT_MACHINE_CODE_BITS);
    }

    /**
     * Return the sequence number from the given encoded Snowflake id.
     *
     * @param id the id to decode from
     */
    public static int decodeSequence(long id) {
        return (int)(id & SnowflakeEncodingProvider.SEQUENCE_MASK);
    }
}
