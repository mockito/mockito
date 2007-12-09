package org.mockito.exceptions;

import org.junit.Test;


public class ExceptionsTest {

    @Test(expected=TooLittleActualInvocationsError.class)
    public void shouldLetPassingNullLastActualStackTrace() throws Exception {
        Exceptions.tooLittleActualInvocations(1, 2, "wanted", null);
    }
}