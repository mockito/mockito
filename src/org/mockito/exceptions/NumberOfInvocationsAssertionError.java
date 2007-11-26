/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import org.mockito.internal.ExpectedInvocation;

public class NumberOfInvocationsAssertionError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public NumberOfInvocationsAssertionError(int expectedInvoked, int actuallyInvoked, ExpectedInvocation invocation) {
        super(  "\n" +
                invocation.toString() +
        		"\n" +
        		"Expected " + pluralize(expectedInvoked) + " but was " + actuallyInvoked);
    }

    private static String pluralize(int expectedInvoked) {
        return expectedInvoked == 1 ? "1 time" : expectedInvoked + " times";
    }
}