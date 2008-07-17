package org.mockito.internal.stubbing;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsVoid implements Answer<Object> {
    
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return null;
    }
}