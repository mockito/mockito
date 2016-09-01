package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * One parameter function which returns something
 *
 * @param <T> return type
 * @param <A> input parameter 1 type
 */
@Incubating
public interface Answer1<T, A> {
    T answer(A a);
}

