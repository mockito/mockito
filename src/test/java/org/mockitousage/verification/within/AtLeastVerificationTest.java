/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import static org.mockito.Mockito.verify;
import static org.mockito.verification.Within.untilNow;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;


public class AtLeastVerificationTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    
    @Mock
    private IMethods mock;


    @Test
    public void shouldVerifyAtLeastXTimes() {
        // when
        mock.simpleMethod();
        mock.simpleMethod();
        mock.simpleMethod();

        // then
        verify(mock, untilNow().atLeast(2)).simpleMethod();
    }

    @Test
    public void shouldFailVerifiationAtLeastXTimes() {
        mock.simpleMethod();
        verify(mock, untilNow().atLeast(1)).simpleMethod();

        exception.expect(MockitoAssertionError.class);
        verify(mock, untilNow().atLeast(2)).simpleMethod();
    }

    @Test
    public void shouldNotAllowAtLeastZeroForTheSakeOfVerifyNoMoreInteractionsSometimes() {
        exception.expect(MockitoException.class);
        exception.expectMessage("The minimum number of invocations must be greater that 0!");
        exception.expectMessage("If you want to verify that nothing was called use Mocktio.never() instead!");
        
        verify(mock, untilNow().atLeast(0)).simpleMethod();
    }

    
}