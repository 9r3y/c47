package com.y3r9.c47.dog.demo.concurrent;

/**
 * Created by zyq on 2014/8/19.
 */
public class SequentialPrimeFinder extends AbstractPrimeFinder{
    @Override
    public int countPrimes(final int number) {
        return countPrimesInRange(1, number);
    }

    public static void main(String[] args) {
        new SequentialPrimeFinder().timeAndCompute(10000000);
    }
}
