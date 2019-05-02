/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.internal.exceptions.Reporter.smartNullPointerException;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.io.Serializable;
import org.mockito.Mockito;
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
 * ReturnsSmartNulls first tries to return ordinary return values (see
 * {@link ReturnsMoreEmptyValues}) then it tries to return SmartNull. If the
 * return type is not mockable (e.g. final) then ordinary null is returned.
 * <p>
 * ReturnsSmartNulls will be probably the default return values strategy in
 * Mockito 2.1.0
 */
public class ReturnsSmartNulls implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 7618312406617949441L;

    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    @Override
    public Object answer(final InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = delegate.answer(invocation);

        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }

        return RetrieveGenericsForDefaultAnswers.returnTypeForMockWithCorrectGenerics(invocation,
            new RetrieveGenericsForDefaultAnswers.AnswerCallback() {
                @Override
                public Object apply(Class<?> type) {
                    if (type == null) {
                        return null;
                    }

                    return Mockito.mock(type, new ThrowsSmartNullPointer(invocation, new LocationImpl()));
                }
            });
    }

    private static class ThrowsSmartNullPointer implements Answer {

        private final InvocationOnMock unstubbedInvocation;

        private final Location location;

        ThrowsSmartNullPointer(InvocationOnMock unstubbedInvocation, Location location) {
            this.unstubbedInvocation = unstubbedInvocation;
            this.location = location;
        }

        public Object answer(InvocationOnMock currentInvocation) throws Throwable {
            if (isToStringMethod(currentInvocation.getMethod())) {
                return "SmartNull returned by this unstubbed method call on a mock:\n" +
                    unstubbedInvocation.toString();
            }

            throw smartNullPointerException(unstubbedInvocation.toString(), location);
        }
    }
}
