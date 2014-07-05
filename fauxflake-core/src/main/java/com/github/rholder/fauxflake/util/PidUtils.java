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

package com.github.rholder.fauxflake.util;

import java.lang.management.ManagementFactory;

/**
 * This class adds functionality for working with Unix process id's (PID).
 */
public abstract class PidUtils {

    /**
     * Return the PID from the currently running JVM or 0 if it could not be
     * retrieved. This should work on most *nix JVM implementations.
     */
    public static int pid() {
        int localPID = 0;
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String[] nameSplit = name.split("@");
            if(nameSplit.length > 1) {
                localPID = Integer.parseInt(nameSplit[0]);
            }
            return localPID;
        } catch(Throwable t) {
            throw new UnsupportedOperationException("An error occurred while getting the PID: " + t.getMessage()) ;
        }
    }
}
