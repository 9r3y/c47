package com.y3r9.c47.dog;

import cn.com.netis.dp.commons.common.packet.FlowSide;

/**
 * The class Connection.
 *
 * @version 1.0
 */
final class Connection {
    /** The Ip pair. */
    private long ipPair;

    /** The Port pair. */
    private int portPair;

    /**
     * Instantiates a new Connection.
     *
     * @param srcIp the src ip
     * @param destIp the dest ip
     * @param srcPort the src port
     * @param destPort the dest port
     * @param flowSide the flow side
     */
    public Connection(final int srcIp, final int destIp, final int srcPort,
            final int destPort, final int flowSide) {
        if (FlowSide.LITTLE == flowSide) {
            ipPair = (long) srcIp << 32 | destIp;
            portPair = srcPort << 16 | destPort;
        } else {
            ipPair = (long) destIp << 32 | srcIp;
            portPair = destPort << 16 | srcPort;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return ipPair == that.ipPair &&
                portPair == that.portPair;
    }

    @Override
    public int hashCode() {
        return (int) ipPair * 31 + portPair;
    }
}
