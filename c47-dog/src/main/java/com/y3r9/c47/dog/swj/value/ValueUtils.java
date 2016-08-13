package com.y3r9.c47.dog.swj.value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.OutOfRangeArgumentException;
import cn.com.netis.dp.commons.lang.YesNoValue;
import cn.com.netis.dp.commons.util.NormText;

/**
 * The Class ValueUtils, convert string to the corresponding type value.
 * 
 * @version 1.8
 */
public final class ValueUtils {

    /**
     * Used to build output as Hex.
     */
    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Gets integer value.
     * 
     * @param value the value
     * @return the double
     */
    public static int toInteger(final String value) {
        try {
            return Integer.valueOf(value);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(value + " is not integer value", e);
        }
    }

    /**
     * Gets the double value.
     * 
     * @param value the value
     * @return the double value
     */
    public static long toLong(final String value) {
        try {
            return Long.valueOf(value);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(value + " is not long value", e);
        }
    }

    /**
     * Gets the double value.
     * 
     * @param value the value
     * @return the double value
     */
    public static double toDouble(final String value) {
        try {
            return Double.valueOf(value);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(value + " is not double value", e);
        }
    }

    /**
     * Gets the boolean value.
     * 
     * @param value the value
     * @return the boolean value
     */
    public static boolean toBoolean(final String value) {
        return EnumUtils.toEnum(YesNoValue.class, NormText.trimAndUpper(value)).asBoolean();
    }

    /**
     * To hex string.
     * 
     * @param value the value
     * @param size the size
     * @return the string
     */
    public static String toHexString(final int value, final int size) {
        final StringBuilder builder = new StringBuilder();
        int val = value;
        while (val != 0) {
            builder.insert(0, DIGITS_LOWER[val & Constants.HALF_BYTE_MASK]);
            val >>>= 4;
        }
        while (builder.length() < size) {
            builder.insert(0, '0'); // padding with leading zero if needed
        }
        return builder.toString();
    }

    /**
     * To hex string.
     * 
     * @param value the value
     * @param size the size
     * @return the string
     */
    public static String toHexString(final long value, final int size) {
        final StringBuilder builder = new StringBuilder();
        long val = value;
        while (val != 0) {
            builder.insert(0, DIGITS_LOWER[(int) (val & Constants.HALF_BYTE_MASK)]);
            val >>>= 4;
        }
        while (builder.length() < size) {
            builder.insert(0, '0'); // pad with leading zero if needed
        }
        return builder.toString();
    }

    /**
     * Parses the long, parseLong("FF", 16) returns 255L parseLong("1100110",2) returns 102L.
     * 
     * @param s the s
     * @param radix the radix
     * @return the long
     */
    public static long parseLong(final String s, final int radix) {
        if (s == null) {
            return 0;
        }
        long result = 0;
        int digit;
        final int len = s.length();
        for (int j = 0; j < len; j++) {
            digit = Character.digit(s.charAt(j), radix);
            result *= radix;
            result -= digit;
        }
        return -result;
    }

    /**
     * Extract.
     * 
     * @param value the value
     * @param mask the mask
     * @return the extract integer
     */
    public static int extract(final int value, final int mask) {
        final int trailingZeros = Integer.numberOfTrailingZeros(mask);
        return (value & mask) >>> trailingZeros;
    }

    /**
     * Gets the non negative value.
     * 
     * @param value the value text
     * @param maxValue the max value
     * @param title the title text
     * @return the positive integer value
     */
    public static int getNonNegativeValue(final String value, final int maxValue,
            final String title) {
        try {
            final int result = Integer.parseInt(value.trim());
            OutOfRangeArgumentException.check(result, maxValue, title);
            return result;
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(String.format("invalid %s format \"%s\", %s", title,
                    value, e.getMessage()));
        }
    }

    /**
     * Roundup4.
     * 
     * @param value the value to roundup
     * @return the value roundup4
     */
    public static int roundup4(final int value) {
        return value + 3 & -4;
    }

    /**
     * List to map values.
     *
     * @param <T> the generic type
     * @param listValues the values
     * @return the map
     */
    public static <T> Map<String, T> toMap(final List<Pair<String, T>> listValues) {
        final Map<String, T> result = new HashMap<>();
        for (final Pair<String, T> item : listValues) {
            result.put(item.getKey(), item.getValue());
        }
        return result;
    }

    /**
     * Instantiates a new string to value utils.
     */
    private ValueUtils() {
    }
}
