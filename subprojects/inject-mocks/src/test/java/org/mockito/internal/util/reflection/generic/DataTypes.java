/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.util.Collection;
import java.util.List;

public class DataTypes {
    @SuppressWarnings("unused")
    static class CollectionBox<T> {
        private Collection<T> collection;
    }

    static class SubOfBox<S> extends CollectionBox<S> {}

    static class ConcreteSubOfBox extends CollectionBox<Integer> {}

    static class SubOfSubOfBox<S> extends SubOfBox<S> {}

    static class SubOfConcrete extends ConcreteSubOfBox {}

    abstract static class Change {}

    @SuppressWarnings("unused")
    static class ChangeCollection<T extends Change> {
        private List<T> changes;
    }

    static class Box<X> {}

    @SuppressWarnings("unused")
    static class WithBox<T> {
        Box<T> box;
    }

    @SuppressWarnings("unused")
    static class WithBoxArray<T> {
        Box<T>[] boxArray;
    }

    @SuppressWarnings("unused")
    static class WithArrayBox<T> {
        Box<T[]> arrayBox;
    }

    @SuppressWarnings("unused")
    static class WithBoxArrayTwoDim<T> {
        private Box<T>[][] boxArrayTwoDim;
    }

    static class ArgToArray<T> extends WithBox<T[]> {}

    static class ArgToArrayTwoDim<T> extends WithBox<T[][]> {}

    static class ArgToArrayWithArray<T> extends WithBoxArray<T[]> {}
}
