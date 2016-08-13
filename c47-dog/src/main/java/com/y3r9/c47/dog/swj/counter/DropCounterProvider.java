package com.y3r9.c47.dog.swj.counter;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.com.netis.dp.commons.common.DropReason;
import cn.com.netis.dp.commons.common.PhaseType;
import cn.com.netis.dp.commons.common.statis.DropCounter;

/**
 * The Class DropCounterProvider.
 * 
 * @version 1.1
 * @see ByteCounterProvider
 * @see DropCounter
 * @since project 2.2
 */
public class DropCounterProvider implements DropCounter {

    @Override
    public final void addDrop(final PhaseType phaseType, final DropReason reason,
            final int dropCount) {
        enumMap.get(phaseType).addDrop(reason, dropCount);
    }

    @Override
    public final long getDropCount(final PhaseType phaseType) {
        return enumMap.get(phaseType).getTatalCount();
    }

    @Override
    public final long getDropCount(final PhaseType phaseType, final DropReason reason) {
        final DropCounterReason dropReasionCount = enumMap.get(phaseType);
        return dropReasionCount.getDropCount(reason);
    }

    @Override
    public final long getDropCount() {
        long totalCount = 0;
        for (Map.Entry<PhaseType, DropCounterReason> entry : enumMap.entrySet()) {
            totalCount += entry.getValue().getTatalCount();
        }
        return totalCount;
    }

    /**
     * Instantiates a new drop counter provider.
     */
    public DropCounterProvider() {
        for (PhaseType phaseType : PhaseType.values()) {
            final DropCounterReason reasonCount = new DropCounterReason(phaseType);
            enumMap.put(phaseType, reasonCount);
        }
    }

    /** The enum map. */
    private final Map<PhaseType, DropCounterReason> enumMap =
            new LinkedHashMap<PhaseType, DropCounterReason>();
}
