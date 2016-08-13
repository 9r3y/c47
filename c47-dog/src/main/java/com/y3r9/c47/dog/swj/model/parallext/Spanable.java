package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.packet.Packet;

/**
 * The Interface Spanable.
 * 
 * @param <P> the generic type
 * @version 1.0
 * @since project 3.0
 */
public interface Spanable<P extends Packet> {

    /**
     * Gets the packet.
     * 
     * @return the packet
     */
    P getPacket();

    /**
     * Gets the part id.
     * 
     * @return the part id
     */
    int getPartId();
}
