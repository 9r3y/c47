package com.y3r9.c47.dog.swj.counter;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.com.netis.dp.commons.common.DropReason;
import cn.com.netis.dp.commons.common.PhaseType;
import cn.com.netis.dp.commons.common.packet.PacketDropType;

/**
 * The Class DropReasonCount.
 * 
 * @version 1.0
 * @since 3.0
 */
final class DropCounterReason {

    /**
     * Adds the drop.
     * 
     * @param dropReson the drop reason
     * @param dropCount the drop count
     */
    public final void addDrop(final DropReason dropReson, final int dropCount) {
        tatalCount += dropCount;
        final DropNum dropNum = enumMap.get(dropReson);
        dropNum.addCount(dropCount);
    }

    /**
     * Gets the drop count.
     * 
     * @param dropReson the drop reson
     * @return the drop count
     */
    public final long getDropCount(final DropReason dropReson) {
        return enumMap.get(dropReson).getCount();
    }

    /**
     * Instantiates a new drop reason count.
     * 
     * @param phaseType the phase type
     */
    public DropCounterReason(final PhaseType phaseType) {
        for (PacketDropType dropType : PacketDropType.values()) {
            if (dropType.getPhase() == phaseType) {
                enumMap.put(dropType.getReason(), new DropNum());
            }
        }
    }

    /**
     * Gets the tatal count.
     * 
     * @return the tatal count
     */
    public final long getTatalCount() {
        return tatalCount;
    }

    /** The tatal count. */
    private long tatalCount;

    /** The enum map. */
    private final Map<DropReason, DropNum> enumMap = new LinkedHashMap<DropReason, DropNum>();
}
