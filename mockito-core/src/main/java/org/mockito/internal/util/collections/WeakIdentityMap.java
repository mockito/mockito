/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import org.mockito.CanIgnoreReturnValue;
import org.mockito.CheckReturnValue;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * A weak-key map that uses identity ({@code ==}) rather than {@code hashCode()}/{@code equals()}
 * for key comparison. This avoids calling instrumented methods on mock instances during map
 * operations, which would cause infinite recursion. Stale entries whose keys have been
 * garbage-collected are cleaned up via a {@link ReferenceQueue}.
 *
 * <p>Internally, entries are bucketed by {@link System#identityHashCode} for O(1) amortized
 * lookups. Hash collisions (distinct objects sharing an identity hash code) are resolved by
 * linear scan within the bucket using {@code ==}. Lookups do not allocate.
 */
@CheckReturnValue
public class WeakIdentityMap<V> extends AbstractMap<Object, V> {

    private final Map<Integer, List<WeakEntry<V>>> delegate = new HashMap<>();
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private int size;

    private void expungeStaleEntries() {
        WeakEntry<?> ref;
        while ((ref = (WeakEntry<?>) queue.poll()) != null) {
            int hash = ref.hashCode;
            List<WeakEntry<V>> bucket = delegate.get(hash);
            if (bucket != null) {
                if (bucket.remove(ref)) {
                    size--;
                    if (bucket.isEmpty()) {
                        delegate.remove(hash);
                    }
                }
            }
        }
    }

    @Override
    public V get(Object key) {
        expungeStaleEntries();
        List<WeakEntry<V>> bucket = delegate.get(System.identityHashCode(key));
        if (bucket != null) {
            for (WeakEntry<V> entry : bucket) {
                if (entry.get() == key) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        expungeStaleEntries();
        List<WeakEntry<V>> bucket = delegate.get(System.identityHashCode(key));
        if (bucket != null) {
            for (WeakEntry<V> entry : bucket) {
                if (entry.get() == key) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @CanIgnoreReturnValue
    public V put(Object key, V value) {
        expungeStaleEntries();
        int hash = System.identityHashCode(key);
        List<WeakEntry<V>> bucket = delegate.get(hash);
        if (bucket != null) {
            for (WeakEntry<V> entry : bucket) {
                if (entry.get() == key) {
                    V old = entry.value;
                    entry.value = value;
                    return old;
                }
            }
        } else {
            bucket = new ArrayList<>(2);
            delegate.put(hash, bucket);
        }
        bucket.add(new WeakEntry<>(key, hash, value, queue));
        size++;
        return null;
    }

    @Override
    @CanIgnoreReturnValue
    public V remove(Object key) {
        expungeStaleEntries();
        int hash = System.identityHashCode(key);
        List<WeakEntry<V>> bucket = delegate.get(hash);
        if (bucket != null) {
            Iterator<WeakEntry<V>> it = bucket.iterator();
            while (it.hasNext()) {
                WeakEntry<V> entry = it.next();
                if (entry.get() == key) {
                    it.remove();
                    size--;
                    if (bucket.isEmpty()) {
                        delegate.remove(hash);
                    }
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        expungeStaleEntries();
        return size;
    }

    @Override
    public Set<Entry<Object, V>> entrySet() {
        expungeStaleEntries();
        Set<Entry<Object, V>> result = new HashSet<>();
        for (List<WeakEntry<V>> bucket : delegate.values()) {
            for (WeakEntry<V> entry : bucket) {
                Object referent = entry.get();
                if (referent != null) {
                    result.add(new SimpleEntry<>(referent, entry.value));
                }
            }
        }
        return result;
    }

    private static class WeakEntry<V> extends WeakReference<Object> {

        final int hashCode;
        V value;

        WeakEntry(Object key, int hashCode, V value, ReferenceQueue<Object> queue) {
            super(key, queue);
            this.hashCode = hashCode;
            this.value = value;
        }
    }
}
