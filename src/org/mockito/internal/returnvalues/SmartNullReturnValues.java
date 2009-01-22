/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.mockito.Mockito;
import org.mockito.ReturnValues;
import org.mockito.exceptions.cause.BecauseThisMethodWasNotStubbed;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

/**
 * Optional ReturnValues to be used with
 * {@link Mockito#mock(Class, ReturnValues)}
 * <p>
 * {@link ReturnValues} defines the return values of unstubbed calls.
 * <p>
 * This implementation can be helpful when working with legacy code. Unstubbed
 * methods often return null. If your code uses the object returned by an
 * unstubbed call you get a NullPointerException. This implementation of
 * ReturnValues makes unstubbed methods return SmartNulls instead of nulls.
 * SmartNull gives nicer exception message than NPE because it points out the
 * line where unstubbed method was called. You just click on the stack trace.
 * <p>
 * SmartNullReturnValues first tries to return ordinary return values (see
 * {@link MoreEmptyReturnValues}) then it tries to return SmartNull. If the
 * return type is not mockable (e.g. final) then ordinary null is returned.
 * <p>
 * SmartNullReturnValues will be probably the default return values strategy in
 * Mockito 2.0
 */
public class SmartNullReturnValues implements ReturnValues {

    private final class ThrowingInterceptor implements MethodInterceptor {
        private final InvocationOnMock invocation;
        private final Exception whenCreated = new BecauseThisMethodWasNotStubbed("\nBecause this method was not stubbed correctly:");

        private ThrowingInterceptor(InvocationOnMock invocation) {
            this.invocation = invocation;
        }

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (Invocation.isToString(method)) {
                return "SmartNull returned by " + invocation.getMethod().getName() + "() method on mock";
            }
            throw new SmartNullPointerException("\nYou have a NullPointerException here:", whenCreated);
        }
    }

    private final ReturnValues delegate = new MoreEmptyReturnValues();

    public Object valueFor(final InvocationOnMock invocation) {
        Object defaultReturnValue = delegate.valueFor(invocation);
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        Class<?> type = invocation.getMethod().getReturnType();
        if (ClassImposterizer.INSTANCE.canImposterise(type)) {
            return ClassImposterizer.INSTANCE.imposterise(new ThrowingInterceptor(invocation), type);
        }
        return null;
    }
}