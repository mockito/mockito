/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stacktrace;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.runners.MockitoJUnit44Runner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

//This is required to make sure stack trace is well filtered when runner is ON
@SuppressWarnings("deprecation")
@RunWith(MockitoJUnit44Runner.class)
public class PointingStackTraceToActualInvocationTest extends TestBase {
    
    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;

    @Before
    public void setup() {
        first();
        second();
        third();
        fourth();
    }

    private void first() {
        mock.simpleMethod(1);
    }
    private void second() {
        mockTwo.simpleMethod(2);
    }
    private void third() {
        mock.simpleMethod(3);
    }
    private void fourth() {
        mockTwo.simpleMethod(4);
    }
    
    @Test
    public void shouldPointToTooManyInvocationsChunkOnError() {
        try {
            verify(mock, times(0)).simpleMethod(1);
            fail();
        } catch (NeverWantedButInvoked e) {
            assertContains("first(", e.getMessage());
        }
    }   
    
    @Test
    public void shouldNotPointStackTracesToRunnersCode() {
        try {
            verify(mock, times(0)).simpleMethod(1);
            fail();
        } catch (NeverWantedButInvoked e) {
            assertNotContains(".runners.", e.getMessage());
        }
    }   
}