package com.y3r9.c47.dog.swj.value;

import cn.com.netis.dp.commons.common.packet.FlowInfo;
import cn.com.netis.dp.commons.common.packet.FlowSide;
import cn.com.netis.dp.commons.protocol.layer.agent.AgentKey;
import cn.com.netis.dp.commons.protocol.layer.mpls.Mpls;

/**
 * The type Flow utils.
 *
 * @version 2.5
 * @since 1.0
 * @since v3.9 updates 2.4
 */
public final class FlowUtils {

    /** The Constant FLOW_ID_DELI. */
    public static final char FLOW_ID_DELI = ':';

    /**
     * Checks if is same side.
     *
     * @param flow1 the flow1
     * @param flow2 the flow2
     * @return true, if is same side
     */
    public static boolean isSameSide(final FlowInfo flow1, final FlowInfo flow2) {
        if (flow1 != null && flow2 != null) {
            return FlowSide.isSameSide(flow1.getFlowSide(), flow2.getFlowSide());
        }
        return false;
    }

    /**
     * Checks if is peer side.
     *
     * @param flow1 the flow1
     * @param flow2 the flow2
     * @return true, if is peer side
     */
    public static boolean isPeerSide(final FlowInfo flow1, final FlowInfo flow2) {
        if (flow1 != null && flow2 != null) {
            return FlowSide.isPeerSide(flow1.getFlowSide(), flow2.getFlowSide());
        }
        return false;
    }

    /**
     * Norm flow id.
     *
     * @param flow the flow
     * @return the string
     */
    public static String normFlowId(final FlowInfo flow) {
        final StringBuilder buf = new StringBuilder().append(flow.getFlowIdComp1())
                .append(FLOW_ID_DELI).append(flow.getFlowIdComp2()).append(FLOW_ID_DELI)
                .append(flow.getFlowIdComp3());
        if (flow.getFlowIdComp4() != null) {
            buf.append(getComp4String(flow.getFlowIdComp4()));
        }
        return buf.toString();
    }

    /**
     * Norm flow id string.
     *
     * @param flow the flow
     * @param agentKey the agent key
     * @return the string
     */
    public static String normFlowId(final FlowInfo flow, final AgentKey agentKey) {
        final StringBuilder builder = new StringBuilder();
        builder.append(flow.getFlowIdComp1()).append(FLOW_ID_DELI).append(flow.getFlowIdComp2())
                .append(FLOW_ID_DELI).append(flow.getFlowIdComp3());
        if (flow.getFlowIdComp4() != null) {
            builder.append(getComp4String(flow.getFlowIdComp4()));
        }
        if (agentKey != null) {
            builder.append(FLOW_ID_DELI).append(agentKey.getFlowIdComponent());
        }
        return builder.toString();
    }

    /**
     * Gets the comp4 string.
     *
     * @param comps the comps
     * @return the comp4 string
     */
    private static StringBuilder getComp4String(final Mpls comps) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < comps.getSize(); i++) {
            buf.append(FLOW_ID_DELI).append(comps.getMpls(i));
        }
        return buf;
    }

    /**
     * Instantiates a new packet.
     */
    private FlowUtils() {
    }
}
