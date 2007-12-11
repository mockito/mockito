package org.mockito.internal;

public interface Verifier {

    void verify(RegisteredInvocations registeredInvocations, InvocationMatcher wanted, VerifyingMode mode);
    
}
