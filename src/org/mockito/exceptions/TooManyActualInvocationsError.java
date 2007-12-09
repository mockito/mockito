package org.mockito.exceptions;

import org.mockito.exceptions.parents.MockitoAssertionError;

public class TooManyActualInvocationsError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public TooManyActualInvocationsError(String message, Throwable cause) {
        super(message, cause);
    }
}
