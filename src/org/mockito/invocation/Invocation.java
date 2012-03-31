package org.mockito.invocation;

/**
 * by Szczepan Faber, created at: 3/31/12
 */
public interface Invocation extends InvocationOnMock {

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
