package cn.com.netis.taux.zmqsvr;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

/**
 * The class Main.
 *
 * @version 1.0
 */
public final class Main implements ProgressObserver {

    /** The last progress index, guarded by lock. */
    private int lastProgressIndex = 0;

    /** The constant PROGRESS_FREQ. */
    private static final int PROGRESS_FREQ = 10;

    /** The progress chars. */
    private static final char[] PROGRESS_CHARS = { '|', '/', '-', '\\' };

    /** The constant LOG. */
    private static Logger LOG = LoggerFactory.getLogger(ZmqMsgConsumer.class);

    /**
     * Main void.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        new Main().start(args);
    }

    /**
     * Start void.
     *
     * @param args the args
     */
    public void start(final String[] args) {
        countDownLatch = new CountDownLatch(args.length);
        for (String arg : args) {
            final int port = Integer.parseInt(arg);
            ZmqMsgConsumable zmqMsgConsumer = new ZmqMsgConsumer(port, this);
            final Thread thr = new Thread(zmqMsgConsumer, "ZmqMsgConsumer " + port);
            thr.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOG.error(e.toString(), e);
        }
    }

    @Override
    public void countDown() {
        countDownLatch.countDown();
    }

    /**
     * Update progress.
     */
    @Override
    public void updateProgress() {
        // Get the seconds, roughly equivalent to divided by 1000.
        long now = System.currentTimeMillis() >> PROGRESS_FREQ;

        int index = (int) (now % PROGRESS_CHARS.length);

        if (index != lastProgressIndex) {
            System.out.print('\b');
            System.out.print(PROGRESS_CHARS[index]);
            lastProgressIndex = index;
        }
    }

    /** The Count down latch. */
    private CountDownLatch countDownLatch;
}
