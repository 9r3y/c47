package com.y3r9.c47.dog.swj.counter;

/**
 * The Interface PureMonitorCounter.
 * 
 * @version 1.1
 * @since project 2.10
 */
public interface PureMonitorCounter {

    /**
     * Gets the last file name.
     * 
     * @return the last file name
     * @since 1.1
     */
    String getLastFileName();

    /**
     * Gets the remaining file count.
     * 
     * @return the file count
     * @since 1.1
     */
    long getRemainingFileCount();

    /**
     * Gets the file count.
     * 
     * @return the file count
     */
    long getFiles();

    /**
     * Gets the monitor folder.
     * 
     * @return the monitor folder
     */
    String getFolder();

    /**
     * Gets the bytes.
     * 
     * @return the bytes
     */
    long getBytes();

    /**
     * Sets the bytes.
     * 
     * @param value the new bytes
     */
    void setBytes(long value);

    /**
     * Sets the files.
     * 
     * @param value the new files
     */
    void setFiles(long value);

    /**
     * Sets the folder.
     * 
     * @param value the new folder
     */
    void setFolder(String value);

    /**
     * Gets the start time.
     * 
     * @return the start time
     */
    long getStartTime();

    /**
     * Sets the start time.
     * 
     * @param startTime the new start time
     */
    void setStartTime(long startTime);

    /**
     * Gets the last time.
     * 
     * @return the last time
     */
    long getLastTime();

    /**
     * Sets the last time.
     * 
     * @param lastTime the new last time
     */
    void setLastTime(long lastTime);

    /**
     * Update data.
     * 
     * @param incFileCount the increase file count
     * @param byteValue the byte value
     * @param lastFileName the last file name
     * @param fileCount the file count
     * @since 1.1
     */
    void updateData(long incFileCount, long byteValue, String lastFileName, long fileCount);

    /**
     * Update data.
     * 
     * @param timestamp the timestamp
     */
    void updateData(long timestamp);

    /**
     * Reset.
     */
    void reset();
}
