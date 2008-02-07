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
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockito.exceptions.verification.VerifcationInOrderFailure;

public class PointingStackTraceToActualInvocationInOrderTest extends TestBase {
    
    private IMethods mock;
    private IMethods mockTwo;
    private InOrder inOrder;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
        mockTwo = Mockito.mock(IMethods.class);
        inOrder = inOrder(mock, mockTwo);
        
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
    public void shouldPointStackTraceToPreviousVerified() {
        inOrder.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        inOrder.verify(mockTwo).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mock).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("fourth"));
        }
    }
    
    @Test
    public void shouldPointToThirdMethod() {
        inOrder.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("third"));
        }
    }
    
    @Test
    public void shouldPointToSecondMethod() {
        inOrder.verify(mock).simpleMethod(anyInt());
        inOrder.verify(mockTwo).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(3)).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("second"));
        }
    }
    
    @Test
    public void shouldPointToFirstMethodBecauseOfTooManyActualInvocations() {
        try {
            inOrder.verify(mock, times(0)).simpleMethod(anyInt());
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("first"));
        }
    }    
    
    @Test
    public void shouldPointToSecondMethodBecauseOfTooManyActualInvocations() {
        inOrder.verify(mock).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(0)).simpleMethod(anyInt());
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("second"));
        }
    }
    
    @Test
    public void shouldPointToFourthMethodBecauseOfTooLittleActualInvocations() {
        inOrder.verify(mock).simpleMethod(anyInt());
        inOrder.verify(mockTwo).simpleMethod(anyInt());
        inOrder.verify(mock).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(3)).simpleMethod(anyInt());
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e.getCause(), hasFirstMethodInStackTrace("fourth"));
        }
    }
}