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

/**
 * These tests check that ThrowsException#answer throws an instance returned
 * by Throwable#fillInStackTrace of the provided throwable.
 *
 * <p>A well-behaved Throwable implementation must always return a reference to this
 * from #fillInStackTrace according to the method contract.
 * However, Mockito throws the exception returned from #fillInStackTrace for backwards compatibility
 * (or the provided exception if the method returns null).
 *
 * @see Throwable#fillInStackTrace()
 * @see <a href="https://github.com/mockito/mockito/issues/866">#866</a>
 */
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
