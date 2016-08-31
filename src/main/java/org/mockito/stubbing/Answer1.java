package org.mockito.stubbing;

/**
 * One parameter function which returns something
 *
 * @param <T> return type
 * @param <A> input parameter 1 type
 */
public interface Answer1<T, A> {
    T answer(A a);
}

