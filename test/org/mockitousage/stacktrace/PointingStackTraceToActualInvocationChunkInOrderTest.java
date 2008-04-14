/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockito.exceptions.verification.VerifcationInOrderFailure;
import org.mockitousage.IMethods;

public class PointingStackTraceToActualInvocationChunkInOrderTest extends TestBase {
    
    private IMethods mock;
    private IMethods mockTwo;
    private InOrder inOrder;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
        mockTwo = Mockito.mock(IMethods.class);
        inOrder = inOrder(mock, mockTwo);
        
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
    public void shouldPointStackTraceToPreviousInvocation() {
        inOrder.verify(mock, times(2)).simpleMethod(anyInt());
        inOrder.verify(mockTwo, times(2)).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mock).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("secondChunk"));
        }
    }
    
    @Test
    public void shouldPointToThirdInteractionBecauseAtLeastOnceUsed() {
        inOrder.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("thirdChunk"));
        }
    }
    
    @Test
    public void shouldPointToThirdChunkWhenTooLittleActualInvocations() {
        inOrder.verify(mock, times(2)).simpleMethod(anyInt());
        inOrder.verify(mockTwo, times(2)).simpleMethod(anyInt());
        inOrder.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(3)).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("thirdChunk"));
        }
    }
    
    @Test
    public void shouldPointToFourthChunkBecauseTooManyActualInvocations() {
        inOrder.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(0)).simpleMethod(anyInt());
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("fourthChunk"));
        }
    }
}