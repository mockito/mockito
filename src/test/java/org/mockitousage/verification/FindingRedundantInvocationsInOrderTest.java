/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class FindingRedundantInvocationsInOrderTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mock2;

    @Test
    public void shouldWorkFineIfNoInvocations() throws Exception {
        //when
        InOrder inOrder = inOrder(mock);

        //then
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldSayNoInteractionsWanted() throws Exception {
        //when
        mock.simpleMethod();

        //then
        InOrder inOrder = inOrder(mock);
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(VerificationInOrderFailure e) {
            assertThat(e).hasMessageContaining("No interactions wanted");
        }
    }

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
    public void shouldVerifyNoMoreInteractionsInOrderWithMultipleMocks() throws Exception {
        //when
        mock.simpleMethod();
        mock2.simpleMethod();
        mock.otherMethod();

        //then
        InOrder inOrder = inOrder(mock, mock2);
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
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).simpleMethod(10);
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(VerificationInOrderFailure e) {}
    }

    @Test
    public void shouldFailToVerifyNoMoreInteractionsInOrderWithMultipleMocks() throws Exception {
        //when
        mock.simpleMethod();
        mock2.simpleMethod();
        mock.otherMethod();

        //then
        InOrder inOrder = inOrder(mock, mock2);
        inOrder.verify(mock2).simpleMethod();
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(VerificationInOrderFailure e) {}
    }

    @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
    @Test
    public void shouldValidateState() throws Exception {
        //when
        InOrder inOrder = inOrder(mock);
        verify(mock); // mess up state

        //then
        try {
            inOrder.verifyNoMoreInteractions();
            fail();
        } catch(UnfinishedVerificationException e) {}
    }
}
