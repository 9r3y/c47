package com.y3r9.c47.dog.demo.jvm;

import java.util.Vector;

/**
 * Created by Ethan.Zhou on 2014/9/15.
 */
public class Main {

    public static void main(String[] args) {
        //xmx();
        xms();

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
}
