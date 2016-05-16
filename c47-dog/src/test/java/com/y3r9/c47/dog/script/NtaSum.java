package com.y3r9.c47.dog.script;

import java.nio.file.Path;
import java.text.SimpleDateFormat;

/**
 * The class Sum.
 *
 * @version 1.0
 */
final class NtaSum {

    private Path file;

    private long tsMin = Long.MAX_VALUE;

    private long tsMax = Long.MIN_VALUE;

    private long pktCntSum;

    private long pktLenSum;

    private long msgCntSum;

    private long msgCntSumServer;

    private long msgCntSumClient;

    private long procTimeSum;

    private long procTimeServerSum;

    private long procTimeClientSum;

    private long respTimeSum;

    private long transTimeSum;

    private long transTimeSumServer;

    private long transTimeSumClient;

    private long networkDelaySum;

    private long networkDelaySumServer;

    private long networkDelaySumClient;

    public long getPktCntSum() {
        return pktCntSum;
    }

    public void setPktCntSum(final long value) {
        pktCntSum = value;
    }

    public long getPktLenSum() {
        return pktLenSum;
    }

    public void setPktLenSum(final long value) {
        pktLenSum = value;
    }

    public long getMsgCntSum() {
        return msgCntSum;
    }

    public void setMsgCntSum(final long value) {
        msgCntSum = value;
    }

    public long getMsgCntSumServer() {
        return msgCntSumServer;
    }

    public void setMsgCntSumServer(final long value) {
        msgCntSumServer = value;
    }

    public long getMsgCntSumClient() {
        return msgCntSumClient;
    }

    public void setMsgCntSumClient(final long value) {
        msgCntSumClient = value;
    }

    public long getProcTimeSum() {
        return procTimeSum;
    }

    public void setProcTimeSum(final long value) {
        procTimeSum = value;
    }

    public long getProcTimeServerSum() {
        return procTimeServerSum;
    }

    public void setProcTimeServerSum(final long value) {
        procTimeServerSum = value;
    }

    public long getProcTimeClientSum() {
        return procTimeClientSum;
    }

    public void setProcTimeClientSum(final long value) {
        procTimeClientSum = value;
    }

    public long getRespTimeSum() {
        return respTimeSum;
    }

    public void setRespTimeSum(final long value) {
        respTimeSum = value;
    }

    public long getTransTimeSum() {
        return transTimeSum;
    }

    public void setTransTimeSum(final long value) {
        transTimeSum = value;
    }

    public long getTransTimeSumServer() {
        return transTimeSumServer;
    }

    public void setTransTimeSumServer(final long value) {
        transTimeSumServer = value;
    }

    public long getTransTimeSumClient() {
        return transTimeSumClient;
    }

    public void setTransTimeSumClient(final long value) {
        transTimeSumClient = value;
    }

    public long getNetworkDelaySum() {
        return networkDelaySum;
    }

    public void setNetworkDelaySum(final long value) {
        networkDelaySum = value;
    }

    public long getNetworkDelaySumServer() {
        return networkDelaySumServer;
    }

    public void setNetworkDelaySumServer(final long value) {
        networkDelaySumServer = value;
    }

    public long getNetworkDelaySumClient() {
        return networkDelaySumClient;
    }

    public void setNetworkDelaySumClient(final long value) {
        networkDelaySumClient = value;
    }

    public void setTs(final long value) {
        if (value < tsMin) {
            tsMin = value;
        }
        if (value > tsMax) {
            tsMax = value;
        }
    }

    public NtaSum(final Path file) {
        this.file = file;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(file);
        sb.append("\n");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("\tStartTime: ").append(sdf.format(tsMin)).append(" EndTime: ").append(sdf.format(tsMax));
        sb.append("\n");

        sb.append("\tNtaSum:");
        sb.append("\n\tpktCntSum=").append(pktCntSum);
        sb.append("\n\tpktLenSum=").append(pktLenSum);
        sb.append("\n\tmsgCntSum=").append(msgCntSum);
        sb.append("\n\tmsgCntServerSum=").append(msgCntSumServer);
        sb.append("\n\tmsgCntClientSum=").append(msgCntSumClient);
        sb.append("\n\tprocTimeSum=").append(procTimeSum);
        sb.append("\n\tprocTimeServerSum=").append(procTimeServerSum);
        sb.append("\n\tprocTimeClientSum=").append(procTimeClientSum);
        sb.append("\n\trespTimeSum=").append(respTimeSum);
        sb.append("\n\ttransTimeSum=").append(transTimeSum);
        sb.append("\n\ttransTimeServerSum=").append(transTimeSumServer);
        sb.append("\n\ttransTimeClientSum=").append(transTimeSumClient);
        sb.append("\n\tnetworkDelaySum=").append(networkDelaySum);
        sb.append("\n\tnetworkDelayServerSum=").append(networkDelaySumServer);
        sb.append("\n\tnetworkDelayClientSum=").append(networkDelaySumClient);
        return sb.toString();
    }
}
