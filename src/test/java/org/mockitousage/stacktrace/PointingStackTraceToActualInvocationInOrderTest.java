/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stacktrace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//This is required to make sure stack trace is well filtered when runner is ON
@RunWith(MockitoJUnitRunner.class)
public class PointingStackTraceToActualInvocationInOrderTest extends TestBase {
    
    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;
    private InOrder inOrder;

    @Before
    public void setup() {
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
        } catch (VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("fourth(");
        }
    }
    
    @Test
    public void shouldPointToThirdMethod() {
        inOrder.verify(mock, atLeastOnce()).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo).simpleMethod(999);
            fail();
        } catch (VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("third(");
        }
    }
    
    @Test
    public void shouldPointToSecondMethod() {
        inOrder.verify(mock).simpleMethod(anyInt());
        inOrder.verify(mockTwo).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(3)).simpleMethod(999);
            fail();
        } catch (VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("second(");
        }
    }
    
    @Test
    public void shouldPointToFirstMethodBecauseOfTooManyActualInvocations() {
        try {
            inOrder.verify(mock, times(0)).simpleMethod(anyInt());
            fail();
        } catch (VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("first(");
        }
    }    
    
    @Test
    public void shouldPointToSecondMethodBecauseOfTooManyActualInvocations() {
        inOrder.verify(mock).simpleMethod(anyInt());
        
        try {
            inOrder.verify(mockTwo, times(0)).simpleMethod(anyInt());
            fail();
        } catch (VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("second(");
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
        } catch (VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("fourth(");
        }
    }
}