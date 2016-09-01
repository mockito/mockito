package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Two parameter void function
 *
 * @param <A> input parameter 1 type
 * @param <B> input parameter 2 type
 */
@Incubating
public interface VoidAnswer2<A, B> {
    void answer(A a, B b);
}
