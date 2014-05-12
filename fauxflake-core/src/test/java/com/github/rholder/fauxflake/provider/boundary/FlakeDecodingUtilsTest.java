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

package com.github.rholder.fauxflake.provider.boundary;

import com.github.rholder.fauxflake.DefaultIdGenerator;
import com.github.rholder.fauxflake.api.Id;
import com.github.rholder.fauxflake.api.IdGenerator;
import com.github.rholder.fauxflake.provider.SystemTimeProvider;
import com.github.rholder.fauxflake.util.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Date;

import static com.github.rholder.fauxflake.provider.boundary.FlakeDecodingUtils.decodeDate;
import static com.github.rholder.fauxflake.provider.boundary.FlakeDecodingUtils.decodeMachineId;
import static com.github.rholder.fauxflake.provider.boundary.FlakeDecodingUtils.decodeSequence;

public class FlakeDecodingUtilsTest {

    private static final int TEST_MACHINE_ID = 53;
    private IdGenerator idGenerator;

    @Before
    public void before() {
        idGenerator = new DefaultIdGenerator(new SystemTimeProvider(), new FlakeEncodingProvider(TEST_MACHINE_ID));
    }

    @Test
    public void decodedValueCheck() {
        // known id
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(1355181068268L);
        bb.put((byte) 0x00);
        bb.put((byte) 0xDE);
        bb.put((byte) 0xAD);
        bb.put((byte) 0xBE);
        bb.put((byte) 0xEF);
        bb.put((byte) 0x00);
        bb.putShort((short) 1337);

        byte[] id = bb.array();

        Assert.assertEquals("Timestamps did not match", 1355181068268L, decodeDate(id).getTime());
        Assert.assertEquals("Machine id's did not match", 956397711104L, decodeMachineId(id));
        Assert.assertEquals("Sequence numbers did not match", 1337, decodeSequence(id));

        StringBuilder sb = new StringBuilder();
        for(byte i : id) {
            sb.append(StringUtils.leftPad(Integer.toHexString(i & 0xFF), 2, '0'));
        }
        System.out.println(StringUtils.leftPad(sb.toString(), 32, '0'));
    }

    @Test
    public void encodedValueCheck() throws InterruptedException {
        Date now = new Date();
        Id id = idGenerator.generateId(5);
        byte[] idBytes = id.asBytes();
        Date idDate = decodeDate(idBytes);

        Assert.assertTrue("Now is greater than generated id", now.getTime() <= idDate.getTime());
        Assert.assertEquals("Unexpected machine id", TEST_MACHINE_ID, decodeMachineId(idBytes));
    }

    @Test
    public void lengthVerify() throws InterruptedException {
        String id = idGenerator.generateId(100).asString();
        Assert.assertEquals(32, id.length());
        System.out.println(id);
    }
}
