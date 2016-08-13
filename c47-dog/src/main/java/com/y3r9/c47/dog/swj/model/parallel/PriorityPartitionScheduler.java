package com.y3r9.c47.dog.swj.model.parallel;

import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.y3r9.c47.dog.swj.model.collection.SafeInDualQueue;
import com.y3r9.c47.dog.swj.model.collection.spi.DualQueue;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;

/**
 * The class PriorityPartitionProducer.
 * Keep a PartitionNode heap sorted by head-token. If SplitNode dispatch an item to a empty
 * PartitionNode, add it to the heap. A WorkNode always consume the PartitionNode with the
 * minimal head-token, give it back to the heap if it is not empty after consumed, or leave for
 * the SplitNode to add it back to the heap.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 */
final class PriorityPartitionScheduler<D, C, R>
        extends AbstractPartitionScheduler<D, C, R> {

    @Override
    public void dispatchItem(final long token, final D data) {
        final int selector = getPartitionNodeManager().getPartSelector(token, data);
        final PartitionNode<D, C, R> part = getPartitionNodeManager()
                .getPartitionBySelector(selector);
        if (part.unmarkEmptyAddItem(token, data)) {
            // If partition is marked empty by consumer before, the dispatcher should put partition
            // into ready partition queue after new item is added.
            readyParts.addToInQueue(part);
        }
    }

    @Override
    public PartitionNode<D, C, R> getPartition(final int selector,
            final PartitionNode<D, C, R> lastConsumed) {
        heapLock.lock();
        PartitionNode<D, C, R> part;
        try {
            if (lastConsumed != null) {
                if (!lastConsumed.markEmpty()) {
                    // If partition is not empty, it's the consumer's responsibility to give
                    // the last consumed partition back to the heap.
                    heap.add(lastConsumed);
                }
            }
            part = heap.peek();
            if (part != null) {
                PartitionNode<D, C, R> readyPart = readyParts.peekFromInQueue();
                if (readyPart != null &&
                        part.getCacheQueueHeadToken() > readyPart.getCacheQueueHeadToken()) {
                    // If head token in ready queue is earlier, should drain ready queue to heap.
                    drainReadyPartitions();
                }
            } else {
                // heap is empty, need drain ready queue to heap.
                drainReadyPartitions();
            }
            return heap.poll();
        } finally {
            heapLock.unlock();
        }
    }

    /**
     * Drain ready partitions.
     */
    private void drainReadyPartitions() {
        readyParts.safeDrainInQueueToOut();
        PartitionNode<D, C, R> part;
        while ((part = readyParts.removeFromOutQueue()) != null) {
            heap.add(part);
        }
    }

    @Override
    public boolean needLockPartition() {
        return false;
    }

    /**
     * Instantiates a new Abstract partition producer.
     *
     * @param partMgr the part mgr
     */
    protected PriorityPartitionScheduler(final PartitionNodeManageable<D, C, R> partMgr) {
        super(partMgr);
    }

    /** The Heap lock. */
    private ReentrantLock heapLock;

    /** The Heap. */
    private PriorityQueue<PartitionNode<D, C, R>> heap;

    /** The partitions inside readyParts are ready to be add to the heap. */
    private DualQueue<PartitionNode<D, C, R>> readyParts;

    {
        heapLock = new ReentrantLock();
        heap = new PriorityQueue<>(new PartitionComparator());
        readyParts = new SafeInDualQueue<>();
    }

}
