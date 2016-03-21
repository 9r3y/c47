package com.y3r9.c47.dog.script.ntaoutoforder;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import cn.com.netis.dp.commons.lang.Constants;

/**
 * Timestamp related. The unit of the timestamp is nanosecond, so, this class only process the
 * conversions from or to nanosecond.
 * 
 * For other unit, the caller should reference java.util.concurrent.TimeUnit for help.
 * 
 * @version 2.10
 * @since project 3.0 updates 2.10
 */
public final class Timestamp {

    /** The Constant SEC_TO_NANO. */
    public static final long SEC_TO_NANO = 1000000000;

    /** The Constant MILLI_TO_NANO. */
    public static final long MILLI_TO_NANO = 1000000;

    /** The Constant SECOND_TO_MILI. */
    public static final double SECOND_TO_MILI = 1000.0;

    /**
     * From seconds and nanoseconds.
     * 
     * @param sec the seconds
     * @param nano the nanoseconds
     * @return the total timestamp in nanoseconds
     */
    public static long fromSecNanosec(final long sec, final long nano) {
        return sec * SEC_TO_NANO + nano;
    }

    /**
     * To double seconds.
     * 
     * @param timestamp the timestamp
     * @return the float
     */
    public static double toDoubleSeconds(final long timestamp) {
        return (double) timestamp / SEC_TO_NANO;
    }

    /**
     * To long seconds. This method only execute one division, while TimeUnit does two times, so,
     * this one is better performance.
     * 
     * @param timestamp the timestamp
     * @return the long
     */
    public static long toLongSeconds(final long timestamp) {
        return timestamp / SEC_TO_NANO;
    }

    /**
     * To double milliseconds.
     * 
     * @param timestamp the timestamp
     * @return the float
     */
    public static double toDoubleMilliseconds(final long timestamp) {
        return (double) timestamp / MILLI_TO_NANO;
    }

    /**
     * To long millisecond. This method only execute one division, while TimeUnit does two times,
     * so, this one is better performance.
     * 
     * @param timestamp the timestamp
     * @return the long
     */
    public static long toLongMilliseconds(final long timestamp) {
        return timestamp / MILLI_TO_NANO;
    }

    /**
     * from long millisecond. This method only execute one multiplication, while TimeUnit does more
     * complex like 'return x(d, C2/C0, MAX/(C2/C0))', so, this one is better performance.
     * 
     * @param timestamp the millisecond timestamp
     * @return the nanosecond timestamp
     * @since 1.8
     */
    public static long fromLongMilliseconds(final long timestamp) {
        return timestamp * MILLI_TO_NANO;
    }

    /**
     * Gets the sub second.
     * 
     * @param timestamp the timestamp
     * @return the sub second
     */
    public static double getSubSecond(final long timestamp) {
        return toDoubleSeconds(timestamp % SEC_TO_NANO);
    }

    /**
     * Gets the sub nanosecond.
     * 
     * @param timestamp the timestamp
     * @return the sub second
     */
    public static long getSubNanosecond(final long timestamp) {
        return timestamp % SEC_TO_NANO;
    }

    /**
     * Align to.
     * 
     * @param timestamp the timestamp
     * @param align the align
     * @return the long
     */
    public static long alignTo(final long timestamp, final long align) {
        return (timestamp / align) * align;
    }

    /**
     * Ceiling align to.
     * 
     * @param timestamp the timestamp
     * @param align the align
     * @return the long
     */
    public static long ceilingAlignTo(final long timestamp, final long align) {
        return Timestamp.alignTo(timestamp - 1, align) + align;
    }

    /**
     * Millisecond to second.
     * 
     * @param milli the millisecond
     * @return the float second
     */
    public static double milliToSecond(final long milli) {
        return milli / SECOND_TO_MILI;
    }

    /**
     * To date nanosecond text.
     * 
     * @param nanoseconds the nanoseconds
     * @return the string
     */
    public static String toDateNanosecondText(final long nanoseconds) {
        final StringBuilder builder = new StringBuilder();
        toDateNanosecondText(nanoseconds, builder);
        return builder.toString();
    }

    /**
     * To date nanosecond text.
     * 
     * @param nanoseconds the nanoseconds
     * @param builder the builder
     * @since 2.10
     */
    public static void toDateNanosecondText(final long nanoseconds, final StringBuilder builder) {
        final Date date = new Date();
        date.setTime(Timestamp.toLongMilliseconds(nanoseconds));
        builder.append(DateFormatUtils.format(date, Constants.DATE_PATTERN));
        final String subSeconds = String.format(Constants.NANO_FORMAT,
                Timestamp.getSubSecond(nanoseconds));
        builder.append(subSeconds.substring(1));
    }

    /**
     * To date nanosecond text with brackets.
     * 
     * @param nanoseconds the nanoseconds
     * @param builder the builder
     * @since 2.10
     */
    public static void toDateNanosecondTextWithBrackets(final long nanoseconds,
            final StringBuilder builder) {
        builder.append('[');
        toDateNanosecondText(nanoseconds, builder);
        builder.append(']');
    }

    /**
     * To date second text.
     * 
     * @param milliseconds the milliseconds
     * @return the string
     */
    public static String toDateSecondText(final long milliseconds) {
        return DateFormatUtils.format(milliseconds, Constants.DATE_PATTERN);
    }

    /**
     * To ISO date second text. E.g. UnixTime "1372350243000" is ISO time "2013-06-28T00:24:03"
     * 
     * @param milliseconds the milliseconds
     * @return the ISO time string
     * @since 1.6
     */
    public static String toIsoDateSecondText(final long milliseconds) {
        return DateFormatUtils.format(milliseconds, Constants.DATE_T_PATTERN);
    }

    /**
     * From ISO date second text. E.g. ISO time "2013-06-28T00:24:03" is UnixTime "1372350243000".
     * 
     * @param value the value
     * @return the Unix time milliseconds
     * @throws ParseException the parse exception
     */
    public static long fromIsoDateSecondText(final String value) throws ParseException {
        return DateUtils.parseDate(value, Constants.DATE_T_PATTERN).getTime();
    }

    /**
     * Prevents instantiation.
     */
    private Timestamp() {
    }
}
