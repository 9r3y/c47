package com.y3r9.c47.dog.swj.model.collection;

import com.y3r9.c47.dog.swj.model.collection.spi.NameMap;

/**
 * The Class HashNameMap.
 * 
 * @param <V> the value type
 * @version 1.0
 * @since project 2.0
 */
public class HashNameMap<V> extends java.util.HashMap<String, V> implements NameMap<V> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2513455756598760978L;

    /** The Constant SYSTEM_SEPARATOR. */
    public static final String SYSTEM_SEPARATOR = "://";

    /** The Constant GROUP_SEPARATOR. */
    public static final String GROUP_SEPARATOR = "/";

    /**
     * Join.
     * 
     * @param system the system
     * @param group the group
     * @param name the name
     * @return the string
     */
    public static String join(final String system, final String group, final String name) {
        final StringBuilder builder = new StringBuilder();
        builder.append(system).append(SYSTEM_SEPARATOR).append(group).append(GROUP_SEPARATOR)
                .append(name);
        return builder.toString();
    }

    /**
     * Join.
     * 
     * @param system the system
     * @param name the name
     * @return the string
     */
    public static String join(final String system, final String name) {
        final StringBuilder builder = new StringBuilder();
        builder.append(system).append(SYSTEM_SEPARATOR).append(name);
        return builder.toString();
    }

    @Override
    public final void
            put(final String system, final String group, final String name, final V value) {
        put(join(system, group, name), value);
    }

    @Override
    public final void put(final String system, final String name, final V value) {
        put(join(system, name), value);
    }

    @Override
    public final V get(final String system, final String group, final String name) {
        return get(join(system, group, name));
    }

    @Override
    public final V get(final String system, final String name) {
        return get(join(system, name));
    }
}
