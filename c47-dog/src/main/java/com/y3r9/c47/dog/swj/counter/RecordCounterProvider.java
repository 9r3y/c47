package com.y3r9.c47.dog.swj.counter;

import cn.com.netis.dp.commons.lang.Constants;

/**
 * The Class RecordCounterProvider.
 * 
 * @version 1.1
 * @see ByteCounterProvider, RecordCounter
 * @since project 2.2
 */
public final class RecordCounterProvider extends ByteCounterProvider implements RecordCounter {

    @Override
    public long getFields() {
        return fields;
    }

    @Override
    public void updateFields(final long incFields) {
        fields += incFields;
    }

    @Override
    public void setFields(final long value) {
        fields = value;
    }

    @Override
    public void resetCounter() {
        super.resetCounter();
        fields = 0;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(Constants.STRING_BUILDER_MIDDLE_SIZE);
        builder.append("RecordCounterProvider [fields=").append(fields).append(", toString()=")
                .append(super.toString()).append(']');
        return builder.toString();
    }

    /** The fields. */
    private long fields;
}
