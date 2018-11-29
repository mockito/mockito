/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.collections;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Basic list/collection operators.
 * We know that there are existing libraries that implement those use cases neatly.
 * However, we want to keep Mockito dependencies minimal.
 * In Java8 we should be able to get rid of this class.
 */
public class ListUtil {

    public static <T> LinkedList<T> filter(Collection<T> collection, Filter<T> filter) {
        LinkedList<T> filtered = new LinkedList<T>();
        for (T t : collection) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public static <T> LinkedList<T> filterBy(Collection<T> collection, Predicate<T> predicate) {
        LinkedList<T> filtered = new LinkedList<T>();
        for (T t : collection) {
            if (predicate.test(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public static <From, To> LinkedList<To> convert(Collection<From> collection, Converter<From, To> converter) {
        LinkedList<To> converted = new LinkedList<To>();
        for (From f: collection) {
            converted.add(converter.convert(f));
        }
        return converted;
    }

    public interface Filter<T> {
        boolean isOut(T object);
    }

    // heavily inspired by Java8 java.util.function.Predicate
    public abstract static class Predicate<T> {
        public abstract boolean test(T object);

        public Predicate<T> negate() {
            return new Predicate<T>() {
                @Override
                public String toString() {
                    return "!" + Predicate.this.toString();
                }

                @Override
                public boolean test(T object) {
                    return !Predicate.this.test(object);
                }
            };
        }

        public Predicate<T> and(Predicate<? super T> other) {
            assert other != null;
            final Predicate<? super T> otherPred = other;

            return new Predicate<T>() {
                @Override
                public String toString() {
                    return Predicate.this.toString() + " && " + otherPred.toString();
                }

                @Override
                public boolean test(T object) {
                    return Predicate.this.test(object) && otherPred.test(object);
                }
            };
        }

        public Predicate<T> or(Predicate<? super T> other) {
            assert other != null;
            final Predicate<? super T> otherPred = other;

            return new Predicate<T>() {
                @Override
                public String toString() {
                    return Predicate.this.toString() + " || " + otherPred.toString();
                }

                @Override
                public boolean test(T object) {
                    return Predicate.this.test(object) || otherPred.test(object);
                }
            };
        }
    }

    public interface Converter<From, To> {
        To convert(From from);
    }
}
