/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

/**
 * @deprecated as of 2.27.5. Please use {@link TooFewActualInvocations} instead.
 * @see TooFewActualInvocations
 */
@Deprecated
public class TooLittleActualInvocations extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public TooLittleActualInvocations(String message) {
        super(message);
    }
}
