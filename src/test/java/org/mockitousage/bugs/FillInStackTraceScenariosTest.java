/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class FillInStackTraceScenariosTest extends TestBase {

    @Mock IMethods mock;

    private class SomeException extends RuntimeException {}

    class NullStackTraceException extends RuntimeException {
        public Exception fillInStackTrace() {
            return null;
        }
    }

    class NewStackTraceException extends RuntimeException {
        public Exception fillInStackTrace() {
            return new SomeException();
        }
    }

    //issue 866
    @Test
    public void avoids_NPE() {
        when(mock.simpleMethod()).thenThrow(new NullStackTraceException());
        try {
            mock.simpleMethod();
            fail();
        } catch(NullStackTraceException e) {}
    }

    @Test
    public void uses_return_value_from_fillInStackTrace() {
        when(mock.simpleMethod()).thenThrow(new NewStackTraceException());
        try {
            mock.simpleMethod();
            fail();
        } catch(SomeException e) {}
    }
}
