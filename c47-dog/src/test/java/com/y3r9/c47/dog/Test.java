package com.y3r9.c47.dog;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.HexDump;
import org.apache.commons.net.ntp.TimeStamp;

import com.google.common.collect.MinMaxPriorityQueue;
import com.ibm.as400.access.AS400Text;
import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
import com.y3r9.c47.dog.swj.model.collection.BoundedPriorityQueue;
import com.y3r9.c47.dog.swj.value.Power2Utils;
import com.y3r9.c47.dog.util.JsonUtils;

import cn.com.netis.dp.commons.common.packet.FlowSide;
import redis.clients.jedis.Jedis;
import scala.tools.nsc.Global;
import tachyon.util.io.FileUtils;

public class Test {

    static class El {
        @Override
        public int hashCode() {
            return -324534;
        }
    }

    public static void main(String[] args) throws DecoderException, XMLStreamException, IOException {
//        MinMaxPriorityQueue<Long> queue = MinMaxPriorityQueue.maximumSize(50).create();
/*        BoundedPriorityQueue<Long> queue = new BoundedPriorityQueue<>(50, (o1, o2) -> {
            return o1.compareTo(o2);
        });
        Random random = new Random();
        final long[] data = new long[10000000];
        for (int i = 0; i < 10000000; i++) {
            data[i] = random.nextLong();
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < data.length; i++) {
            queue.add(new Long(4));
        }
        System.out.println(System.currentTimeMillis() - start);
        final StringBuilder sb = new StringBuilder(100);
        long last = Long.MIN_VALUE;
        while (true) {
            Long l = queue.poll();
            if (l == null) {
                break;
            } else if (l < last) {
                throw new RuntimeException(l + " < " + last);
            }
            last = l;
            sb.append(' ').append(l);
        }
        System.out.println(sb.toString());*/
        Calendar cld = Calendar.getInstance();
        cld.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Path monitor = Paths.get("D:/TMP/monitor");
        for (int i = 0; i < 100; i++) {
            cld.add(Calendar.MINUTE, 1);
            final String fileName = sdf.format(cld.getTime()) + ".pcap";
            FileUtils.createFile(monitor.resolve(fileName).toString());
        }
    }

