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
 * Implementations of this interface provide a mechanism to uniquely identify
 * the machine where the {@link IdGenerator} is running. Particular attention
 * should be paid to whether a single JVM per machine or multiple JVM processes
 * per machine are the intended targets for deployment as they will affect which
 * implementations are suitable for a given setup.
 */
public interface MachineIdProvider {

    /**
     * Return the unique machine identifier.
     */
    public long getMachineId();
}
