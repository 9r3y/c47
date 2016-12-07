package com.y3r9.c47.dog.script;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The class NtaRecord.
 *
 * @version 1.0
 */
final class NtrRecord {

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

    /** The Path. */
    Path path;

    /** The Line. */
    int line;

    /** The Ip id. */
    int ipId;

    /** The Pkt len. */
    long pktLen;

    /** The Pkt cnt. */
    long pktCnt;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NtrRecord ntaRecord = (NtrRecord) o;
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
     * Gets pkt len.
     *
     * @return the pkt len
     */
    public long getPktLen() {
        return pktLen;
    }

    /**
     * Gets pkt cnt.
     *
     * @return the pkt cnt
     */
    public long getPktCnt() {
        return pktCnt;
    }

    /**
     * Sets pkt cnt.
     *
     * @param value the value
     */
    public void setPktCnt(final long value) {
        pktCnt = value;
    }

    /**
     * Sets pkt len.
     *
     * @param value the value
     */
    public void setPktLen(final long value) {
        pktLen = value;
    }

    /**
     * Gets ip id.
     *
     * @return the ip id
     */
    public int getIpId() {
        return ipId;
    }

    /**
     * Sets ip id.
     *
     * @param value the value
     */
    public void setIpId(final int value) {
        ipId = value;
    }
}
