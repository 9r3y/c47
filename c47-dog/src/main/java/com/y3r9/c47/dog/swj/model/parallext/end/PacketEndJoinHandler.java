package com.y3r9.c47.dog.swj.model.parallext.end;

import cn.com.netis.dp.commons.common.packet.Packet;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinHandler;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineEnd;

/**
 * The Interface PacketEndJoinHandler.
 * 
 * @version 1.0
 * @see JoinHandler, PipelineEnd, Packet
 * @since project 3.0
 */
public interface PacketEndJoinHandler extends JoinHandler<Packet>, PipelineEnd<Packet> {
}
