package com.y3r9.c47.dog.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.sun.org.apache.regexp.internal.RE;
import com.y3r9.c47.dog.IpText;

import scala.Int;

/**
 * The class Scripts.
 *
 * @version 1.0
 */
public class Scripts {

    @Test
    public void checkServerAttach() throws ParseException, IOException {
        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("D:\\e\\serversyn\\test.nta"));
        paths.add(Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\target\\output\\ATest\\A.nts"));
        for (Path path : paths) {
            List<NtaRecord> ntas = readNtas(path);
            for (NtaRecord nta : ntas) {
                if (nta.getSrcPort() == 443 && nta.getAttachFailSum() > 0) {
                    System.out.println(nta.getLine());
                    return;
                }
            }
        }
    }

    @Test
    public void topNta() throws IOException {
        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("D:/e/loadnta/20161116150000_1.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp-3.11.7\\dp-engine\\src\\test\\java\\cn\\com\\netis\\dp\\engine\\regression\\debug\\NtaeOnly\\expected\\multiple\\20120625080000_60s.ntae"));
        paths.add(Paths.get("D:\\APP\\netis\\dp-3.11.7\\dp-engine\\target\\output\\NtaeOnlyTest\\20120625080000_60s.ntae"));
        long out = 0;
        long in = 0;
        for (Path path : paths) {
            List<NtaRecord> ntas = readNtas(path);
            for (NtaRecord nta : ntas) {
                if (nta.getSrcIp() == -1263971318) {
                    out += nta.getPktCntSum();
                } else if (nta.getDestIp() == -1263971318) {
                    in += nta.getPktCntSum();
                }
            }
        }
        System.out.println(out + in);
//        System.out.println(in);
    }

    @Test
    public void spvsyn() throws IOException {
//        Path nts = Paths.get("D:\\APP\\netis\\dp_\\dp-engine\\target\\output\\ATest\\nts\\eth2\\20160815\\2016081517\\20160815171800_0@1m.nts");
//        Path nta = Paths.get("D:\\APP\\netis\\dp_\\dp-engine\\target\\output\\ATest\\nta\\eth2\\20160815\\2016081517\\1m\\20160815171800_0.nta");
        Path nts = Paths.get("D:\\APP\\netis\\dp_\\dp-engine\\target\\output\\ATest\\default_0_20160826153000.nts");
        List<String> lines = Files.readAllLines(nts);

        class Sum {
            private int synSum;
            private int attachSuccSum;
            private int attachFailSum;
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("Sum{");
                sb.append("synSum=").append(synSum);
                sb.append(", attachSuccSum=").append(attachSuccSum);
                sb.append(", attachFailSum=").append(attachFailSum);
                sb.append('}');
                return sb.toString();
            }
        }
        Sum[] sums = new Sum[]{new Sum(), new Sum()};
        int ln = 1;
        for (String line : lines) {
            final String[] kvs = line.split("\t");
            final Map<String, String> map = new HashMap<>();
            for (String kv : kvs) {
                if (!kv.contains("=")) {
                    map.put("ts", kv);
                    continue;
                }
                String[] k_v = kv.split("=");
                if (k_v.length > 1) {
                    map.put(k_v[0], k_v[1]);
                }
            }
            final String spvdir = map.get("SpvDir");
            if (StringUtils.isNotEmpty(spvdir)) {
                int dir = Integer.parseInt(spvdir);
                Sum sum = sums[dir];
                String value;
                int oldAttachSuccSum = sum.attachSuccSum;
                int oldAttachFailSum = sum.attachFailSum;

                value = map.get("Syn");
                if (StringUtils.isNotEmpty(value)) {
                    sum.synSum += Integer.parseInt(value);
                }
                value = map.get("AttachSucc");
                if (StringUtils.isNotEmpty(value)) {
                    sum.attachSuccSum += Integer.parseInt(value);
                }
                value = map.get("AttachFail");
                if (StringUtils.isNotEmpty(value)) {
                    sum.attachFailSum += Integer.parseInt(value);
                }
/*                value = map.get("Syn_Sum");
                if (StringUtils.isNotEmpty(value)) {
                    sum.synSum += Integer.parseInt(value);
                }
                value = map.get("AttachSucc_Sum");
                if (StringUtils.isNotEmpty(value)) {
                    sum.attachSuccSum += Integer.parseInt(value);
                }
                value = map.get("AttachFail_Sum");
                if (StringUtils.isNotEmpty(value)) {
                    sum.attachFailSum += Integer.parseInt(value);
                }*/
//                if (sum.attachSuccSum != sum.synSum) {
//                    System.out.println(dir + " " + ln);// 194
//                }
                if (dir == 0 && sum.attachFailSum > oldAttachFailSum) {
                    System.out.println(dir + " " + ln);// 873
                }
            }
            ln++;
        }
        System.out.println(sums[0]);
        System.out.println(sums[1]);
    }

    @Test
    public void compareConn() throws IOException {
        List<Path> ntss = new ArrayList<>();
        ntss.add(Paths.get("D:\\e\\loadnts2\\20160705120000_0@1m.nts"));
//        ntss.add(Paths.get("D:\\e\\loadnts2\\20160706T105652F692694Rixo\\20160705120000_0.nts"));
//        ntss.add(Paths.get("D:\\e\\loadnts2\\20160706T105652F692694Rixo\\20160705120000_1.nts"));
        Set<IpPortPair> ntsSet = new HashSet<>();
        for (Path path : ntss) {
            List<String> lines = Files.readAllLines(path);
            int i = 1;
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
                IpPortPair ipPortPair = new IpPortPair();
                ipPortPair.setSrcIp(map.get("SrcIp"));
                ipPortPair.setDestIp(map.get("DestIp"));
                ipPortPair.setSrcPort(map.get("SrcPort"));
                ipPortPair.setDestPort(map.get("DestPort"));
                ipPortPair.setPath(path);
                ipPortPair.setLine(i++);
                ntsSet.add(ipPortPair);
            }
        }

        System.out.println(ntsSet.size());

        List<Path> ntas = new ArrayList<>();
        ntas.add(Paths.get("D:\\APP\\netis\\dp_\\dp-adapter\\target\\output\\BTest\\20160705120000_0@1m.nts.cpt.nta"));
