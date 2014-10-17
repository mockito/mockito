/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.creation.cglib;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.creation.util.MockitoMethodProxy;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.realmethod.CleanTraceRealMethod;
import org.mockito.internal.progress.SequenceNumber;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Should be one instance per mock instance, see CglibMockMaker.
 */
class MethodInterceptorFilter implements MethodInterceptor, Serializable {

    private static final long serialVersionUID = 6182795666612683784L;
    private final InternalMockHandler handler;
    final ObjectMethodsGuru objectMethodsGuru = new ObjectMethodsGuru();
    private final MockCreationSettings mockSettings;
    private final AcrossJVMSerializationFeature acrossJVMSerializationFeature = new AcrossJVMSerializationFeature();

    public MethodInterceptorFilter(InternalMockHandler handler, MockCreationSettings mockSettings) {
        this.handler = handler;
        this.mockSettings = mockSettings;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (objectMethodsGuru.isEqualsMethod(method)) {
            return proxy == args[0];
        } else if (objectMethodsGuru.isHashCodeMethod(method)) {
            return hashCodeForMock(proxy);
        } else if (acrossJVMSerializationFeature.isWriteReplace(method)) {
            return acrossJVMSerializationFeature.writeReplace(proxy);
        }
        
        MockitoMethodProxy mockitoMethodProxy = createMockitoMethodProxy(methodProxy);
        new CGLIBHacker().setMockitoNamingPolicy(methodProxy);
        
        MockitoMethod mockitoMethod = createMockitoMethod(method);
        
        CleanTraceRealMethod realMethod = new CleanTraceRealMethod(mockitoMethodProxy);
        Invocation invocation = new InvocationImpl(proxy, mockitoMethod, args, SequenceNumber.next(), realMethod);
        return handler.handle(invocation);
    }
   
    public MockHandler getHandler() {
        return handler;
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