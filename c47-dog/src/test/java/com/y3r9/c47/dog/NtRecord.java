package com.y3r9.c47.dog;

import java.util.HashMap;
import java.util.Map;

/**
 * The class NtRecord.
 *
 * @version 1.0
 */
final class NtRecord {

    int srcIp;

    int destIp;

    int pktLenSum;

    static NtRecord create(final String line) {
        final NtRecord result = new NtRecord();
        String[] kvs = line.split("\\t");
        Map<String, String> map = new HashMap<>();
        for (String kv : kvs) {
            if (!kv.contains("=")) {
                continue;
            }
            String[] keyValue = kv.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        String str;
        str = map.get("DestIp");
        if (str != null) {
            result.destIp = Integer.parseInt(str);
        }
        str = map.get("SrcIp");
        if (str != null) {
            result.srcIp = Integer.parseInt(str);
        }
        str = map.get("PktLen_Sum");
        if (str != null) {
            result.pktLenSum = Integer.parseInt(str);
        }
        return result;
    }
}
