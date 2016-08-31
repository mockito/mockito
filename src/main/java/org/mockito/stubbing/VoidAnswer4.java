package org.mockito.stubbing;

/**
 * Two parameter void function
 *
 * @param <A> input parameter 1 type
 * @param <B> input parameter 2 type
 * @param <C> input parameter 3 type
 * @param <D> input parameter 4 type
 */
public interface VoidAnswer4<A, B, C, D> {
    void answer(A a, B b, C c, D d);
}
