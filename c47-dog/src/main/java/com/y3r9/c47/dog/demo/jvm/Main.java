package com.y3r9.c47.dog.demo.jvm;

import java.util.Set;
import java.util.Vector;

/**
 * Created by Ethan.Zhou on 2014/9/15.
 */
public class Main {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        //xmx();
        //xms();
        //testAllocation();
        testPretenureSizeThreshold();
    }

    public static void xmx() {
        Vector v = new Vector<>();
        for (int i=1; i<=10; i++) {
            byte[] b = new byte[1024*1024];
            v.add(b);
            System.out.println(i + "M is allocated");
        }
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory()/1024/1024 + "M");
    }

    public static void xms() {
        Vector v = new Vector<>();
        for (int i=1; i<=10; i++) {
            byte[] b = new byte[1024*1024];
            if (v.size() == 3) {
                v.clear();
            }
        }
    }

    /**
     * -XX:+UseSerialGC -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     */
    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB];
    }

    /**
     * -XX:+UseSerialGC -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:PretenureSizeThreshold=3145728
     */
    public static void testPretenureSizeThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB];
    }

}
