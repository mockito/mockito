package org.mockito.stubbing;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.InvocationOnMock;
import org.objenesis.ObjenesisHelper;

public class ClonesArguments implements Answer<Object> {
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            Object from = arguments[i];
            Object newInstance = ObjenesisHelper.newInstance(from.getClass());
            new LenientCopyTool().copyToRealObject(from, newInstance);
            arguments[i] = newInstance;
        }
        return new ReturnsEmptyValues().answer(invocation);
    }
}