package com.github.rholder.fauxflake.provider;

import org.junit.Assert;
import org.junit.Test;

public class MacPidMachineIdProviderTest {

    @Test
    public void validateProvider() {
        Assert.assertEquals("Machine id's are not deterministic",
                new MacPidMachineIdProvider().getMachineId(),
                new MacPidMachineIdProvider().getMachineId());

        if(new MacPidMachineIdProvider().getMachineId() == 0L) {
            System.err.println("Could not detect MAC address");
        }
    }
}
