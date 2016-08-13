package com.y3r9.c47.dog.swj.model.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;
import com.y3r9.c47.dog.swj.model.collection.spi.HotColdMap;

/**
 * The Class HotColdHashMap.
 * 
 * @param <K> the key type
 * @param <V> the value type
 * @version 1.1
 * @see HotColdMap
 * @since project 2.0
 */
public final class HotColdHashMap<K, V> implements HotColdMap<K, V> {

    @Override
    public int hotSize() {
        return hotMap.size();
    }

    @Override
    public int coldSize() {
        return coldMap.size();
    }

    @Override
    public int getHotCapacity() {
        return hotCapacity;
    }

    @Override
    public void setHotCapacity(final int value) {
        NonPositiveArgumentException.check(value, "hotCapacity");
        hotCapacity = value;
    }

    @Override
    public int size() {
        return hotSize() + coldSize();
    }

    @Override
    public boolean isEmpty() {
        return hotMap.isEmpty() && coldMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return hotMap.containsKey(key) || coldMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return hotMap.containsValue(value) || coldMap.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        final V result = hotMap.get(key);
        return (result == null) ? coldMap.get(key) : result;
    }

    @Override
    public V put(final K key, final V value) {
        if (hotSize() >= getHotCapacity()) {
            // exchange the maps
            final Map<K, V> temp = coldMap;
            coldMap = hotMap;
            hotMap = temp;

            // rest hotNames
            hotMap.clear();
        }
        return hotMap.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        final V result = hotMap.remove(key);
        return (result == null) ? coldMap.remove(key) : result;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> item : map.entrySet()) {
            put(item.getKey(), item.getValue());
        }
    }

    @Override
    public void clear() {
        hotMap.clear();
        coldMap.clear();
    }

    @Override
    public Set<K> keySet() {
        final Set<K> result = new HashSet<K>();
        result.addAll(hotMap.keySet());
        result.addAll(coldMap.keySet());
        return result;
    }

    @Override
    public Collection<V> values() {
        final Collection<V> result = new HashSet<V>();
        result.addAll(hotMap.values());
        result.addAll(coldMap.values());
        return result;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
        result.addAll(hotMap.entrySet());
        result.addAll(coldMap.entrySet());
        return result;
    }

    @Override
    public String toString() {
        // @since 1.1
        final StringBuilder builder = new StringBuilder();
        builder.append("HotColdHashMap [hotSize()=").append(hotSize()).append(", coldSize()=")
                .append(coldSize()).append(", hotCapacity=").append(hotCapacity).append("]");
        return builder.toString();
    }

    /** The hot map. */
    private transient Map<K, V> hotMap;

    /** The cold map. */
    private transient Map<K, V> coldMap;

    /** The hot capacity. */
    private int hotCapacity;

    {
        hotMap = new HashMap<K, V>();
        coldMap = new HashMap<K, V>();
        setHotCapacity(DEFAULT_HOT_CAPACITY);
    }
}
