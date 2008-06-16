/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTraceThrowableWrapper;
import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.invocation.Invocation;

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
            public Object answer() throws Throwable {
                return value;
            }
        };
    }
    
    public Answer createThrowingAnswer(final Throwable throwable, Invocation invocation) {
        validateThrowable(throwable, invocation);
        return new Answer<Object>() {
            public Object answer() throws Throwable {
                if (throwable == DontThrow.DONT_THROW) {
                    return null;
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