package com.y3r9.c47.dog.demo.concurrent;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by zyq on 2014/8/19.
 */
public class ConcurrentPrimeFinder extends AbstractPrimeFinder {
    private final int poolSize;
    private final int numberOfParts;

    public ConcurrentPrimeFinder(int poolSize, int numberOfParts) {
        this.poolSize = poolSize;
        this.numberOfParts = numberOfParts;
    }

    @Override
    public int countPrimes(int number) {
        int count = 0;

        try {
            final List<Callable<Integer>> partitions = new ArrayList<>();
            final int chunksPerPartition = number /numberOfParts;
            for (int i=0; i<numberOfParts; i++) {
                final int lower = (i * chunksPerPartition) + 1;
                final int upper = (i == numberOfParts - 1) ? number : lower + chunksPerPartition - 1;
                partitions.add(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return countPrimesInRange(lower, upper);
                    }
                });
            }
            final ExecutorService executorPool = Executors.newFixedThreadPool(poolSize);
            final List<Future<Integer>> resultFromParts = executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
            executorPool.shutdown();
            for (final Future<Integer> result : resultFromParts) {
                count += result.get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public static void main(String[] args) {
        new ConcurrentPrimeFinder(4, 100000).timeAndCompute(10000000);

    }
}
