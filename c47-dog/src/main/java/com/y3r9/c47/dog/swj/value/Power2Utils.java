package com.y3r9.c47.dog.swj.value;

/**
 * The Class Power2Utils.
 * 
 * @version 1.0
 * @since project 3.0
 */
public final class Power2Utils {

    /** The Constant NORM_MAX. */
    private static final int NORM_MAX = 0x40000000;

    /**
     * Clip value.
     * 
     * @param value the value
     * @param range1 the range1
     * @param range2 the range2
     * @return the integer
     */
    public static int clipValue(final int value, final int range1, final int range2) {
        return range1 < range2 ? Math.min(Math.max(value, range1), range2) :
                Math.min(Math.max(value, range2), range1);
    }

    /**
     * Power2 ceiling value.
     * 
     * @param value the value
     * @return the power2 ceiling value
     */
    public static int power2Ceil(final int value) {
        if (value >= NORM_MAX) {
            return NORM_MAX;
        }

        int bitMask = 0x40000000;
        for (int i = 0; i < 32; i++) {
            if (bitMask < NORM_MAX && (value & bitMask) != 0) {
                break;
            }
            bitMask >>= 1;
        }
        return value > bitMask ? bitMask << 1 : bitMask;
    }

    /**
     * Power2 ceiling value.
     * 
     * @param value the value
     * @param range1 the range1
     * @param range2 the range2
     * @return the power2 ceiling value
     */
    public static int power2Ceil(final int value, final int range1, final int range2) {
        final int clip = clipValue(value, range1, range2);
        return power2Ceil(clip);
    }

    /**
     * Gets the highest bit index.
     * 
     * @param value the value
     * @return the highest bit index
     */
    public static int getHighestBitIndex(final int value) {
        if (value == 0) {
            return 0;
        }
        int bitMask = 0x40000000;
        for (int i = 31; i > 0; i--) {
            if ((value & bitMask) != 0) {
                return i;
            }
            bitMask >>= 1;
        }
        return 0;
    }

    /**
     * Instantiates a new sort utils.
     */
    private Power2Utils() {
    }
}
