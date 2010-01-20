package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;

public class VerificationDataImplTest extends TestBase {

    @Mock
    List mock;

    @Test
    public void shouldOrdinaryMethodBeVerifiable() throws Exception {
        VerificationDataImpl v = new VerificationDataImpl(null, new InvocationBuilder().toInvocationMatcher());
        v.assertWantedIsVerifiable();
    }

    @Test
    public void shouldNotBreakWhenWantedIsNull() throws Exception {
        VerificationDataImpl v = new VerificationDataImpl(new LinkedList<Invocation>(), null);
        v.assertWantedIsVerifiable();
    }

    @Test
    public void shouldToStringBeNotVerifiable() throws Exception {
        InvocationMatcher toString = new InvocationBuilder().method("toString").toInvocationMatcher();
        try {
            new VerificationDataImpl(null, toString);
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    public void shouldEqualsBeNotVerifiable() throws Exception {
        InvocationMatcher equals = new InvocationMatcher(invocationOf(Object.class, "equals", new Object()));
        try {
            new VerificationDataImpl(null, equals);
            fail();
        } catch (MockitoException e) {}
    }
}
