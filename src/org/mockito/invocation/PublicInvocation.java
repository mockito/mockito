package org.mockito.invocation;

import org.mockito.internal.invocation.StubInfoImpl;

/**
 * by Szczepan Faber, created at: 3/31/12
 */
public interface PublicInvocation extends InvocationOnMock {

    boolean isVerified();

    int getSequenceNumber();

    Location getLocation();

    Object[] getRawArguments();

    void markVerified();

    StubInfo stubInfo();

    void markStubbed(StubInfo stubInfo);

    boolean isIgnoredForVerification();

    void ignoreForVerification();
}
