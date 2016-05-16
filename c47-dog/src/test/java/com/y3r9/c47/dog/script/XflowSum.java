package com.y3r9.c47.dog.script;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class XflowSum.
 *
 * @version 1.0
 */
final class XflowSum {

    private Path file;

    private long packetDeltaCount;

    private long octetDeltaCount;

    private long transactionCountDelta;

    private long sumTotalRespTime;

    private long sumServerRespTime;

    private long sumTransactionTime;

    private long sumServerNwkTime;

    private long sumClientNwkTime;

    private long monitoringIntervalStartMilliSecondsMin = Long.MAX_VALUE;

    private long monitoringIntervalStartMilliSecondsMax = Long.MIN_VALUE;

    private long expTimeMin = Long.MAX_VALUE;

    private long expTimeMax = Long.MIN_VALUE;

    public XflowSum(final Path file) {
        this.file = file;
    }

    public long getPacketDeltaCount() {
        return packetDeltaCount;
    }

    public void setPacketDeltaCount(final long value) {
        packetDeltaCount = value;
    }

    public long getOctetDeltaCount() {
        return octetDeltaCount;
    }

    public void setOctetDeltaCount(final long value) {
        octetDeltaCount = value;
    }

    public long getTransactionCountDelta() {
        return transactionCountDelta;
    }

    public void setTransactionCountDelta(final long value) {
        transactionCountDelta = value;
    }

    public long getSumTotalRespTime() {
        return sumTotalRespTime;
    }

    public void setSumTotalRespTime(final long value) {
        sumTotalRespTime = value;
    }

    public long getSumServerRespTime() {
        return sumServerRespTime;
    }

    public void setSumServerRespTime(final long value) {
        sumServerRespTime = value;
    }

    public long getSumTransactionTime() {
        return sumTransactionTime;
    }

    public void setSumTransactionTime(final long value) {
        sumTransactionTime = value;
    }

    public long getSumServerNwkTime() {
        return sumServerNwkTime;
    }

    public void setSumServerNwkTime(final long value) {
        sumServerNwkTime = value;
    }

    public long getSumClientNwkTime() {
        return sumClientNwkTime;
    }

    public void setSumClientNwkTime(final long value) {
        sumClientNwkTime = value;
    }

    public void setMonitoringIntervalStartMilliSeconds(final long value) {
        if (monitoringIntervalStartMilliSecondsMax < value) {
            monitoringIntervalStartMilliSecondsMax = value;
        }
        if (monitoringIntervalStartMilliSecondsMin > value) {
            monitoringIntervalStartMilliSecondsMin = value;
        }
    }

    public void setExpTime(final long value) {
        if (expTimeMax < value) {
            expTimeMax = value;
        }
        if (expTimeMin > value) {
            expTimeMin= value;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(file);
        sb.append("\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        sb.append(String.format("\tmonitoringIntervalStartMilliSeconds start: %s end: %s",
                sdf.format(new Date(monitoringIntervalStartMilliSecondsMin)), sdf.format(new Date(monitoringIntervalStartMilliSecondsMax))));
        sb.append("\n");
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sb.append(String.format("\tExpTime start: %s end: %s",
                sdf.format(new Date(expTimeMin)), sdf.format(new Date(expTimeMax))));
        sb.append("\n");

        sb.append("\tXflowSum{");
        sb.append("packetDeltaCount=").append(packetDeltaCount);
        sb.append(", octetDeltaCount=").append(octetDeltaCount);
        sb.append(", transactionCountDelta=").append(transactionCountDelta);
        sb.append(", sumTotalRespTime=").append(sumTotalRespTime);
        sb.append(", sumServerRespTime=").append(sumServerRespTime);
        sb.append(", sumTransactionTime=").append(sumTransactionTime);
        sb.append(", sumServerNwkTime=").append(sumServerNwkTime);
        sb.append(", sumClientNwkTime=").append(sumClientNwkTime);
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }


}
