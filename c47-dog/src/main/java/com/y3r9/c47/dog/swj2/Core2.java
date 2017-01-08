package com.y3r9.c47.dog.swj2;

/**
 * The class Core.
 *
 * @version 1.0
 */
final class Core2 implements Runnable, Core {

    public void start() {
        new Thread(this, "Core" + id).start();
    }

    @Override
    public void prepareNextSprint() {

    }

    @Override
    public void run() {
        int index = 0;
        while (!Thread.interrupted()) {

            int tryCount = 0;
            while (true) {
                index = (index + 1) & partitions.length - 1;
                final Partition partition = partitions[index];
                final int consumed = partition.lockConsume();
                if (consumed > 0) {
                    break;
                } else {
                    if (++tryCount == 8) {
                        Thread.yield();
                        break;
                    }
                }
            }
        }
    }

    public Core2(int id, Processor processor) {
        this.id = id;
        this.partitions = processor.partitions;
    }

    private final int id;

    private final Partition[] partitions;

}
