package org.mockito.exceptions;

public class VerificationAssertionError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public VerificationAssertionError(String message) {
        super(message);
    }
}
