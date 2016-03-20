package com.y3r9.c47.dog;

import java.security.InvalidParameterException;

/**
 * Unsigned value.
 * 
 * @version 1.3
 */
public final class UnsignedValue {

    /** Mask for unsigned byte. */
    public static final int UBYTE_MASK = 0xFF;

    /** Mask for unsigned short. */
    public static final int USHORT_MASK = 0xFFFF;

    /** Mask for unsigned short byte 0. */
    public static final int USHORT_0_MASK = 0xFF00;
    /** Mask for unsigned short byte 1. */
    public static final int USHORT_1_MASK = 0x00FF;

    /** Shift for unsigned short byte 0. */
    public static final int USHORT_0_SHIFT = 8;

    /** Mask for unsigned integer. */
    public static final long UINT_MASK = 0xFFFFFFFFL;

    /** Mask for unsigned integer byte 0. */
    public static final long UINT_0_MASK = 0xFF000000;
    /** Mask for unsigned integer byte 1. */
    public static final long UINT_1_MASK = 0x00FF0000;
    /** Mask for unsigned integer byte 2. */
    public static final long UINT_2_MASK = 0x0000FF00;
    /** Mask for unsigned integer byte 3. */
    public static final long UINT_3_MASK = 0x000000FF;

    /** Shift for unsigned integer byte 0. */
    public static final int UINT_0_SHIFT = 24;
    /** Shift for unsigned integer byte 1. */
    public static final int UINT_1_SHIFT = 16;
    /** Shift for unsigned integer byte 2. */
    public static final int UINT_2_SHIFT = 8;
    /** Shift for unsigned integer byte 3. */
    public static final int UINT_3_SHIFT = 0;

    /** Offset for byte 0. */
    public static final int BYTE0_OFFSET = 0;
    /** Offset for byte 1. */
    public static final int BYTE1_OFFSET = 1;
    /** Offset for byte 2. */
    public static final int BYTE2_OFFSET = 2;
    /** Offset for byte 3. */
    public static final int BYTE3_OFFSET = 3;
    /** Count for byte in int. */
    public static final int BYTES_COUNT = 4;

    /** Shift value for a byte. */
    public static final int BYTE_SHIFT = 3;

    /**
     * Convert byte to unsigned byte in integer format.
     * 
     * @param value Value to be converted.
     * @return Unsigned byte in integer format.
     */
    public static int toUbyte(final byte value) {
        return value & UBYTE_MASK;
    }

    /**
     * Convert integer to unsigned byte in integer format.
     * 
     * @param value Value to be converted.
     * @return the unsigned byte in integer format
     */
    public static int toUbyte(final int value) {
        return value & UBYTE_MASK;
    }

    /**
     * Convert a byte in integer to unsigned byte in integer format. Highest
     * byte is position 0.
     * 
     * @param value the value
     * @param byteIndex the byte index
     * @return the unsigned byte in integer format
     */
    public static int toUbyte(final int value, final int byteIndex) {
        if (byteIndex < 0 || byteIndex > BYTE3_OFFSET) {
            throw new InvalidParameterException("byteIndex must less then 4");
        }
        return (value >> ((BYTE3_OFFSET - byteIndex) << BYTE_SHIFT))
                & UBYTE_MASK;
    }

    /**
     * Convert byte to unsigned short in integer format.
     * 
     * @param value Value to be converted.
     * @return Unsigned short in integer format.
     */
    public static int toUshort(final int value) {
        return value & USHORT_MASK;
    }

    /**
     * Convert short data in a byte buffer to unsigned short in integer format.
     * 
     * @param buffer Buffer which contains the data to be converted.
     * @param offset Offset of the data.
     * @return Unsigned short in integer format.
     */
    public static int toUshort(final byte[] buffer, final int offset) {
        return ((buffer[offset] << USHORT_0_SHIFT) & USHORT_0_MASK)
                | (buffer[offset + BYTE1_OFFSET] & USHORT_1_MASK);
    }

    /**
     * Convert byte to unsigned integer in long format.
     * 
     * @param value Value to be converted.
     * @return Unsigned integer in long format.
     */
    public static long toUint(final int value) {
        return value & UINT_MASK;
    }

    /**
     * Convert integer data in a byte buffer to unsigned integer in long format.
     * 
     * @param buffer Buffer which contain the data to be converted.
     * @param offset Offset of the data.
     * @return Unsigned integer in long format.
     */
    public static long toUint(final byte[] buffer, final int offset) {
        return ((buffer[offset] << UINT_0_SHIFT) & UINT_0_MASK)
                | ((buffer[offset + BYTE1_OFFSET] << UINT_1_SHIFT) & UINT_1_MASK)
                | ((buffer[offset + BYTE2_OFFSET] << UINT_2_SHIFT) & UINT_2_MASK)
                | (buffer[offset + BYTE3_OFFSET] & UINT_3_MASK);
    }

    /**
     * Used to prevent from creating instance.
     */
    private UnsignedValue() {

    }
}
