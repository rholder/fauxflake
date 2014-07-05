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

/**
 * Use the MAC address to uniquely identify a machine. The returned encoding is
 * such that the last 6 bytes of an 8 byte long are the MAC address.  Running
 * more than a single JVM process on a machine may result in a non-unique
 * machine id being generated.  This introduces the possibility of ultimately
 * generating a duplicate distributed identifier so use this class with caution.
 * The {@link MacPidMachineIdProvider} might be a better choice if planning to
 * run multiple JVM's from a single machine without centralized coordination.
 */
public class MacMachineIdProvider implements MachineIdProvider {

    private final long machineId;

    public MacMachineIdProvider() {
        long value = 0L;
        try {
            // first 6 bytes are MAC
            byte[] raw = Arrays.copyOf(macAddress(), 8);
            value = new DataInputStream(new ByteArrayInputStream(raw)).readLong();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        machineId = value;
    }

    /**
     * Return the unique machine id based on the MAC address or 0 if an error
     * occurs.
     */
    @Override
    public long getMachineId() {
        return machineId;
    }
}
