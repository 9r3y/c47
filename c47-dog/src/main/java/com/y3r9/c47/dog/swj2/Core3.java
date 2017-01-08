package com.y3r9.c47.dog.swj2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The class Core.
 *
 * @version 1.0
 */
final class Core3 implements Runnable, Core {

    public void start() {
        new Thread(this, "Core" + id).start();
    }

    @Override
    public void prepareNextSprint() {
        head.set(start);
    }

    @Override
    public void run() {
        prepareNextSprint();
        while (!Thread.interrupted()) {

            int index;
            while((index = head.getAndIncrement()) < end) {
                final Partition partition = partitions[index];
                partition.consume();
            }

//            for (int i = (id + 1) & coreSizeMask ; i != id; i = ++i & coreSizeMask) {
//                final Core3 steal = (Core3) cores[i];
//                final AtomicInteger stealHead = steal.head;
//                final int stealEnd = steal.end;
//                while((index = stealHead.getAndIncrement()) < stealEnd) {
//                    final Partition partition = partitions[index];
//                    partition.consume();
//                }
//            }

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public Core3(int id, Processor processor, int start, int end) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.partitions = processor.partitions;
        this.cores = processor.cores;
        coreSize = cores.length;
        coreSizeMask = coreSize - 1;
        this.barrier = processor.barrier;
    }

    private final int id;

    private final int start;

    private final int end;

    private AtomicInteger head = new AtomicInteger();

    private final Partition[] partitions;

    private final Core[] cores;

    private final int coreSize;

    private final int coreSizeMask;

    private final CyclicBarrier barrier;
}
