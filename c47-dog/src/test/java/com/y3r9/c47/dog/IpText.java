package com.y3r9.c47.dog;

import org.apache.commons.lang3.StringUtils;

import com.y3r9.c47.dog.UnsignedValue;

/**
 * Format IP to text.
 * 
 * @version 1.6
 */
public final class IpText {

    /**
     * Get IP text from IP value.
     * 
     * @param ipValue the IP value
     * @return the string
     */
    public static String fromIp(final int ipValue) {
        final StringBuilder builder = new StringBuilder();
        builder.append(UnsignedValue.toUbyte(ipValue, UnsignedValue.BYTE0_OFFSET));
        builder.append('.');
        builder.append(UnsignedValue.toUbyte(ipValue, UnsignedValue.BYTE1_OFFSET));
        builder.append('.');
        builder.append(UnsignedValue.toUbyte(ipValue, UnsignedValue.BYTE2_OFFSET));
        builder.append('.');
        builder.append(UnsignedValue.toUbyte(ipValue, UnsignedValue.BYTE3_OFFSET));
        return builder.toString();
    }

    /**
     * Get the integer value from string.
     * 
     * @param ipAddress the IP address like XXX.XXX.XXX.XXX
     * @return the integer value for IP address
     */
    public static int toIp(final String ipAddress) {
        final String[] address = StringUtils.split(ipAddress, ".");
        if (address.length != 4) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Invalid IP format \"")
                    .append(ipAddress).append("\", should be XXX.XXX.XXX.XXX");
            throw new IllegalArgumentException(builder.toString());
        }
        int result = 0;
        for (int i = 0; i < address.length; i++) {
            final int segment = Integer.parseInt(address[i]);
            if (segment < 0 || segment >= 256) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Invalid IP format \"")
                        .append(ipAddress).append("\"");
                throw new IllegalArgumentException(builder.toString());
            }
            result = (result << 8) | segment;
        }
        return result;
    }

    /**
     * Prevents instantiation.
     */
    private IpText() {
    }
}
