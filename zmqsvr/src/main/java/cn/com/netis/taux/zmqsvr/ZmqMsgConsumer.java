package cn.com.netis.taux.zmqsvr;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import cn.com.netis.dp.commons.common.packet.PacketMetaType;

/**
 * The class ZmqMsgConsumer.
 *
 * @version 1.0
 */
final class ZmqMsgConsumer implements ZmqMsgConsumable {

    /** The constant LOG. */
    private static Logger LOG = LoggerFactory.getLogger(ZmqMsgConsumer.class);

    /** The Constant END_TEXT. */
    public static final String END_TEXT = String.format("MetaType=%d", PacketMetaType.JOB_END);

    private String lastNta = "";

    @Override
    public void run() {
        final ZMQ.Socket socket = zmqContext.socket(ZMQ.PULL);
        final String endPoint = "tcp://0.0.0.0:" + port;
        socket.bind(endPoint);
        LOG.info(String.format("Start listening to %s", endPoint));


        try {
            while (!Thread.interrupted()) {
                final MessagePack messagePack = new MessagePack();
                // skip header
                final byte[] header = socket.recv(0);
//                final ZmqHeader zmqHeader = processHeader(messagePack, header);

                // fetch body
                final byte[] body = socket.recv(0);
                final ZmqBody zmqBody = processBody(messagePack, body);
                LOG.debug(zmqBody.getUnpackBody());

//                if (zmqBody.getStreamId() == null) {
//                    //LOG.info(zmqBody.getUnpackBody());
//                    continue;
//                }
//                if (zmqBody.getStreamId().equals("1545107434")) {
//                    LOG.info(zmqBody.getUnpackBody());
//                }

//                if (!check(zmqHeader, zmqBody)) {
//                    LOG.error("Out-of-order occurred!!!");
//                    LOG.info(lastNta);
//                    LOG.info(zmqBody.getUnpackBody());
//                    //System.exit(-1);
//                }
//                lastNta = zmqBody.getUnpackBody();
//
//                // check terminate and break
//                if (StringUtils.contains(zmqBody.getUnpackBody(), END_TEXT)) {
//                    LOG.info("End text received.");
//                    break;
//                }

//                Thread.sleep(100);
                pgObs.updateProgress();
            }
        } catch (Exception e) {
            LOG.error(e.toString(), e);
        }
        LOG.info("Finished.");
        pgObs.countDown();
    }

    /**
     * Check boolean.
     *
     * @param zmqHeader the zmq header
     * @param zmqBody the zmq body
     * @return the boolean
     */
    boolean check(final ZmqHeader zmqHeader, final ZmqBody zmqBody) {
        final String streamId = zmqBody.getStreamId();
        ZmqHeader last = streams.get(streamId);
        boolean isOutOfOrder = false;
        boolean isRoutIdMess = false;
        if (last == null) {
            streams.put(streamId, zmqHeader);
            last = zmqHeader;
        } else {
            if (zmqHeader.getTs() < last.getTs()) {
                isOutOfOrder = true;
            }
            if (zmqHeader.getRouteId() != last.getRouteId()) {
                isRoutIdMess = true;
            }
        }

        boolean isRoutIdError = false;
        final int routId = zmqHeader.getRouteId();
        // NTA
/*        switch (streamId) {
        case "2017197750":
            isRoutIdError = routId != -1669603999;
            break;
        case "-1663473769":
            isRoutIdError = routId != -707557662;
            break;
        case "2017197751":
            isRoutIdError = routId != -1668521474;
            break;
        case "2017197748":
            isRoutIdError = routId != -1671769049;
            break;
        }*/
        // NTS
/*        switch (streamId) {
        case "-1663473769":
            isRoutIdError = routId != -707557662;
            break;
        case "2017197751":
            isRoutIdError = routId != -707557662;
            break;
        case "2017197748":
            isRoutIdError = routId != -707557662;
            break;
        case "2017197749":
            isRoutIdError = routId != -707557662;
            break;
        }*/
        if (isOutOfOrder) {
            LOG.error(String.format("Last TS [%d] differ from current [%d] of Stream [%s]",
                    last.getTs(), zmqHeader.getTs(), streamId));
        }
        if (isRoutIdMess) {
            LOG.error(String.format("Last RoutId [%d] differ from current [%d] of Stream [%s]",
                    last.getRouteId(), zmqHeader.getRouteId(), streamId));
        }
        if (isRoutIdError) {
            LOG.error(String.format("Expected RoutId not match: [%d] of Stream [%s]", routId, streamId));
        }

        last.setTs(zmqHeader.getTs());
        return !(isOutOfOrder || isRoutIdMess || isRoutIdError);
    }

    /**
     * Process header.
     *
     * @param messagePack the message pack
     * @param header the header
     * @return the zmq header
     * @throws IOException the iO exception
     */
    private ZmqHeader processHeader(final MessagePack messagePack, final byte[] header)
            throws IOException {
        final Unpacker unpacker = messagePack.createBufferUnpacker(header);
        final ZmqHeader result = new ZmqHeader();

        unpacker.readArrayBegin();
        long sec = unpacker.readInt();
        long subSec = unpacker.readInt();
        long ts = sec * 1000000000 + subSec;
        result.setTs(ts);
        result.setRouteId(unpacker.readInt());
        return result;
    }

