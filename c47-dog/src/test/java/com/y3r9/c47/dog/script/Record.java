package com.y3r9.c47.dog.script;

import java.util.Objects;

/**
 * The class Record.
 *
 * @version 1.0
 */
final class Record {

    private String srcIp;

    private String destIp;

    private String flowSide;

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

    public String getFlowSide() {
        return flowSide;
    }

    public void setFlowSide(final String value) {
        flowSide = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(srcIp, record.srcIp) &&
                Objects.equals(destIp, record.destIp) &&
                Objects.equals(flowSide, record.flowSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcIp, destIp, flowSide);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Record{");
        sb.append("srcIp='").append(srcIp).append('\'');
        sb.append(", destIp='").append(destIp).append('\'');
        sb.append(", flowSide='").append(flowSide).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
