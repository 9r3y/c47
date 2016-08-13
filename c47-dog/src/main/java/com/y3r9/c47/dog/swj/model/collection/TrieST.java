package com.y3r9.c47.dog.swj.model.collection;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The <tt>TrieST</tt> class represents an symbol table of key-value
 * pairs, with string keys and generic values.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>, <em>delete</em>,
 * <em>size</em>, and <em>is-empty</em> methods.
 * It also provides character-based methods for finding the string
 * in the symbol table that is the <em>longest prefix</em> of a given prefix,
 * finding all strings in the symbol table that <em>start with</em> a given prefix,
 * and finding all strings in the symbol table that <em>match</em> a given pattern.
 * A symbol table implements the <em>associative array</em> abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that
 * values cannot be <tt>null</tt>&mdash;setting the
 * value associated with a key to <tt>null</tt> is equivalent to deleting the key
 * from the symbol table.
 * <p>
 * This implementation uses a 128-way trie. The <em>put</em>, <em>contains</em>, <em>delete</em>,
 * and <em>longest prefix</em> operations take time proportional to the length of the key (in the
 * worst case). Construction takes constant time. The <em>size</em>, and <em>is-empty</em>
 * operations take constant time. Construction takes constant time.
 * <p>
 * For additional documentation, see <a href="http://algs4.cs.princeton.edu/52trie">Section 5.2</a>
 * of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @param <Value>  the type parameter
 * @version 1.0
 */
public final class TrieST<Value> {

    /** extended ASCII. */
    private static final int R = 128;

    /** root of trie. */
    private Node<Value> root;

    /** number of keys in trie. */
    private int N;

    /**
     * R-way trie node.
     */
    private static class Node<Value> {
        /** The Val. */
        private Value val;

        /** The Next. */
        @SuppressWarnings("unchecked")
        private Node<Value>[] next = (Node<Value>[])new Node[R];
    }

    /**
     * Instantiates a new Trie sT.
     */
    public TrieST() {
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and <tt>null</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public Value get(final String key) {
        Node<Value> x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return x.val;
    }

    /**
     * Does this symbol table contain the given key?
     * 
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(final String key) {
        return get(key) != null;
    }

    /**
     * Get node.
     *
     * @param x the x
     * @param key the key
     * @param d the d
     * @return the node
     */
    private Node<Value> get(final Node<Value> x, final String key, final int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        final Node<Value> maskNode = x.next['.'];
        if (maskNode != null) {
            return maskNode;
        }
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * 
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(final String key, final Value val) {
        if (val == null) {
            delete(key);
        } else {
            root = put(root, key, val, 0);
        }
    }

    /**
     * Put node.
     *
     * @param x the x
     * @param key the key
     * @param val the val
     * @param d the d
     * @return the node
     */
    private Node<Value> put(Node<Value> x, final String key, final Value val, final int d) {
        if (x == null) {

            x = new Node<>();
        }
        if (d == key.length()) {
            if (x.val == null) {
                N++;
            }
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * 
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    /**
     * Is this symbol table empty?
     * 
     * @return <tt>true</tt> if this symbol table is empty and <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all keys in the symbol table as an <tt>Iterable</tt>.
     * To iterate over all of the keys in the symbol table named <tt>st</tt>,
     * use the foreach notation: <tt>for (Key key : st.keys())</tt>.
     * 
     * @return all keys in the sybol table as an <tt>Iterable</tt>
     */
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     * 
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>,
     *         as an iterable
     */
    public Iterable<String> keysWithPrefix(final String prefix) {
        Queue<String> results = new LinkedList<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    /**
     * Collect void.
     *
     * @param x the x
     * @param prefix the prefix
     * @param results the results
     */
    private void collect(final Node x, final StringBuilder prefix, final Queue<String> results) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            results.add(prefix.toString());
        }
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match <tt>pattern</tt>,
     * where . symbol is treated as a wildcard character.
     * 
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match <tt>pattern</tt>,
     *         as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(final String pattern) {
        Queue<String> results = new LinkedList<>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    /**
     * Collect void.
     *
     * @param x the x
     * @param prefix the prefix
     * @param pattern the pattern
     * @param results the results
     */
    private void collect(final Node x, final StringBuilder prefix, final String pattern, final Queue<String> results) {
        if (x == null) {
            return;
        }
        int d = prefix.length();
        if (d == pattern.length() && x.val != null) {
            results.add(prefix.toString());
        }
        if (d == pattern.length()) {
            return;
        }
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        } else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of <tt>query</tt>,
     * or <tt>null</tt>, if no such string.
     * 
     * @param query the query string
     * @throws NullPointerException if <tt>query</tt> is <tt>null</tt>
     * @return the string in the symbol table that is the longest prefix of <tt>query</tt>,
     *         or <tt>null</tt> if no such string
     */
    public String longestPrefixOf(final String query) {
        int length = longestPrefixOf(root, query, 0, 0);
        return query.substring(0, length);
    }

    /**
     * returns the length of the longest string key in the subtrie
     * rooted at x that is a prefix of the query string,
     * assuming the first d character match and we have already
     * found a prefix match of length length.
     *
     * @param x the x
     * @param query the query
     * @param d the d
     * @param length the length
     * @return the int
     */
    private int longestPrefixOf(final Node x, final String query, final int d, int length) {
        if (x == null) {
            return length;
        }
        if (x.val != null) {
            length = d;
        }
        if (d == query.length()) {
            return length;
        }
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d + 1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     * 
     * @param key the key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void delete(final String key) {
        root = delete(root, key, 0);
    }

    /**
     * Delete node.
     *
     * @param x the x
     * @param key the key
     * @param d the d
     * @return the node
     */
    private Node<Value> delete(final Node<Value> x, final String key, final int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            if (x.val != null) {
                N--;
            }
            x.val = null;
        } else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) {
            return x;
        }
        for (int c = 0; c < R; c++) {
            if (x.next[c] != null) {
                return x;
            }
        }
        return null;
    }

}
