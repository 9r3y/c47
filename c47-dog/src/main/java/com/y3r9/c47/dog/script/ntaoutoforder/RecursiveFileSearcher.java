package com.y3r9.c47.dog.script.ntaoutoforder;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The type Recursive file searcher.
 *
 * @version 1.0
 */
public final class RecursiveFileSearcher implements FileSearcher {

    /** The Path. */
    private Path path;

    /** The Pattern. */
    private String pattern;

    /**
     * Instantiates a new Recursive file searcher.
     *
     * @param path the path
     */
    public RecursiveFileSearcher(final Path path) {
        new SinglePathFileSearcher(path, "*");
    }

    /**
     * Instantiates a new Recursive file searcher.
     *
     * @param path    the path
     * @param pattern the pattern
     */
    public RecursiveFileSearcher(final Path path, final String pattern) {
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
        if (Files.exists(path)) {
            FileWalker walker = new FileWalker(pattern);
            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            try {
                Files.walkFileTree(path, opts, Integer.MAX_VALUE, walker);
            } catch (IOException e) {
                e.printStackTrace();
                return ret;
            }
            ret = walker.getResult();
        }
        return ret;
    }

    /**
     * The type File walker.
     *
     * @version 1.0
     */
    private static final class FileWalker implements FileVisitor<Path> {

        /** The Matcher. */
        private final PathMatcher matcher;

        /** The Result. */
        private List<Path> result = new ArrayList<>();

        /**
         * Instantiates a new File walker.
         *
         * @param glob the glob
         */
        private FileWalker(final String glob) {
            this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                throws IOException {
            Path fileName = file.getFileName();
            if (null != fileName && matcher.matches(fileName)) {
                result.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path file, final IOException exc)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
                throws IOException {
            return FileVisitResult.CONTINUE;
        }

        /**
         * Gets result.
         *
         * @return the result
         */
        public List<Path> getResult() {
            return this.result;
        }
    }

}
