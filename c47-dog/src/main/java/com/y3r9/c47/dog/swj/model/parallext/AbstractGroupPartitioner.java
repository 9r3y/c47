package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.packet.Packet;
import cn.com.netis.dp.commons.common.packet.PacketMetaType;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;
import com.y3r9.c47.dog.swj.model.parallel.spi.GroupPartitionParam;
import com.y3r9.c47.dog.swj.model.parallel.spi.Partitioner;

/**
 * The Class AbstractGroupPartitioner.
 * 
 * @param <D> the generic data type
 * @version 1.0
 * @see PipelinePacket, Packet, Partitioner, GroupPartitionParam
 * @since project 3.0
 */
public abstract class AbstractGroupPartitioner<D extends PipelinePacket<Packet>> implements
        Partitioner<D>, GroupPartitionParam {

    @Override
    public final int getPartSelector(final long token, final D data) {
        final int metaType = data.getPacketMetaType();

        if (PacketMetaType.isSyncPacket(metaType)) {
            // special packet should use partId, and for BROADCAST_ID dispatch to part 0
            return data.getPartId() < 0 ? 0 : data.getPartId();
        }

        return getPartSelector(data.getGroupSelector(), data.getFlowHash());
    }

    /**
     * Gets the part selector.
     * 
     * @param groupSelector the group selector
     * @param flowHash the flow hash
     * @return the part selector
     */
    public final int getPartSelector(final int groupSelector, final int flowHash) {
        final int groupIndex = safeGetGroupIndex(groupSelector);
        final int insideGroupIndex = flowHash % waysInsideGroup;
        return groupIndex * waysInsideGroup + insideGroupIndex;
    }

    /**
     * Safe get group index.
     * 
     * @param groupSelector the group selector
     * @return the int
     */
    protected abstract int safeGetGroupIndex(int groupSelector);

    /**
     * Gets the group selector.
     * 
     * @param groupIndex the group index
     * @return the group selector
     */
    public abstract int getGroupSelector(int groupIndex);

    /**
     * Gets the group selector from partition.
     * 
     * @param partId the part id
     * @return the group selector from partition
     */
    public final int getGroupSelectorFromPartition(final int partId) {
        final int groupIndex = partId / waysInsideGroup;
        return getGroupSelector(groupIndex);
    }

    /**
     * Gets the inside group index from partition.
     * 
     * @param partId the part id
     * @return the inside group index from partition
     */
    public final int getInsideGroupIndexFromPartition(final int partId) {
        return partId % waysInsideGroup;
    }

    @Override
    public final int getWaysInsideGroup() {
        return waysInsideGroup;
    }

    /**
     * Sets the ways inside group.
     * 
     * @param value the new ways inside group
     */
    public final void setWaysInsideGroup(final int value) {
        NonPositiveArgumentException.check(value, "waysInsideGroup");
        waysInsideGroup = value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("waysInsideGroup=").append(waysInsideGroup);
        return builder.toString();
    }

    /** The ways inside group. */
    private int waysInsideGroup;
}
