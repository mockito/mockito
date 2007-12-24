package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

public class WantedButNotInvokedError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public WantedButNotInvokedError(String message, Throwable cause) {
        super(message, cause);
    }
}
