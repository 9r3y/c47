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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
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

import com.ibm.as400.access.AS400Text;
import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
import com.y3r9.c47.dog.util.JsonUtils;

import redis.clients.jedis.Jedis;

public class Test {


    public static void main(String[] args) throws DecoderException, XMLStreamException, IOException {
//        final byte[] b = Hex.decodeHex("0e5c4a5b6a0ff1f27af0f67af3f00e5b9e4a430f0e4ae55b7b50485c4a5b6b0f".toCharArray());
//        AS400Text te = new AS400Text(b.length, 1388);
//        System.out.println(te.toObject(b));
//        final AtomicInteger c = new AtomicInteger(0);
//        Files.walkFileTree(Paths.get("D:\\APP\\netis\\dcd\\dcd-parser\\src\\main\\xdl"), new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
//                if (Files.isDirectory(file)) {
//                    return FileVisitResult.CONTINUE;
//                }
//                if ("register.xml".equals(file.getFileName().toString())) {
//                    return FileVisitResult.CONTINUE;
//                }
//                c.incrementAndGet();
//                return FileVisitResult.CONTINUE;
//            }
//        });
//        System.out.println(c);
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
