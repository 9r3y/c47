package com.y3r9.c47.dog.swj.counter;

import cn.com.netis.dp.commons.common.DecodeStatis;
import cn.com.netis.dp.commons.common.statis.DecodeObservable;

/**
 * The Interface DecodeCounter.
 * 
 * @version 1.0
 * @see DecodeDataObservable
 * @since project 3.3
 */
public interface DecodeCounter extends DecodeObservable {

    /**
     * Update counter.
     *
     * @param streamId the stream id
     * @param decodeId the decode id
     * @param data the data
     * @param startTime the start time
     * @param lastTime the last time
     */
    void updateCounter(int streamId, int decodeId, DecodeStatis data, long startTime,
            long lastTime);

}
