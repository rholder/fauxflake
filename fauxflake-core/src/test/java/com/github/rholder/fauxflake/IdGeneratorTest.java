/*
 * Copyright 2012-2013 Ray Holder
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

package com.github.rholder.fauxflake;

import com.github.rholder.fauxflake.api.EncodingProvider;
import com.github.rholder.fauxflake.api.IdGenerator;
import com.github.rholder.fauxflake.api.TimeProvider;
import com.github.rholder.fauxflake.provider.SystemTimeProvider;
import com.github.rholder.fauxflake.provider.twitter.SnowflakeEncodingProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.rholder.fauxflake.provider.twitter.SnowflakeDecodingUtils.decodeDate;
import static com.github.rholder.fauxflake.provider.twitter.SnowflakeDecodingUtils.decodeMachineId;
import static com.github.rholder.fauxflake.provider.twitter.SnowflakeDecodingUtils.decodeSequence;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IdGeneratorTest {

    private IdGenerator idGenerator;

    /**
     * Concurrently generate 1 million unique id's and check them for uniqueness.
     *
     * @throws InterruptedException
     */
    @Test
    public void threadSafety() throws InterruptedException {

        idGenerator = new DefaultIdGenerator(new SystemTimeProvider(), new SnowflakeEncodingProvider(1234));

        final ExecutorService generatorService = Executors.newFixedThreadPool(100);
        final ExecutorService analyzerService = Executors.newSingleThreadExecutor();
        final BlockingQueue<Long> q = new LinkedBlockingQueue<Long>();
        final int total = 1 * 1000 * 1000;

        // generate 1M unique id's as fast fast as possible
        for(long i = 0; i < total; i++) {
            generatorService.submit(new Runnable() {
                public void run() {
                    try {
                        // stuff them onto a queue
                        q.put(idGenerator.generateId(10).asLong());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });
        }

        generatorService.shutdown();
        boolean generatorSuccessful = generatorService.awaitTermination(1, TimeUnit.MINUTES);
        Assert.assertTrue("All id's could not be successfully generated", generatorSuccessful);

        // check the 1M id's for uniqueness
        final AtomicInteger count = new AtomicInteger(0);
        analyzerService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<Long, Long> map = new HashMap<Long, Long>();
                    while (count.get() < total) {
                        Long id = null;
                        try {
                            id = q.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(id == null) {
                            System.out.println("Null id after " + count.get());
                            generatorService.shutdownNow();
                            break;
                        }

                        if (map.get(id) != null) {
                            System.out.println("Duplicate: " + id + " after " + count.get());
                            System.out.println(decodeDate(id) + " " + decodeMachineId(id) + " " + decodeSequence(id));
                            generatorService.shutdownNow();
                            break;
                        }

                        map.put(id, id);
                        if (count.incrementAndGet() % 1000 == 0) {
                            System.out.println("Unique count: " + count.get());
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });

        analyzerService.shutdown();
        boolean analyzerSuccessful = analyzerService.awaitTermination(5, TimeUnit.MINUTES);
        Assert.assertTrue("Timed out waiting for all id's to be processed", analyzerSuccessful);

        Assert.assertEquals("There are still items left in the analysis queue", 0, q.size());
    }

    @Test
    public void backwardsTime() throws InterruptedException {
        TimeProvider timeProvider = mock(TimeProvider.class);

        long now = System.currentTimeMillis();
        when(timeProvider.getCurrentTime())
                .thenReturn(now) // initial time
                .thenReturn(now) // get id 1
                .thenReturn(now) // get id 2
                .thenReturn(now) // get id 3
                .thenReturn(now - 50); // simulate time running backwards

        idGenerator = new DefaultIdGenerator(timeProvider, new SnowflakeEncodingProvider(1234));

        // sanity check that we have 3 ids
        Set<Long> ids = new HashSet<Long>();
        ids.add(idGenerator.generateId(10).asLong());
        ids.add(idGenerator.generateId(10).asLong());
        ids.add(idGenerator.generateId(10).asLong());
        Assert.assertEquals("Duplicate id detected", 3, ids.size());

        try {
            idGenerator.generateId(10);
            Assert.fail("Expected Exception for backwards time");
        } catch (InterruptedException e) {
            Assert.assertTrue("Exception did not relate to backwards time", e.getMessage().contains("Backwards"));
        }
    }

    @Test
    public void endOfSequenceNumbers() throws InterruptedException {
        EncodingProvider encodingProvider = new SnowflakeEncodingProvider(1234);

        TimeProvider timeProvider = mock(TimeProvider.class);
        long now = System.currentTimeMillis();

        // max out the sequence numbers for a single period of time
        when(timeProvider.getCurrentTime()).thenReturn(now);
        idGenerator = new DefaultIdGenerator(timeProvider, encodingProvider);

        // sanity check that we have unique ids
        Set<String> ids = new HashSet<String>();
        for(int i = 0; i < encodingProvider.maxSequenceNumbers(); i++) {
            ids.add(idGenerator.generateId(0).asString());
        }
        Assert.assertEquals("Duplicate id detected", encodingProvider.maxSequenceNumbers(), ids.size());

        try {
            idGenerator.generateId(0);
            Assert.fail("Expected Exception for waiting too long");
        } catch (InterruptedException e) {
            Assert.assertTrue("Exception did not relate to maximum time to wait", e.getMessage().contains("maximum time to wait "));
        }
    }

}
