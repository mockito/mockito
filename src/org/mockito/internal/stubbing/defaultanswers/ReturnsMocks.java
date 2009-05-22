/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsMocks implements Answer<Object> {
    
    private MockitoCore mockitoCore = new MockitoCore();
    private Answer<Object> delegate = new ReturnsMoreEmptyValues();
    
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object ret = delegate.answer(invocation);
        if (ret != null) {
            return ret;
        }
            
        return returnValueFor(invocation.getMethod().getReturnType());
    }

    @SuppressWarnings("unchecked")
    Object returnValueFor(Class<?> clazz) {
        if (!ClassImposterizer.INSTANCE.canImposterise(clazz)) {
            return null;
        }
        
        return mockitoCore.mock((Class) clazz, new MockSettingsImpl().defaultAnswer(this));
    }
}