package com.y3r9.c47.dog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * The class Main
 *
 * @version 1.0
 */
public final class Main {

    public static void main(String[] args) throws IOException {
        final URL url = Main.class.getResource("dir/");
        System.out.println("toString(): " + url.toString());
        System.out.println("getRef(): " + url.getRef());
        System.out.println("getHost(): " + url.getHost());
        System.out.println("getPort(): " + url.getPort());
        System.out.println("getFile(): " + url.getFile());
        System.out.println("getPath(): " + url.getPath());
        System.out.println("getAuthority(): " + url.getAuthority());
        System.out.println("getQuery(): " + url.getQuery());
        System.out.println("getUserInfo(): " + url.getUserInfo());
        System.out.println("getProtocol(): " + url.getProtocol());
        System.out.println("getContent(): " + url.getContent().toString());
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
