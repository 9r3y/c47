package com.y3r9.c47.dog.swj.value;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility to convert hex string to hex byte array.
 * 
 * @version 1.8
 */
public final class HexByteArray {

    /**
     * Count valid byte in the HEX string.
     * 
     * @param hexString The given string to be converted.
     * @return Count of valid chars in string
     */
    public static int countValidChars(final String hexString) {

        if (StringUtils.isEmpty(hexString)) {
            return 0;
        }

        int retval = 0;
        for (int i = 0; i < hexString.length(); i++) {
            final char chInS = hexString.charAt(i);

            if (chInS == ' ') {
                if (retval % 2 != 0) {
                    final StringBuilder builder = new StringBuilder();
                    builder.append("Missing half byte at #").append(i);
                    throw new IllegalArgumentException(builder.toString());
                }
                continue;
            }

            if (((chInS | HEX_MASK) != HEX_MASK) || !HEX[chInS]) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Invalid char \"").append(chInS)
                        .append("\" at #").append(i);
                throw new IllegalArgumentException(builder.toString());
            }

            retval++;
        }

        if (retval % 2 != 0) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Missing half byte at the end");
            throw new IllegalArgumentException(builder.toString());
        }

        return retval / 2;
    }

    /**
     * Convert HEX string to binary array.
     * 
     * @param hexString The given string to be converted.
     * @return <code>true</code> Convert successfully, and store result in
     *         target. <code>false</code>Target is meaningless.
     */
    public static byte[] fromString(final String hexString) {
        final int stringCount = countValidChars(hexString);

        if (stringCount == 0) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        final String normString = StringUtils.replace(hexString, BLANKSPACE,
                StringUtils.EMPTY);
        byte[] result = new byte[stringCount];

        for (int i = 0, j = 0; i < normString.length(); i += 2, j++) {
            final String temp = normString.substring(i, i + 2);
            result[j] = (byte) Integer.parseInt(temp, 16);
        }

        return result;
    }

    /**
     * Used to prevent from creating instance.
     */
    private HexByteArray() {
    }

    /** Used to mask. */
    private static final int HEX_MASK = 0x7F;

    /** The Constant BLANKSPACE. */
    private static final String BLANKSPACE = " ";

    /**
     * Valid HEX: [0x30-0x39] | [0x41-0x46] | [0x61-0x66].
     */
    private static final boolean[] HEX = {
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            true, true, true, true, true, true, true, true,
            true, true, false, false, false, false, false, false,
            false, true, true, true, true, true, true, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, true, true, true, true, true, true, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false };
}
