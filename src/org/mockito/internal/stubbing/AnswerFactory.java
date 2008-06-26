/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTraceThrowableWrapper;
import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

@SuppressWarnings("unchecked")
public class AnswerFactory {

    private final StackTraceFilter filter;
    private final Reporter reporter;

    public AnswerFactory(StackTraceFilter filter) {
        this.filter = filter;
        this.reporter = new Reporter();
    }

    public Answer createReturningAnswer(final Object value) {
        return new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return value;
            }
        };
    }

    public Answer createThrowingAnswer(final Throwable throwable, Invocation invocation) {
        validateThrowable(throwable, invocation);
        return new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (throwable instanceof DontThrow) {
                    return ((DontThrow) throwable).getAnswer().answer(invocation);
                }
                Throwable filtered = throwable.fillInStackTrace();
                filter.filterStackTrace(new HasStackTraceThrowableWrapper(filtered));
                throw filtered;
            }
        };
    }

    private void validateThrowable(Throwable throwable, Invocation invocation) {
        if (throwable == null) {
            reporter.cannotStubWithNullThrowable();
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }

        if (!invocation.isValidException(throwable)) {
            reporter.checkedExceptionInvalid(throwable);
        }
    }
}