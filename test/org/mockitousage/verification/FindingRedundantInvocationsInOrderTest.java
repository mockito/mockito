/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@Ignore
public class FindingRedundantInvocationsInOrderTest extends TestBase {

    @Mock private IMethods mock;
    
    @Test
    public void shouldVerifyNoMoreInteractionsInOrder() throws Exception {
        //when
        mock.simpleMethod();
        mock.simpleMethod(10);
        mock.otherMethod();
        
        //then
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod(10);
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
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod(10);
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(VerificationInOrderFailure e) {}
    }
}