package com.github.rholder.fauxflake.provider;

import org.junit.Assert;
import org.junit.Test;

public class MacMachineIdProviderTest {

    @Test
    public void validateProvider() {
        Assert.assertEquals("Machine id's are not deterministic",
                new MacMachineIdProvider().getMachineId(),
                new MacMachineIdProvider().getMachineId());

        Assert.assertFalse("Could not detect MAC address", new MacMachineIdProvider().getMachineId() == 0L);
    }
}
