package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * One parameter void function
 *
 * @param <A> input parameter 1 type
 */
@Incubating
public interface VoidAnswer1<A> {
    void answer(A a);
}
