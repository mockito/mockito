package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockitoutil.TestBase;

public class VerificationDataImplTest extends TestBase {

    @Test
    public void shouldToStringBeNotVerifiable() throws Exception {
        InvocationMatcher toString = new InvocationBuilder().method("toString").toInvocationMatcher();
        try {
            new VerificationDataImpl(null, toString);
            fail();
        } catch (MockitoException e) {}
    }
}
