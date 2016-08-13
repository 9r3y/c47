package com.y3r9.c47.dog.swj.counter;

import cn.com.netis.dp.commons.common.statis.DropCounter;

/**
 * A factory for creating Counter objects.
 * 
 * @version 1.7
 * @since project 2.0
 */
public final class CounterFactory {

    /**
     * Creates a new simple counter object.
     * 
     * @return the simple counter
     */
    public static ByteCounter createByteCounter() {
        return new ByteCounterProvider();
    }

    /**
     * Creates a new packet cycle counter object.
     * 
     * @return the packet cycle counter
     */
    public static CycleCounter createPacketCycleCounter() {
        return new CycleCounterProvider();
    }

    /**
     * Creates a new Counter object.
     * 
     * @return the drop counter
     * @since 1.3
     */
    public static DropCounter createDropCounter() {
        return new DropCounterProvider();
    }

    /**
     * Creates a new Counter object.
     * 
     * @return the record counter
     * @since 1.4
     */
    public static RecordCounter createRecordCounter() {
        return new RecordCounterProvider();
    }

    /**
     * Creates a new Counter object.
     * 
     * @return the pure drop counter
     * @since 1.5
     */
    public static PureDropCounter createPureDropCounter() {
        return new PureDropCounterProvider();
    }

    /**
     * Creates the decode counter.
     *
     * @return the decode counter
     * @since 1.7
     */
    public static DecodeCounter createDecodeCounter() {
        return new DecodeCounterProvider();
    }

    /**
     * Instantiates a new counter factory.
     */
    private CounterFactory() {
    }
}
