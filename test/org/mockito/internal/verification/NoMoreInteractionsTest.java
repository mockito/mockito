/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;

import org.junit.Test;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

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
}