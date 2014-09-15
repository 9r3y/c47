package com.y3r9.c47.dog.demo.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示ExecutorCompletionService的使用
 * Created by zyq on 2014/8/19.
 */
public class MultipleServices {

    public static class Exp implements Callable {

        private double m;
        private int n;

        public Exp(double m, int n) {
            this.m = m;
            this.n = n;
        }

        @Override
        public Double call() throws Exception {
            double result = 1;
            for (int i = 0; i < n; i++) {
                result *= m;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("%ncomputed %.02f raised to %d%n", m, n);
            return result;
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Double>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            double m = Math.random() * 10;
            int n = (int) (Math.random() * 1000);
            System.out.printf("Created task for computing: " +
                    "%.02f raised to %d\n", m, n);
            tasks.add(new Exp(m, n));
        }

        ExecutorCompletionService service = new ExecutorCompletionService(executor);
        for (Callable<Double> task : tasks) {
            service.submit(task);
        }
        Lock lock = new ReentrantLock();
        for (int i = 0; i < tasks.size(); i++) {
            lock.lock();
            try {
                Double d = (Double) service.take().get();
                System.out.printf("Result: %E%n", d);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("Error detected during task execution");
            } finally {
                lock.unlock();
            }
        }
        executor.shutdown();
    }
}
