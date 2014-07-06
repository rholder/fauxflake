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

import com.github.rholder.fauxflake.provider.MacMachineIdProvider;
import com.github.rholder.fauxflake.provider.MacPidMachineIdProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MacUtils.class, NetworkInterface.class})
public class MacUtilsMockTest {

    @Test
    public void alwaysUnsupported() {
        mockStatic(MacUtils.class);
        when(MacUtils.macAddress()).thenThrow(new UnsupportedOperationException());

        Assert.assertTrue(new MacMachineIdProvider().getMachineId() == 0);
        Assert.assertTrue(new MacPidMachineIdProvider().getMachineId() == 0);
    }

    @Test
    public void alwaysSupported() {
        mockStatic(MacUtils.class);
        when(MacUtils.macAddress()).thenReturn(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        Assert.assertTrue(new MacMachineIdProvider().getMachineId() != 0);
        Assert.assertTrue(new MacPidMachineIdProvider().getMachineId() != 0);
    }

    @Test
    public void brokenNetworkInterfaces() throws SocketException {
        mockStatic(NetworkInterface.class);
        when(NetworkInterface.getNetworkInterfaces()).thenThrow(new SocketException("Something bad"));

        try {
            MacUtils.realMacAddress();
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(e.getMessage().contains("SocketException occurred"));
        }
    }

    @Test
    public void emptyNetworkInterfaces() throws SocketException, IllegalAccessException, InstantiationException {
        mockStatic(NetworkInterface.class);
        Enumeration<NetworkInterface> empty = Collections.enumeration(Collections.<NetworkInterface>emptyList());
        when(NetworkInterface.getNetworkInterfaces()).thenReturn(empty);

        try {
            MacUtils.realMacAddress();
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(e.getMessage().contains("no MAC addresses detected"));
        }
    }

    @Test
    public void wonkyNetworkInterfaces() throws SocketException, IllegalAccessException, InstantiationException {
        mockStatic(NetworkInterface.class);
        NetworkInterface n = PowerMockito.mock(NetworkInterface.class);
        when(n.getHardwareAddress()).thenReturn(null);
        when(NetworkInterface.getNetworkInterfaces()).thenReturn(Collections.enumeration(Collections.singleton(n)));

        try {
            MacUtils.realMacAddress();
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(e.getMessage().contains("no MAC addresses detected"));
        }
    }
}
