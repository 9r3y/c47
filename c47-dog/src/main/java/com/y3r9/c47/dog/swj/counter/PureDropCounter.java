package com.y3r9.c47.dog.swj.counter;

import cn.com.netis.dp.commons.common.statis.DropStatis;

/**
 * The Interface PureDropCounter.
 * 
 * @version 1.0
 * @see DropStatis
 * @since project 2.6
 */
public interface PureDropCounter extends DropStatis {

    /**
     * Update drops.
     * 
     * @param incDrops the incremental drops
     */
    void updateDrops(long incDrops);

    /**
     * Sets the drops.
     * 
     * @param value the new drops
     */
    void setDrops(long value);
}
