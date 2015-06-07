/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.objenesis.ObjenesisHelper;

public class ThrowsExceptionClass implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 4118083979304825900L;
    private final Class<? extends Throwable> throwableClass;
    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    public ThrowsExceptionClass(final Class<? extends Throwable> throwableClass) {
        this.throwableClass = throwableClass;
    }

    public Object answer(final InvocationOnMock invocation) throws Throwable {
        //TODO centralize the use of Objenesis. Why do we use ObjenesisHelper?
        final Throwable throwable = ObjenesisHelper.newInstance(throwableClass);
        throwable.fillInStackTrace();
        filter.filter(throwable);
        throw throwable;
    }

    public Class<? extends Throwable> getThrowableClass() {
        return throwableClass;
    }
}
