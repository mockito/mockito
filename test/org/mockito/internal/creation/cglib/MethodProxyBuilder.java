package org.mockito.internal.creation.cglib;

import static org.mockito.Mockito.*;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.invocation.ExposedInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;

public class MethodProxyBuilder {

    public MethodProxy build() {
        IMethods mock = mock(IMethods.class);
        when(mock.objectReturningMethodNoArgs()).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation;
            }});
        
        Invocation i = (Invocation) mock.objectReturningMethodNoArgs();
        return new ExposedInvocation(i).getMethodProxy();
    }
}