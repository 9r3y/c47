package com.y3r9.c47.dog.util;

/**
 * Created by Ethan.Zhou on 2014/9/24.
 */
public class ByteUtil {

    public static byte[] int2byte(int n) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (n & 0xFF);
        ret[2] = (byte) ((n >> 8) & 0xFF);
        ret[1] = (byte) ((n >> 16) & 0xFF);
        ret[0] = (byte) (n >>> 24);
        return ret;
    }

    public static int byte2int(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException();
        } else {
            return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        }
    }
}
