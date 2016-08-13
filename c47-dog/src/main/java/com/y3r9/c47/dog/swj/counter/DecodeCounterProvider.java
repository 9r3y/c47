package com.y3r9.c47.dog.swj.counter;

import java.util.HashMap;
import java.util.Map;

import cn.com.netis.dp.commons.common.DecodeStatis;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The Class DecodeCounterProvider.
 * 
 * @version 1.0
 * @see DecodeCounter
 * @since project 3.3
 */
public final class DecodeCounterProvider implements DecodeCounter {

    @Override
    public void updateCounter(final int streamId, final int decodeId,
            final DecodeStatis data, final long startTime, final long lastTime) {
        data.updateStartTime(startTime);
        data.updateLastTime(lastTime);
        final Pair<Integer, Integer> key = Pair.of(streamId, decodeId);
        if (map.containsKey(key)) {
            final DecodeStatis result = map.get(key);
            result.updateDecodeData(data);
        } else {
            map.put(key, data);
        }
    }

    @Override
    public Map<Pair<Integer, Integer>, DecodeStatis> getDataMap() {
        return map;
    }

    /** The map. */
    private Map<Pair<Integer, Integer>, DecodeStatis> map;

    {
        map = new HashMap<Pair<Integer, Integer>, DecodeStatis>();
    }

}
