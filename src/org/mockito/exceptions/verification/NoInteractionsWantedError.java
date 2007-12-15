/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

/**
 * No interactions wanted. See exception's cause for location of undesired invocation.
 */
public class NoInteractionsWantedError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public NoInteractionsWantedError(String message, Throwable cause) {
        super(message, cause);
    }
}