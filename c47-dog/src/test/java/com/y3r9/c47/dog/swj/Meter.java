package com.y3r9.c47.dog.swj;

import java.util.concurrent.TimeUnit;

import cn.com.netis.dp.commons.common.statis.JoinNodeObservable;
import cn.com.netis.dp.commons.common.statis.ParallelGraphObservable;

/**
 * The class Meter.
 *
 * @version 1.0
 */
final class Meter implements Runnable {

    /** The Obs. */
    private ParallelGraphObservable obs;

    /** The Last print time. */
    private long lastPrintTime = 0;

    /** The Last out token. */
    private long lastOutToken = 0;


    @Override
    public void run() {
        while (true) {
            long now = System.currentTimeMillis();
            final long timeSpan = now - lastPrintTime;
            final long dispatchToken = obs.getDispatchTokenPosition();
            final JoinNodeObservable joinNodeObs = obs.getJoinNodeObservable();
            final long outToken = joinNodeObs.getOutTokenPosition();
            final long outTokenDelta = outToken - lastOutToken;
            final long outTokenPS = outTokenDelta / timeSpan;

            final int[] caches = obs.getPartitionProfile();
            int cache = 0;
            for (int num : caches) {
                cache += num;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("dispatchToken: ").append(dispatchToken)
                    .append("\toutToken: ").append(outToken)
                    .append("\toutTokenPerSec: ").append(outTokenPS)
                    .append("\tinUseToken: ").append(dispatchToken - outToken)
                    .append("\tcache: ").append(cache);
            System.out.println(sb.toString());

            lastOutToken = outToken;
            lastPrintTime = now;

            try {
                TimeUnit.SECONDS.sleep(1);
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
    public Meter(final ParallelGraphObservable obs) {
        this.obs = obs;
    }
}
