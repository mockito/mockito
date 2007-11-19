package org.mockito.exceptions;

import org.mockito.exceptions.stacktrace.LastClassIsCglibEnchantedFilter;

public class NumberOfInvocationsAssertionError extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public NumberOfInvocationsAssertionError(int expectedInvoked, int actuallyInvoked) {
        super("Expected to be invoked " + expectedInvoked + " times but was " + actuallyInvoked, new LastClassIsCglibEnchantedFilter());
    }
}
