package com.y3r9.c47.dog.swj2;

/**
 * The class Meter.
 *
 * @version 1.0
 */
final class Meter implements Runnable {

    /** The Obs. */
    private Processor obs;

    /** The Last print time. */
    private long lastPrintTime = 0;

    /** The Last out token. */
    private long lastToken = 0;


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long now = System.currentTimeMillis();
            final long timeSpan = now - lastPrintTime;
            final long token = obs.getToken();
            final long cache = obs.getCache();
            final long tokenDelta = token - lastToken;
            final long tokenPS = tokenDelta / timeSpan;

//            int cache = obs.getCache();

            StringBuilder sb = new StringBuilder();
            sb.append("TokenPerSec: ").append(tokenPS);
            sb.append("\tCache: ").append(cache);
            System.out.println(sb.toString());

            lastToken = token;
            lastPrintTime = now;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Instantiates a new Meter.
     *
     * @param obs the obs
     */
    Meter(final Processor obs) {
        this.obs = obs;
    }
}
