package com.y3r9.c47.dog.guarder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-10
 * Time: 下午7:20
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final String PROP_FILE_NAME = "conf.properties";
    public static final String PROP_KEY_PROCESS_NAME = "processName";
    public static final String PROP_KEY_CHECK_INTERVAL = "checkInverval";
    public static final String PROP_KEY_PORT = "port";
    public static final String PROP_KEY_PORT_TIMEOUT = "portTimeout";
    public static final String PROP_KEY_CMD = "cmd";

    public static void main(String[] args) {
        Guarder gd = Guarder.getInstance();

        InputStream in = ClassLoader.getSystemResourceAsStream(PROP_FILE_NAME);
        Properties p = new Properties();

        try {
            p.load(in);
        } catch (IOException e) {
            log.warn("read properties file failed", e);
        }

        String processName = p.getProperty(PROP_KEY_PROCESS_NAME);
        if (null != processName && !"".equals(processName)) {
            gd.setProcessName(processName);
        }

        String interval = p.getProperty(PROP_KEY_CHECK_INTERVAL);
        if (null != interval && !"".equals(interval)) {
            try {
                long l = Long.parseLong(interval);
                gd.setChkIntvl(l);
            } catch (NumberFormatException e) {}
        }

        String port = p.getProperty(PROP_KEY_PORT);
        if (null != port && !"".equals(port)) {
            try {
                int n = Integer.parseInt(port);
                gd.setPort(n);
            } catch (NumberFormatException e) {}
        }

        String portTimeout = p.getProperty(PROP_KEY_PORT_TIMEOUT);
        if (null != portTimeout && !"".equals(portTimeout)) {
            try {
                int n = Integer.parseInt(portTimeout);
                gd.setChkPortTimeout(n);
            } catch (NumberFormatException e) {}
        }

        String cmd = p.getProperty(PROP_KEY_CMD);
        if (null != cmd && !"".equals(cmd)) {
            gd.setDoAfterNegativeResultCmd(cmd);
        }

        new Thread(gd, Guarder.class.getName()).start();
    }
}
