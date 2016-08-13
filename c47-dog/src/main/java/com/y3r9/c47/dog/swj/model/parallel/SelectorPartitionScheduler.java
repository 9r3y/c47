package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;

/**
 * The class DefaultPartitionNodeScheduler.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 */
final class SelectorPartitionScheduler<D, C, R> extends AbstractPartitionScheduler<D, C, R> {

    /**
     * Instantiates a new Abstract partition producer.
     *
     * @param partMgr the part mgr
     */
    protected SelectorPartitionScheduler(final PartitionNodeManageable<D, C, R> partMgr) {
        super(partMgr);
    }
}
