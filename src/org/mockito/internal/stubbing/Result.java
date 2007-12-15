/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.base.HasStackTraceThrowableWrapper;
import org.mockito.exceptions.base.StackTraceFilter;

@SuppressWarnings("unchecked")
public class Result implements IAnswer {

    private IAnswer value;

    private Result(IAnswer value) {
        this.value = value;
    }

    public static Result createThrowResult(final Throwable throwable, final StackTraceFilter filter) {
        return new Result(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                Throwable filtered = throwable.fillInStackTrace();
                filter.filterStackTrace(new HasStackTraceThrowableWrapper(filtered));
                throw filtered;
            }
        });
    }
    public static Result createReturnResult(final Object value) {
        return new Result(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                return value;
            }
        });
    }

    public Object answer() throws Throwable {
        return value.answer();
    }
}
