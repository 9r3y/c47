package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SpzFramedOutputStream;
import org.xerial.snappy.SzFramedOutputStream;

/**
 * The Class CompressionFactory.
 *
 * @version 1.3
 * @since v3.6 update to 1.3, add buffer size.
 */
public final class CompressionFactory {

    /**
     * Builds the compress stream.
     *
     * @param mode the mode
     * @param bufferSize the buffer size
     * @param stream the stream
     * @param prompt the prompt
     * @return the output stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static OutputStream buildCompressStream(final CompressionMode mode,
            final int bufferSize, final OutputStream stream, final String prompt)
            throws IOException {
        final OutputStream result;
        switch (mode) {

            case NONE:
                result = stream;
                break;
            case SPZ:
                result = new SpzFramedOutputStream(stream);
                break;
            case SZ:
                result = new SzFramedOutputStream(stream);
                break;
            case GZ:
                result = new GZIPOutputStream(stream);
                break;
            case ZIP:
                final ZipOutputStream zip = new ZipOutputStream(stream);
                zip.putNextEntry(new ZipEntry(prompt));
                result = zip;
                break;
            default:
                // for ZIP1 to ZIP9
                final ZipOutputStream zipWithLevel = new ZipOutputStream(stream);
                zipWithLevel.setLevel(mode.getLevel());
                zipWithLevel.putNextEntry(new ZipEntry(prompt));
                result = zipWithLevel;
                break;
        }
        return bufferSize == 0 ? result : new BufferedOutputStream(result, bufferSize);
    }

    /**
     * Builds the uncompress stream.
     *
     * @param mode the mode
     * @param file the file
     * @return the output stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static InputStream buildUncompressStream(final CompressionMode mode,
            final File file) throws IOException {
        final FileInputStream stream = new FileInputStream(file);
        switch (mode) {

            case NONE:
                return new BufferedInputStream(stream);

            case SPZ:
                // return new SpzFramedInputStream(stream);
                final int start = stream.read();
                stream.close();

                // old SnappyInputStream
                return start == 130 ? new SnappyInputStream(new FileInputStream(file))
                        : new SpzInputStreamWrapper(new FileInputStream(file));

            case SZ:
                // return new SzFramedInputStream(stream);
                return new SzInputStreamWrapper(stream);

            case GZ:
                throw new UnsupportedOperationException("GZ does not support break reading.");

            default:
                throw new UnsupportedOperationException("ZIP does not support break reading.");
        }
    }

    /**
     * Builds the break read input stream.
     *
     * @param mode the mode
     * @param file the file
     * @return the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static InputStream buildInputStream(final CompressionMode mode,
            final File file) throws IOException {
        return buildUncompressStream(mode, file);
    }

    /**
     * Builds the output stream.
     *
     * @param mode the mode
     * @param file the file
     * @param prompt the prompt
     * @return the output stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static OutputStream buildOutputStream(final CompressionMode mode, final int bufferSize,
            final File file, final String prompt) throws IOException {
        final FileOutputStream stream = new FileOutputStream(file);
        return buildCompressStream(mode, bufferSize, stream, prompt);
    }

    /**
     * Instantiates a new compression stream.
     */
    private CompressionFactory() {
    }
}
