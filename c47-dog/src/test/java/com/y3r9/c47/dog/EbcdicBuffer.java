package com.y3r9.c47.dog;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * The Class EbcdicBuffer.
 *
 * @version 1.6
 * @since project 2.19 updates to 1.4
 */
public final class EbcdicBuffer {

    /**
     * Gets the string.
     *
     * @param buffer the buffer
     * @param offset the offset
     * @param length the length
     * @return the string
     */
    public static String getString(final ByteBuffer buffer, final int offset, final int length) {
        final int position = buffer.position();
        try {
            buffer.position(buffer.position() + offset);
            return getString(buffer, length);
        } finally {
            buffer.position(position);
        }
    }

    /**
     * Gets the string.
     *
     * @param buffer the buffer
     * @param length the length
     * @return the string
     */
    public static String getString(final ByteBuffer buffer, final int length) {
        return getString(buffer, length, EBCDIC_DOT);
    }

    /**
     * Gets the string.
     *
     * @param buffer the buffer
     * @param length the length
     * @param replaceChar the replace char
     * @return the string
     * @since 1.4
     */
    public static String getString(final ByteBuffer buffer, final int length,
            final char replaceChar) {

        final StringBuilder builder = new StringBuilder();
        if (replaceChar == EBCDIC_DOT) {
            for (int i = 0; i < length; i++) {
                final int index = UnsignedValue.toUbyte(buffer.get());
                final char convert = EBCDIC_TABLE[index];
                if (convert == EBCDIC_NULL) {
                    builder.append(BLANK_CHAR);
                    continue;
                }
                builder.append(convert);
            }
        } else {
            for (int i = 0; i < length; i++) {
                final int index = UnsignedValue.toUbyte(buffer.get());
                final char convert = EBCDIC_TABLE[index];
                if (convert == EBCDIC_NULL) {
                    builder.append(BLANK_CHAR);
                    continue;
                }
                builder.append(convert == EBCDIC_DOT && index != EBCDIC_DOT_INDEX ? replaceChar
                        : convert);
            }
        }
        return builder.toString();
    }

    /**
     * Gets the integer.
     *
     * @param buffer the buffer
     * @param length the length
     * @return the integer
     */
    public static int getInt(final ByteBuffer buffer, final int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must >= 0, but actual is " + length);
        }
        return Integer.parseInt(getString(buffer, length));
    }

    /**
     * Prevents from instantiating.
     */
    private EbcdicBuffer() {
    }

    /** The EBCDIC char set. */
    private static final Charset CS_EBCDIC = Charset.forName("IBM500");

    /** The Constant EBCDIC_DOT. */
    public static final char EBCDIC_DOT = '.';

    /** The Constant BLANK_CHAR. */
    public static final char BLANK_CHAR = ' ';

    /** The Constant EBCDIC_DOT_INDEX. */
    private static final int EBCDIC_DOT_INDEX = 0x4B;

    /** The Constant EBCDIC_DOT. */
    private static final char EBCDIC_NULL = '\0';

    /** The Constant EBCDIC_TABLE. refer to EBCDIC CP500. */
    private static final char[] EBCDIC_TABLE = {
            // 0x0X
            '\0', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
            // 0x1X
            '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
            // 0x2X
            '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
            // 0x3X
            '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
            // 0x4X, 0x4B is real '.'
            ' ', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '<', '(', '+', '|',
            // 0x5X
            '&', '.', '.', '.', '.', '.', '.', '.', '.', '.', '!', '$', '*', ')', ';', '.',
            // 0x6X
            '-', '/', '.', '.', '.', '.', '.', '.', '.', '.', '|', ',', '%', '_', '>', '?',
            // 0x7X
            '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', ':', '#', '@', '\'', '=', '"',
            // 0x8X
            '.', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', '.', '.', '.', '.', '.', '.',
            // 0x9X
            '.', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', '.', '.', '.', '.', '.', '.',
            // 0xAX
            '.', '~', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '.', '.', '.', '.', '.', '.',
            // 0xBX
            '^', '.', '.', '.', '.', '.', '.', '.', '.', '.', '[', ']', '.', '.', '.', '.',
            // 0xCX
            '{', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', '.', '.', '.', '.', '.', '.',
            // 0xDX
            '}', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', '.', '.', '.', '.', '.', '.',
            // 0xEX
            '\\', '.', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '.', '.', '.', '.', '.',
            // 0xFX
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '.', '.', '.', '.', '.',
    };
}
