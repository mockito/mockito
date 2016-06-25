/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AtLeastXNumberOfInvocationsCheckerInOrderTest   {

    @Rule
    public ExpectedException exception = ExpectedException.none();
            
    @Test
    public void shouldMarkActualInvocationsAsVerified() {
        InOrderContext context = new InOrderContextImpl();
        //given
        AtLeastXNumberOfInvocationsInOrderChecker c = new AtLeastXNumberOfInvocationsInOrderChecker(context);
       
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        //when
        c.check(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1);

        //then
        assertThat(invocation.isVerified()).isTrue();
    }
    
    @Test
    public void shouldReportTooLittleInvocations() {
        InOrderContext context = new InOrderContextImpl();
        //given
        AtLeastXNumberOfInvocationsInOrderChecker c = new AtLeastXNumberOfInvocationsInOrderChecker(context);
       
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("iMethods.simpleMethod()");
        exception.expectMessage("Wanted *at least* 2 times");
        exception.expectMessage("But was 1 time");
        
        //when
        c.check(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 2);

       
    }
}
