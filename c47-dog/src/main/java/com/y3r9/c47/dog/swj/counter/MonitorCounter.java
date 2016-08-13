package com.y3r9.c47.dog.swj.counter;

import java.util.Map;

/**
 * The Interface MonitorCounter.
 * 
 * @version 1.1
 * @since project 2.10
 */
public interface MonitorCounter {

    /**
     * Update data.
     * 
     * @param streamId the stream id
     * @param incFileCount the increase file count
     * @param bytes the bytes
     * @param lastFilePath the last file path
     * @param fileCount the file count
     * @since 1.1
     */
    void updateData(int streamId, long incFileCount, long bytes, String lastFilePath,
            long fileCount);

    /**
     * Adds the data.
     * 
     * @param streamId the stream id
     * @param folder the folder
     */
    void addData(final int streamId, final String folder);

    /**
     * Update data.
     * 
     * @param streamId the stream id
     * @param timestamp the timestamp
     */
    void updateData(final int streamId, final long timestamp);

    /**
     * Gets the counters.
     * 
     * @return the counters
     */
    Map<Integer, PureMonitorCounter> getCounters();
}
