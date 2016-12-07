package cn.com.netis.taux.zmqsvr;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

/**
 * The class ZmqMsgConsumer.
 *
 * @version 1.0
 */
final class DebugClient implements Runnable {

    /** The constant LOG. */
    private static Logger LOG = LoggerFactory.getLogger(DebugClient.class);

    @Override
    public void run() {
        final ZMQ.Socket socket = zmqContext.socket(ZMQ.PUSH);
        socket.setHWM(20000);
//        socket.setSndHWM(1);
//        socket.setSendBufferSize(1);
        socket.connect(endPoint);

        LOG.info(String.format("Start connecting to %s", endPoint));

        final String a = StringUtils.repeat("a", 1000);
        int ts = 1;
        try {
            socket.send(new byte[]{1,1,3}, ZMQ.SNDMORE);
            socket.send(new byte[]{1,1,3}, 0);
/*            while (!Thread.interrupted()) {
                for (int i = 0; i < 10; i++) {
                    byte[] msg = String.valueOf(ts + " " + a).getBytes();
                    socket.send(msg, 0);
                    LOG.debug(String.valueOf(ts));
                    ts++;
                }
                Thread.sleep(1000);
            }*/
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            zmqContext.term();
        }
        LOG.info("Finished.");
    }

    public static void main(String[] args) {
//        new Thread(new DebugClient(args[0])).start();
//        new Thread(new DebugClient("tcp://127.0.0.1:38000")).start();
        new Thread(new DebugClient("tcp://172.16.14.112:38184")).start();
    }

    /**
     * Instantiates a new Zmq msg consumer.
     *
     */
    public DebugClient(final String endPoint) {
        this.endPoint = endPoint;
    }

    /** The Context. */
    private final ZMQ.Context zmqContext = ZMQ.context(1);

    private final String endPoint;

}
