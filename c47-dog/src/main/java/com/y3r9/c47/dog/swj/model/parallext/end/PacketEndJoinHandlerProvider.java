package com.y3r9.c47.dog.swj.model.parallext.end;

import cn.com.netis.dp.commons.common.packet.Packet;
import com.y3r9.c47.dog.swj.model.parallext.AbstractJoinHandler;

/**
 * The Class PacketEndJoinHandlerProvider.
 * 
 * @version 1.0
 * @see AbstractJoinHandler, Packet, PacketEndJoinHandler
 * @since project 3.0
 */
public final class PacketEndJoinHandlerProvider extends AbstractJoinHandler<Packet, Packet>
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

        // update for meta type
        updateInbandTerminate(resultData.getMetaType());
    }

    @Override
    public Packet getLastValue() {
        return lastValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PacketEndJoinHandlerProvider [").append(super.toString())
                .append(", lastValue=").append(lastValue).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new packet end join handler provider.
     * 
     * @param name the name
     */
    public PacketEndJoinHandlerProvider(final String name) {
        super(name);
    }

    /** The value. */
    private transient Packet lastValue;
}
