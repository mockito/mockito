/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.instance.InstantiatorProvider;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Modifier;


/**
 * A MockMaker that uses cglib to generate mocks on a JVM.
 */
public class CglibMockMaker implements MockMaker {

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        InternalMockHandler mockitoHandler = cast(handler);
        new AcrossJVMSerializationFeature().enableSerializationAcrossJVM(settings);
        return new ClassImposterizer(new InstantiatorProvider().getInstantiator(settings)).imposterise(
                new MethodInterceptorFilter(mockitoHandler, settings), settings.getTypeToMock(), settings.getExtraInterfaces());
    }

    private InternalMockHandler cast(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException("At the moment you cannot provide own implementations of MockHandler." +
                    "\nPlease see the javadocs for the MockMaker interface.");
        }
        return (InternalMockHandler) handler;
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((Factory) mock).setCallback(0, new MethodInterceptorFilter(cast(newHandler), settings));
    }

    @Override
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

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers());
            }

            @Override
            public String nonMockableReason() {
                if(type.isPrimitive()) {
                    return "primitive type";
                }
                if(Modifier.isFinal(type.getModifiers())) {
                    return "final or anonymous class";
                }
                return join("not handled type");
            }
        };
    }
}
