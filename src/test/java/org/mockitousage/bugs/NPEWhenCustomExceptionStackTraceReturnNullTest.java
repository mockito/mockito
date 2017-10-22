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

public class NPEWhenCustomExceptionStackTraceReturnNullTest extends TestBase {

    @Mock
    IMethods mock;

    class NullStackTraceException extends RuntimeException {
        @Override
        public Exception fillInStackTrace() {
            return null;
        }
    }

    //issue 866
    @Test
    public void shouldNotThrowNPE() {
        when(mock.simpleMethod()).thenThrow(new NullStackTraceException());
        try {
            mock.simpleMethod();
            fail();
        } catch(NullStackTraceException e) {}
    }
}
