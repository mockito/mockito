/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.base.HasStackTraceThrowableWrapper;
import org.mockito.exceptions.base.StackTraceFilter;

@SuppressWarnings("unchecked")
public class AnswerFactory {

    public static Answer createThrowingAnswer(final Throwable throwable, final StackTraceFilter filter) {
        return new Answer<Object>() {
            public Object answer() throws Throwable {
                Throwable filtered = throwable.fillInStackTrace();
                filter.filterStackTrace(new HasStackTraceThrowableWrapper(filtered));
                throw filtered;
            }
        };
    }
    
    public static Answer createReturningAnswer(final Object value) {
        return new Answer<Object>() {
            public Object answer() throws Throwable {
                return value;
            }
        };
    }
}