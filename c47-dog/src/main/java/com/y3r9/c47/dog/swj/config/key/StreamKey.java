package com.y3r9.c47.dog.swj.config.key;

/**
 * The Enum StreamKey.
 *
 * @version 1.3
 * @since v3.0
 * @since v3.10 update to 1.3, add virtualStream relates
 */
public enum StreamKey {

    /** The stream id. */
    streamId,

    /** The flow hash include MAC enable. */
    flowHashIncludeMacEnable,

    /** The flow hash include VLAN enable. */
    flowHashIncludeVlanEnable,

    /** The flow hash include mpls enable. @since 1.2 */
    flowHashIncludeMplsEnable,

    /** The Flow hash include gre key enable stream key @since 1.1. */
    flowHashIncludeGreKeyEnable,

    /** The multiple VLAN strategy. */
    useFirstVlan,

    /** The time out of order check enable. */
    timeOutOfOrderCheckEnable,

    /** The strip GRE enable. */
    stripGreEnable,

    /** The drop none GRE packet enable. */
    dropNoneGrePacketEnable,

    /** The IP reassemble enable. */
    ipReassembleEnable,

    /** The virtual stream map. */
    virtualStreamMap,

    /** The virtual stream. */
    virtualStream,

    /** The group. */
    group,

    /** The group id. */
    groupId,
}
