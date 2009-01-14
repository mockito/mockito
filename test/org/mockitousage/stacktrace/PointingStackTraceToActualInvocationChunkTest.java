/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.mockito.Mockito.*;
import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.mockito.VerboseMockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

//This is required to make sure stack trace is well filtered when runner is ON
@RunWith(VerboseMockitoJUnitRunner.class)
public class PointingStackTraceToActualInvocationChunkTest extends TestBase {
    
    private IMethods mock;
    private IMethods mockTwo;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
        mockTwo = Mockito.mock(IMethods.class);
        
        firstChunk();
        secondChunk();
        thirdChunk();
        fourthChunk();
    }

    private void firstChunk() {
        mock.simpleMethod(1);
        mock.simpleMethod(1);
    }
    private void secondChunk() {
        mockTwo.simpleMethod(2);
        mockTwo.simpleMethod(2);
    }
    private void thirdChunk() {
        mock.simpleMethod(3);
        mock.simpleMethod(3);
    }
    private void fourthChunk() {
        mockTwo.simpleMethod(4);
        mockTwo.simpleMethod(4);
    }
    
    @Test
    public void shouldPointToActualInvocationOnVerificationError() {
        try {
            verify(mock).simpleMethod(999);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("firstChunk"));
        }
    }
    
    @Test
    public void shouldPointToUnverifiedActualInvocationOnVerificationError() {
        verify(mock, atLeastOnce()).simpleMethod(1);
        try {
            verify(mock, atLeastOnce()).simpleMethod(999);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("thirdChunk"));
        }
    }   
    
    @Test
    public void shouldPointToTooLittleInvocationsChunkOnError() {
        verify(mock, atLeastOnce()).simpleMethod(1);
        try {
            verify(mock, times(3)).simpleMethod(3);
            fail();
        } catch (TooLittleActualInvocations e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("thirdChunk"));
        }
    }   
    
    @Test
    public void shouldPointToTooManyInvocationsChunkOnError() {
        try {
            verify(mock).simpleMethod(1);
            fail();
        } catch (TooManyActualInvocations e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("firstChunk"));
        }
    }   
}