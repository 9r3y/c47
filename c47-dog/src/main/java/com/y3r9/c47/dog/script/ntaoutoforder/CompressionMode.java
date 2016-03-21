package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * The Enum CompressionMode.
 * 
 * @version 1.5
 * @since 1.2 move to util.compression package.
 * @since v3.6 update to 1.5, add buffer size.
 */
public enum CompressionMode {

    /** The NONE. */
    NONE(""),

    /** The SNAPPY. */
    SPZ(".spz"),

    /** The SNAPPY without crc32 checksum. */
    SZ(".sz"),

    /** The GZ. */
    GZ(".gz"),

    /** The ZIP, with default level. */
    ZIP(".zip"),

    /** The ZIP, level=1. */
    ZIP1(".zip1", 1),

    /** The ZIP, level=2. */
    ZIP2(".zip2", 2),

    /** The ZIP, level=3. */
    ZIP3(".zip3", 3),

    /** The ZIP, level=4. */
    ZIP4(".zip4", 4),

    /** The ZIP, level=5. */
    ZIP5(".zip5", 5),

    /** The ZIP, level=6. */
    ZIP6(".zip6", 6),

    /** The ZIP, level=7. */
    ZIP7(".zip7", 7),

    /** The ZIP, level=8. */
    ZIP8(".zip8", 8),

    /** The ZIP, level=9. */
    ZIP9(".zip9", 9);

    /** The Constant LEVEL_UNDEFINED. */
    public static final int LEVEL_UNDEFINED = -1;

    /**
     * Extract from path.
     * 
     * @param path the path
     * @return the compression mode
     */
    public static CompressionMode extractFromPath(final String path) {
        for (final CompressionMode mode : CompressionMode.values()) {
            if (NONE.equals(mode)) {
                continue;
            }

            if (StringUtils.endsWithIgnoreCase(path, mode.suffix)) {
                return mode;
            }
        }
        return NONE;
    }

    /**
     * Builds the compress stream.
     *
     * @param stream the stream
     * @param bufferSize the buffer size
     * @param prompt the prompt
     * @return the output stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public OutputStream buildCompressStream(final OutputStream stream, final int bufferSize,
            final String prompt) throws IOException {
        return CompressionFactory.buildCompressStream(this, bufferSize, stream, prompt);
    }

    /**
     * Builds the uncompress stream.
     *
     * @param file the file
     * @return the input stream
     * @throws IOException the IO exception
     */
    public InputStream buildUncompressStream(final File file)
            throws IOException {
        return CompressionFactory.buildUncompressStream(this, file);
    }

    /**
     * Instantiates a new compression mode.
     * 
     * @param aSuffix the a suffix
     */
    private CompressionMode(final String aSuffix) {
        suffix = aSuffix;
        level = LEVEL_UNDEFINED;
    }

    /**
     * Instantiates a new compression mode.
     * 
     * @param aSuffix the a suffix
     * @param aLevel the a level
     */
    private CompressionMode(final String aSuffix, final int aLevel) {
        suffix = aSuffix;
        level = aLevel;
    }

    /**
     * Gets the suffix.
     * 
     * @return the suffix
     * @since 1.2
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Gets the level.
     * 
     * @return the level
     * @since 1.3
     */
    public int getLevel() {
        return level;
    }

    /** The suffix. */
    private final transient String suffix;

    /** The level. */
    private final transient int level;
}
