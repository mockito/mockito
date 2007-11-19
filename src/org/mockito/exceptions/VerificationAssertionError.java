package org.mockito.exceptions;

import org.mockito.exceptions.stacktrace.*;


public class VerificationAssertionError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    private VerificationAssertionError(String message, StackTraceFilter filter) {
        super(message, filter);
    }
    
    public static VerificationAssertionError createNotInvokedError(String message) {
        return new VerificationAssertionError(message, new LastClassIsCglibEnchantedFilter());
    }

    public static VerificationAssertionError createNoMoreInteractionsError(String message) {
        return new VerificationAssertionError(message, new LastClassIsMockitoFilter());
    }
}
