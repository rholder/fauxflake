package com.github.rholder.fauxflake;

import com.github.rholder.fauxflake.provider.MacPidMachineIdProvider;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class IdGeneratorsTest {

    @Ignore("skip for Travis CI")
    @Test
    public void verifyNonNull() {
        Assert.assertNotNull(IdGenerators.newFlakeIdGenerator());
        Assert.assertNotNull(IdGenerators.newFlakeIdGenerator(new MacPidMachineIdProvider()));
        Assert.assertNotNull(IdGenerators.newSnowflakeIdGenerator());
        Assert.assertNotNull(IdGenerators.newSnowflakeIdGenerator(new MacPidMachineIdProvider()));
    }
}
