/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class ReturnsMocks implements Answer<Object>, Serializable {
    
    private static final long serialVersionUID = -6755257986994634579L;
    private final MockitoCore mockitoCore = new MockitoCore();
    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();
    
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object ret = delegate.answer(invocation);
        if (ret != null) {
            return ret;
        }
            
        return returnValueFor(invocation.getMethod().getReturnType());
    }

    Object returnValueFor(Class<?> clazz) {
        if (!mockitoCore.isTypeMockable(clazz)) {
            return null;
        }
        
        return mockitoCore.mock(clazz, new MockSettingsImpl().defaultAnswer(this));
    }
}
