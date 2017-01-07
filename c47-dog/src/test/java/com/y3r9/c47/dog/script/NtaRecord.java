package com.y3r9.c47.dog.script;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The class NtaRecord.
 *
 * @version 1.0
 */
final class NtaRecord {

    /** The Src ip. */
    int srcIp;

    /** The Dest ip. */
    int destIp;

    /** The Src port. */
    int srcPort;

    /** The Dest port. */
    int destPort;

    /** The Flow side. */
    int flowSide;

    /** The Conn count. */
    int connCount;

    /** The Path. */
    Path path;

    /** The Line. */
    int line;

    /** The Ts min. */
    private long tsMin = Long.MAX_VALUE;

    /** The Ts max. */
    private long tsMax = Long.MIN_VALUE;

    /** The Pkt cnt sum. */
    private long pktCntSum;

    /** The Pkt len sum. */
    private long pktLenSum;

    /** The Msg cnt sum. */
    private long msgCntSum;

    /** The Msg cnt sum server. */
    private long msgCntSumServer;

    /** The Msg cnt sum client. */
    private long msgCntSumClient;

    /** The Proc time sum. */
    private long procTimeSum;

    /** The Proc time server sum. */
    private long procTimeServerSum;

    /** The Proc time client sum. */
    private long procTimeClientSum;

    /** The Resp time sum. */
    private long respTimeSum;

    /** The Trans time sum. */
    private long transTimeSum;

    /** The Trans time sum server. */
    private long transTimeSumServer;

    /** The Trans time sum client. */
    private long transTimeSumClient;

    /** The Network delay sum. */
    private long networkDelaySum;

    /** The Network delay sum server. */
    private long networkDelaySumServer;

    /** The Network delay sum client. */
    private long networkDelaySumClient;

    /** The Attach success sum. */
    private long attachSuccessSum;

    /** The Attach fail sum. */
    private long attachFailSum;

    /** The Flow dir. */
    private int flowDir;

