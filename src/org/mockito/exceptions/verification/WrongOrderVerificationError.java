package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

public class WrongOrderVerificationError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;
    
    public WrongOrderVerificationError(String message, Throwable cause) {
        super(message, cause);
    }
}
