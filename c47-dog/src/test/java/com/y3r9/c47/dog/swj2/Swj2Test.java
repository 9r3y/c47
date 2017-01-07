package com.y3r9.c47.dog.swj2;

import java.io.IOException;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.com.netis.dp.commons.common.statis.JoinNodeObservable;
import cn.com.netis.dp.commons.common.statis.ParallelGraphObservable;

/**
 */
public final class Swj2Test {

    /** The backspace char. */
    private static final char BACKSPACE_CHAR = '\b';

    /** The Last print time. */
    private long lastPrintTime = 0;

    /** The Last out token. */
    private long lastOutToken = 0;

    /**
     * Set the test up.
     */
    @Before
    public void setUp() {
        mockery = new JUnit4Mockery();
    }

    /**
     * Test the default.
     */
    @Test
    public void testDefault() throws IOException {
        final Partitionable<Data> partitioner = new Partitioner();
        final OutputHandler<Data> outHandle = new OutHandler();
        final WorkHandler<Data, Data> work = new Work();
        Processor<Data, Data> processor = new Processor<>(16, partitioner, 2000000, outHandle, work);
        while (true) {
            processor.dispatch(new Data());
            printGraphStatus(processor);
        }
    }

    private void printGraphStatus(final Processor<Data, Data> obs) {
        long now = System.currentTimeMillis();
        final long timeSpan = now - lastPrintTime;
        if (timeSpan < 1000) {
            return;
        }
        final long outToken = obs.getOutToken();
        final long outTokenDelta = outToken - lastOutToken;
        final long outTokenPS = outTokenDelta / timeSpan;

        int cache = obs.getCache();

        StringBuilder sb = new StringBuilder();
                sb.append("\toutTokenPerSec: ").append(outTokenPS)
                .append("\tcache: ").append(cache);
        System.out.println(sb.toString());

        lastOutToken = outToken;
        lastPrintTime = now;
    }

    /**
     * Tear the test down.
     */
    @After
    public void tearDown() {
        mockery.assertIsSatisfied();
    }

    /** The mockery. */
    private transient Mockery mockery;

}