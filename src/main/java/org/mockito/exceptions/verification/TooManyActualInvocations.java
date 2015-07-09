/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

public class TooManyActualInvocations extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public TooManyActualInvocations(String message) {
        super(message);
    }
}
