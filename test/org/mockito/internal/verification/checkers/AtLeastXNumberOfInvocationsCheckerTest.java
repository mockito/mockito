/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.*;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

public class AtLeastXNumberOfInvocationsCheckerTest extends TestBase {

    @Test
    public void shouldMarkActualInvocationsAsVerified() {
        //given
        AtLeastXNumberOfInvocationsChecker c = new AtLeastXNumberOfInvocationsChecker();
        c.invocationMarker = Mockito.mock(InvocationMarker.class);
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        //when
        c.check(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1);

        //then
        Mockito.verify(c.invocationMarker).markVerified(eq(asList(invocation)), any(CapturesArgumensFromInvocation.class));
    }
}
