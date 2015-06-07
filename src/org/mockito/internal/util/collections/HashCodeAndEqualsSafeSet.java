/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static java.lang.reflect.Array.newInstance;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mockito.internal.util.Checks;

/**
 * hashCode and equals safe hash based set.
 *
 * <p>
 *     Useful for holding mocks that have un-stubbable hashCode or equals method,
 *     meaning that in this scenario the real code is always called and will most probably
 *     cause an {@link NullPointerException}.
 * </p>
 * <p>
 *     This collection wraps the mock in an augmented type {@link HashCodeAndEqualsMockWrapper}
 *     that have his own implementation.
 * </p>
 *
 * @see HashCodeAndEqualsMockWrapper
 */
@SuppressWarnings("unchecked")
public class HashCodeAndEqualsSafeSet implements Set<Object> {

    private final HashSet<HashCodeAndEqualsMockWrapper> backingHashSet = new HashSet<HashCodeAndEqualsMockWrapper>();

    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private final Iterator<HashCodeAndEqualsMockWrapper> iterator = backingHashSet.iterator();

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Object next() {
                return iterator.next().get();
            }

            public void remove() {
                iterator.remove();
            }
        };
    }

    public int size() {
        return backingHashSet.size();
    }

    public boolean isEmpty() {
        return backingHashSet.isEmpty();
    }

    public boolean contains(final Object mock) {
        return backingHashSet.contains(HashCodeAndEqualsMockWrapper.of(mock));
    }

    public boolean add(final Object mock) {
        return backingHashSet.add(HashCodeAndEqualsMockWrapper.of(mock));
    }

    public boolean remove(final Object mock) {
        return backingHashSet.remove(HashCodeAndEqualsMockWrapper.of(mock));
    }

    public void clear() {
        backingHashSet.clear();
    }

    @Override public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override public boolean equals(final Object o) {
        if (!(o instanceof HashCodeAndEqualsSafeSet)) {
            return false;
        }
        final HashCodeAndEqualsSafeSet that = (HashCodeAndEqualsSafeSet) o;
        return backingHashSet.equals(that.backingHashSet);
    }

    @Override public int hashCode() {
        return backingHashSet.hashCode();
    }

    public Object[] toArray() {
        return unwrapTo(new Object[size()]);
    }

    private <T> T[] unwrapTo(final T[] array) {
        final Iterator<Object> iterator = iterator();
        for (int i = 0, objectsLength = array.length; i < objectsLength; i++) {
            if (iterator.hasNext()) {
                array[i] = (T) iterator.next();
            }
        }
        return array;
    }


    public <T> T[] toArray(final T[] typedArray) {
        final T[] array = typedArray.length >= size() ? typedArray
                : (T[]) newInstance(typedArray.getClass().getComponentType(), size());
        return unwrapTo(array);
    }

    public boolean removeAll(final Collection<?> mocks) {
        return backingHashSet.removeAll(asWrappedMocks(mocks));
    }

    public boolean containsAll(final Collection<?> mocks) {
        return backingHashSet.containsAll(asWrappedMocks(mocks));
    }

    public boolean addAll(final Collection<?> mocks) {
        return backingHashSet.addAll(asWrappedMocks(mocks));
    }

    public boolean retainAll(final Collection<?> mocks) {
        return backingHashSet.retainAll(asWrappedMocks(mocks));
    }

    private HashSet<HashCodeAndEqualsMockWrapper> asWrappedMocks(final Collection<?> mocks) {
        Checks.checkNotNull(mocks, "Passed collection should notify() be null");
        final HashSet<HashCodeAndEqualsMockWrapper> hashSet = new HashSet<HashCodeAndEqualsMockWrapper>();
        for (final Object mock : mocks) {
            assert !(mock instanceof HashCodeAndEqualsMockWrapper) : "WRONG";
            hashSet.add(HashCodeAndEqualsMockWrapper.of(mock));
        }
        return hashSet;
    }

    @Override public String toString() {
        return backingHashSet.toString();
    }

    public static HashCodeAndEqualsSafeSet of(final Object... mocks) {
        return of(Arrays.asList(mocks));
    }

    public static HashCodeAndEqualsSafeSet of(final Iterable<Object> objects) {
        final HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet = new HashCodeAndEqualsSafeSet();
        if (objects != null) {
            for (final Object mock : objects) {
                hashCodeAndEqualsSafeSet.add(mock);
            }
        }
        return hashCodeAndEqualsSafeSet;
    }
}
