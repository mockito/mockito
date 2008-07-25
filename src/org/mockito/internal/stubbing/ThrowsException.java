/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.base.HasStackTraceThrowableWrapper;
import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ThrowsException implements Answer<Object> {

    private final Throwable throwable;
    private final StackTraceFilter filter = new StackTraceFilter();

    public ThrowsException(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        Throwable filtered = throwable.fillInStackTrace();
        filter.filterStackTrace(new HasStackTraceThrowableWrapper(filtered));
        throw filtered;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}