package org.mockito.exceptions;


public class NumberOfInvocationsAssertionError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public NumberOfInvocationsAssertionError(int expectedInvoked, int actuallyInvoked) {
        super("Expected to be invoked " + expectedInvoked + " times but was " + actuallyInvoked);
    }
}
