package com.y3r9.c47.dog.swj.model.parallext;

import java.util.HashMap;
import java.util.Map;

import cn.com.netis.dp.commons.common.packet.Packet;

/**
 * The Class MappingGroupPartitioner.
 * 
 * @param <D> the generic data type
 * @version 1.0
 * @see AbstractGroupPartitioner, PipelinePacket, Packet
 * @since project 3.0
 */
public final class MappingGroupPartitioner<D extends PipelinePacket<Packet>> extends
        AbstractGroupPartitioner<D> {

    @Override
    public int getGroupCount() {
        return groupSelectorToIndex.size();
    }

    @Override
    public int safeGetGroupIndex(final int groupSelector) {
        final Integer result = groupSelectorToIndex.get(groupSelector);
        return result == null ? 0 : result;
    }

    @Override
    public int getGroupSelector(final int groupIndex) {
        final Integer result = groupIndexToSelector.get(groupIndex);
        if (result == null) {
            throw new IllegalArgumentException(new StringBuilder().append("groupIndex=")
                    .append(groupIndex).append(" is invalid.").toString());
        }
        return result;
    }

    /**
     * Put selector.
     * 
     * @param selector the selector
     */
    public void putSelector(final int selector) {
        if (!groupSelectorToIndex.containsKey(selector)) {
            final int index = getGroupCount();
            groupSelectorToIndex.put(selector, index);
            groupIndexToSelector.put(index, selector);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MappingGroupPartitioner [").append(super.toString())
                .append(", groupSelectorToIndex=").append(groupSelectorToIndex)
                .append(", groupIndexToSelector=").append(groupIndexToSelector).append("]");
        return builder.toString();
    }

    /** The group selector to index. */
    private final Map<Integer, Integer> groupSelectorToIndex;

    /** The group index to selector. */
    private final Map<Integer, Integer> groupIndexToSelector;

    {
        groupSelectorToIndex = new HashMap<>();
        groupIndexToSelector = new HashMap<>();
    }
}
