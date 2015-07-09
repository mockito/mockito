/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.answers;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.objenesis.ObjenesisHelper;

import java.io.Serializable;

public class ThrowsExceptionClass implements Answer<Object>, Serializable {

    private final Class<? extends Throwable> throwableClass;
    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    public ThrowsExceptionClass(Class<? extends Throwable> throwableClass) {
        this.throwableClass = throwableClass;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        //TODO centralize the use of Objenesis. Why do we use ObjenesisHelper?
        Throwable throwable = (Throwable) ObjenesisHelper.newInstance(throwableClass);
        throwable.fillInStackTrace();
        filter.filter(throwable);
        throw throwable;
    }

    public Class<? extends Throwable> getThrowableClass() {
        return throwableClass;
    }
}
