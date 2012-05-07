/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.internal.creation.MockitoMethodProxy;
import org.mockito.internal.invocation.ExposedInvocation;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;

public class MethodProxyBuilder {

    public MockitoMethodProxy build() {
        IMethods mock = mock(IMethods.class);
        when(mock.objectReturningMethodNoArgs()).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation;
            }});
        
        InvocationImpl i = (InvocationImpl) mock.objectReturningMethodNoArgs();
        return new ExposedInvocation(i).getMethodProxy();
    }
}