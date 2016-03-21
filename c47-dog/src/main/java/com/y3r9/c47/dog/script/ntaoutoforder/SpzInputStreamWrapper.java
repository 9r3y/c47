package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.IOException;
import java.io.InputStream;

import org.xerial.snappy.SpzFramedInputStream;

/**
 * The Class SpzInputStreamWrapper.
 * 
 * @version 1.1
 * @since project 3.0
 */
final class SpzInputStreamWrapper extends InputStream {

    /** The Constant EOF. */
    public static final int EOF = -1;

    @Override
    public int read() throws IOException {
        return innerStream.read();
    }

    @Override
    public int read(final byte[] output, final int offset, final int length) throws IOException {
        int lastRead = 0;
        int nextOffset = offset;
        int nextLen = length;
        boolean cache = false;
        int cacheLen = 0;
        for (;;) {
            lastRead = innerStream.read(output, nextOffset, nextLen);
            nextOffset = nextOffset + lastRead;
            nextLen = nextLen - lastRead;
            if (lastRead == EOF) {
                return cache ? cacheLen : 0;
            }
            if (nextLen > 0) {
                cache = true;
                cacheLen += lastRead;
                continue;
            }
            return length;
        }
    }

    @Override
    public int available() throws IOException {
        // @since 1.1 fix bug for spz reading
        return innerStream.available() > 0 ? innerStream.available() : input.available();
    }

    @Override
    public void close() throws IOException {
        innerStream.close();
    }

    /**
     * Instantiates a new SPZ input stream wrapper.
     * 
     * @param inputStream the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public SpzInputStreamWrapper(final InputStream inputStream) throws IOException {
        innerStream = new SpzFramedInputStream(inputStream);
        input = inputStream;
    }

    /** The input stream. */
    private final InputStream input;

    /** The inner stream. */
    private final SpzFramedInputStream innerStream;

}
