package org.mockito.internal.verification;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.OngoingVerifyingMode;


public interface Verifier {

    void verify(RegisteredInvocations registeredInvocations, InvocationMatcher wanted, OngoingVerifyingMode mode);
    
}