    public static void testDecompress() throws IOException {
        byte[] data = org.bouncycastle.util.encoders.Hex.decode("0000054F00558FFA010000000000" +
                "007E00000000000000E04040FF000F00" +
                "F0FFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFFFFFFFFFFF7F494D49" +
                "582E312E30054F043300015472406465" +
                "58416C6C004D5343406C6C5573657273" +
                "0001569798FB8C0004490002CC41903A" +
                "A7CA7400879B6E532B6805002ACA0A40" +
                "5A700333373D32303136303831373034" +
                "32303030303030310131313D33343934" +
                "36323437313435303533313030330135" +
                "343D310133313D302E30303030303030" +
                "300131303038373D302E303030303030" +
                "0135393D300139393D302E3030303030" +
                "303030013135303D460133393D320135" +
                "33373D310131303130353D3001313031" +
                "37363D340131373D4342543230313630" +
                "3831373030303030310131303031393D" +
                "310131303331373D310134383D313230" +
                "3230310132323D3130310134343D3939" +
                "2E32323030303030300133323D313030" +
                "30303030302E3030303030300136333D" +
                "310137353D3230313630383137013630" +
                "3D32303136303831372D31363A32333A" +
                "32302E3339310131303331383D31363A" +
                "32333A32302E333931013233323D3101" +
                "3233333D5969656C6432013233343D34" +
                "2E303039323438393331343334013931" +
                "393D300135383D2D0131303032323D4E" +
                "013435333D32013434383D3130303030" +
                "30333131303030303030313031303031" +
                "013435323D313139013830323D313001" +
                "3532333D6C69756A6961303101383033" +
                "3D32013532333DE58898E4BDB3013830" +
                "333D313031013532333DE58898E4BDB3" +
                "013830333D313236013532333D424348" +
                "4F013830333D313235013532333DE4B8" +
                "ADE59BBDE993B6E8A18C013830333D31" +
                "3234013532333DE4B8ADE59BBDE993B6" +
                "E8A18CE680BBE8A18C013830333D3131" +
                "30013532333D31313034303033393301" +
                "3830333D3135013532333D3001383033" +
                "3D313233013532333D2D013830333D32" +
                "3233013532333D413030303330303030" +
                "3031013830333D313135013434383D31" +
                "30303030303231313030303030303130" +
                "31303031013435323D31323001383032" +
                "3D39013532333D6C696E736875013830" +
                "333D32013532333DE69E97E888920138" +
                "30333D313031013532333DE69E97E888" +
                "92013830333D313236013532333D4142" +
                "4349013830333D313235013532333DE5" +
                "869CE4B89AE993B6E8A18C013830333D" +
                "313234013532333DE4B8ADE59BBDE586" +
                "9CE4B89AE993B6E8A18CE8B584E98791" +
                "E6B885E7AE97E4B8ADE5BF8301383033" +
                "3D313130013532333D31313034303033" +
                "3839013830333D3135013532333D3001" +
                "3830" +
                "333D313233013532333D4130" +
                "303032303030303031013830333D3131" +
                "350131303330303D30013135393D322E" +
                "32353130393239300131303030323D32" +
                "32353130392E32383936313701313033" +
                "31323D393932323030302E3030303030" +
                "300131303034383D3130312E34373130" +
                "393239300136343D3230313630383137" +
                "013131393D31303134373130392E3239" +
                "303030300131303436333D3001313039" +
                "36343D310131303D30393901");
        ByteBuffer input = ByteBuffer.wrap(data);
        ByteBuffer out = decompressTwoByte(input);
        byte[] clear = new byte[out.remaining()];
        out.get(clear);
        HexDump.dump(clear, 0, System.out, 0);
    }

