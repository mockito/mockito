/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Basic list/collection operators. We know that there are existing libraries that implement those
 * use cases neatly. However, we want to keep Mockito dependencies minimal. In Java8 we should be
 * able to get rid of this class.
 */
public final class ListUtil {

    public static <T> LinkedList<T> filter(Collection<T> collection, Filter<T> filter) {
        LinkedList<T> filtered = new LinkedList<T>();
        for (T t : collection) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public static <FromT, To> LinkedList<To> convert(
            Collection<FromT> collection, Converter<FromT, To> converter) {
        LinkedList<To> converted = new LinkedList<To>();
        for (FromT f : collection) {
            converted.add(converter.convert(f));
        }
        return converted;
    }

    public interface Filter<T> {
        boolean isOut(T object);
    }

    public interface Converter<FromT, To> {
        To convert(FromT from);
    }

    private ListUtil() {}
}
