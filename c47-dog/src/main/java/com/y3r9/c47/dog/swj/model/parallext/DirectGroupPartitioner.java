package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.packet.Packet;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;
import cn.com.netis.dp.commons.lang.OutOfRangeArgumentException;

/**
 * The Class DirectGroupPartitioner.
 * 
 * @param <D> the generic data type
 * @version 1.0
 * @see AbstractGroupPartitioner, PipelinePacket, Packet
 * @since project 3.0
 */
public final class DirectGroupPartitioner<D extends PipelinePacket<Packet>> extends
        AbstractGroupPartitioner<D> {

    @Override
    public int getGroupCount() {
        return groupCount;
    }

    @Override
    public int safeGetGroupIndex(final int groupSelector) {
        // no check
        return groupSelector;
    }

    @Override
    public int getGroupSelector(final int groupIndex) {
        OutOfRangeArgumentException.check(groupIndex, groupCount, "groupIndex");
        return groupIndex;
    }

    /**
     * Sets the group count.
     * 
     * @param value the new group count
     */
    public void setGroupCount(final int value) {
        NonPositiveArgumentException.check(value, "groupCount");
        groupCount = value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DirectGroupPartitioner [").append(super.toString()).append(", groupCount=")
                .append(groupCount).append("]");
        return builder.toString();
    }

    /** The group count. */
    private int groupCount;
}
