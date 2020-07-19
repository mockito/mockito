/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Basic list/collection operators.
 * We know that there are existing libraries that implement those use cases neatly.
 * However, we want to keep Mockito dependencies minimal.
 * In Java8 we should be able to get rid of this class.
 */
public class ListUtil {

    public static <T> List<T> filter(Collection<T> collection, Filter<T> filter) {
        ArrayList<T> filtered = new ArrayList<>();
        for (T t : collection) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public static <From, To> List<To> convert(
            Collection<From> collection, Converter<From, To> converter) {
        ArrayList<To> converted = new ArrayList<>();
        for (From f : collection) {
            converted.add(converter.convert(f));
        }
        return converted;
    }

    public interface Filter<T> {
        boolean isOut(T object);
    }

    public interface Converter<From, To> {
        To convert(From from);
    }
}
