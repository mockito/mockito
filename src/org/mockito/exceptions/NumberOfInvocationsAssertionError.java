package org.mockito.exceptions;

public class NumberOfInvocationsAssertionError extends AssertionError {

    public NumberOfInvocationsAssertionError(int expectedInvoked, int actuallyInvoked) {
        super("Expected to be invoked " + expectedInvoked + " times but was " + actuallyInvoked);
    }
}
