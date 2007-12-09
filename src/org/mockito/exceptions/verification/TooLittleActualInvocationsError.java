package org.mockito.exceptions.verification;

import org.mockito.exceptions.parents.MockitoAssertionError;

public class TooLittleActualInvocationsError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;
    
    public TooLittleActualInvocationsError(String message, Throwable cause) {
        super(message, cause);
    }
}
