/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
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

    private final class ThrowingInterceptor implements MethodInterceptor {
        private final InvocationOnMock invocation;
        private final Location location = new Location();

        private ThrowingInterceptor(InvocationOnMock invocation) {
            this.invocation = invocation;
        }

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (new ObjectMethodsGuru().isToString(method)) {
                return "SmartNull returned by unstubbed " + formatMethodCall()  + " method on mock";
            }

            new Reporter().smartNullPointerException(location);
            return null;
        }

		private String formatMethodCall() {
			String args = Arrays.toString(invocation.getArguments());
			return invocation.getMethod().getName() + "(" + args.substring(1, args.length() - 1) +	")";
		}
    }

    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    public Object answer(final InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = delegate.answer(invocation);
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
