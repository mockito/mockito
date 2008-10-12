package org.mockito.internal.verification.api;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public interface VerificationData {

    List<Invocation> getAllInvocations();

    InvocationMatcher getWanted();   
    
}