package com.y3r9.c47.dog.script.ntaoutoforder;

import java.nio.file.Path;
import java.util.List;

/**
 * The interface File searcher.
 *
 * @version 1.0
 */
public interface FileSearcher {

    /**
     * Search the file paths.
     * @return file path list
     */
    List<Path> search();
}