    /**
     * Process body.
     *
     * @param messagePack the message pack
     * @param body the body
     * @return the string
     * @throws IOException the iO exception
     */
    private ZmqBody processBody(final MessagePack messagePack, final byte[] body)
            throws IOException {
        final Unpacker unpacker = messagePack.createBufferUnpacker(body);
        final ZmqBody result = new ZmqBody();
        StringBuilder sb = new StringBuilder();
        int size = unpacker.readMapBegin();
        // deserialize each pair of key and value
        for (int i = 0; i < size; i++) {
            // deserialize key
            final String key = unpacker.read(Templates.TString);
            // deserialize value
            final Object val = unpackValue(unpacker.readValue());
            if (val == null) {
                continue;
            }
            if (result.getStreamId() == null && key.equals("StreamId")) {
                result.setStreamId(val.toString());
            }
            if (result.getMetaType() == null && key.equals("MetaType")) {
                result.setMetaType(val.toString());
            }
            sb.append(key).append("=").append(val.toString()).append(" ");
        }
        unpacker.readMapEnd();
        result.setUnpackBody(sb.toString());
        return result;
    }

    /**
     * To object.
     *
     * @param value the value
     * @return the object
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Object unpackValue(final Value value) throws IOException {
        try (final Converter conv = new Converter(value)) {
            if (value.isNilValue()) {
                // null
                return null;

            } else if (value.isRawValue()) {
                // byte[] or String or maybe Date?
                // deserialize value to String object
                value.asRawValue();
                return conv.read(Templates.TString);

            } else if (value.isBooleanValue()) {
                // boolean
                return conv.read(Templates.TBoolean);

            } else if (value.isIntegerValue()) {
                // integer or long or BigInteger
                // deserialize value to integer
                value.asIntegerValue();
                return conv.read(Templates.TLong);

            } else if (value.isFloatValue()) {
                // float or double
                // deserialize value to double
                value.asFloatValue();
                return conv.read(Templates.TDouble);

            } else if (value.isArrayValue()) {
                // List or Set
                // deserialize value to Map object
                throw new UnsupportedOperationException("Unsupport Array in pack data");

            } else if (value.isMapValue()) {
                // Map
                // deserialize value to Map object
                throw new UnsupportedOperationException("Unsupport Map in pack data");

            }
        }

        throw new IOException("Unpack error");
    }

    /**
     * Instantiates a new Zmq msg consumer.
     *
     * @param port the port
     * @param pgObs the pg obs
     */
    public ZmqMsgConsumer(final int port, final ProgressObserver pgObs) {
        this.port = port;
        this.pgObs = pgObs;
    }

    /** The Context. */
    private final ZMQ.Context zmqContext = ZMQ.context(1);

    /** The Port. */
    private final int port;

    /** The Pg obs. */
    private final ProgressObserver pgObs;

    /** The Streams. */
    private final Map<String, ZmqHeader> streams;

    {
        streams = new HashMap<>();
    }

    /**
     * The class Zmq header.
     *
     * @version 1.0
     */
    private static final class ZmqHeader {
        /** The Ts. */
        private long ts;

        /** The Route id. */
        private int routeId;

        /**
         * Gets route id.
         *
         * @return the route id
         */
        public int getRouteId() {
            return routeId;
        }

        /**
         * Sets route id.
         *
         * @param value the value
         */
        public void setRouteId(final int value) {
            routeId = value;
        }

        /**
         * Gets ts.
         *
         * @return the ts
         */
        public long getTs() {
            return ts;
        }

        /**
         * Sets ts.
         *
         * @param value the value
         */
        public void setTs(final long value) {
            ts = value;
        }
    }

    /**
     * The class Zmq body.
     *
     * @version 1.0
     */
    private static final class ZmqBody {

        /** The Stream id. */
        private String streamId;

        /** The Unpack body. */
        private String unpackBody;

        /** The Meta type. */
        private String metaType;

        /**
         * Gets unpack body.
         *
         * @return the unpack body
         */
        public String getUnpackBody() {
            return unpackBody;
        }

        /**
         * Sets unpack body.
         *
         * @param value the value
         */
        public void setUnpackBody(final String value) {
            unpackBody = value;
        }

        /**
         * Gets stream id.
         *
         * @return the stream id
         */
        public String getStreamId() {
            return streamId;
        }

        /**
         * Sets stream id.
         *
         * @param value the value
         */
        public void setStreamId(final String value) {
            streamId = value;
        }

        /**
         * Gets meta type.
         *
         * @return the meta type
         */
        public String getMetaType() {
            return metaType;
        }

        /**
         * Sets meta type.
         *
         * @param value the value
         */
        public void setMetaType(final String value) {
            metaType = value;
        }
    }
}
