package com.y3r9.c47.dog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;

import sun.misc.Signal;

/**
 * The class Main
 *
 * @version 1.0
 */
public final class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
/*        Thread thr = new Thread(() -> {
            while (true) {
                System.out.println("ping");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thr.setDaemon(true);
        thr.start();

        Thread thr2 = new Thread(() -> {
            while (true) {
                System.out.println("dang");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thr2.start();

        Set<ByteBuffer> sets = new HashSet<>();
        while (System.currentTimeMillis() > 0) {
            sets.add(ByteBuffer.allocate(1000000000));
        }*/
//        Signal.handle(new Signal("KILL"), signal -> {
//            System.out.println("I was killed!!!");
//
//        });
//        while (System.currentTimeMillis() > 0) {
//            Thread.sleep(1000);
//        }
        final CommandLine cmdLine = new CommandLine("java");
//        cmdLine.addArgument("-agentlib:feagoe");
        cmdLine.addArgument("-jar");
        cmdLine.addArgument("dp/bin/dp-nseelic.jar");
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final ByteArrayOutputStream err = new ByteArrayOutputStream()) {
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            final DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(System.out, System.err));
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            System.out.println(resultHandler.getExitValue());

        }


//        final URL url = Main.class.getResource("dir/");
//        System.out.println("toString(): " + url.toString());
//        System.out.println("getRef(): " + url.getRef());
//        System.out.println("getHost(): " + url.getHost());
//        System.out.println("getPort(): " + url.getPort());
//        System.out.println("getFile(): " + url.getFile());
//        System.out.println("getPath(): " + url.getPath());
//        System.out.println("getAuthority(): " + url.getAuthority());
//        System.out.println("getQuery(): " + url.getQuery());
//        System.out.println("getUserInfo(): " + url.getUserInfo());
//        System.out.println("getProtocol(): " + url.getProtocol());
//        System.out.println("getContent(): " + url.getContent().toString());
/*        Path binFolder = Paths.get(CommonFolder.getRelativePath("projectpath:redis/bin"));

        final CommandLine cmdLine = buildCommandLine();
        final Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(binFolder.toFile());
        executor.setStreamHandler(new PumpStreamHandler(System.out, System.err));
        ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
        executor.setWatchdog(executeWatchdog);
        final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        LOG.info("Starting Redis server with args: " + Arrays.toString(cmdLine.getArguments()));
        executor.execute(cmdLine, resultHandler);
        LOG.info("Redis server started");*/

    }

    public static String getMainClassName()
    {
        for(final Map.Entry<String, String> entry : System.getenv().entrySet())
        {
            if(entry.getKey().startsWith("JAVA_MAIN_CLASS"))
                return entry.getValue();
        }
        throw new IllegalStateException("Cannot determine main class.");
    }
}
