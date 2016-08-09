package com.y3r9.c47.dog.pdutest;

/**
 * The class Decoder.
 *
 * @version 1.0
 */
final class Decoder {

    static int PDU_LEN = 100;

    boolean decode(Buf buf) {
        if (buf.remaining() == PDU_LEN) {
            while (buf.hasRemaining()) {
                buf.getByte();
            }
            return true;
        }
        return false;
    }

}
