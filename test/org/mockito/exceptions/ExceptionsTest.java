package org.mockito.exceptions;

import org.junit.Test;
import org.mockito.util.RequiresValidState;


public class ExceptionsTest extends RequiresValidState {

    @Test(expected=TooLittleActualInvocationsError.class)
    public void shouldLetPassingNullLastActualStackTrace() throws Exception {
        Exceptions.tooLittleActualInvocations(1, 2, "wanted", null);
    }
}