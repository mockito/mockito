package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

public class StrictVerificationFailure extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public StrictVerificationFailure(String message, Throwable cause) {
        super(message, cause);
    }

    public StrictVerificationFailure(String message) {
        super(message);
    }
}
