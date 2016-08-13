package com.y3r9.c47.dog.swj.model.parallext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.netis.dp.commons.common.packet.PacketMetaType;
import cn.com.netis.dp.commons.common.statis.DropCounter;
import cn.com.netis.dp.commons.common.statis.DropObservable;
import com.y3r9.c47.dog.swj.counter.DropCounterProvider;
import com.y3r9.c47.dog.swj.model.parallel.spi.PipelineAwareJoinHandler;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineInput;

/**
 * The Class AbstractJoinHandler.
 * 
 * @param <R> the generic result data type
 * @param <V> the generic value type
 * @version 1.0
 * @see PipelineAwareJoinHandler
 * @since project 3.0
 */
public abstract class AbstractJoinHandler<R, V> implements PipelineAwareJoinHandler<R, V> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJoinHandler.class);

    /** The Constant WAIT_INBAND_SLEEP_MILLI. */
    private static final int WAIT_INBAND_SLEEP_MILLI = 100;

    @Override
    public final boolean waitForInbandTerminate(final int timeoutSecond) {
        final long threshold = TimeUnit.SECONDS.toMillis(timeoutSecond);
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < threshold) {
            if (inbandTerminate) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(WAIT_INBAND_SLEEP_MILLI);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Update inband terminate.
     * 
     * @param metaType the meta type
     */
    protected final void updateInbandTerminate(final int metaType) {
        if (metaType == PacketMetaType.TERMINATE) {
            inbandTerminate = true;
            LOG.debug(new StringBuilder().append("JoinHandler=").append(getNodeName())
                    .append(" detects inband TERMINATE sginal.").toString());
        }
    }

    @Override
    public final String getNodeName() {
        return handlerName;
    }

    /**
     * Put to next pipeline.
     * 
     * @param value the value
     */
    protected final void putToNextPipeline(final V value) {
        nextPipeline.dispatch(value);
    }

    /**
     * Gets the drop observer.
     * 
     * @return the drop observer
     */
    @Override
    public final DropObservable getDropObserver() {
        return dropCounter;
    }

    @Override
    public boolean notifyAfterThreadOpen() {
        /**
         * Can be extended.
         */
        return true;
    }

    @Override
    public boolean notifyBeforeThreadClose() {
        /**
         * Can be extended.
         */
        return true;
    }

    @Override
    public void open() throws IOException {
        // to open next pipeline
        if (nextPipeline != null) {
            nextPipeline.open();
        }
        inbandTerminate = false;
    }

    @Override
    public final void close() throws IOException {
        // to close next pipeline force
        if (nextPipeline != null) {
            try {
                nextPipeline.close();
            } catch (Exception e) {
                LOG.debug(new StringBuilder().append("JoinHandler=").append(getNodeName())
                        .append(" detects an exception  while to close the next pipeline. ")
                        .append(" exception=").append(e.toString()).toString());
            }
        }
    }

    @Override
    public final PipelineInput<V> getNextPipeline() {
        return nextPipeline;
    }

    @Override
    public final void setNextPipeline(final PipelineInput<V> value) {
        nextPipeline = value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("handlerName=").append(handlerName).append(", nextPipeline=")
                .append(nextPipeline).append(", inbandTerminate=").append(inbandTerminate)
                .append(", dropCounter=").append(dropCounter);
        return builder.toString();
    }

    /**
     * Instantiates a new abstract join handler.
     * 
     * @param name the name
     */
    protected AbstractJoinHandler(final String name) {
        handlerName = name;
    }

    /** The handler name. */
    private final String handlerName;

    /** The next pipeline. */
    private PipelineInput<V> nextPipeline;

    /** The inband terminate. */
    private volatile boolean inbandTerminate;

    /** The drop counter. */
    private final DropCounter dropCounter;

    {
        dropCounter = new DropCounterProvider();
    }
}
