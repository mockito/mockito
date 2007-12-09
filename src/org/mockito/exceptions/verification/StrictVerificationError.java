/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification;

import org.mockito.exceptions.parents.MockitoAssertionError;

public class StrictVerificationError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public StrictVerificationError(String string) {
        super(string);
    }
}
