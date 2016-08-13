package com.y3r9.c47.dog.swj.counter;

import cn.com.netis.dp.commons.common.statis.RecordStatis;

/**
 * The Interface DropCounter.
 * 
 * @version 2.1
 * @see ByteCounter, RecordStatis
 * @since project 2.2
 * @since project 3.0 updates to 2.1
 */
public interface RecordCounter extends ByteCounter, RecordStatis {

    /**
     * Update fields.
     * 
     * @param incFields the fields
     */
    void updateFields(long incFields);

    /**
     * Sets the fields.
     * 
     * @param value the new fields
     */
    void setFields(long value);
}
