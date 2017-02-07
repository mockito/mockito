/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.verification.Within.untilNow;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class OnlyVerificationTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private List<Object> mock;

    @Mock
    private List<Object> mock2;

    @Test
    public void shouldVerifyMethodWasInvokedExclusively() {
        mock.clear();
        verify(mock, untilNow().only()).clear();
    }

    @Test
    public void shouldVerifyMethodWasInvokedExclusivelyWithMatchersUsage() {
        mock.get(0);
        verify(mock, only()).get(anyInt());
    }

    @Test
    public void shouldFailIfMethodWasNotInvoked() {
        mock.clear();

        exception.expect(NoInteractionsWanted.class);
        
        verify(mock, untilNow().only()).get(0);
    }

    @Test
    public void shouldFailIfMethodWasInvokedMoreThanOnce() {
        mock.clear();
        mock.clear();

        exception.expect(NoInteractionsWanted.class);
        
        verify(mock, untilNow().only()).clear();

    }

    @Test
    public void shouldFailIfMethodWasInvokedButWithDifferentArguments() {
        mock.get(0);
        mock.get(2);
       
        exception.expect(NoInteractionsWanted.class);
        
        verify(mock, untilNow().only()).get(999);
           
    }

    @Test
    public void shouldFailIfExtraMethodWithDifferentArgsFound() {
        mock.get(0);
        mock.get(2);
        
        exception.expect(NoInteractionsWanted.class);
        
        verify(mock, untilNow().only()).get(2);
        
    }

    @Test
    public void shouldVerifyMethodWasInvokedExclusivelyWhenTwoMocksInUse() {
        mock.clear();
        mock2.get(0);
        
        verify(mock, untilNow().only()).clear();
        verify(mock2, untilNow().only()).get(0);
    }

}