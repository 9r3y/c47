package com.y3r9.c47.dog.swj.counter;

/**
 * The Class PureDropCounterProvider.
 * 
 * @version 1.0
 * @see PureDropCounter
 * @since project 2.6
 */
public final class PureDropCounterProvider implements PureDropCounter {

    @Override
    public long getDrops() {
        return drops;
    }

    @Override
    public void updateDrops(final long incDrops) {
        drops += incDrops;
    }

    @Override
    public void setDrops(final long value) {
        drops = value;
    }

    /** The drops. */
    private long drops;
}
