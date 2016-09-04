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
}
