package com.y3r9.c47.dog.pdutest;

/**
 * The class Decoder.
 *
 * @version 1.0
 */
final class Decoder {

    static int MTU = 1500;

    static int PDU_PKT_COUNT = 10;

    boolean decode(Buf buf) {
        if (buf.remaining() == PDU_PKT_COUNT * MTU) {
//            while (buf.hasRemaining()) {
//                byte b = buf.getByte();
//            }
            return true;
        }
        return false;
    }

}
