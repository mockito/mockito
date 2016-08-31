package org.mockito.stubbing;

/**
 * Two parameter void function
 *
 * @param <A> input parameter 1 type
 * @param <B> input parameter 2 type
 * @param <C> input parameter 3 type
 * @param <D> input parameter 4 type
 * @param <E> input parameter 5 type
 */
public interface VoidAnswer5<A, B, C, D, E> {
    void answer(A a, B b, C c, D d, E e);
}
