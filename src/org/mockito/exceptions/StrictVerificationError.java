/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

public class StrictVerificationError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public StrictVerificationError() {
        super("blah");
    }
}
