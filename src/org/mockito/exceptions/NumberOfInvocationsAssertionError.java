/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import org.mockito.internal.ExpectedInvocation;

public class NumberOfInvocationsAssertionError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public NumberOfInvocationsAssertionError(int expectedCount, int actualCount, ExpectedInvocation expected) {
        super(  "\n" +
                expected.toString() +
        		"\n" +
        		"Expected " + pluralize(expectedCount) + " but was " + actualCount);
    }

    private static String pluralize(int expectedInvoked) {
        return expectedInvoked == 1 ? "1 time" : expectedInvoked + " times";
    }
}