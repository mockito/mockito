/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import org.mockito.internal.util.Checks;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.lang.reflect.Array.*;

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

    public boolean contains(Object mock) {
        return backingHashSet.contains(HashCodeAndEqualsMockWrapper.of(mock));
    }

    public boolean add(Object mock) {
        return backingHashSet.add(HashCodeAndEqualsMockWrapper.of(mock));
    }

    public boolean remove(Object mock) {
        return backingHashSet.remove(HashCodeAndEqualsMockWrapper.of(mock));
    }

    public void clear() {
        backingHashSet.clear();
    }

    @Override public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof HashCodeAndEqualsSafeSet)) {
            return false;
        }
        HashCodeAndEqualsSafeSet that = (HashCodeAndEqualsSafeSet) o;
        return backingHashSet.equals(that.backingHashSet);
    }

    @Override public int hashCode() {
        return backingHashSet.hashCode();
    }

    public Object[] toArray() {
        return unwrapTo(new Object[size()]);
    }

    @SuppressWarnings("unchecked")
    private <T> T[] unwrapTo(T[] array) {
        Iterator<Object> iterator = iterator();
        for (int i = 0, objectsLength = array.length; i < objectsLength; i++) {
            if (iterator.hasNext()) {
                array[i] = (T) iterator.next();
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] typedArray) {
        T[] array = typedArray.length >= size() ? typedArray : (T[]) newInstance(typedArray.getClass().getComponentType(), size());
        return unwrapTo(array);
    }

    public boolean removeAll(Collection<?> mocks) {
        return backingHashSet.removeAll(asWrappedMocks(mocks));
    }

    public boolean containsAll(Collection<?> mocks) {
        return backingHashSet.containsAll(asWrappedMocks(mocks));
    }

    public boolean addAll(Collection<?> mocks) {
        return backingHashSet.addAll(asWrappedMocks(mocks));
    }

    public boolean retainAll(Collection<?> mocks) {
        return backingHashSet.retainAll(asWrappedMocks(mocks));
    }

    private HashSet<HashCodeAndEqualsMockWrapper> asWrappedMocks(Collection<?> mocks) {
        Checks.checkNotNull(mocks, "Passed collection should notify() be null");
        HashSet<HashCodeAndEqualsMockWrapper> hashSet = new HashSet<HashCodeAndEqualsMockWrapper>();
        for (Object mock : mocks) {
            assert ! (mock instanceof HashCodeAndEqualsMockWrapper) : "WRONG";
            hashSet.add(HashCodeAndEqualsMockWrapper.of(mock));
        }
        return hashSet;
    }

    @Override public String toString() {
        return backingHashSet.toString();
    }

    public static HashCodeAndEqualsSafeSet of(Object... mocks) {
        return of(Arrays.asList(mocks));
    }

    public static HashCodeAndEqualsSafeSet of(Iterable<Object> objects) {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet = new HashCodeAndEqualsSafeSet();
        if (objects != null) {
            for (Object mock : objects) {
                hashCodeAndEqualsSafeSet.add(mock);
            }
        }
        return hashCodeAndEqualsSafeSet;
    }
}
