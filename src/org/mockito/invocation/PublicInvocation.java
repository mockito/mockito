package org.mockito.invocation;

import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.StubInfo;

/**
 * by Szczepan Faber, created at: 3/31/12
 */
public interface PublicInvocation extends InvocationOnMock {

    boolean isVerified();

    int getSequenceNumber();

    LocationImpl getLocation();

    Object[] getRawArguments();

    void markVerified();

    StubInfo stubInfo();

    void markStubbed(StubInfo stubInfo);

    boolean isIgnoredForVerification();

    void ignoreForVerification();
}
