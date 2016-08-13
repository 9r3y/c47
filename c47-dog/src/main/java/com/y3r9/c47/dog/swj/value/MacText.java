package com.y3r9.c47.dog.swj.value;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import cn.com.netis.dp.commons.lang.Constants;

/**
 * The Class MacText.
 * 
 * @version 2.2
 */
public final class MacText {

    /** The Constant MAC_BYTE_LEN. */
    private static final int MAC_BYTE_LEN = 6;

    /**
     * From MAC address.
     * 
     * @param value the value
     * @return the string
     */
    public static String fromMac(final byte[] value) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length - 1; i++) {
            final byte bb = value[i];
            builder.append(ValueUtils.toHexString(UnsignedValue.toUbyte(bb), 2)).append(':');
        }
        final byte bb = value[value.length - 1];
        builder.append(ValueUtils.toHexString(UnsignedValue.toUbyte(bb), 2));
        return builder.toString();
    }

    /**
     * From MAC address.
     * 
     * @param value the value
     * @return the string
     */
    public static String fromMac(final long value) {
        final StringBuilder builder = new StringBuilder();
        for (int i = MAC_BYTE_LEN - 1; i > 0; i--) {
            final byte bb = (byte) ((value >> (8 * i)) & Constants.BYTE_MASK);
            builder.append(ValueUtils.toHexString(UnsignedValue.toUbyte(bb), 2)).append(':');
        }
        final byte bb = (byte) (value & Constants.BYTE_MASK);
        builder.append(ValueUtils.toHexString(UnsignedValue.toUbyte(bb), 2));
        return builder.toString();
    }
    
    /**
     * To MAC.
     * 
     * @param macAddress the MAC address
     * @return the MAC
     * @throws DecoderException the decoder exception
     */
    public static long toMac(final String macAddress) throws DecoderException {
        final StringBuilder hexStringBuilder = new StringBuilder();
        for (int i = 0; i < MAC_BYTE_LEN; ++i) {
            hexStringBuilder.append(macAddress.substring(i * 3, i * 3 + 2));
        }

        final String hexString = hexStringBuilder.toString();
        final byte[] hexBytes = ArrayUtils.addAll(new byte[2],
                Hex.decodeHex(hexString.toCharArray()));

        final ByteBuffer hexBuffer = ByteBuffer.wrap(hexBytes);
        return hexBuffer.getLong();
    }

    /**
     * Instantiates a new MAC text.
     */
    private MacText() {
    }
}
