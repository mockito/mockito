/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.creation.jmock.ClassImposterizer;

/**
 * A MockMaker that uses cglib to generate mocks on a JVM.
 */
public final class CglibMockMaker implements MockMaker {

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        InternalMockHandler mockitoHandler = cast(handler);
        new AcrossJVMSerializationFeature().enableSerializationAcrossJVM(settings);
        return ClassImposterizer.INSTANCE.imposterise(
                new MethodInterceptorFilter(mockitoHandler, settings), settings.getTypeToMock(), settings.getExtraInterfaces());
    }

    private InternalMockHandler cast(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException("At the moment you cannot provide own implementations of MockHandler." +
                    "\nPlease see the javadocs for the MockMaker interface.");
        }
        return (InternalMockHandler) handler;
    }

    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((Factory) mock).setCallback(0, new MethodInterceptorFilter(cast(newHandler), settings));
    }

    public MockHandler getHandler(Object mock) {
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
