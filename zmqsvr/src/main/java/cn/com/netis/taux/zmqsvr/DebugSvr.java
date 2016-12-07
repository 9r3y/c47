package cn.com.netis.taux.zmqsvr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

/**
 * The class ZmqMsgConsumer.
 *
 * @version 1.0
 */
final class DebugSvr implements ZmqMsgConsumable {

    /** The constant LOG. */
    private static Logger LOG = LoggerFactory.getLogger(DebugSvr.class);

    @Override
    public void run() {
        final ZMQ.Socket socket = zmqContext.socket(ZMQ.PULL);
        socket.setRcvHWM(1);
        socket.setReceiveBufferSize(1);
//        socket.setLinger(1);
//        socket.setConflate(true);
        socket.bind(endPoint);
        LOG.info(String.format("Start listening to %s", endPoint));

        long lastTs = -1;
        try {
            while (!Thread.interrupted()) {
                final byte[] msg = socket.recv(0);
                long ts = Long.valueOf(new String(msg).split(" ")[0]);

//                LOG.debug(String.valueOf(ts));
                System.out.write(msg, 0, msg.length);
                if (ts < lastTs) {
                    System.exit(-1);
                }
                lastTs = ts;

//                Thread.sleep(300);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Finished.");
    }

    public static void main(String[] args) {
        new Thread(new DebugSvr(args[0])).start();
    }

    /**
     * Instantiates a new Zmq msg consumer.
     *
     * @param endPoint the end point
     */
    public DebugSvr(final String endPoint) {
        this.endPoint = endPoint;
    }

    /** The Context. */
    private final ZMQ.Context zmqContext = ZMQ.context(1);

    /** The Port. */
    private final String endPoint;

}
