package org.mockito.internal;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.MockitoMethodProxy;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MockitoMethod;

public interface IMockHandler extends Serializable {

    Object handle(Invocation invocation) throws Throwable;
    
    MockitoMethodProxy createMockitoMethodProxy(MethodProxy methodProxy);

    MockitoMethod createMockitoMethod(Method method);
}
