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

import java.io.ByteArrayOutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static java.net.NetworkInterface.getNetworkInterfaces;

/**
 * This class adds functionality for working with MAC hardware addresses.
 */
public abstract class MacUtils {

    /**
     * System property used to override the MAC returned by getOverride().
     */
    public static final String OVERRIDE_MAC_PROP = "fauxflake.override.mac";

    /**
     * Return the overridden MAC address, if the system property
     * OVERRIDE_MAC_PROP has been set. If the value of the property isn't a
     * valid MAC address or it was not set, then null is returned.
     */
    public static byte[] getOverride() {
        String overrideMac = System.getProperty(OVERRIDE_MAC_PROP);
        byte[] macBytes = null;
        if (overrideMac != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String[] rawBytes = overrideMac.split(":");
            if (rawBytes.length == 6) {
                try {
                    for (String b : rawBytes) {
                        out.write(Integer.parseInt(b, 16));
                    }
                    macBytes = out.toByteArray();
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }
        }
        return macBytes;
    }

    /**
     * Return a 6 byte array where each byte represents a value in the MAC
     * address of the first valid interface found or an
     * {@link UnsupportedOperationException} is thrown if an error occurs.
     */
    public static byte[] realMacAddress() {
        byte[] mac = null;
        try {
            // enumerations are wonky...
            Enumeration<NetworkInterface> networkInterfaces = getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface n = networkInterfaces.nextElement();
                byte[] possibleMac = n.getHardwareAddress();
                if (possibleMac != null && possibleMac.length == 6) {
                    mac = possibleMac;
                    break;
                }
            }
            if (mac == null) {
                throw new UnsupportedOperationException("Could not retrieve hardware MAC address, no MAC addresses detected");
            }
        } catch (SocketException e) {
            throw new UnsupportedOperationException("Could not retrieve hardware MAC address, SocketException occurred", e);
        }
        return mac;
    }

    /**
     * Return a 6 byte array where each byte represents a value in the MAC
     * address of the first valid interface found or an
     * {@link UnsupportedOperationException} is thrown if an error occurs.
     * Optionally setting the system property OVERRIDE_MAC_PROP to a valid MAC
     * address will result in it being returned instead.
     */
    public static byte[] macAddress() {
        byte[] override = getOverride();
        return  override != null ? override : realMacAddress();
    }
}