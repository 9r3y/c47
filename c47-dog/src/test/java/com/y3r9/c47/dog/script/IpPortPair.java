package com.y3r9.c47.dog.script;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The class IpPortPair.
 *
 * @version 1.0
 */
final class IpPortPair {

    String srcIp;

    String destIp;

    String srcPort;

    String destPort;

    Path path;

    int line;

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(final String value) {
        srcIp = value;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(final String value) {
        destIp = value;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(final String value) {
        srcPort = value;
    }

    public String getDestPort() {
        return destPort;
    }

    public void setDestPort(final String value) {
        destPort = value;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(final Path value) {
        path = value;
    }

    public int getLine() {
        return line;
    }

    public void setLine(final int value) {
        line = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpPortPair that = (IpPortPair) o;
        return Objects.equals(srcIp, that.srcIp) &&
                Objects.equals(destIp, that.destIp) &&
                Objects.equals(srcPort, that.srcPort) &&
                Objects.equals(destPort, that.destPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcIp, destIp, srcPort, destPort);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IpPortPair{");
        sb.append("srcIp='").append(srcIp).append('\'');
        sb.append(", destIp='").append(destIp).append('\'');
        sb.append(", srcPort='").append(srcPort).append('\'');
        sb.append(", destPort='").append(destPort).append('\'');
        sb.append(", path=").append(path);
        sb.append(", line=").append(line);
        sb.append('}');
        return sb.toString();
    }
}
