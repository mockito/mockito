package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Three parameter function which returns something
 *
 * @param <T> return type
 * @param <A> input parameter 1 type
 * @param <B> input parameter 2 type
 * @param <C> input parameter 3 type
 * @param <D> input parameter 4 type
 * @param <E> input parameter 5 type
 */
@Incubating
public interface Answer5<T, A, B, C, D, E> {
    T answer(A a, B b, C c, D d, E e);
}
