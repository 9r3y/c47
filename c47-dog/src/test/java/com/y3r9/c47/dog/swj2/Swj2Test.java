package com.y3r9.c47.dog.swj2;

import java.io.IOException;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 */
public final class Swj2Test {

    /** The backspace char. */
    private static final char BACKSPACE_CHAR = '\b';

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
        final WorkHandler<Data, Object, Data> work = new Work();


        Processor<Data, Object, Data> processor = new Processor<>(4, partitioner, 100000, outHandle, work);
        final Thread meterThread = new Thread(new Meter(processor), "Meter");
        meterThread.setDaemon(true);
        meterThread.start();
        while (true) {
            if (!processor.dispatch(new Data())) {
                Thread.yield();
            }
        }
    }

    @Test
    public void test() {
        new Loop().start();
        while (true) {
            l++;
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += 1;
            }
        }
    }

    private long l;

    class Loop extends Thread {

        /** The Last print time. */
        private long lastPrintTime = 0;

        /** The Last out token. */
        private long lastOutToken = 0;

        @Override
        public void run() {
            while (true) {
                long now = System.currentTimeMillis();
                final long timeSpan = now - lastPrintTime;
                final long outTokenDelta = l -  lastOutToken;
                final long outTokenPS = outTokenDelta / timeSpan;


                StringBuilder sb = new StringBuilder();
                sb.append("\toutTokenPerSec: ").append(outTokenPS);
                System.out.println(sb.toString());

                lastOutToken = l;
                lastPrintTime = now;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

        }
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