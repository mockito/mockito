/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.util.ExtraMatchers.hasFirstMethodInStackTrace;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.StackTraceFilter;

public class ResultTest extends TestBase {

    @Test
    public void shouldCreateReturnResult() throws Throwable {
        Result result = Result.createReturnResult("lol");
        assertEquals("lol", result.answer());
    }
    
    @Test(expected=RuntimeException.class)
    public void shouldCreateThrowResult() throws Throwable {
        Result.createThrowResult(new RuntimeException(), new StackTraceFilter()).answer();
    }
    
    @Test
    public void shouldFilterStackTraceWhenCreatingThrowResult() throws Throwable {
        StackTraceFilterStub filterStub = new StackTraceFilterStub();
        Result result = Result.createThrowResult(new RuntimeException(), filterStub);
        try {
            result.answer(); 
            fail();
        } catch (RuntimeException e) {
            assertTrue(Arrays.equals(filterStub.hasStackTrace.getStackTrace(), e.getStackTrace()));
            assertThat("should fill in stack trace", e, hasFirstMethodInStackTrace("answer"));
        }
    }
    
    class StackTraceFilterStub extends StackTraceFilter {
        HasStackTrace hasStackTrace;
        @Override public void filterStackTrace(HasStackTrace hasStackTrace) {
            this.hasStackTrace = hasStackTrace;
        }
    }
}
