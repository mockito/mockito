package org.mockito.internal.invocation;

import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.progress.OngoingVerifyingMode;

//TODO do I need an interface here? fix the name!
public interface InvocationsCalculator {

    int countActual(InvocationMatcher wanted);

    Invocation findActualInvocation(InvocationMatcher wanted);

    HasStackTrace getLastInvocationStackTrace(InvocationMatcher wanted);

    HasStackTrace getFirstUndesiredInvocationStackTrace(InvocationMatcher wanted, OngoingVerifyingMode mode);

    Invocation getFirstUnverified();
}