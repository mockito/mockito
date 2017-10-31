package org.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public interface CallableAnswer<R> extends Answer<R> {

    R answer() throws Throwable;

    @Override
    default R answer(InvocationOnMock invocation) throws Throwable {
        return this.answer();
    }
}
