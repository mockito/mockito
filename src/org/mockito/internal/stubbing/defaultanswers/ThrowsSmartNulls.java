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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
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
 * Unlike ReturnsSmartNulls, ThrowsSmartNulls does <b>not</b> try to return ordinary return values
 * before it tries to return SmartNull.
 *
 * If the return type is not mockable (e.g. a primitive or final) then a smart null exception is
 * thrown immediately.
 */
public class ThrowsSmartNulls implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 7144947647279773153L;

    public Object answer(final InvocationOnMock unstubbedInvocation) throws Throwable {
        Class<?> type = unstubbedInvocation.getMethod().getReturnType();
        final Location location = new LocationImpl();
        if (type.isPrimitive() || Modifier.isFinal(type.getModifiers())) {
            new Reporter().smartNullPointerException(unstubbedInvocation.toString(), location);
        } else {
            return Mockito.mock(type, new ThrowsSmartNullPointer(unstubbedInvocation, location));
        }
        return null;
    }
}