    /**
     * Merge.
     *
     * @param nta the nta
     */
    public void merge(final NtaRecord nta) {
        this.connCount += nta.connCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NtaRecord ntaRecord = (NtaRecord) o;
        return srcIp == ntaRecord.srcIp &&
                destIp == ntaRecord.destIp &&
                srcPort == ntaRecord.srcPort &&
                destPort == ntaRecord.destPort &&
                flowSide == ntaRecord.flowSide;
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcIp, destIp, srcPort, destPort, flowSide);
    }

    /**
     * Gets src ip.
     *
     * @return the src ip
     */
    public int getSrcIp() {
        return srcIp;
    }

    /**
     * Sets src ip.
     *
     * @param value the value
     */
    public void setSrcIp(final int value) {
        srcIp = value;
    }

    /**
     * Gets dest ip.
     *
     * @return the dest ip
     */
    public int getDestIp() {
        return destIp;
    }

    /**
     * Sets dest ip.
     *
     * @param value the value
     */
    public void setDestIp(final int value) {
        destIp = value;
    }

    /**
     * Gets src port.
     *
     * @return the src port
     */
    public int getSrcPort() {
        return srcPort;
    }

    /**
     * Sets src port.
     *
     * @param value the value
     */
    public void setSrcPort(final int value) {
        srcPort = value;
    }

    /**
     * Gets dest port.
     *
     * @return the dest port
     */
    public int getDestPort() {
        return destPort;
    }

    /**
     * Sets dest port.
     *
     * @param value the value
     */
    public void setDestPort(final int value) {
        destPort = value;
    }

    /**
     * Gets flow side.
     *
     * @return the flow side
     */
    public int getFlowSide() {
        return flowSide;
    }

    /**
     * Sets flow side.
     *
     * @param value the value
     */
    public void setFlowSide(final int value) {
        flowSide = value;
    }

    /**
     * Gets conn count.
     *
     * @return the conn count
     */
    public int getConnCount() {
        return connCount;
    }

    /**
     * Sets conn count.
     *
     * @param value the value
     */
    public void setConnCount(final int value) {
        connCount = value;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public Path getPath() {
        return path;
    }

    /**
     * Sets path.
     *
     * @param value the value
     */
    public void setPath(final Path value) {
        path = value;
    }

    /**
     * Gets line.
     *
     * @return the line
     */
    public int getLine() {
        return line;
    }

    /**
     * Sets line.
     *
     * @param value the value
     */
    public void setLine(final int value) {
        line = value;
    }

    /**
     * Gets ts min.
     *
     * @return the ts min
     */
    public long getTsMin() {
        return tsMin;
    }

    /**
     * Sets ts min.
     *
     * @param value the value
     */
    public void setTsMin(final long value) {
        tsMin = value;
    }

    /**
     * Gets ts max.
     *
     * @return the ts max
     */
    public long getTsMax() {
        return tsMax;
    }

    /**
     * Sets ts max.
     *
     * @param value the value
     */
    public void setTsMax(final long value) {
        tsMax = value;
    }

    /**
     * Gets pkt cnt sum.
     *
     * @return the pkt cnt sum
     */
    public long getPktCntSum() {
        return pktCntSum;
    }

    /**
     * Sets pkt cnt sum.
     *
     * @param value the value
     */
    public void setPktCntSum(final long value) {
        pktCntSum = value;
    }

    /**
     * Gets pkt len sum.
     *
     * @return the pkt len sum
     */
    public long getPktLenSum() {
        return pktLenSum;
    }

    /**
     * Sets pkt len sum.
     *
     * @param value the value
     */
    public void setPktLenSum(final long value) {
        pktLenSum = value;
    }

    /**
     * Gets msg cnt sum.
     *
     * @return the msg cnt sum
     */
    public long getMsgCntSum() {
        return msgCntSum;
    }

    /**
     * Sets msg cnt sum.
     *
     * @param value the value
     */
    public void setMsgCntSum(final long value) {
        msgCntSum = value;
    }

    /**
     * Gets msg cnt sum server.
     *
     * @return the msg cnt sum server
     */
    public long getMsgCntSumServer() {
        return msgCntSumServer;
    }

    /**
     * Sets msg cnt sum server.
     *
     * @param value the value
     */
    public void setMsgCntSumServer(final long value) {
        msgCntSumServer = value;
    }

    /**
     * Gets msg cnt sum client.
     *
     * @return the msg cnt sum client
     */
    public long getMsgCntSumClient() {
        return msgCntSumClient;
    }

    /**
     * Sets msg cnt sum client.
     *
     * @param value the value
     */
    public void setMsgCntSumClient(final long value) {
        msgCntSumClient = value;
    }

    /**
     * Gets proc time sum.
     *
     * @return the proc time sum
     */
    public long getProcTimeSum() {
        return procTimeSum;
    }

    /**
     * Sets proc time sum.
     *
     * @param value the value
     */
    public void setProcTimeSum(final long value) {
        procTimeSum = value;
    }

    /**
     * Gets proc time server sum.
     *
     * @return the proc time server sum
     */
    public long getProcTimeServerSum() {
        return procTimeServerSum;
    }

    /**
     * Sets proc time server sum.
     *
     * @param value the value
     */
    public void setProcTimeServerSum(final long value) {
        procTimeServerSum = value;
    }

    /**
     * Gets proc time client sum.
     *
     * @return the proc time client sum
     */
    public long getProcTimeClientSum() {
        return procTimeClientSum;
    }

    /**
     * Sets proc time client sum.
     *
     * @param value the value
     */
    public void setProcTimeClientSum(final long value) {
        procTimeClientSum = value;
    }

    /**
     * Gets resp time sum.
     *
     * @return the resp time sum
     */
    public long getRespTimeSum() {
        return respTimeSum;
    }

    /**
     * Sets resp time sum.
     *
     * @param value the value
     */
    public void setRespTimeSum(final long value) {
        respTimeSum = value;
    }

    /**
     * Gets trans time sum.
     *
     * @return the trans time sum
     */
    public long getTransTimeSum() {
        return transTimeSum;
    }

    /**
     * Sets trans time sum.
     *
     * @param value the value
     */
    public void setTransTimeSum(final long value) {
        transTimeSum = value;
    }

    /**
     * Gets trans time sum server.
     *
     * @return the trans time sum server
     */
    public long getTransTimeSumServer() {
        return transTimeSumServer;
    }

    /**
     * Sets trans time sum server.
     *
     * @param value the value
     */
    public void setTransTimeSumServer(final long value) {
        transTimeSumServer = value;
    }

    /**
     * Gets trans time sum client.
     *
     * @return the trans time sum client
     */
    public long getTransTimeSumClient() {
        return transTimeSumClient;
    }

    /**
     * Sets trans time sum client.
     *
     * @param value the value
     */
    public void setTransTimeSumClient(final long value) {
        transTimeSumClient = value;
    }

    /**
     * Gets network delay sum.
     *
     * @return the network delay sum
     */
    public long getNetworkDelaySum() {
        return networkDelaySum;
    }

    /**
     * Sets network delay sum.
     *
     * @param value the value
     */
    public void setNetworkDelaySum(final long value) {
        networkDelaySum = value;
    }

    /**
     * Gets network delay sum server.
     *
     * @return the network delay sum server
     */
    public long getNetworkDelaySumServer() {
        return networkDelaySumServer;
    }

    /**
     * Sets network delay sum server.
     *
     * @param value the value
     */
    public void setNetworkDelaySumServer(final long value) {
        networkDelaySumServer = value;
    }

    /**
     * Gets network delay sum client.
     *
     * @return the network delay sum client
     */
    public long getNetworkDelaySumClient() {
        return networkDelaySumClient;
    }

    /**
     * Sets network delay sum client.
     *
     * @param value the value
     */
    public void setNetworkDelaySumClient(final long value) {
        networkDelaySumClient = value;
    }

    /**
     * Gets attach success sum.
     *
     * @return the attach success sum
     */
    public long getAttachSuccessSum() {
        return attachSuccessSum;
    }

    /**
     * Sets attach success sum.
     *
     * @param value the value
     */
    public void setAttachSuccessSum(final long value) {
        attachSuccessSum = value;
    }

    /**
     * Gets attach fail sum.
     *
     * @return the attach fail sum
     */
    public long getAttachFailSum() {
        return attachFailSum;
    }

    /**
     * Sets attach fail sum.
     *
     * @param value the value
     */
    public void setAttachFailSum(final long value) {
        attachFailSum = value;
    }

    /**
     * Gets flow dir.
     *
     * @return the flow dir
     */
    public int getFlowDir() {
        return flowDir;
    }

    /**
     * Sets flow dir.
     *
     * @param value the value
     */
    public void setFlowDir(final int value) {
        flowDir = value;
    }
}
