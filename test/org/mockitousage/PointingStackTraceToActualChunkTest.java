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
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.Strictly;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.InvocationDiffersFromActual;

public class PointingStackTraceToActualChunkTest extends RequiresValidState {
    
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
        } catch (InvocationDiffersFromActual e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("thirdChunk"));
        }
    }
    
    @Test
    public void shouldPointToActualInvocation() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        
        try {
            strictly.verify(mockTwo).simpleMethod(999);
            fail();
        } catch (InvocationDiffersFromActual e) {
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
        } catch (InvocationDiffersFromActual e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("fourthChunk"));
        }
    }
    
    @Test
    public void shouldPointToTooManyInvocationsChunk() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        
        try {
            strictly.verify(mockTwo).simpleMethod(anyInt());
            fail();
        } catch (TooManyActualInvocations e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("secondChunk"));
        }
    }
    
    @Test
    public void shouldPointToTooLittleInvocationsUnverifiedChunk() {
        Strictly strictly = createStrictOrderVerifier(mock, mockTwo);
        strictly.verify(mockTwo, times(2)).simpleMethod(anyInt());
        
        try {
            strictly.verify(mockTwo, times(3)).simpleMethod(anyInt());
            fail();
        } catch (TooLittleActualInvocations e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("fourthChunk"));
        }
    }
    
    @Test
    public void shouldPointToActualInvocationOnVerificationError() {
        try {
            verify(mock).simpleMethod(999);
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("firstChunk"));
        }
    }
    
    @Test
    public void shouldPointToUnverifiedActualInvocationOnVerificationError() {
        verify(mock, atLeastOnce()).simpleMethod(1);
        try {
            verify(mock, atLeastOnce()).simpleMethod(999);
            fail();
        } catch (InvocationDiffersFromActual e) {
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