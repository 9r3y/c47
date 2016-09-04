package com.y3r9.c47.easy;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.y3r9.c47.dog.init.Initializer;
import com.y3r9.c47.dog.init.InitializerMark;

/**
 * The class Main.
 *
 * @version 1.0
 */
final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Package pkg = InitializerMark.class.getPackage();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String pkgPath = pkg.getName().replace('.', '/');
            Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(pkgPath) : ClassLoader.getSystemResources(pkgPath));
            List<URL> scanPkgs = new ArrayList<>();
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                scanPkgs.add(url);
            }
            for (URL scanPkg : scanPkgs) {
                if (scanPkg.getProtocol().equals("jar")) {
                    // pkgFileName: "file:/path/to/jar/file!/path/to/package".
                    final String pkgFileName = URLDecoder.decode(scanPkg.getFile(), "UTF-8");
                    final String jarFileName = pkgFileName.substring(5, pkgFileName.indexOf("!"));
                    final JarFile jf = new JarFile(jarFileName);
                    final Enumeration<JarEntry> jarEntries = jf.entries();
                    while (jarEntries.hasMoreElements()) {
                        final String classFileName = jarEntries.nextElement().getName();
                        if (classFileName.startsWith(pkgPath)
                                && classFileName.length() > pkgPath.length() + 5) {
                            final String className = pkg.getName() + '.'
                                    + classFileName.substring(pkgPath.length() + 1, classFileName.lastIndexOf('.'));
                            try {
                                Class cls = Class.forName(className);
                                if (cls.getAnnotation(InitializerMark.class) != null) {
                                    System.out.println("Found " + cls);
                                }
                            } catch (ClassNotFoundException e) {
                                LOG.error(e.toString(), e);
                            }
                        }
                    }
                } else {
                    final URI uri = scanPkg.toURI();
                    final File folder = new File(uri.getPath());
                    final File[] classFiles = folder.listFiles();
                    if (classFiles == null) {
                        continue;
                    }
                    for (File classFile : classFiles) {
                        final String classFileName = classFile.getName();
                        final String className = pkg.getName() + '.'
                                + classFileName.substring(0, classFileName.lastIndexOf('.'));
                        try {
                            Class cls = Class.forName(className);
                            if (cls.getAnnotation(InitializerMark.class) != null) {
                                System.out.println("Found " + cls);
                            }
                        } catch (ClassNotFoundException e) {
                            LOG.error(e.toString(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(pkg.getName());
        System.out.println(System.currentTimeMillis() - start + "        342555555555555555555555555555555555555555");
    }

}
