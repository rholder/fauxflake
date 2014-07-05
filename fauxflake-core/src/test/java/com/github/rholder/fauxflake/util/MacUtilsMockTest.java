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
        Enumeration<NetworkInterface> empty = Collections.emptyEnumeration();
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
