package com.github.rholder.fauxflake.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest extends StringUtils {

    @Test
    public void isEmpty() {
        Assert.assertTrue(StringUtils.isEmpty(null));
        Assert.assertTrue(StringUtils.isEmpty(""));
        Assert.assertFalse(StringUtils.isEmpty(" "));
        Assert.assertFalse(StringUtils.isEmpty("bob"));
        Assert.assertFalse(StringUtils.isEmpty("  bob  "));
    }

    @Test
    public void leftPad() {
        Assert.assertNull(StringUtils.leftPad(null, 9));
        Assert.assertEquals("   ", StringUtils.leftPad("", 3));
        Assert.assertEquals("bat", StringUtils.leftPad("bat", 3));
        Assert.assertEquals("  bat", StringUtils.leftPad("bat", 5));
        Assert.assertEquals("bat", StringUtils.leftPad("bat", 1));
        Assert.assertEquals("bat", StringUtils.leftPad("bat", -1));

        // go over the pad limit
        Assert.assertEquals(8193, StringUtils.leftPad("", 8193).length());

        Assert.assertNull(StringUtils.leftPad(null, 9, "ddd"));
        Assert.assertEquals("zzz", StringUtils.leftPad("", 3, "z"));
        Assert.assertEquals("bat", StringUtils.leftPad("bat", 3, "yz"));
        Assert.assertEquals("yzbat", StringUtils.leftPad("bat", 5, "yz"));
        Assert.assertEquals("yzyzybat", StringUtils.leftPad("bat", 8, "yz"));
        Assert.assertEquals("bat", StringUtils.leftPad("bat", 1, "yz"));
        Assert.assertEquals("bat", StringUtils.leftPad("bat", -1, "yz"));
        Assert.assertEquals("  bat", StringUtils.leftPad("bat", 5, null));
        Assert.assertEquals("  bat", StringUtils.leftPad("bat", 5, ""));
    }

    @Test
    public void padding() {
        Assert.assertEquals("", padding(0, 'e'));
        Assert.assertEquals("eee", padding(3, 'e'));

        try {
            padding(-2, 'e');
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertTrue(e.getMessage().contains("Cannot pad a negative amount:"));
        }
    }
}
