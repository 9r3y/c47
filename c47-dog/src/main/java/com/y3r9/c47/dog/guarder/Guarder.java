package com.y3r9.c47.dog.guarder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-10
 * Time: 下午7:23
 * To change this template use File | Settings | File Templates.
 */
public class Guarder implements Runnable{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static Guarder singleton = null;

    private String processName = null;

    private long chkIntvl = 5000;

    private int chkPortTimeout = 5000;

    private int port = -1;

    private String doAfterNegativeResultCmd = null;

    private Guarder() {
    }

    public static synchronized Guarder getInstance() {
        if (singleton == null) {
            singleton = new Guarder();
        }
        return singleton;
    }

    @Override
    public void run() {
        while (true) {
            boolean isAlive = false;
            try {
                isAlive = testAlive();
            } catch (Exception e) {
                break;
            }

            if (!isAlive) {
                log.warn("test alive negative");
                try {
                    doAfterNegativeResult();
                } catch (IOException e) {
                    break;
                }
            }

            try {
                Thread.sleep(this.chkIntvl);
            } catch (InterruptedException e) {
                log.warn("guarder interrupted");
            }
        }
    }

    private boolean testAlive() throws Exception{
        boolean isAlive = true;

        if (port != -1) {
            try {
                isAlive = isAlive && Utils.isHostPortListened(this.port, this.chkPortTimeout);
            } catch (IllegalArgumentException e) {
                log.error("cannot test port", e);
                throw e;
            }
        }

        if (!isAlive) {
            log.info("port is not listened");
        } else {
            if (null != this.processName) {
                try {
                    isAlive = isAlive || Utils.isProcessRunnning(this.processName);
                } catch (IllegalArgumentException e) {
                    log.error("cannot test process", e);
                    throw e;
                } catch (IOException e) {
                    log.error("cannot test process", e);
                    throw e;
                }
            }

            if (!isAlive) {
                log.info("process is not running");
            }
        }
        return isAlive;
    }

    private void doAfterNegativeResult() throws IOException {
        if (null != doAfterNegativeResultCmd) {
            try {
                Utils.exeCmd(this.doAfterNegativeResultCmd);
            } catch (IOException e) {
                log.error("doAfterNegativeResult");
            }
            log.info("doAfterNegativeResult done");
        }
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setChkIntvl(long chkIntvl) {
        this.chkIntvl = chkIntvl;
    }

    public void setChkPortTimeout(int chkPortTimeout) {
        this.chkPortTimeout = chkPortTimeout;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDoAfterNegativeResultCmd(String doAfterNegativeResultCmd) {
        this.doAfterNegativeResultCmd = doAfterNegativeResultCmd;
    }
}