    /**
     * Decompress two byte byte buffer.
     *
     * @param input the input
     * @return the byte buffer
     */
    private static ByteBuffer decompressTwoByte(final ByteBuffer input) {
        final int outSize = input.getInt();
        final ByteBuffer result = ByteBuffer.allocate(outSize + 20);
        final int bitStreamLen = UnsignedValue.toUshort(input.getShort());
        final int bitStreamStartPos = input.position();
        input.position(bitStreamStartPos + bitStreamLen);

        final short empty = 0;
        int i = 0;
        for (int inCount = 0; inCount < bitStreamLen; inCount++) {
            result.putShort((input.get(bitStreamStartPos + inCount) & 1) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 2) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 4) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 8) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 16) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 32) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 64) > 0 ? input.getShort() : empty);
            result.putShort((input.get(bitStreamStartPos + inCount) & 128) > 0 ? input.getShort() : empty);
        }

        if ((outSize & 1) > 0) {
            result.put(outSize - 1, input.get());
        }
        result.flip();
        return result;
    }

    public static byte[] decompress(final Inflater infater, final byte[] data) {
        byte[] output = new byte[0];

//        final Inflater decompresser = new Inflater();
//        decompresser.reset();
        infater.setInput(data);

        final ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            final byte[] buf = new byte[1024];
            while (!infater.finished()) {
                final int i = infater.inflate(buf);
                if (i == 0) {
                    if (infater.needsInput()) {
                        break;
                    }
                }
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        decompresser.end();
        return output;
    }

    private void testWatchFolder() throws IOException {
        final Path path = Paths.get("D:/TMP");
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while (true) {
                final WatchKey key = watchService.take();
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    final WatchEvent.Kind<?> kind = watchEvent.kind();

                    @SuppressWarnings("unchecked")
                    final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                    final Path filename = watchEventPath.context();
                    System.out.println(kind.toString() + " " + filename);
                }
                if (!key.reset()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void json() {
        String str = "{\"value\":\"forwarder:resource=alerts&output=syslog\",\"merge\":\"first\"}";
        str = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
        System.out.println(str);
        Map map = JsonUtils.fromJson(new String(Base64.getDecoder().decode(str.getBytes()),
                StandardCharsets.UTF_8), Map.class);
        System.out.println(map);
    }

    private static void testBrcbDES() throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IOException, InvalidKeySpecException, DecoderException {
        final byte[] _$2 = new byte[] { (byte) 47, (byte) 1, (byte) 34, (byte) 52, (byte) 87,
                (byte) 49, (byte) 83, (byte) 35 };

        String key = "f31120800015510912";
        SecretKeySpec sk = new SecretKeySpec(key.getBytes(), "DES");
        byte[] var1 = new byte[] { (byte) 97, (byte) 100, (byte) 85, (byte) 115, (byte) 37,
                (byte) 94, (byte) 38, (byte) 83 };

        byte[] var4 = new byte[8];
        for (int i = 0; i < _$2.length; i++) {
            var4[i] = (byte) (_$2[i] ^ var1[i]);
        }

        SecureRandom var3 = new SecureRandom();
        DESKeySpec var5 = new DESKeySpec(var4);
        SecretKey var7 = SecretKeyFactory.getInstance("DES").generateSecret(var5);
        Cipher var8;
        (var8 = Cipher.getInstance("DES")).init(2, var7, var3);
        byte[] initKey = Files.readAllBytes(Paths.get("E:\\TestMsgUsingCsiiJars\\key\\initKey"));
        byte[] lastKey = var8.update(initKey, 0, 32);
        System.out.println("InitKey(24 byte): " + Hex.encodeHexString(lastKey));
        lastKey = Hex.decodeHex("cb042d9d9583361a".toCharArray());

        byte[] currentKey = Hex.decodeHex("faac1938974792b8".toCharArray());
        currentKey = decryptNoPadding(currentKey, lastKey);
        System.out.println("CurrentKey: " + Hex.encodeHexString(currentKey));

        // byte[] commonKey = Hex.decodeHex("86ebc13d0a103ab4".toCharArray());
        byte[] commonKey = Hex.decodeHex("78556a946f7e6460".toCharArray());
        byte[] sessionKey = decryptNoPadding(commonKey, currentKey);
        System.out.println("SessionKey: " + Hex.encodeHexString(sessionKey));

        String data = "02 00 00 00 60 02 00 00  00 00 f8 24 5d 08 ef 9a" +
                "53 62 e0 e0 36 02 f2 20  ff 55 a8 97 17 85 98 b6" +
                "0e 58 0c 95 79 a0 9b ac  70 24 9c 5d 0a fb d7 45" +
                "bd 29 9c 23 86 71 26 25  fe f6 95 f8 16 ee ff 60" +
                "5b 02 60 aa fc 79 b8 b0  cf 0a 19 aa fc 79 b8 b0" +
                "cf 0a 19 0f d3 b1 08 ac  7c c4 89 c7 e0 5f 86 bb" +
                "17 ad 49 01 0f";
        // String data = "02 00 00 00 82 02 00 00  00 00 f3 f4 57 75 22 33" +
        // "fe ac ee 13 4d 8f 7d b5  f0 51 40 f2 de 7b 09 e2" +
        // "ba 9a 28 ff 89 a9 b4 c1  62 5e 7d e2 4c 90 33 54" +
        // "31 55 3a c0 b1 a6 03 2d  02 55 53 79 76 78 74 40" +
        // "23 41 58 c3 80 97 73 11  93 12 a3 c3 80 97 73 11" +
        // "93 12 a3 cc 6d 53 8f d6  31 23 d7 c3 80 97 73 11" +
        // "93 12 a3 a7 23 35 d5 88  cd 2e a7 e9 a1 0c d1 36" +
        // "71 af c3 75 00 0f f2 9c  3c 12 d6 c3 80 97 73 11" +
        // "93 12 a3 40 40 40 40";
        byte[] buffer = Hex.decodeHex(data.replace(" ", "").toCharArray());

        byte[] buffer2 = new byte[80];
        System.arraycopy(buffer, 11, buffer2, 0, buffer2.length);
        byte[] real = decryptNoPadding(buffer2, sessionKey);
        // HexDump.dump(real, 0, System.out, 1);
        System.out.println("Data: " + new String(real, Charset.forName("IBM500")));
    }

    public static byte[] decryptNoPadding(byte[] currentKey, byte[] lastKey) {
        SecretKeySpec var6 = new SecretKeySpec(lastKey, "DES");

        try {
            Cipher var7 = Cipher.getInstance("DES/ECB/NoPadding");
            var7.init(2, var6);
            return var7.doFinal(currentKey, 0, currentKey.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得类所在的jar包或class文件路径
     * 
     * @param cls 类的Class对象
     * @return jar包或class文件路径
     */
    public static URL getClassLocation(final Class cls) {
        if (cls == null)
            throw new IllegalArgumentException("null input: cls");
        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();
        // java.lang.Class contract does not specify if 'pd' can ever be null;
        // it is not the case for Sun's implementations, but guard against null
        // just in case:
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            // 'cs' can be null depending on the classloader behavior:
            if (cs != null)
                result = cs.getLocation();
            if (result != null) {
                // Convert a code source location into a full class file location
                // for some common cases:
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") ||
                                result.toExternalForm().endsWith(".zip"))
                            result = new URL("jar:".concat(result.toExternalForm())
                                    .concat("!/").concat(clsAsResource));
                        else if (new File(result.getFile()).isDirectory())
                            result = new URL(result, clsAsResource);
                    } catch (MalformedURLException ignore) {
                    }
                }
            }
        }
        if (result == null) {
            // Try to find 'cls' definition as a resource; this is not
            // document．d to be legal, but Sun's implementations seem to //allow this:
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ?
                    clsLoader.getResource(clsAsResource) :
                    ClassLoader.getSystemResource(clsAsResource);
        }
        return result;
    }

    public static void bubbleSort(int[] array) {
        for (int i = 0, lenI = array.length - 1; i < lenI; i++) {
            boolean done = true;
            for (int j = array.length - 1; j > i; j--) {
                if (array[j] < array[j - 1]) {
                    exch(array, j, j - 1);
                    done = false;
                }
            }
            if (done) {
                break;
            }
        }
    }

    public static void selectSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[i]) {
                    exch(array, j, i);
                }
            }
        }
    }

    public static void insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int v = array[i];
            int j = i;
            while (j > 0 && v < array[j - 1]) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = v;
        }
    }

    public static void shellSort(int[] array) {
        int h = 1;
        int N = array.length;
        while (h < N) {
            h = 3 * h + 1;// 1, 4, 13, 40, 121, 364, 1093, ...
        }
        while (h > 0) {// 希尔排序就是在插入排序中加一个外循环将h按照递增序列递减
            for (int i = h; i < N; i += h) {// 插入排序成h有序数组，如果h为1就是相当于完成一一次普通插入排序
                int v = array[i];
                int j = i;
                while (j > 0 && v < array[j - h]) {
                    array[j] = array[j - h];
                    j -= h;
                }
                array[j] = v;
            }
            h = h / 3;
        }
    }

    public static void quickSort(int[] array, int lo, int hi) {
        if (lo >= hi)
            return;
        int lt = lo;
        int gt = hi;
        int i = lo;
        int v = array[lo];
        while (i <= gt) {
            int cmp = array[i] - v;
            if (cmp < 0) {
                exch(array, lt++, i++);
            } else if (cmp > 0) {
                exch(array, i, gt--);
            } else {
                i++;
            }
        }
        quickSort(array, lo, lt - 1);
        quickSort(array, gt + 1, hi);
    }

    private static void exch(int[] array, int n, int m) {
        int v = array[n];
        array[n] = array[m];
        array[m] = v;
    }

}
