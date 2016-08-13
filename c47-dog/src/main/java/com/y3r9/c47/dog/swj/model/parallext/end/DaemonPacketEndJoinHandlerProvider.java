package com.y3r9.c47.dog.swj.model.parallext.end;

import cn.com.netis.dp.commons.common.packet.Packet;
import com.y3r9.c47.dog.swj.model.parallext.AbstractJoinHandler;

/**
 * The Class DaemonPacketEndJoinHandlerProvider.
 * 
 * @version 1.0
 * @see AbstractJoinHandler, PacketEndJoinHandler
 * @since project 3.0
 */
public final class DaemonPacketEndJoinHandlerProvider extends AbstractJoinHandler<Packet, Packet>
        implements PacketEndJoinHandler {

    @Override
    public void consumeJoinResult(final Packet resultData) {
        // if result data is null, return directly
        if (resultData == null) {
            return;
        }
        // skip to drop packet
        if (resultData.isToDrop()) {
            return;
        }
        // save last value
        lastValue = resultData;
    }

    @Override
    public Packet getLastValue() {
        return lastValue;
    }

    /**
     * Instantiates a new daemon packet end join handler provider.
     * 
     * @param name the name
     */
    public DaemonPacketEndJoinHandlerProvider(final String name) {
        super(name);
    }

    /** The value. */
    private transient Packet lastValue;

}
