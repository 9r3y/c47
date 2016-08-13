package com.y3r9.c47.dog.swj.config.key;

/**
 * The Enum DecodeKey.
 *
 * @version 1.2
 * @since v3.0
 * @since v3.3.8 update to 1.1
 * @since v3.9 update to 1.2
 */
public enum DecodeKey {

    /** The parser. */
    parser,

    /** The packet parse time millisecond. */
    packetParseTimeMilli,

    /** The flow timeout milliseconds. */
    flowTimeout,

    /** The flow attach timeout milliseconds. */
    flowAttachTimeout,

    /** The flow detach timeout milliseconds. */
    flowDetachTimeout,

    /** The flow context packet count. */
    flowContextPacketCount,

    /** The flow context packet clean rate. */
    flowContextPacketCleanRate,

    /** The mismatch drop packet enable. */
    mismatchDropPacketEnable,

    /** The global connection count. */
    globalConnectionCount,

    /** The local drop connection count. */
    localDropConnectionCount,

    /** The global VLAN count. @since 1.1. */
    globalVlanCacheCount,

    /** The unmark flow merge. */
    unmarkFlowMerge,

    /** The millisecond time format. */
    millisecondTimeFormat,

    /** The exclude network delay. */
    excludeNetworkDelay,

    /** The map record enable. */
    mapRecordEnable,

    /** The TCP fast retransmission timeout milliseconds. */
    tcpFastRetransmissionTimeout,

    /** The TCP out of order timeout milliseconds. */
    tcpOutOfOrderTimeout,

    /** The TCP check peer sequence disable. */
    tcpCheckPeerSequenceDisable,

    /** The TCP sequence valid distance. */
    tcpSequenceValidDistance,

    /** The TCP statistics fixture enable. */
    tcpStatisFixtureEnable,

    /** The PDU buffer size. */
    pduBufferSize,

    /** The PDU cache packet count. */
    pduCachePacketCount,

    /** The PDU count. */
    pduCount,

    /** The complete payload required. */
    payloadCompleteRequired,

    /** The group complete required. */
    groupCompleteRequired,

    /** The detect mode. */
    detectMode,

    /** The group. */
    group,

    /** The decode id. */
    decodeId,

    /** The record code field. */
    recordCodeField,

    /** The network enable. */
    networkEnable,

    /** The network unit default. */
    networkUnitDefault,

    /** The network unit. */
    networkUnit,

    /** The network rule enable. */
    networkRuleEnable,

    /** The network rule. */
    networkRule,

    /** The IP. */
    ip,

    /** The port. */
    port,

    /** The name. */
    name,

    /** The type. */
    type,

    /** The from. */
    from,

    /** The to. */
    to,

    /** The decode as dummy. */
    decodeAsDummy,

    /** The optimize option for optimistic decode. */
    optimizeOption1,

    /** The optimize option1's buffer range attribute. */
    range,

    /** The protocol. */
    protocol,

    /** The root node. */
    rootNode,

    /** The start search size. */
    startSearchSize,

    /** The end search size. */
    endSearchSize,

    /** The byte order. */
    byteOrder,

    /** The encoding. */
    encoding,

    /** The wrap length type. */
    wrapLengthType,

    /** The wrap length bias size. */
    wrapLengthBiasSize,

    /** The payloadOffset. */
    payloadOffset,

    /** The wrapLengthOffset. */
    wrapLengthOffset,

    /** The charset. */
    charset,

    /** The payload complete pending. */
    payloadCompletePending,

    /** The field. */
    field,

    /** The mismatch action. */
    mismatchAction,

    /** The ignore case. */
    ignoreCase,

    /** The key. */
    key,

    /** The value. */
    value,

    /** The hint. */
    hint,

    /** The action. */
    action,

    /** The item. */
    item,

    /** The target. */
    target,

    /** The id. */
    id,

    /** The base protocol. */
    baseProtocol,

    /** The all item. */
    allItem,

    /** The template. */
    template,

    /** The record field. */
    recordField,

    /** The extend recordCodeField. */
    extendField,

    /** The operator. */
    op,

    /** The default value. */
    defaultValue,

    /** The start index. */
    startIndex,

    /** The count. */
    count,

    /** The old string. */
    oldString,

    /** The new string. */
    newString,

    /** The separator. */
    separator,

    /** The property. */
    property,

    /** The expression. */
    exp,

    /** The pattern string. */
    regex,

    /** The appIdPolicy. */
    appIdPolicy,

    /** The disableSessionRecord. */
    disableSessionRecord
}
