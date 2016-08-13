package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.packet.Packet;
import cn.com.netis.dp.commons.common.packet.PacketDropInfo;

/**
 * The Interface PipelinePacket.
 * 
 * @param <P> the generic type
 * @version 1.0
 * @see Spanable, PacketDropInfo
 * @since project 3.0
 */
public interface PipelinePacket<P extends Packet> extends Spanable<P>, PacketDropInfo {

    /**
     * Gets the packet meta type.
     * 
     * @return the packet meta type
     */
    int getPacketMetaType();

    /**
     * Gets the flow hash.
     * 
     * @return the flow hash
     */
    int getFlowHash();

    /**
     * Checks if is data empty.
     * 
     * @return true, if is data empty
     */
    boolean isDataEmpty();

    /**
     * Gets the group selector.
     * 
     * @return the group selector
     */
    int getGroupSelector();
}
