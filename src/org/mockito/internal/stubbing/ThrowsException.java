/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ThrowsException implements Answer<Object> {

    private final Throwable throwable;
    private final StackTraceFilter filter = new StackTraceFilter();

    public ThrowsException(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (MockUtil.isMock(throwable)) {
            throw throwable;
        }
        Throwable t = throwable.fillInStackTrace();
        filter.filterStackTrace(t);
        throw t;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}