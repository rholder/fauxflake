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
