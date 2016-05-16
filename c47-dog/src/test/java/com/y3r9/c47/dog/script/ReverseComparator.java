package com.y3r9.c47.dog.script;

import java.util.Comparator;

/**
 * The class ReverseComparator.
 *
 * @version 1.0
 */
final class ReverseComparator implements Comparator<Integer> {
    @Override
    public int compare(final Integer o1, final Integer o2) {
        return -o1.compareTo(o2);
    }
}
