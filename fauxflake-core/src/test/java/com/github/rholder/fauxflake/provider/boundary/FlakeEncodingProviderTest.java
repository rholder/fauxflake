package com.github.rholder.fauxflake.provider.boundary;

import org.junit.Assert;
import org.junit.Test;

public class FlakeEncodingProviderTest {

    @Test
    public void notImplemented() {
        try {
            new FlakeEncodingProvider(1).encodeAsLong(1, 1);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(e.getMessage().contains("Long value not supported"));
        }
    }
}
