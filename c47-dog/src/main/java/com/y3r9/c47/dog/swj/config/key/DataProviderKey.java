package com.y3r9.c47.dog.swj.config.key;

/**
 * The Enum DataProviderKey.
 *
 * @version 1.1
 * @since project 3.0
 */
public enum DataProviderKey {

    /** The time zone. */
    timeZone,

    /** The network packet length. */
    networkPacketLength,

    /** The work as non daemon. */
    workAsNonDaemon,

    /** The sync. */
    sync,

    /** The sync beat tick time milliseconds. */
    syncBeatTickMilli,

    /** The report beat tick time milliseconds. */
    reportBeatTickMilli,

    /** The partition cleanup group count. */
    partCleanupGroupCount,

    /** The partition cleanup enable subgroup. */
    partCleanupEnableSubgroup,

    /** The partition subgroup count. */
    partSubgroupCount,

    /** The Group parallel key. */
    group,

    /** The Property parallel key. */
    property,

    /** The Id parallel key. */
    id,

    /** The Global known unit cnt key, for nta/ntae. */
    globalKnownUnitCount,

    /** The Global unknown unit cnt key, for nta/ntae/ntse. */
    globalUnknownUnitCount,

    /** The Shrinkage data provider key, for nta/ntae/ntse. @since 1.1 */
    unitShrinkage,

    /** The ensure Accurate. @since 1.1 */
    ensureAccurate,

    /** The top N size. @since 1.1 */
    topNSize,

    /** The top N matrics. @since 1.1 */
    topNMatrics,
}
