/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AtLeastXNumberOfInvocationsCheckerTest   {

    @Test
    public void shouldMarkActualInvocationsAsVerified() {
        //given
        AtLeastXNumberOfInvocationsChecker c = new AtLeastXNumberOfInvocationsChecker();
       
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        //when
        c.check(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1);

        //then
        assertThat(invocation.isVerified()).isTrue();
    }
}
