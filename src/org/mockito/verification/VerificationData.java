package org.mockito.verification;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public interface VerificationData {

    List<Invocation> getAllInvocations();

    InvocationMatcher getWanted();   
    
}