package org.mockito.internal.stubbing;

import org.mockito.invocation.InvocationOnMock;

public class Returns implements Answer<Object> {

    private final Object value;

    public Returns(Object value) {
        this.value = value;
    }

    public Returns() {
        this(null);
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return value;
    }
}