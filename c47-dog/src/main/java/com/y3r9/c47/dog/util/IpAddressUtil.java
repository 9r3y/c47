package com.y3r9.c47.dog.util;

/**
 * Created by Ethan.Zhou on 2014/9/24.
 */
public class IpAddressUtil {

    private IpAddressUtil() {}

    public static int ipArray2int(int[] array) {
        int out = 0;
        for (int i=array.length-1, j=0; i>=0; i--, j+=8) {
            out = out | ((array[i] & 0xFF) << j);
        }
        return out;
    }

    public static int[] int2IpArray(int ip) {
        int[] ret = new int[4];
        for (int i=0, j=24; i<4; i++, j-=8) {
            ret[i] = 0xFF & (ip >> j);
        }
        return ret;
    }
}
