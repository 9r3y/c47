package com.y3r9.c47.dog.swj2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import com.y3r9.c47.dog.swj.value.Power2Utils;

/**
 * The class Input.
 *
 * @version 1.0
 */
public final class Processor<I extends SwjData, O> {

    @SuppressWarnings("unchecked")
    public boolean dispatch(I input) {
        final int newTail = tail + 1 & cacheSizeMask;
        final boolean result = newTail != head;
        if (result) {
            tail = newTail;
            input.setToken(newTail);
            final int part = partitionable.partition(input);
            partitions[part].add(input);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Processor(int partCount, Partitionable<I> part, int cacheSize, OutputHandler<O> outHandle, WorkHandler<I, O> work) {
        final int count = Power2Utils.power2Ceil(partCount);
        partitions = new Partition[count];
        for (int i = 0; i < count; i++) {
            partitions[i] = new Partition<>();
        }
        part.setPartitionCount(count);
        partitionable = part;

        final int size = Power2Utils.power2Ceil(cacheSize);
        cacheSizeMask = size - 1;
        cache = (O[]) new Object[size];

        outHandler = outHandle;

        workHandler = work;

        workAction = new WorkAction(0, cache.length);

        new Thread(new OutputRunner(), "OutputRunner").start();
        pool.invoke(workAction);
    }

    public long getOutToken() {
        return outToken;
    }

    public int getCache() {
        int result  = tail - head;
        return result < 0 ? result + cache.length : result;
    }

    private final WorkAction workAction;

    private final WorkHandler<I, O> workHandler;

    private final ForkJoinPool pool = ForkJoinPool.commonPool();

    private Partition<I>[] partitions;

    private O[] cache;

    private Partitionable<I> partitionable;

    private OutputHandler<O> outHandler;

    private final int cacheSizeMask;

    /** The Head. */
    private volatile int head = 0;

    /** The Tail. */
    private volatile int tail = 0;

    /** The Out token. */
    private long outToken;

    private final class OutputRunner implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (workAction.isDone()) {
                    workAction.reinitialize();
                    pool.invoke(workAction);
                }
                final O out = cache[head];
                if (out == null) {
                    Thread.yield();
                    continue;
                }
                head = head + 1 & cacheSizeMask;
                outHandler.handle(out);
                outToken++;
            }
        }
    }

    final class WorkAction extends RecursiveAction {

        private final int start;

        private final int end;

        @Override
        protected void compute() {
            if (end - start <= 1) {
                final Partition<I> partition = partitions[start];
                partition.fetch();
                while (true) {
                    final I input = partition.get();
                    if (input == null) {
                        break;
                    }
                    final O out = workHandler.handle(input);
                    cache[input.getToken()] = out;
                }
            } else {
                int middle = (start + end) / 2;
                invokeAll(new WorkAction(start, middle),
                        new WorkAction(middle, end));
            }
        }

        public WorkAction(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
    }


}
