package com.github.rholder.fauxflake.provider;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class MacPidMachineIdProviderTest {

    @Ignore("skip for Travis CI")
    @Test
    public void validateProvider() {
        Assert.assertEquals("Machine id's are not deterministic",
                new MacPidMachineIdProvider().getMachineId(),
                new MacPidMachineIdProvider().getMachineId());

        Assert.assertFalse("Could not detect MAC address", new MacPidMachineIdProvider().getMachineId() == 0L);
    }
}
