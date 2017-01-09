package com.y3r9.c47.dog.swj2;

import java.util.concurrent.CyclicBarrier;

import com.y3r9.c47.dog.swj.value.Power2Utils;

/**
 * The class Input.
 *
 * @version 1.0
 */
public final class Processor<I extends SwjData, C, O> {

    @SuppressWarnings("unchecked")
    public boolean dispatch(I input) {
        final int oldTail = tail;
        final int newTail = (oldTail + 1) & cacheSizeMask;
        final boolean result = newTail != head;
        if (result) {
            token++;
            input.setToken(oldTail);
            tail = newTail;
            final int part = partitionable.partition(input);
            partitions[part].add(input);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Processor(int partCount, Partitionable<I> part, int cacheSize, OutputHandler<O> outHandle, WorkHandler<I,C, O> work) {
        final int size = Power2Utils.power2Ceil(cacheSize);
        cacheSizeMask = size - 1;
        cache = (O[]) new Object[size];

        workHandler = work;

        final int count = Power2Utils.power2Ceil(partCount);
        partitions = new Partition[count];
        for (int i = 0; i < count; i++) {
            partitions[i] = new Partition(this, new Object());
        }
        part.setPartitionCount(count);
        partitionable = part;

        outHandler = outHandle;


        int coreAvail = Runtime.getRuntime().availableProcessors();
        int avail = 4;
        cores = new Core[avail];
        final int ppCount = partCount / avail;
        barrier = new CyclicBarrier(avail, new Bumper());
        for (int i = 0, j = 0; i < avail; i++, j+=ppCount) {
//            cores[i] = new Core1(i, this, j, i == avail -1 ? partCount : j + ppCount);
//            cores[i] = new Core2(i, this);
            cores[i] = new Core3(i, this, j, i == avail -1 ? partCount : j + ppCount);
        }
        for (Core core : cores) {
            core.start();
        }
//        new Thread(new OutputRunner(), "OutputRunner").start();
    }

    public long getToken() {
        return token;
    }

    public int getCache() {
        int result  = tail - head;
        return result < 0 ? result + cache.length : result;
    }

    final WorkHandler<I,C, O> workHandler;

    Partition[] partitions;

    O[] cache;

    Core[] cores;

    private Partitionable<I> partitionable;

    private OutputHandler<O> outHandler;

    private final int cacheSizeMask;

    CyclicBarrier barrier;

    /** The Head. */
    private int head = 0;

    /** The Tail. */
    private int tail = 0;

    /** The Out token. */
    private long token;

    private final class OutputRunner implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                final int oldHead = head;
                final O out = cache[oldHead];
                if (out == null) {
                    Thread.yield();
                    continue;
                }
                cache[oldHead] = null;
                head = (oldHead + 1) & cacheSizeMask;
                outHandler.handle(out);
            }
        }
    }

    private final class Bumper implements Runnable {

        @Override
        public void run() {
            int count = 0;
            while (true) {
                final int oldHead = head;
                final O out = cache[oldHead];
                if (out == null) {
                    break;
                }
                count++;
                cache[oldHead] = null;
                head = (oldHead + 1) & cacheSizeMask;
                outHandler.handle(out);
            }
//            System.out.println("sprint " + count);
            for (Core core : cores) {
                core.prepareNextSprint();
            }
        }
    }

}
