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

package com.github.rholder.fauxflake.provider;

import com.github.rholder.fauxflake.api.MachineIdProvider;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Arrays;

import static com.github.rholder.fauxflake.util.MacUtils.macAddress;
import static com.github.rholder.fauxflake.util.PidUtils.pid;

/**
 * Use a combination of the MAC address and the current process id to uniquely
 * identify a machine. The returned encoding is such that the first 6
 * bytes of an 8 byte long are the MAC address and the last 2 bytes are the
 * current PID % 65536.  While a PID collision is unlikely, there is still a
 * chance that it might occur, resulting in a non-unique machine id being
 * generated on the same machine.  This introduces the possibility of ultimately
 * generating a duplicate distributed identifier so use this class with caution.
 * Sequentially started processes are most likely going to have sequential (or
 * at least close enough) PID's within the range of 65536 such that a collision
 * is extremely unlikely (or in other words, this is probably "good enough").
 */
public class MacPidMachineIdProvider implements MachineIdProvider {

    public static final long MACHINE_ID;

    static {
        long value = 0L;
        byte[] raw = Arrays.copyOf(macAddress(), 8);
        try {
            // first 6 bytes are MAC
            value = new DataInputStream(new ByteArrayInputStream(raw)).readLong();

            // next 2 bytes are pid % 2^16
            value |= pid() % 65536;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        MACHINE_ID = value;
    }

    /**
     * Return the unique machine id based on the MAC address and current PID of
     * the running JVM or 0 if an error occurs.
     */
    @Override
    public long getMachineId() {
        return MACHINE_ID;
    }
}
