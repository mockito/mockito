/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.answers;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.objenesis.ObjenesisHelper;

import static org.mockito.internal.exceptions.Reporter.notAnException;

import java.io.Serializable;

public class ThrowsExceptionClass implements Answer<Object>, Serializable {

    private final Class<? extends Throwable> throwableClass;
    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    public ThrowsExceptionClass(Class<? extends Throwable> throwableClass) {
        this.throwableClass = checkNonNullThrowable(throwableClass);
    }

    private Class<? extends Throwable> checkNonNullThrowable(Class<? extends Throwable> throwableClass) {
        if(throwableClass == null || !Throwable.class.isAssignableFrom(throwableClass)) {
            throw notAnException();
        }
        return throwableClass;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        //TODO centralize the use of Objenesis. Why do we use ObjenesisHelper?
        Throwable throwable = ObjenesisHelper.newInstance(throwableClass);
        throwable.fillInStackTrace();
        filter.filter(throwable);
        throw throwable;
    }

    public Class<? extends Throwable> getThrowableClass() {
        return throwableClass;
    }
}
