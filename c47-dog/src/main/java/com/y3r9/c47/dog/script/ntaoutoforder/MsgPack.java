package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;

import cn.com.netis.dp.commons.lang.Constants;

/**
 * The Class MsgPack.
 * 
 * @version 1.10
 */
public final class MsgPack {

    /**
     * Unpack.
     *
     * @param packFileName the pack file name
     * @param textFileName the text file name
     * @param existHeader the exist header
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void unpack(final String packFileName, final String textFileName,
            final boolean existHeader) throws IOException {
        final File textFile = StringUtils.isEmpty(textFileName) ? null : new File(textFileName);
        unpack(new File(packFileName), textFile, existHeader);
    }

    /**
     * Unpack.
     *
     * @param packFile the pack file
     * @param textFile the text file
     * @param existHeader the exist header
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void unpack(final File packFile, final File textFile, final boolean existHeader)
            throws IOException {

        try (final InputStream packStream = getInputStream(packFile.getCanonicalPath());
                final OutputStream textStream = textFile == null ? System.out
                        : new BufferedOutputStream(new FileOutputStream(textFile))) {
            unpack(packStream, textStream, existHeader);

        } catch (EOFException e) {
            // success to the file end
            return;
        }

    }

    /**
     * Gets the input stream.
     * 
     * @param inputFile the input file
     * @return the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static InputStream getInputStream(final String inputFile) throws IOException {
        final CompressionMode mode = CompressionMode.extractFromPath(inputFile);
        return CompressionFactory.buildInputStream(mode, new File(inputFile));
    }

    /**
     * Unpack body.
     * 
     * @param packFile the pack file
     * @param textFile the text file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void unpackBody(final File packFile, final File textFile) throws IOException {
        try (final InputStream packStream = new FileInputStream(packFile);
                final OutputStream textStream = new FileOutputStream(textFile)) {
            unpackBody(packStream, textStream);

        } catch (EOFException e) {
            // success to the file end
            return;
        }

    }

    /**
     * Unpack.
     *
     * @param packStream the pack stream
     * @param textStream the text stream
     * @param existHeader the exist header
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void unpack(final InputStream packStream, final OutputStream textStream,
            final boolean existHeader)
            throws IOException {
        final MessagePack msgpack = new MessagePack();
        final Unpacker unpacker = msgpack.createUnpacker(packStream);

        while (true) {
            if (existHeader) {
                final String header = unpackHeader(unpacker);
                textStream.write(header.getBytes(Constants.CS_DEFAULT));
            }
            final String record = unpackBody(unpacker);
            textStream.write(record.getBytes(Constants.CS_DEFAULT));
        }
    }

    /**
     * Unpack body.
     *
     * @param packStream the pack stream
     * @param textStream the text stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void unpackBody(final InputStream packStream, final OutputStream textStream)
            throws IOException {
        final MessagePack msgpack = new MessagePack();
        final Unpacker unpacker = msgpack.createUnpacker(packStream);

        while (true) {
            final String record = unpackBody(unpacker);
            textStream.write(record.getBytes(Constants.CS_DEFAULT));
        }
    }

    /**
     * Unpack record.
     * 
     * @param unpacker the unpacker
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String unpackBody(final Unpacker unpacker) throws IOException {
        return unpackBody(unpacker, Constants.TEXT_ITEM_SEPARATOR);
    }

    /**
     * Unpack body.
     *
     * @param unpacker the unpacker
     * @param separator the separator
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String unpackBody(final Unpacker unpacker, final char separator)
            throws IOException {
        final Map<String, Value> record = unpackMap(unpacker);
        if (!record.containsKey(Constants.KEY_TS)) {
            throw new UnsupportedOperationException(
                    "Unsupport recrod in pack data which has not \"ts\" item");
        }
        final long timestamp = longValue(record.get(Constants.KEY_TS));
        record.remove(Constants.KEY_TS);

        final StringBuilder builder = new StringBuilder();

        // timestamp
        Timestamp.toDateNanosecondTextWithBrackets(timestamp, builder);

        // values
        for (Entry<String, Value> value : record.entrySet()) {
            builder.append(separator);
            builder.append(value.getKey());
            builder.append("=");
            builder.append(getString(value.getValue()));
        }

        // new line
        builder.append('\n');

        return builder.toString();
    }

    /**
     * Unpack header.
     *
     * @param unpacker the unpacker
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String unpackHeader(final Unpacker unpacker) throws IOException {
        final StringBuilder builder = new StringBuilder();
        int size = unpacker.readArrayBegin();
        // values
        for (int i = 0; i < size; ++i) {
            builder.append(unpacker.readInt());
            builder.append(Constants.TEXT_ITEM_SEPARATOR);
        }
        unpacker.readArrayEnd();

        return builder.toString();
    }

    /**
     * Unpack.
     * 
     * @param unpacker the unpacker
     * @return the map
     * @throws IOException Signals that an I/O exception has occurred.
     * @since 1.9
     */
    public static Map<String, Value> unpackMap(final Unpacker unpacker) throws IOException {
        final Map<String, Value> result = new LinkedHashMap<>();
        final int size = unpacker.readMapBegin();
        // deserialize each pair of key and value
        for (int i = 0; i < size; i++) {
            // deserialize key
            final String key = unpacker.read(Templates.TString);
            // deserialize value
            final Value val = unpacker.readValue();
            result.put(key, val);
        }
        unpacker.readMapEnd();
        return result;
    }

    /**
     * Long value.
     * 
     * @param value the value
     * @return the long
     * @since 1.9
     */
    public static long longValue(final Value value) {
        return value.asIntegerValue().longValue();
    }

    /**
     * Integer value.
     * 
     * @param value the value
     * @return the integer
     * @since 1.9
     */
    public static int intValue(final Value value) {
        return value.asIntegerValue().intValue();
    }

    /**
     * Double value.
     * 
     * @param value the value
     * @return the double
     * @since 1.9
     */
    public static double doubleValue(final Value value) {
        return value.asFloatValue().doubleValue();
    }

    /**
     * Boolean value.
     * 
     * @param value the value
     * @return true, if successful
     * @since 1.9
     */
    public static boolean boolValue(final Value value) {
        return value.asBooleanValue().getBoolean();
    }

    /**
     * Gets the string.
     * 
     * @param value the value
     * @return the string
     * @since 1.9
     */
    public static String getString(final Value value) {
        return value.getType() == ValueType.RAW ? value.asRawValue().getString() : value.toString();
    }

    /**
     * Prevents from instantiating.
     */
    private MsgPack() {
    }
}
