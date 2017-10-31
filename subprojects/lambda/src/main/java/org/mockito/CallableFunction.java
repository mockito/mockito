package org.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public interface CallableFunction<A, R> extends Answer<R> {

    R answer(A first) throws Throwable;

    @SuppressWarnings("unchecked")
    @Override
    default R answer(InvocationOnMock invocation) throws Throwable {
        return this.answer((A) invocation.getArgument(0));
    }
}
