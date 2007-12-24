/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.Strictly;
import org.mockito.exceptions.verification.TooLittleActualInvocationsError;
import org.mockito.exceptions.verification.TooManyActualInvocationsError;
import org.mockito.exceptions.verification.VerificationError;

public class PointingStackTraceToActualInvocationTest extends RequiresValidState {
    
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
    
    public void shouldPointStackTraceToActualInvocation() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        
        strictly.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        strictly.verify(mockTwo, times(2)).simpleMethod(anyInt());
        
        try {
            strictly.verify(mock).simpleMethod(999);
            fail();
        } catch (VerificationError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("thirdChunk"));
        }
    }
    
    @Test
    public void shouldPointToActualInvocation() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        
        try {
            strictly.verify(mockTwo).simpleMethod(999);
            fail();
        } catch (VerificationError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("secondChunk"));
        }
    }
    
    @Test
    public void shouldPointToUnverifiedInvocation() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        strictly.verify(mockTwo, times(2)).simpleMethod(anyInt());
        
        try {
            strictly.verify(mockTwo, times(3)).simpleMethod(999);
            fail();
        } catch (VerificationError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("fourthChunk"));
        }
    }
    
    @Ignore
    @Test
    public void shouldPointToTooManyInvocationsChunk() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        
        try {
            strictly.verify(mockTwo).simpleMethod(anyInt());
            fail();
        } catch (TooManyActualInvocationsError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("firstChunk"));
        }
    }
    
    @Ignore
    @Test
    public void shouldPointToTooLittleInvocationsUnverifiChunk() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        
        try {
            strictly.verify(mockTwo).simpleMethod(anyInt());
            fail();
        } catch (TooLittleActualInvocationsError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("firstChunk"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerify() {
        verify(mock, atLeastOnce()).simpleMethod(anyInt());
        
        try {
            verify(mockTwo).simpleMethod(999);
            fail();
        } catch (VerificationError e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("secondChunk"));
        }
    }
}