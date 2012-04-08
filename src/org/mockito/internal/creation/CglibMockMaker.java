/*
 * Copyright (c) 2012 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.plugins.MockMaker;
import org.mockito.invocation.MockitoInvocationHandler;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.mock.MockSettingsInfo;

/**
 * A MockMaker that uses cglib to generate mocks on a JVM.
 */
public final class CglibMockMaker implements MockMaker {

    public <T> T createMock(Class<T> typeToMock, Class<?>[] extraInterfaces,
            MockitoInvocationHandler handler, MockSettingsInfo settings) {
        MockHandlerInterface mockitoHandler = cast(handler);
        return ClassImposterizer.INSTANCE.imposterise(
                new MethodInterceptorFilter(mockitoHandler, settings), typeToMock, extraInterfaces);
    }

    private MockHandlerInterface cast(MockitoInvocationHandler handler) {
        if (!(handler instanceof MockHandlerInterface)) {
            throw new MockitoException("At the moment you cannot provide own implementations of MockitoInvocationHandler." +
                    "\nPlease see the javadocs for the MockMaker interface.");
        }
        return (MockHandlerInterface) handler;
    }

    public void resetMock(Object mock, MockitoInvocationHandler newHandler, MockSettingsInfo settings) {
        ((Factory) mock).setCallback(0, new MethodInterceptorFilter(cast(newHandler), settings));
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
