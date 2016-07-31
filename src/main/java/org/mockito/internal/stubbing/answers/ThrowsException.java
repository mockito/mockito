/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class ThrowsException implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 1128820328555183980L;
    private final Throwable throwable;
    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    public ThrowsException(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (MockUtil.isMock(throwable)) {
            throw throwable;
        }
        Throwable t = throwable.fillInStackTrace();
        filter.filter(t);
        throw t;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}