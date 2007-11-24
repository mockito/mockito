package org.mockito.exceptions;

public class StrictVerificationError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public StrictVerificationError() {
        super("blah");
    }
}
