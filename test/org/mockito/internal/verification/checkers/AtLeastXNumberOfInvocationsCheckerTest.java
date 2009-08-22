package org.mockito.internal.verification.checkers;

import org.junit.Test;
import org.mockito.internal.invocation.*;
import org.mockito.Mockito;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.any;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.LinkedList;
import static java.util.Arrays.asList;

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
