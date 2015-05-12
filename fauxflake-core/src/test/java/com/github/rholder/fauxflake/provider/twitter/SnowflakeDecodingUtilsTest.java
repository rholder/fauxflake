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

package com.github.rholder.fauxflake.provider.twitter;

import com.github.rholder.fauxflake.DefaultIdGenerator;
import com.github.rholder.fauxflake.api.Id;
import com.github.rholder.fauxflake.api.IdGenerator;
import com.github.rholder.fauxflake.provider.SystemTimeProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@RunWith(Parameterized.class)
public class SnowflakeDecodingUtilsTest extends SnowflakeDecodingUtils {

    private final int machineId;
    private IdGenerator idGenerator;
    
    @Parameters
    public static Collection<?> data() {
      Object[][] data = new Object[][] { {53}, {-53} };
      return Arrays.asList(data);
    }
    
    public SnowflakeDecodingUtilsTest(int machineId) {
        this.machineId = machineId;
    }

    @Before
    public void before() {
        idGenerator = new DefaultIdGenerator(new SystemTimeProvider(), new SnowflakeEncodingProvider(machineId));
    }

    @Test
    public void decodedValueCheck() {
        // known id
        long id = 278275685817122816L;
        Assert.assertEquals("Timestamps did not match", 1355181068268L, decodeDate(id).getTime());
        Assert.assertEquals("Machine id's did not match", 32, decodeMachineId(id));
        Assert.assertEquals("Sequence numbers did not match", 0, decodeSequence(id));
    }

    @Test
    public void encodedValueCheck() throws InterruptedException {
        Date now = new Date();
        Id id = idGenerator.generateId(5);
        long longId = id.asLong();
        byte[] byteId = id.asBytes();

        Date idDate = decodeDate(longId);

        Assert.assertTrue("Now is greater than generated id", now.getTime() <= idDate.getTime());
        Assert.assertEquals("Unexpected machine id", Math.abs(machineId), decodeMachineId(longId));
        Assert.assertEquals("Unexpected number of bytes in id", 8, byteId.length);
    }

    @Test
    public void lengthVerify() throws InterruptedException {
        String id = idGenerator.generateId(100).asString();
        Assert.assertEquals(16, id.length());
    }
}
