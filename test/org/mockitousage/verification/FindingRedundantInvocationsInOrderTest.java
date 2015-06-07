/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class FindingRedundantInvocationsInOrderTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mock2;
    
    @Test
    public void shouldWorkFineIfNoInvocatins() throws Exception {
        //when
        final InOrder inOrder = inOrder(mock);
        
        //then
        inOrder.verifyNoMoreInteractions();        
    }
    
    @Test
    public void shouldSayNoInteractionsWanted() throws Exception {
        //when
        mock.simpleMethod();
        
        //then
        final InOrder inOrder = inOrder(mock);
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(final VerificationInOrderFailure e) {
            assertContains("No interactions wanted", e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyNoMoreInteractionsInOrder() throws Exception {
        //when
        mock.simpleMethod();
        mock.simpleMethod(10);
        mock.otherMethod();
        
        //then
        final InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod(10);
        inOrder.verify(mock).otherMethod();
        inOrder.verifyNoMoreInteractions();        
    }
    
    @Test
    public void shouldVerifyNoMoreInteractionsInOrderWithMultipleMocks() throws Exception {
        //when
        mock.simpleMethod();
        mock2.simpleMethod();
        mock.otherMethod();
        
        //then
        final InOrder inOrder = inOrder(mock, mock2);
        inOrder.verify(mock2).simpleMethod();
        inOrder.verify(mock).otherMethod();
        inOrder.verifyNoMoreInteractions();        
    }
    
    @Test
    public void shouldFailToVerifyNoMoreInteractionsInOrder() throws Exception {
        //when
        mock.simpleMethod();
        mock.simpleMethod(10);
        mock.otherMethod();
        
        //then
        final InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod(10);
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(final VerificationInOrderFailure e) {}
    }
    
    @Test
    public void shouldFailToVerifyNoMoreInteractionsInOrderWithMultipleMocks() throws Exception {
        //when
        mock.simpleMethod();
        mock2.simpleMethod();
        mock.otherMethod();
        
        //then
        final InOrder inOrder = inOrder(mock, mock2);
        inOrder.verify(mock2).simpleMethod();
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(final VerificationInOrderFailure e) {}
    }
    
    @Test
    public void shouldValidateState() throws Exception {
        //when
        final InOrder inOrder = inOrder(mock);
        verify(mock); // mess up state
        
        //then
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(final UnfinishedVerificationException e) {}
    }
}