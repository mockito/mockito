package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class TriesToReturnSelf implements Answer<Object>, Serializable{

    private final MockUtil mockUtil = new MockUtil();

    private ReturnsEmptyValues defaultReturn = new ReturnsEmptyValues();

    public Object answer(InvocationOnMock invocation) throws Throwable {
        Class<?> methodReturnType = invocation.getMethod().getReturnType();
        Object mock = invocation.getMock();
        Class<?> mockType = mockUtil.getMockHandler(mock).getMockSettings().getTypeToMock();

        if (methodReturnType.isAssignableFrom(mockType)) {
            return invocation.getMock();
        }

        return defaultReturn.returnValueFor(methodReturnType);
    }

}
