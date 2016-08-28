package com.y3r9.c47.dog.pdutest;

/**
 * The class Decoder.
 *
 * @version 1.0
 */
final class Decoder {

    /**
     * Decode boolean.
     *
     * @param buf the buf
     * @return the boolean
     */
    boolean decode(Buf buf) {
        final int remain = buf.remaining();
        if (remain == pduLen) {
            return true;
        }
        return false;
    }

    /**
     * Instantiates a new Decoder.
     *
     * @param pduLen the pdu len
     */
    public Decoder(final int pduLen) {
        this.pduLen = pduLen;
    }

    /** The Pdu len. */
    private final int pduLen;
}
