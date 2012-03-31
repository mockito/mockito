/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.configuration.ClassPathLoader;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MockitoInvocationHandler;
import org.mockito.stubbing.Answer;

/**
 * Optional Answer that can be used with
 * {@link Mockito#mock(Class, Answer)}
 * <p>
 * This implementation can be helpful when working with legacy code. Unstubbed
 * methods often return null. If your code uses the object returned by an
 * unstubbed call you get a NullPointerException. This implementation of
 * Answer returns SmartNulls instead of nulls.
 * SmartNull gives nicer exception message than NPE because it points out the
 * line where unstubbed method was called. You just click on the stack trace.
 * <p>
 * ReturnsSmartNulls first tries to return ordinary return values (see
 * {@link ReturnsMoreEmptyValues}) then it tries to return SmartNull. If the
 * return type is not mockable (e.g. final) then ordinary null is returned.
 * <p>
 * ReturnsSmartNulls will be probably the default return values strategy in
 * Mockito 2.0
 */
public class ReturnsSmartNulls implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 7618312406617949441L;
    private static MockMaker mockMaker = ClassPathLoader.getMockMaker();

    private final class ThrowingInterceptor implements MockitoInvocationHandler {
        private final InvocationOnMock invocation;
        private final Location location = new LocationImpl();

        private ThrowingInterceptor(InvocationOnMock invocation) {
            this.invocation = invocation;
        }

        public Object handle(Invocation nullDereference) throws Throwable {
            if (new ObjectMethodsGuru().isToString(nullDereference.getMethod())) {
                return "SmartNull returned by this unstubbed method call on a mock:\n" +
                        invocation.toString();
            }

            new Reporter().smartNullPointerException(invocation.toString(), location);
            return null;
        }
    }

    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    public Object answer(final InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = delegate.answer(invocation);
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        Class<?> type = invocation.getMethod().getReturnType();
        if (!type.isPrimitive() && !Modifier.isFinal(type.getModifiers())) {
            ThrowingInterceptor handler = new ThrowingInterceptor(invocation);
            return mockMaker.createMock(type, new Class[0], handler, new MockSettingsImpl());
        }
        return null;
    }
}
