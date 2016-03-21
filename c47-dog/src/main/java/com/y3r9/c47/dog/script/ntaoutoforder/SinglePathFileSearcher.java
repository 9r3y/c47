package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Single path file searcher.
 *
 * @version 1.0
 */
public final class SinglePathFileSearcher implements FileSearcher {

    /** The Path. */
    private Path path;

    /** The Pattern. */
    private String pattern;

    /**
     * Instantiates a new Single path file searcher.
     *
     * @param path the path
     */
    public SinglePathFileSearcher(final Path path) {
        new SinglePathFileSearcher(path, "*");
    }

    /**
     * Instantiates a new Single path file searcher.
     *
     * @param path the path
     * @param pattern the pattern
     */
    public SinglePathFileSearcher(final Path path, final String pattern) {
        this.path = path;
        if (null == pattern || "".equals(pattern)) {
            throw new IllegalArgumentException();
        } else {
            this.pattern = pattern;
        }
    }

    @Override
    public List<Path> search() {
        List<Path> ret = new ArrayList<>(0);
        if (Files.exists(path) && Files.isDirectory(path)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, pattern)) {
                for (Path file : ds) {
                    ret.add(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

}
