package com.y3r9.c47.dog.swj.counter;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class MonitorCounterProvider.
 * 
 * @version 1.2
 * @since project 2.10
 */
public final class MonitorCounterProvider implements MonitorCounter {

    @Override
    public void updateData(final int streamId, final long incFileCount, final long bytes,
            final String lastFileName, final long fileCount) {
        final PureMonitorCounter counter = counters.get(streamId);
        if (counter == null) {
            return;
        }
        counter.updateData(incFileCount, bytes, lastFileName, fileCount);
    }

    @Override
    public void updateData(final int streamId, final long timestamp) {
        final PureMonitorCounter counter = counters.get(streamId);
        if (counter == null) {
            return;
        }
        counter.updateData(timestamp);
    }

    @Override
    public void addData(final int streamId, final String folder) {
        final PureMonitorCounter counter = new PureMonitorCounterData();
        counter.setFolder(folder);
        counters.put(streamId, counter);
    }

    @Override
    public Map<Integer, PureMonitorCounter> getCounters() {
        return counters;
    }

    /** The counters. */
    private final transient Map<Integer, PureMonitorCounter> counters;

    {
        counters = new HashMap<Integer, PureMonitorCounter>();
    }
}
