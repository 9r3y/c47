package cn.com.netis.taux.zmqsvr;

/**
 * The interface to observe the ProgressObservable objects.
 *
 * @version 1.0
 */
public interface ProgressObserver {

    /**
     * Update the progress by ProgressObservable objects.
     */
    void updateProgress();

    /**
     * Count down.
     */
    void countDown();

}
