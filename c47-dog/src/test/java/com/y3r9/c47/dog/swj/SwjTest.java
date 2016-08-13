package com.y3r9.c47.dog.swj;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import com.y3r9.c47.dog.swj.config.key.ParallelKey;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitNode;

import cn.com.netis.dp.commons.common.statis.JoinNodeObservable;
import cn.com.netis.dp.commons.common.statis.ParallelGraphObservable;

/**
 */
public final class SwjTest {

    /** The backspace char. */
    private static final char BACKSPACE_CHAR = '\b';

    /** The Last print time. */
    private long lastPrintTime = 0;

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
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            System.exit(3);
        });

        DataGraph graph = DataGraphBuilder.buildGraph(getBtrParseParallelConfig());
        SplitNode<Data, DataContext, DataResult> splitNode = graph.getSplitNode();

        splitNode.open();
        Random random = new Random();
        while (true) {
            Data data = new Data();
            data.setMs(random.nextInt(4096));
            printGraphStatus(graph);
            splitNode.dispatch(data);
        }
    }

    private void printGraphStatus(final ParallelGraphObservable obs) {
        long now = System.currentTimeMillis();
        if (now - lastPrintTime < 1000) {
            return;
        }
        final long dispatchToken = obs.getDispatchTokenPosition();
        final JoinNodeObservable joinNodeObs = obs.getJoinNodeObservable();
        final long outToken = joinNodeObs.getOutTokenPosition();

        final int[] caches = obs.getPartitionProfile();
        int cache = 0;
        for (int num : caches) {
            cache += num;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("dispatchToken: ").append(dispatchToken)
                .append("\toutToken: ").append(outToken)
                .append("\tinUseToken: ").append(dispatchToken - outToken)
                .append("\tcache: ").append(cache);
        System.out.println(sb.toString());

        lastPrintTime = now;
    }

    private Configuration getPreParseParallelConfig() {
        final Configuration result = new XMLConfiguration();
        result.addProperty(ParallelKey.workerCount.name(), 7);
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

    private Configuration getBtrParseParallelConfig() {
        final Configuration result = new XMLConfiguration();
        result.addProperty(ParallelKey.workerCount.name(), 15);
        result.addProperty(ParallelKey.partitionCount.name(), 2048);
//        result.addProperty(ParallelKey.joinPipeCapability.name(), 4000000);
//        result.addProperty(ParallelKey.cacheTokenCount.name(), 1000000);
        result.addProperty(ParallelKey.joinPipeCapability.name(), 4000000);
        result.addProperty(ParallelKey.cacheTokenCount.name(), 10000);

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
        partitionNodeConfig.addProperty(PollingKey.breakProcessSize.name(), 64);
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