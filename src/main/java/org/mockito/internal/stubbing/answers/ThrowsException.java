/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import static org.mockito.internal.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.internal.exceptions.Reporter.checkedExceptionInvalid;

public class ThrowsException implements Answer<Object>, ValidableAnswer, Serializable {

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

        if (t == null) {
            throw throwable;
        }
        filter.filter(t);
        throw t;
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        if (throwable == null) {
            throw cannotStubWithNullThrowable();
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }

        if (!new InvocationInfo(invocation).isValidException(throwable)) {
            throw checkedExceptionInvalid(throwable);
        }
    }
}
