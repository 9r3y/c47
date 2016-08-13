package com.y3r9.c47.dog.swj;

import org.apache.commons.configuration.Configuration;

import com.y3r9.c47.dog.swj.model.parallel.spi.GraphAutoParam;
import com.y3r9.c47.dog.swj.model.parallel.spi.HandlerBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.NodeBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelDataType;
import com.y3r9.c47.dog.swj.model.parallel.spi.Partitioner;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkHandler;
import com.y3r9.c47.dog.swj.model.parallext.AbstractParallelBuilder;

/**
 * The class DataGraphBuilder.
 *
 * @version 1.0
 */
final class DataGraphBuilder extends AbstractParallelBuilder<Data, DataContext, DataResult> {

    public static DataGraph buildGraph(final Configuration parallelConfig) {
        // init builder
        final DataGraphBuilder graphBuilder = new DataGraphBuilder("DATA");
        graphBuilder.setJoinHandler(new DataJoinHandler("JOIN HANDLER"));
        graphBuilder.setSpanHandler(new DataSpanHandler());

        // init graph
        final DataGraph result = new DataGraph();
        result.buildGraph(graphBuilder, parallelConfig);

        return result;
    }

    @Override
    public Partitioner<Data> getPartitioner(final ParallelDataType dataType) {
        return new DataPartitioner();
    }

    @Override
    public DataContext getPartitionContextManager(final int partId, final int totalPartCount) {
        return new DataContext();
    }

    @Override
    public WorkHandler<Data, DataContext, DataResult> getWorkHandler(final int workId) {
        return new DataWorkHandler("WORKER " + workId);
    }

    /**
     * Instantiates a new abstract parallel builder.
     *
     * @param name the name
     */
    protected DataGraphBuilder(final String name) {
        super(name);
    }

}
