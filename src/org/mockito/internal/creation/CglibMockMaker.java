/*
 * Copyright (c) 2012 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.internal.IMockMaker;
import org.mockito.plugins.MockitoInvocationHandler;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.plugins.MockSettingsInfo;

/**
 * A MockMaker that uses cglib to generate mocks on a JVM.
 */
public final class CglibMockMaker implements IMockMaker {

    public <T> T createMock(Class<T> typeToMock, Class<?>[] extraInterfaces,
            MockitoInvocationHandler handler, MockSettingsInfo settings) {
        return ClassImposterizer.INSTANCE.imposterise(
                new MethodInterceptorFilter(handler, settings), typeToMock, extraInterfaces);
    }
    
    public void resetMock(Object mock, MockitoInvocationHandler newHandler, MockSettingsInfo settings) {
        ((Factory) mock).setCallback(0, new MethodInterceptorFilter(newHandler, settings));
    }

    public MockitoInvocationHandler getHandler(Object mock) {
        if (!(mock instanceof Factory)) {
            return null;
        }
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (!(callback instanceof MethodInterceptorFilter)) {
            return null;
        }
        return ((MethodInterceptorFilter) callback).getHandler();
    }
}
