/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;

public class NoMoreInteractionsTest extends TestBase {

    InOrderContextImpl context = new InOrderContextImpl();

    @Test
    public void shouldVerifyInOrder() {
        //given
        NoMoreInteractions n = new NoMoreInteractions();
        Invocation i = new InvocationBuilder().toInvocation();
        assertFalse(context.isVerified(i));

        try {
            //when
            n.verifyInOrder(new VerificationDataInOrderImpl(context, asList(i), null));
            //then
            fail();
        } catch(VerificationInOrderFailure e) {}
    }

    @Test
    public void shouldVerifyInOrderAndPass() {
        //given
        NoMoreInteractions n = new NoMoreInteractions();
        Invocation i = new InvocationBuilder().toInvocation();
        context.markVerified(i);
        assertTrue(context.isVerified(i));

        //when
        n.verifyInOrder(new VerificationDataInOrderImpl(context, asList(i), null));
        //then no exception is thrown
    }

    @Test
    public void shouldVerifyInOrderMultipleInvoctions() {
        //given
        NoMoreInteractions n = new NoMoreInteractions();
        Invocation i = new InvocationBuilder().seq(1).toInvocation();
        Invocation i2 = new InvocationBuilder().seq(2).toInvocation();

        //when
        context.markVerified(i2);

        //then no exception is thrown
        n.verifyInOrder(new VerificationDataInOrderImpl(context, asList(i, i2), null));
    }

    @Test
    public void shouldVerifyInOrderMultipleInvoctionsAndThrow() {
        //given
        NoMoreInteractions n = new NoMoreInteractions();
        Invocation i = new InvocationBuilder().seq(1).toInvocation();
        Invocation i2 = new InvocationBuilder().seq(2).toInvocation();

        try {
            //when
            n.verifyInOrder(new VerificationDataInOrderImpl(context, asList(i, i2), null));
            fail();
        } catch (VerificationInOrderFailure e) {}
    }

    @Test
    public void noMoreInteractionsExceptionMessageShouldDescribeMock() {
        //given
        NoMoreInteractions n = new NoMoreInteractions();
        IMethods mock = mock(IMethods.class, "a mock");
        InvocationMatcher i = new InvocationBuilder().mock(mock).toInvocationMatcher();

        InvocationContainerImpl invocations =
            new InvocationContainerImpl( new MockSettingsImpl());
        invocations.setInvocationForPotentialStubbing(i);

        try {
            //when
            n.verify(new VerificationDataImpl(invocations, null));
            //then
            fail();
        } catch (NoInteractionsWanted e) {
            Assertions.assertThat(e.toString()).contains(mock.toString());
        }
    }

    @Test
    public void noMoreInteractionsInOrderExceptionMessageShouldDescribeMock() {
        //given
        NoMoreInteractions n = new NoMoreInteractions();
        IMethods mock = mock(IMethods.class, "a mock");
        Invocation i = new InvocationBuilder().mock(mock).toInvocation();

        try {
            //when
            n.verifyInOrder(new VerificationDataInOrderImpl(context, asList(i), null));
            //then
            fail();
        } catch (VerificationInOrderFailure e) {
            Assertions.assertThat(e.toString()).contains(mock.toString());
        }
    }
}
