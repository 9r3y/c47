package com.y3r9.c47.dog.swj.model.parallext.end;

import cn.com.netis.dp.commons.common.packet.Packet;
import cn.com.netis.dp.commons.common.statis.CacheObservable;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineEnd;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineInput;

/**
 * The Interface PipelineEndPair.
 * 
 * @param <Data> the generic data type
 * @version 1.0
 * @see CacheObservable
 * @since project 3.0
 */
public interface PipelineEndPair<Data> extends CacheObservable {

    /**
     * Gets the end left.
     * 
     * @return the end left
     */
    PipelineInput<Data> getEndLeft();

    /**
     * Gets the end right.
     * 
     * @return the end right
     */
    PipelineEnd<Packet> getEndRight();
}
