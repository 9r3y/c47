package com.y3r9.c47.dog.swj2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class Partition.
 *
 * @version 1.0
 */
final class Partition {

    void add(SwjData data) {
        final int oldTail = tail;
        queue[oldTail] = data;
        tail = (oldTail + 1) & queueSizeMask;
    }

    int consume() {
        final int deadLine = tail;
        int index = head;
        while (index != deadLine) {
            final SwjData input = queue[index];
            if (input == null) {
                // Memory not synchronized.
                break;
            }
            final Object out = workHandler.handle(input, context);
            cache[input.getToken()] = out;
            index = (index + 1) & queueSizeMask;
        }
        int result = index - head;
        result = result < 0 ? result + queueSize : result;
        head = index;
        return result;
    }

//    synchronized void add(SwjData data) {
//        inQueue.add(data);
//    }
//
//    synchronized void fetch() {
//        Queue tmp = inQueue;
//        inQueue = outQueue;
//        outQueue = tmp;
//    }
//
//    int consume() {
//        fetch();
//        int result = 0;
//        while (true) {
//            final SwjData input = outQueue.poll();
//            if (input == null) {
//                break;
//            }
//            result++;
//            final Object out = workHandler.handle(input, context);
//            cache[input.getToken()] = out;
//        }
//        return result;
//    }

    int lockConsume() {
        if (!partSyn.tryLock()) {
            return 0;
        }
        try {
            return consume();
        } finally {
            partSyn.unlock();
        }
    }

    Partition(Processor processor, Object context) {
        this.cache = processor.cache;
        this.context = context;
        this.workHandler = processor.workHandler;
        queue = new SwjData[cache.length];
        queueSize = queue.length;
        queueSizeMask = queueSize - 1;

    }

    private final SwjData[] queue;

    private final int queueSize;

    private final int queueSizeMask;

    /** The Head. */
    private int head = 0;

    /** The Tail. */
    private int tail = 0;

    private Queue<SwjData> inQueue = new LinkedList<>();

    private Queue<SwjData> outQueue = new LinkedList<>();

    private Object context;

    private final Object[] cache;

    private WorkHandler workHandler;

    private final Lock partSyn = new ReentrantLock();

    private final Lock queueSyn = new ReentrantLock();


}
