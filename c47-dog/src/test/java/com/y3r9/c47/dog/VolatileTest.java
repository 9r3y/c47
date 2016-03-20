package com.y3r9.c47.dog;

/**
 * The class VisibilityTest.
 *
 * @version 1.0
 */
final class VolatileTest {
    public static void main(String[] args) throws Exception {
        VolatileThread v = new VolatileThread();
        v.start();

        Thread.sleep(1000);//停顿1秒等待新启线程执行
        System.out.println("即将置stop值为true");
        v.stopIt();
        Thread.sleep(1000);
        System.out.println("finish main");
        System.out.println("main中通过getStop获取的stop值:" + v.getStop());
    }

    private static class VolatileThread extends Thread {
        private boolean stop;

        public void run() {
            int i = 0;
            System.out.println("start loop.");
            while(!getStop()) {
                i++;
            }
            System.out.println("finish loop,i=" + i);
        }

        public void stopIt() {
            stop = true;
        }

        public boolean getStop(){
            return stop;
        }
    }
}


