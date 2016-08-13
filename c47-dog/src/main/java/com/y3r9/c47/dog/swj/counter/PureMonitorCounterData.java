package com.y3r9.c47.dog.swj.counter;

/**
 * The Class PureMonitorCounterData.
 * 
 * @version 1.2
 * @since project 2.10
 */
public final class PureMonitorCounterData implements PureMonitorCounter {

    /** The Constant UNINITIALIZED_TIME. */
    private static final int UNINITIALIZED_TIME = 0;

    @Override
    public String getLastFileName() {
        return lastFileName;
    }

    @Override
    public long getRemainingFileCount() {
        return remainingFileCount;
    }

    @Override
    public long getBytes() {
        return bytes;
    }

    @Override
    public long getFiles() {
        return files;
    }

    @Override
    public String getFolder() {
        return folder;
    }

    /**
     * Sets the bytes.
     * 
     * @param value the new bytes
     */
    @Override
    public void setBytes(final long value) {
        bytes = value;
    }

    /**
     * Sets the files.
     * 
     * @param value the new files
     */
    @Override
    public void setFiles(final long value) {
        files = value;
    }

    /**
     * Sets the folder.
     * 
     * @param value the new folder
     */
    @Override
    public void setFolder(final String value) {
        folder = value;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(final long value) {
        startTime = value;
    }

    @Override
    public long getLastTime() {
        return lastTime;
    }

    @Override
    public void setLastTime(final long value) {
        lastTime = value;
    }

    /**
     * Update data.
     * 
     * @param incFileCount the increase file count
     * @param byteValue the byte value
     * @param lastFile the last file
     * @param fileCount the file count
     */
    @Override
    public void updateData(final long incFileCount, final long byteValue,
            final String lastFile, final long fileCount) {
        files += incFileCount;
        bytes += byteValue;
        remainingFileCount = fileCount;
        lastFileName = lastFile;
    }

    @Override
    public void updateData(final long timestamp) {
        if (startTime == UNINITIALIZED_TIME) {
            startTime = timestamp;
        }
        lastTime = timestamp;
    }

    @Override
    public void reset() {
        startTime = UNINITIALIZED_TIME;
        remainingFileCount = 0;
    }

    /** The bytes. */
    private transient long bytes;

    /** The files. */
    private transient long files;

    /** The file count. */
    private transient long remainingFileCount;

    /** The folder. */
    private transient String folder;

    /** The last file. */
    private transient String lastFileName;

    /** The start time. */
    private transient long startTime;

    /** The last time. */
    private transient long lastTime;

}
