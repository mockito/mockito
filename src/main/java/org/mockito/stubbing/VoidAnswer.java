package org.mockito.stubbing;

import org.mockito.invocation.InvocationOnMock;

@FunctionalInterface
public interface VoidAnswer {
    void answer(InvocationOnMock invocation) throws Throwable;
}
