package com.github.rholder.fauxflake;

import com.github.rholder.fauxflake.provider.MacPidMachineIdProvider;
import junit.framework.Assert;
import org.junit.Test;

public class IdGeneratorsTest extends IdGenerators {

    @Test
    public void verifyNonNull() {
        Assert.assertNotNull(newFlakeIdGenerator());
        Assert.assertNotNull(newFlakeIdGenerator(new MacPidMachineIdProvider()));
        Assert.assertNotNull(newSnowflakeIdGenerator());
        Assert.assertNotNull(newSnowflakeIdGenerator(new MacPidMachineIdProvider()));
    }
}
