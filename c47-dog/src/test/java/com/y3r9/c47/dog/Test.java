package com.y3r9.c47.dog;

import cn.com.netis.util.JarFolder;
import com.y3r9.c47.dog.demo.serviceprovider.dictionary.spi.*;
import com.y3r9.c47.dog.demo.serviceprovider.dictionary.spi.Dictionary;
import org.apache.commons.lang.text.StrBuilder;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.Security;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class Test {

    public static void main(String[] args) {
/*        String[] strs = {"f", "a", "b", "d", "e", "c"};
        InsertionX.sort(strs);
        for (String elm : strs) {
            System.out.print(elm + ",");
        }*/
        int[] ar = {0, 4, 6, 2, 8, 4, 9, 0, 1, 3, 7};
        //selectSort(ar);
        quickSort(ar, 0, ar.length - 1);
        for (int elm : ar) {
            System.out.print(elm + ",");
        }

        String s = "Jul 18 15:51:45";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH);
        try {
            System.out.println(sdf.parse(s).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("------------------------");
        //DecimalFormat df1 = new DecimalFormat("####");
        //DecimalFormat df1 = new DecimalFormat("####");
        //df1.setMinimumFractionDigits(4);
        //junit.textui.TestRunner.run(new CalculatorTest());
    }

    /**
     * 获得类所在的jar包或class文件路径
     * @param cls 类的Class对象
     * @return jar包或class文件路径
     */
    public static URL getClassLocation(final Class cls) {
        if (cls == null)throw new IllegalArgumentException("null input: cls");
        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();
        // java.lang.Class contract does not specify if 'pd' can ever be null;
        // it is not the case for Sun's implementations, but guard against null
        // just in case:
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            // 'cs' can be null depending on the classloader behavior:
            if (cs != null) result = cs.getLocation();
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
                    }
                    catch (MalformedURLException ignore) {}
                }
            }
        }
        if (result == null) {
            // Try to find 'cls' definition as a resource; this is not
            // document．d to be legal, but Sun's implementations seem to         //allow this:
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
        if (lo >= hi) return;
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
