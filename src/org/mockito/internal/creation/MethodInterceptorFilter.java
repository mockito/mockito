/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.IMockHandler;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.*;
import org.mockito.internal.invocation.realmethod.FilteredCGLIBProxyRealMethod;
import org.mockito.internal.progress.SequenceNumber;
import org.mockito.internal.util.ObjectMethodsGuru;

public class MethodInterceptorFilter implements MethodInterceptor, Serializable {

    private static final long serialVersionUID = 6182795666612683784L;
    private final IMockHandler mockHandler;
    CGLIBHacker cglibHacker = new CGLIBHacker();
    ObjectMethodsGuru objectMethodsGuru = new ObjectMethodsGuru();
    private final MockSettingsImpl mockSettings;

    public MethodInterceptorFilter(IMockHandler mockHandler, MockSettingsImpl mockSettings) {
        this.mockHandler = mockHandler;
        this.mockSettings = mockSettings;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (objectMethodsGuru.isEqualsMethod(method)) {
            return proxy == args[0];
        } else if (objectMethodsGuru.isHashCodeMethod(method)) {
            return hashCodeForMock(proxy);
        }
        
        MockitoMethodProxy mockitoMethodProxy = createMockitoMethodProxy(methodProxy);
        cglibHacker.setMockitoNamingPolicy(mockitoMethodProxy);
        
        MockitoMethod mockitoMethod = createMockitoMethod(method);
        
        FilteredCGLIBProxyRealMethod realMethod = new FilteredCGLIBProxyRealMethod(mockitoMethodProxy);
        Invocation invocation = new Invocation(proxy, mockitoMethod, args, SequenceNumber.next(), realMethod);
        return mockHandler.handle(invocation);
    }
   
    public IMockHandler getMockHandler() {
        return mockHandler;
    }

    private int hashCodeForMock(Object mock) {
        return System.identityHashCode(mock);
    }

    public MockitoMethodProxy createMockitoMethodProxy(MethodProxy methodProxy) {
        if (mockSettings.isSerializable())
            return new SerializableMockitoMethodProxy(methodProxy);
        return new DelegatingMockitoMethodProxy(methodProxy);
    }
    
    public MockitoMethod createMockitoMethod(Method method) {
        if (mockSettings.isSerializable()) {
            return new SerializableMethod(method);
        } else {
            return new DelegatingMethod(method); 
        }
    }
}