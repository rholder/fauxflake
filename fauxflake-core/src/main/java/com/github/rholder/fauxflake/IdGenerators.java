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

package com.github.rholder.fauxflake;

import com.github.rholder.fauxflake.api.EncodingProvider;
import com.github.rholder.fauxflake.api.IdGenerator;
import com.github.rholder.fauxflake.api.MachineIdProvider;
import com.github.rholder.fauxflake.provider.boundary.FlakeEncodingProvider;
import com.github.rholder.fauxflake.provider.MacMachineIdProvider;
import com.github.rholder.fauxflake.provider.MacPidMachineIdProvider;
import com.github.rholder.fauxflake.provider.SystemTimeProvider;
import com.github.rholder.fauxflake.provider.twitter.SnowflakeEncodingProvider;

/**
 * This class provides a collection of convenience methods for constructing
 * common {@link IdGenerator} implementations.
 */
public class IdGenerators {

    /**
     * Create a Snowflake-based {@link IdGenerator} using the MAC address and
     * PID for hashing out a pseudo-unique machine id.
     *
     * @return the {@link IdGenerator}
     */
    public static IdGenerator newSnowflakeIdGenerator() {
        MachineIdProvider machineIdProvider = new MacPidMachineIdProvider();
        return newSnowflakeIdGenerator(machineIdProvider);
    }

    /**
     * Create a Snowflake-based {@link IdGenerator} using the given
     * {@link MachineIdProvider}.
     *
     * @return the {@link IdGenerator}
     */
    public static IdGenerator newSnowflakeIdGenerator(MachineIdProvider machineIdProvider) {
        EncodingProvider encodingProvider = new SnowflakeEncodingProvider(machineIdProvider.getMachineId());
        return new DefaultIdGenerator(new SystemTimeProvider(), encodingProvider);
    }

    /**
     * Create a Flake-based {@link IdGenerator} using the MAC address as the
     * unique machine id.
     *
     * @return the {@link IdGenerator}
     */
    public static IdGenerator newFlakeIdGenerator() {
        MachineIdProvider machineIdProvider = new MacMachineIdProvider();
        return newFlakeIdGenerator(machineIdProvider);
    }

    /**
     * Create a Flake-based {@link IdGenerator} using the given
     * {@link MachineIdProvider}.
     *
     * @return the {@link IdGenerator}
     */
    public static IdGenerator newFlakeIdGenerator(MachineIdProvider machineIdProvider) {
        EncodingProvider encodingProvider = new FlakeEncodingProvider(machineIdProvider.getMachineId());
        return new DefaultIdGenerator(new SystemTimeProvider(), encodingProvider);
    }
}