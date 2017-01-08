package com.y3r9.c47.dog.swj;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.y3r9.c47.dog.swj.config.key.ParallelKey;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.model.collection.SafeInDualQueue;
import com.y3r9.c47.dog.swj.model.collection.spi.DualQueue;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitNode;

import cn.com.netis.dp.commons.common.statis.JoinNodeObservable;
import cn.com.netis.dp.commons.common.statis.ParallelGraphObservable;

/**
 */
public final class SwjTest {

    /** The backspace char. */
    private static final char BACKSPACE_CHAR = '\b';

    /**
     * Set the test up.
     */
    @Before
    public void setUp() {
        mockery = new JUnit4Mockery();
    }

    @Test
    public void testPartitionNodeQueue() {

//        DualQueue<Integer> queue = new SafeInDualQueue<>();
//        Queue<Integer> queue = new ConcurrentLinkedDeque<>();
        final Lock lock = new ReentrantLock();
//        final Lock lock = new CasLock();


        Integer in = new Integer(10);
        for (long i = 0; i < 100000000L; i++) {
            lock.lock();
            lock.unlock();
//            queue.addToInQueue(in);
//            queue.add(10);
        }

        long start = System.currentTimeMillis();

        for (long i = 0; i < 100000000L; i++) {
            lock.lock();
            lock.unlock();
//            queue.addToInQueue(in);
//            queue.add(10);
        }

        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * Test the default.
     */
    @Test
    public void testDefault() throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            System.exit(3);
        });

        DataGraph graph = DataGraphBuilder.buildGraph(getNtrParseParallelConfig());
        SplitNode<Data, DataContext, DataResult> splitNode = graph.getSplitNode();


        splitNode.open();
//        Random random = new Random();
        final Thread meterThread = new Thread(new Meter(graph), "Meter");
        meterThread.setDaemon(true);
        meterThread.start();
        while (true) {
            Data data = new Data();
            splitNode.dispatch(data);
        }
    }

    private Configuration getPreParseParallelConfig() {
        final Configuration result = new XMLConfiguration();
        result.addProperty(ParallelKey.workerCount.name(), 1);
        result.addProperty(ParallelKey.workerToPartFactor.name(), 1);

        result.addProperty(ParallelKey.joinPipeCapability.name(), 65536);
        result.addProperty(ParallelKey.cacheTokenCount.name(), 200000);

        Configuration splitNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.splitNode.name(), splitNodeConfig);
        splitNodeConfig.addProperty(PollingKey.overSizeRetryCount.name(), 2);

        Configuration workNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.workNode.name(), workNodeConfig);
        workNodeConfig.addProperty(PollingKey.idleRetryCount.name(), 8);

        Configuration joinNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.joinNode.name(), joinNodeConfig);
        joinNodeConfig.addProperty(PollingKey.idleRetryCount.name(), -1);

        Configuration partitionNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.partitionNode.name(), partitionNodeConfig);

        return result;
    }

    private Configuration getNtrParseParallelConfig() {
        final Configuration result = new XMLConfiguration();
        result.addProperty(ParallelKey.workerCount.name(), 5);
        result.addProperty(ParallelKey.partitionCount.name(), 1024);
//        result.addProperty(ParallelKey.joinPipeCapability.name(), 4000000);
//        result.addProperty(ParallelKey.cacheTokenCount.name(), 1000000);
        result.addProperty(ParallelKey.joinPipeCapability.name(), 4000000);
        result.addProperty(ParallelKey.cacheTokenCount.name(), 10000000);

        Configuration splitNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.splitNode.name(), splitNodeConfig);
        splitNodeConfig.addProperty(PollingKey.overSizeRetryCount.name(), -1);
        splitNodeConfig.addProperty(PollingKey.idleParkMode.name(), "sleep");

        Configuration workNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.workNode.name(), workNodeConfig);
        workNodeConfig.addProperty(PollingKey.idleRetryCount.name(), 8);
        workNodeConfig.addProperty(PollingKey.idleParkMode.name(), "yield");

        Configuration joinNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.joinNode.name(), joinNodeConfig);
        joinNodeConfig.addProperty(PollingKey.idleRetryCount.name(), -1);
        joinNodeConfig.addProperty(ParallelKey.dataType.name(), "batch");

        Configuration partitionNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.partitionNode.name(), partitionNodeConfig);
//        partitionNodeConfig.addProperty(PollingKey.breakProcessSize.name(), 64);
//        partitionNodeConfig.addProperty(PollingKey.partitionScheduler.name(), "queue");

        return result;
    }

    private Configuration getBtrParseParallelConfig() {
        final Configuration result = new XMLConfiguration();
        result.addProperty(ParallelKey.workerCount.name(), 15);
        result.addProperty(ParallelKey.partitionCount.name(), 2048);
//        result.addProperty(ParallelKey.joinPipeCapability.name(), 4000000);
//        result.addProperty(ParallelKey.cacheTokenCount.name(), 1000000);
        result.addProperty(ParallelKey.joinPipeCapability.name(), 4000000);
        result.addProperty(ParallelKey.cacheTokenCount.name(), 100000);

        Configuration splitNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.splitNode.name(), splitNodeConfig);
        splitNodeConfig.addProperty(PollingKey.overSizeRetryCount.name(), 16);

        Configuration workNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.workNode.name(), workNodeConfig);
        workNodeConfig.addProperty(PollingKey.idleRetryCount.name(), 128);

        Configuration joinNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.joinNode.name(), joinNodeConfig);
        joinNodeConfig.addProperty(PollingKey.idleRetryCount.name(), -1);
        joinNodeConfig.addProperty(ParallelKey.dataType.name(), "batch");

        Configuration partitionNodeConfig = new XMLConfiguration();
        result.addProperty(ParallelKey.partitionNode.name(), partitionNodeConfig);
        partitionNodeConfig.addProperty(PollingKey.breakProcessSize.name(), 1);
        partitionNodeConfig.addProperty(PollingKey.partitionScheduler.name(), "priority");

        return result;
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