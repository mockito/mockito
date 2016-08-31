package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Three parameter function which returns something
 *
 * @param <T> return type
 * @param <A> input parameter 1 type
 * @param <B> input parameter 2 type
 * @param <C> input parameter 3 type
 */
@Incubating
public interface Answer3<T, A, B, C> {
    T answer(A a, B b, C c);
}
