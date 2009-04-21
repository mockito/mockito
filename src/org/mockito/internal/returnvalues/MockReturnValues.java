/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.mockito.ReturnValues;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;

public class MockReturnValues implements ReturnValues {
    
    private MockitoCore mockitoCore = new MockitoCore();
    private ReturnValues delegate = new MoreEmptyReturnValues();
    
    public Object valueFor(InvocationOnMock invocation) throws Throwable {
        Object ret = delegate.valueFor(invocation);
        if (ret != null) {
            return ret;
        }
            
        return returnValueFor(invocation.getMethod().getReturnType());
    }

    @SuppressWarnings("unchecked")
    Object returnValueFor(Class<?> class1) {
        if (!ClassImposterizer.INSTANCE.canImposterise(class1)) {
            return null;
        }
        
        return mockitoCore.mock((Class) class1, null, null, this);
    }

}