//        ntas.add(Paths.get("D:\\e\\loadnts2\\20160706T105458F634251Rdtx\\nta\\20160705120000_0.nta"));
//        ntas.add(Paths.get("D:\\e\\loadnts2\\20160706T105458F634251Rdtx\\nta\\20160705120000_1.nta"));
        Set<IpPortPair> ntaSet = new HashSet<>();
        for (Path path : ntas) {
            List<String> lines = Files.readAllLines(path);
            int i = 1;
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
                IpPortPair ipPortPair = new IpPortPair();
                ipPortPair.setSrcIp(map.get("SrcIp"));
                ipPortPair.setDestIp(map.get("DestIp"));
                ipPortPair.setSrcPort(map.get("SrcPort"));
                ipPortPair.setDestPort(map.get("DestPort"));
                ipPortPair.setPath(path);
                ipPortPair.setLine(i++);
                ntaSet.add(ipPortPair);
            }
        }
        System.out.println(ntaSet.size());

        for (IpPortPair nts : ntsSet) {
            if (!ntaSet.contains(nts)) {
                System.out.println(nts);
            }
        }
    }

    @Test
    public void calcNtaConn() throws IOException {
        List<Path> ntaFiles = new ArrayList<>();
//        ntaFiles.add(Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\target\\output\\ATest\\GRE_TCP.nta"));
//        ntaFiles.add(Paths.get("D:\\APP\\netis\\dp_\\dp-engine\\target\\output\\ATest\\ixia_50cc_1w7c_4_20160919140314.nta"));
//        ntaFiles.add(Paths.get("D:\\APP\\netis\\dp_\\dp-engine\\target\\output\\ATest\\ixa_200cc_1w7c_0_20160919135000.nta"));
        ntaFiles.add(Paths.get("D:/e/machine/20160919171600_1.nta"));
        Map<NtaRecord, NtaRecord> ntaMap = new HashMap<>();
        for (Path ntaFile : ntaFiles) {
            List<String> lines = Files.readAllLines(ntaFile);
            int i = 1;
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
                NtaRecord nta = new NtaRecord();
                nta.setSrcIp(getInt(map, "SrcIp"));
                nta.setDestIp(getInt(map, "DestIp"));
                nta.setSrcPort(getInt(map, "SrcPort"));
                nta.setDestPort(getInt(map, "DestPort"));
                nta.setFlowSide(getInt(map, "FlowSide"));
                nta.setConnCount(getInt(map, "ConnCount60s"));
                nta.setPath(ntaFile);
                nta.setLine(i++);

                NtaRecord exsist = ntaMap.get(nta);
                if (exsist == null) {
                    ntaMap.put(nta, nta);
                } else {
                    exsist.merge(nta);
                }
            }
        }
        int connSum = 0;
        for (NtaRecord nta : ntaMap.values()) {
            if (nta.getFlowSide() == 0) {
                connSum += nta.getConnCount();
            }
        }
        System.out.println(connSum);
    }

    private int getInt(final Map<String, String> map, final String key) {
        String str = map.get(key);
        if (StringUtils.isNotEmpty(str)) {
            return Integer.parseInt(str);
        }
        return 0;
    }

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
//        paths.add(Paths.get("D:\\e\\xflowbyz3\\20160520121800_0.nflow"));
//        paths.add(Paths.get("D:\\e\\transtime\\20160526171900_0.nflow"));
        paths.add(Paths.get("D:\\e\\transtime\\20160526172000_0.nflow"));
        for (Path path : paths) {

            try {
                final String cmd = String.format("python D:\\e\\correctness\\disp-nflow\\disp-nflow.pyc %s --show-all-fields --load-template %s",
//                        path.toString(), "D:\\e\\xflowbzq\\nflow.template");
//                          path.toString(), "D:\\e\\xflowbyz2\\v9.template");
//                          path.toString(), "D:\\e\\nflow\\v9\\nflow.template");
                            path.toString(), "D:\\e\\transtime\\nflow.template");
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
//                    str = map.get("transactionCountDelta");
                    str = map.get("RESERVED42041");
                    if (str != null) {
                        final long num = Long.parseLong(str);
                        sum.setTransactionCountDelta(sum.getTransactionCountDelta() + num);
                    }
//                    str = map.get("sumTotalRespTime");
                    str = map.get("RESERVED42077");
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
    public void sumKeyValueNtr() throws IOException {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\target\\output\\NtsOnlyTest\\TCP_SLICED.nts"));
        for (Path path : paths) {
            List<NtrRecord> ntrs = readNtrs(path);
            long pktLenSum = 0;
            long pktCntSum = 0;
            for (NtrRecord ntr : ntrs) {
                pktLenSum += ntr.getPktLen();
                pktCntSum += ntr.getPktCnt();
            }
            System.out.println(pktLenSum + " " + pktCntSum + " " +  ntrs.size());
        }
    }

    @Test
    public void compareNtrIpReassemble() throws IOException {
        List<Path> paths = new ArrayList<>();
        Path notReassemble = Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\target\\output\\notReassemble.ntr");
        Path reassemble = Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\target\\output\\reassemble.ntr");
        List<NtrRecord> notReassembleNtrs = readNtrs(notReassemble);
        List<NtrRecord> reassembleNtrs = readNtrs(reassemble);
        for (NtrRecord notAsm : notReassembleNtrs) {
            if (notAsm.getPktLen() == 1518) {
                boolean found = false;
                for (NtrRecord asm : reassembleNtrs) {
                    if (asm.getIpId() == notAsm.getIpId() && asm.getPktLen() == notAsm.getPktLen()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println(notAsm.getLine());
                }
            }

        }
    }

    private List<NtrRecord> readNtrs(final Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<NtrRecord> result = new ArrayList<>();
        int lineNum = 1;
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
            NtrRecord ntr = new NtrRecord();
            ntr.setLine(lineNum++);
            String str;
            str = map.get("SrcIp");
            if (str != null) {
                ntr.setSrcIp(Integer.parseInt(str));
            }
            str = map.get("DestIp");
            if (str != null) {
                ntr.setDestIp(Integer.parseInt(str));
            }
            str = map.get("SrcPort");
            if (str != null) {
                ntr.setSrcPort(Integer.parseInt(str));
            }
            str = map.get("DestPort");
            if (str != null) {
                ntr.setDestPort(Integer.parseInt(str));
            }
            str = map.get("IpId");
            if (str != null) {
                ntr.setIpId(Integer.parseInt(str));
            }
            str = map.get("FlowSide");
            if (str != null) {
                ntr.setFlowSide(Integer.parseInt(str));
            }
            str = map.get("PktLen");
            if (str != null) {
                ntr.setPktLen(Integer.parseInt(str));
            }
            str = map.get("PktCnt");
            if (str != null) {
                ntr.setPktCnt(Integer.parseInt(str));
            }
            result.add(ntr);
        }
        return result;
    }

    @Test
    public void sumKeyValueNta() throws ParseException, IOException {
        List<Path> paths = new ArrayList<>();
//        paths.add(Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\target\\output\\SiteAppTest\\SITE_APP.nta"));
//        paths.add(Paths.get("D:\\APP\\netis\\dp3.11\\dp-engine\\src\\test\\java\\cn\\com\\netis\\dp\\engine\\regression\\debug\\SiteApp\\expected\\appDirWithOutAddress\\SITE_APP.nta"));
        paths.add(Paths.get("D:/APP/netis/dp3.11/dp-engine/target/output/ATest/TCP_SLICED.nta"));
        final SimpleDateFormat tsSdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        long leftTs = tsSdf.parse("2016-05-12 15:04:00").getTime();
        long rightTs = tsSdf.parse("2016-05-12 15:05:00").getTime();
        for (Path path : paths) {
            List<NtaRecord> ntas = readNtas(path);

            NtaSum sum = new NtaSum(path);
            for (NtaRecord nta : ntas) {
                sum.setPktLenSum(sum.getPktLenSum() + nta.getPktLenSum());
                sum.setPktCntSum(sum.getPktCntSum() + nta.getPktCntSum());
            }
            System.out.println(sum.getPktLenSum() + " " +  sum.getPktCntSum());
        }
    }

    private List<NtaRecord> readNtas(final Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<NtaRecord> results = new ArrayList<>();
        int lineNum = 0;
        for (String line : lines) {
            final String[] kvs = line.split("\t");
            final Map<String, String> map = new HashMap<>();
            for (String kv : kvs) {
                if (!kv.contains("=")) {
//                            map.put("ts", kv.substring(1, 20));
                    continue;
                }
                String[] k_v = kv.split("=");
                map.put(k_v[0], k_v[1].trim());
            }
            String str;
//                    str = map.get("ts");
//                    if (str == null) {
//                        continue;
//                    }
//                    long ts = tsSdf.parse(str).getTime();

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

//                    sum.setTs(ts);
            NtaRecord nta = new NtaRecord();
            nta.setPath(path);
            nta.setLine(lineNum++);
            str = map.get("SrcIp");
            if (str != null) {
                nta.setSrcIp(Integer.parseInt(str));
            }
            str = map.get("SrcPort");
            if (str != null) {
                nta.setSrcPort(Integer.parseInt(str));
            }
            str = map.get("DestIp");
            if (str != null) {
                nta.setDestIp(Integer.parseInt(str));
            }
            str = map.get("DestPort");
            if (str != null) {
                nta.setDestPort(Integer.parseInt(str));
            }
            str = map.get("PktCnt_Sum");
            if (str != null) {
                nta.setPktCntSum(Long.parseLong(str));
            }
            str = map.get("PktLen_Sum");
            if (str != null) {
                nta.setPktLenSum(Long.parseLong(str));
            }
            str = map.get("MsgCnt_Sum");
            if (str != null) {
                nta.setMsgCntSum(Long.parseLong(str));
            }
            str = map.get("MsgCnt_Server_Sum");
            if (str != null) {
                nta.setMsgCntSumServer(Long.parseLong(str));
            }
            str = map.get("MsgCnt_Client_Sum");
            if (str != null) {
                nta.setMsgCntSumClient(Long.parseLong(str));
            }
            str = map.get("ProcTime_Sum");
            if (str != null) {
                nta.setProcTimeSum(Long.parseLong(str));
            }
            str = map.get("ProcTime_Server_Sum");
            if (str != null) {
                nta.setProcTimeServerSum(Long.parseLong(str));
            }
            str = map.get("ProcTime_Client_Sum");
            if (str != null) {
                nta.setProcTimeClientSum(Long.parseLong(str));
            }
            str = map.get("RespTime_Sum");
            if (str != null) {
                nta.setRespTimeSum(Long.parseLong(str));
            }
            str = map.get("TransTime_Sum");
            if (str != null) {
                nta.setTransTimeSum(Long.parseLong(str));
            }
            str = map.get("TransTime_Server_Sum");
            if (str != null) {
                nta.setTransTimeSumServer(Long.parseLong(str));
            }
            str = map.get("TransTime_Client_Sum");
            if (str != null) {
                nta.setTransTimeSumClient(Long.parseLong(str));
            }
            str = map.get("NetworkDelay_Sum");
            if (str != null) {
                nta.setNetworkDelaySum(Long.parseLong(str));
            }
            str = map.get("NetworkDelay_Server_Sum");
            if (str != null) {
                nta.setNetworkDelaySumServer(Long.parseLong(str));
            }
            str = map.get("NetworkDelay_Client_Sum");
            if (str != null) {
                nta.setNetworkDelaySumClient(Long.parseLong(str));
            }
            str = map.get("AttachSucc_Sum");
            if (str != null) {
                nta.setAttachSuccessSum(Long.parseLong(str));
            }
            str = map.get("AttachFail_Sum");
            if (str != null) {
                nta.setAttachFailSum(Long.parseLong(str));
            }
            results.add(nta);
        }
        return results;
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

    @Test
    public void copyFolder() throws IOException {
        Path from = Paths.get("D:\\APP\\netis\\dcd\\dcd-parser\\src\\test\\java\\cn\\com\\netis\\dcd\\parser\\regression");
        Path to = Paths.get("D:\\TMP\\aaa");

        Files.walkFileTree(from, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Path rel = file.subpath(from.getNameCount(), file.getNameCount());
                Path dest = to.resolve(rel);
                if (Files.isRegularFile(file)) {
                    if (file.getFileName().toString().endsWith("conf.xml")) {
                        if (!Files.exists(dest.getParent())) {
                            Files.createDirectories(dest.getParent());
                        }
                        Files.copy(file, dest);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
