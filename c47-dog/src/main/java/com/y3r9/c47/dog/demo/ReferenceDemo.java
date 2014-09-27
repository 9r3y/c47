package com.y3r9.c47.dog.demo;

import java.lang.ref.*;

/**
 * Created by zyq on 2014/9/27.
 */
public class ReferenceDemo {

    public static void main(String[] args) {
        //softRefernceDemo();
        //weakRefernceDemo();

    }

    public static void phantomRefernceDemo() {
        MyObject obj = new MyObject();
        ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(obj, queue);
        System.out.println("Phantom Get: " + phantomReference.get());
        new CheckRefQueue(queue).start();
        obj = null;
        try {
            Thread.sleep(1000);
            int i = 1;
            while(true) {
                System.out.println("第" + i++ +"次gc");
                System.gc();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void weakRefernceDemo() {
        MyObject obj = new MyObject();
        ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
        WeakReference<MyObject> weakRef = new WeakReference<MyObject>(obj, queue);
        new CheckRefQueue(queue).start();
        obj = null;
        System.out.println("Before GC:Weak Get= " + weakRef.get());
        System.gc();
        System.out.println("After GC:Weak Get= " + weakRef.get());
    }

    /**
     * -Xmx5M
     */
    public static void softRefernceDemo() {
        MyObject obj = new MyObject();
        ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
        SoftReference<MyObject> softReference = new SoftReference<MyObject>(obj, queue);
        new CheckRefQueue(queue).start();
        obj = null;
        System.gc();
        System.out.println("After GC:Soft Get= " + softReference.get());
        System.out.println("分配大块内存");
        byte[] b = new byte[4 * 1024 * 850];
        System.out.println("After new byte[]:Soft Get= " + softReference.get());
    }


}

class MyObject {
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("MyObject's finalize called");
    }

    @Override
    public String toString() {
        return "I am MyObject";
    }
}

class CheckRefQueue implements Runnable {

    private ReferenceQueue<MyObject> queue;

    CheckRefQueue(ReferenceQueue<MyObject> queue) {
        this.queue = queue;
    }

    public void start() {
        Thread thr = new Thread(this);
        thr.setDaemon(true);
        thr.start();
    }

    @Override
    public void run() {
        Reference<MyObject> obj = null;
        try {
            obj = (Reference<MyObject>) queue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (null != obj) {
            System.out.println("Object for SoftReference is " + obj.get());
        }


    }
}
