package org.mockito.stubbing;

/**
 * One parameter void function
 *
 * @param <A> input parameter 1 type
 */
public interface VoidAnswer1<A> {
    void answer(A a);
}
