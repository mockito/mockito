package org.mockito.stubbing;

/**
 * Two parameter void function
 *
 * @param <A> input parameter 1 type
 * @param <B> input parameter 2 type
 * @param <C> input parameter 3 type
 */
public interface VoidAnswer3<A, B, C> {
    void answer(A a, B b, C c);
}
