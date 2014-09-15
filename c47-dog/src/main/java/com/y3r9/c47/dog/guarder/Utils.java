package com.y3r9.c47.dog.guarder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-10
 * Time: 下午8:08
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    /**
     * 检查与名字匹配的进程是否存在
     * @param processName
     * @return
     * @throws IOException
     */
    public static boolean isProcessRunnning(String processName) throws IOException {
        if (null == processName || "".equals(processName)) {
            throw new IllegalArgumentException("processName is empty");
        }
        BufferedReader br = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq " + processName + "\"");
            br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }
    }

    /**
     * 检查主机端口是否被监听
     * @param port
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isHostPortListened(int port, int timeout) throws IllegalArgumentException {
        return isPortListened("127.0.0.1", port, timeout);
    }

    /**
     * 检查端口是否被监听
     * @param port
     * @param timeout
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isPortListened(String address, int port, int timeout) throws IllegalArgumentException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("invalid port number");
        }
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(address, port), timeout);
            s.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 执行命令
     * @param cmd
     * @return
     * @throws IOException
     */
    public static Process exeCmd(String cmd) throws IOException {
        return Runtime.getRuntime().exec(cmd);
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(isHostPortListened(3389, 5000));
        exeCmd("cmd");
    }
}
