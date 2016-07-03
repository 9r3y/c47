package com.y3r9.c47.dog.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.RecursiveAction;

import org.junit.Test;

import com.sun.org.apache.regexp.internal.RE;
import com.y3r9.c47.dog.IpText;

/**
 * The class Scripts.
 *
 * @version 1.0
 */
public class Scripts {

    @Test
    public void dumpNetFlowData() {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("D:\\e\\nflow\\v9\\nflow9_1503.pcap"));
        for (Path path : paths) {
            try {
//                final String cmd = String.format("python D:\\APP\\netis\\disp-ipfix\\dump-data\\dump-data.py %s --dump-folder %s --hide-packet --enable-dump-frame",
                final String cmd = String.format("python D:\\APP\\netis\\disp-ipfix\\dump-data\\dump-data.py %s --dump-folder %s --hide-packet",
                        path.toString(), "D:\\e\\nflow\\v9");
                Process process = Runtime.getRuntime().exec(cmd);
                System.out.println(cmd);
                InputStream is = process.getInputStream();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void dispNetFlowSumKeyValue() {
        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("D:\\e\\byz\\20160505145900_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150000_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150100_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150200_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150300_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150400_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150500_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150600_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150700_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150800_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505150900_0.xflow"));
//        paths.add(Paths.get("D:\\e\\byz\\20160505151000_0.xflow"));

//        paths.add(Paths.get("D:\\e\\xflowbyz2\\20160512155500_0.nflow"));
//        paths.add(Paths.get("D:\\e\\xflowbyz2\\20160512155600_0.nflow"));

//        paths.add(Paths.get("D:\\e\\nflow\\v9\\nflow9_1503.nflow"));
//        paths.add(Paths.get("D:\\e\\nflow\\v9\\20160513174118.dump"));
        paths.add(Paths.get("D:\\e\\xflowbyz3\\20160520121800_0.nflow"));
        for (Path path : paths) {

            try {
                final String cmd = String.format("python D:\\e\\correctness\\disp-nflow\\disp-nflow.pyc %s --show-all-fields --load-template %s",
//                        path.toString(), "D:\\e\\xflowbzq\\nflow.template");
//                          path.toString(), "D:\\e\\xflowbyz2\\v9.template");
                          path.toString(), "D:\\e\\nflow\\v9\\nflow.template");
                Process process = Runtime.getRuntime().exec(cmd);
//                System.out.println(cmd);
                InputStream is = process.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                XflowSum sum = new XflowSum(path);
                final SimpleDateFormat innerSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                final SimpleDateFormat outerSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                long leftPktExpTime = outerSdf.parse("2016-05-12T07:04:00").getTime();
                long rightPktExpTime  = outerSdf.parse("2016-05-12T07:05:00").getTime();
                long leftMism = innerSdf.parse("2016-05-12T07:04:00.000").getTime();
                long rightMism  = innerSdf.parse("2016-05-12T07:05:00.000").getTime();

                long pktExpTime = 0;
                long mism = 0;
                while ((line = br.readLine()) != null) {
                    final String[] kvs = line.split(",");
                    final Map<String, String> map = new HashMap<>();
                    for (String kv : kvs) {
                        if (!kv.contains("=")) {
                            continue;
                        }
                        String[] k_v = kv.split("=");
                        map.put(k_v[0].trim(), k_v[1].trim());
                    }
                    String str;
                    str = map.get("PktExpTime");
                    if (str != null) {
                        pktExpTime = outerSdf.parse(str).getTime();
                    }
                    str = map.get("monitoringIntervalStartMilliSeconds");
                    if (str != null) {
                        mism = innerSdf.parse(str).getTime();
                    }
//                    if (mism < leftMism|| mism >= rightMism) {
//                        continue;
//                    }
                    if (pktExpTime > 0) {
                        sum.setExpTime(pktExpTime);
                    }
                    if (mism > 0) {
                        sum.setMonitoringIntervalStartMilliSeconds(mism);
                    }

//                    final String srcIp = map.get("sourceIPv4Address");
//                    final String destIp = map.get("destinationIPv4Address");
//                    if (srcIp == null || destIp == null) {
//                        continue;
//                    }
//                    if (!("192.168.22.23".equals(srcIp) && "172.16.11.185".equals(destIp))
//                            &&  !("192.168.22.23".equals(destIp) && "172.16.11.185".equals(srcIp))) {
//                        continue;
//                    }

                    str = map.get("packetDeltaCount");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setPacketDeltaCount(sum.getPacketDeltaCount() + num);
                    }
                    str = map.get("octetDeltaCount");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setOctetDeltaCount(sum.getOctetDeltaCount() + num);
                    }
                    str = map.get("sumServerRespTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumServerRespTime(sum.getSumServerRespTime() + num);
                    }
                    str = map.get("transactionCountDelta");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setTransactionCountDelta(sum.getTransactionCountDelta() + num);
                    }
                    str = map.get("sumTotalRespTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumTotalRespTime(sum.getSumTotalRespTime() + num);
                    }
                    str = map.get("sumTransactionTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumTransactionTime(sum.getSumTransactionTime() + num);
                    }
                    str = map.get("sumServerNwkTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumServerNwkTime(sum.getSumServerNwkTime() + num);
                    }
                    str = map.get("sumClientNwkTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumClientNwkTime(sum.getSumClientNwkTime() + num);
                    }
                }
                System.out.println(sum);
                process.waitFor();
            } catch (IOException | InterruptedException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void dispIpfixSumKeyValue() {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("D:\\e\\ipfixbyz\\20160511173100_0.ipfix"));
//        paths.add(Paths.get("D:\\e\\ipfixbyz\\5025.ipfix"));
        for (Path path : paths) {

            try {
                final String cmd = String.format("python D:\\APP\\netis\\disp-ipfix\\disp-ipfix\\disp-ipfix.py %s --show-all-fields --load-template %s",
                        path.toString(), "D:\\e\\ipfixbyz\\ipfix.ipfix-30000.template");
                Process process = Runtime.getRuntime().exec(cmd);
//                System.out.println(cmd);
                InputStream is = process.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                XflowSum sum = new XflowSum(path);
                final SimpleDateFormat innerSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                final SimpleDateFormat outerSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                while ((line = br.readLine()) != null) {
                    final String[] kvs = line.split(",");
                    final Map<String, String> map = new HashMap<>();
                    for (String kv : kvs) {
                        if (!kv.contains("=")) {
                            continue;
                        }
                        String[] k_v = kv.split("=");
                        map.put(k_v[0].trim(), k_v[1].trim());
                    }
                    String str;
                    str = map.get("MsgExpTime");
                    if (str != null) {
                        final long time = outerSdf.parse(str).getTime();
                        sum.setExpTime(time);
                    }
                    str = map.get("monitoringIntervalStartMilliSeconds");
//                    str = map.get("flowStartMilliseconds");
                    if (str != null) {
                        long time = innerSdf.parse(str).getTime();
                        sum.setMonitoringIntervalStartMilliSeconds(time);
                    }
                    final String srcIp = map.get("sourceIPv4Address");
                    final String destIp = map.get("destinationIPv4Address");
                    if (srcIp == null || destIp == null) {
                        continue;
                    }
                    if (!("182.248.53.84".equals(srcIp))
                            &&  !("182.248.53.84".equals(destIp))) {
                        continue;
                    }
                    str = map.get("packetDeltaCount");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setPacketDeltaCount(sum.getPacketDeltaCount() + num);
                    }
                    str = map.get("octetDeltaCount");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setOctetDeltaCount(sum.getOctetDeltaCount() + num);
                    }
                    str = map.get("sumServerRespTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumServerRespTime(sum.getSumServerRespTime() + num);
                    }
                    str = map.get("transactionCountDelta");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setTransactionCountDelta(sum.getTransactionCountDelta() + num);
                    }
                    str = map.get("sumTotalRespTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumTotalRespTime(sum.getSumTotalRespTime() + num);
                    }
                    str = map.get("sumTransactionTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumTransactionTime(sum.getSumTransactionTime() + num);
                    }
                    str = map.get("sumServerNwkTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumServerNwkTime(sum.getSumServerNwkTime() + num);
                    }
                    str = map.get("sumClientNwkTime");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setSumClientNwkTime(sum.getSumClientNwkTime() + num);
                    }
                }
                System.out.println(sum);
                process.waitFor();
            } catch (IOException | InterruptedException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void sumKeyValueNtr() {
        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505145900.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150000.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150100.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150200.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150300.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150400.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150500.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150600.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150700.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150800.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505150900.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160505151000.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160423143000.nta"));

//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160512155500.ntr"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160512155600.ntr"));

        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\5025.ntr"));
        for (Path path : paths) {
            try {
                List<String> lines = Files.readAllLines(path);
                long sum = 0;
                for (String line : lines) {
                    final String[] kvs = line.split("\t");
                    final Map<String, String> map = new HashMap<>();
                    for (String kv : kvs) {
                        if (!kv.contains("=")) {
                            map.put("ts", kv);
                            continue;
                        }
                        String[] k_v = kv.split("=");
                        map.put(k_v[0], k_v[1]);
                    }

                    String str = map.get("PktCnt");
                    if (str != null) {
                        final int num = Integer.parseInt(str);
                        sum += num;
                    }
                }
                System.out.println(String.format("%s: %s", path.getFileName(), sum));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void sumKeyValueNta() throws ParseException {
        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("D:\\e\\xflowbyz2\\20160512155500_0.nta"));
//        paths.add(Paths.get("D:\\e\\xflowbyz2\\20160512155600_0.nta"));

//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160512155500.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160512155600.nta"));

//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\5025.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160511173100_0.ipfix.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\nflow9_1503.nta"));
//        paths.add(Paths.get("D:\\e\\nflow\\v9\\20160512150400_0.nta"));
        paths.add(Paths.get("D:\\e\\xflowbyz3\\20160520121800_0.nta"));
        final SimpleDateFormat tsSdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        long leftTs = tsSdf.parse("2016-05-12 15:04:00").getTime();
        long rightTs = tsSdf.parse("2016-05-12 15:05:00").getTime();
        for (Path path : paths) {
            try {
                List<String> lines = Files.readAllLines(path);
                NtaSum sum = new NtaSum(path);
                for (String line : lines) {
                    final String[] kvs = line.split("\t");
                    final Map<String, String> map = new HashMap<>();
                    for (String kv : kvs) {
                        if (!kv.contains("=")) {
                            map.put("ts", kv.substring(1, 20));
                            continue;
                        }
                        String[] k_v = kv.split("=");
                        map.put(k_v[0], k_v[1].trim());
                    }
                    String str;
                    str = map.get("ts");
                    if (str == null) {
                        continue;
                    }
                    long ts = tsSdf.parse(str).getTime();

//                    if (ts < leftTs || ts >= rightTs) {
//                        continue;
//                    }

//                    final String srcIp = map.get("SrcIp");
//                    final String destIp = map.get("DestIp");
//                    if (srcIp == null || destIp == null) {
//                        continue;
//                    }
//                    final String ip1 = String.valueOf(IpText.toIp("192.168.22.23"));
//                    final String ip2 = String.valueOf(IpText.toIp("172.16.11.185"));
//                    final String ip = String.valueOf(IpText.toIp("182.248.53.84"));
//                    if (!(ip1.equals(srcIp) && ip2.equals(destIp))
//                            &&  !(ip1.equals(destIp) && ip2.equals(srcIp))) {
//                    if (!ip.equals(srcIp) && !ip.equals(destIp)) {
//                        continue;
//                    }

                    sum.setTs(ts);
                    str = map.get("PktCnt_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setPktCntSum(sum.getPktCntSum() + num);
                    }
                    str = map.get("PktLen_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setPktLenSum(sum.getPktLenSum() + num);
                    }
                    str = map.get("MsgCnt_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setMsgCntSum(sum.getMsgCntSum() + num);
                    }
                    str = map.get("MsgCnt_Server_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setMsgCntSumServer(sum.getMsgCntSumServer() + num);
                    }
                    str = map.get("MsgCnt_Client_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setMsgCntSumClient(sum.getMsgCntSumClient() + num);
                    }
                    str = map.get("ProcTime_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setProcTimeSum(sum.getProcTimeSum() + num);
                    }
                    str = map.get("ProcTime_Server_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setProcTimeServerSum(sum.getProcTimeServerSum() + num);
                    }
                    str = map.get("ProcTime_Client_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setProcTimeClientSum(sum.getProcTimeClientSum() + num);
                    }
                    str = map.get("RespTime_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setRespTimeSum(sum.getRespTimeSum() + num);
                    }
                    str = map.get("TransTime_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setTransTimeSum(sum.getTransTimeSum() + num);
                    }
                    str = map.get("TransTime_Server_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setTransTimeSumServer(sum.getTransTimeSumServer() + num);
                    }
                    str = map.get("TransTime_Client_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setTransTimeSumClient(sum.getTransTimeSumClient() + num);
                    }
                    str = map.get("NetworkDelay_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setNetworkDelaySum(sum.getNetworkDelaySum() + num);
                    }
                    str = map.get("NetworkDelay_Server_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setNetworkDelaySumServer(sum.getNetworkDelaySumServer() + num);
                    }
                    str = map.get("NetworkDelay_Client_Sum");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setNetworkDelaySumClient(sum.getNetworkDelaySumClient() + num);
                    }
                }
                System.out.println(sum);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void compareNta() {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\20160423143000.nta"));
        paths.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160423143000.nta"));
        List<Map<Record, String>> sets = new ArrayList<>();
        for (Path path : paths) {
            try {
                Map<Record, String> set = new HashMap<>();
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    final String[] kvs = line.split("\t");
                    final Map<String, String> map = new HashMap<>();
                    for (String kv : kvs) {
                        if (!kv.contains("=")) {
                            map.put("ts", kv);
                            continue;
                        }
                        String[] k_v = kv.split("=");
                        map.put(k_v[0], k_v[1]);
                    }

                    final String flowSide = map.get("FlowSide");
                    final String srcIp = map.get("SrcIp");
                    final String destIp = map.get("DestIp");
                    final String pktCnt = map.get("PktCnt");
                    if (flowSide == null) {
                        continue;
                    }
                    Record rc = new Record();
                    rc.setFlowSide(flowSide);
                    rc.setSrcIp(srcIp);
                    rc.setDestIp(destIp);
                    set.put(rc, pktCnt);
                }
                sets.add(set);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<Record, String> set0 = sets.get(0);
        Map<Record, String> set1 = sets.get(1);
        for (Map.Entry<Record, String> entry0 : set0.entrySet()) {
            Record r0 = entry0.getKey();
            String pktCnt0 = entry0.getValue();
            if (!set1.containsKey(r0)) {
                System.err.println("Missing " + r0 + " in 2.nta");
            }

            String pktCnt1 = set1.get(r0);
            if (pktCnt0 == null) {
                if (pktCnt1 != null) {
                    System.err.println("Not equal: " + r0);
                }
            } else {
                if (!pktCnt0.equals(pktCnt1)) {
                    System.err.println("Not equal: " + r0);
                }

            }
        }
    }

    @Test
    public void mergePcap() throws IOException {
        Path folder = Paths.get("D:\\e\\5585\\eno\\2016041000\\out");
        final StringBuilder sb = new StringBuilder();
        Files.list(folder).forEach(path -> {
            sb.append(" " + path.toString());
        });
        final String cmd = "mergecap -w D:\\e\\5585\\eno\\merged.pcap" + sb.toString() + " -F pcap";
//        System.out.println(cmd);
        Runtime.getRuntime().exec(cmd);
    }

    @Test
    public void uncompressPcap() throws IOException {
        Path folder = Paths.get("D:\\e\\5585\\eno\\2016041000");
        Files.list(folder).forEach(path -> {
            final String oldName = path.getFileName().toString();
            if (!oldName.endsWith("sz")) {
                return;
            }
            final int index = oldName.indexOf(".");
            final String name = oldName.substring(0, index);
            final Path newPath = path.getParent().resolve("out").resolve(name + ".pcap");
            try {
                Runtime.getRuntime().exec(String.format("java -jar D:\\APP\\netis\\dp_\\dp-tools\\bin\\dp-tools.jar uncompress -i %s -o %s",
                        path.toString(), newPath.toString())).waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void duplicatePcap() throws IOException {
        for (int i = 0; i < 120; i++) {
            Runtime.getRuntime().exec("editcap -t " + 60 * i + " D:/e/collector_x3650/collector_x3650.pcap D:/e/collector_x3650/collector_x3650_"+i+".pcap");
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 120; i++) {
            sb.append(" D:/e/collector_x3650/collector_x3650_").append(i).append(".pcap");
        }
        Runtime.getRuntime().exec("mergecap -w D:/e/collector_x3650/collector_x3650_merged.pcap " + sb.toString());
    }

}
