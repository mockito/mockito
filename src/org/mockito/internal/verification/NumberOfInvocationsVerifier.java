package org.mockito.internal.verification;

import org.mockito.exceptions.Exceptions;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.state.OngoingVerifyingMode;

public class NumberOfInvocationsVerifier implements Verifier {

    public void verify(RegisteredInvocations registeredInvocations, InvocationMatcher wanted, OngoingVerifyingMode mode) {
        if (mode.orderOfInvocationsMatters() || mode.atLeastOnceMode()) {
            return;
        }
        
        int actualCount = registeredInvocations.countActual(wanted);
        Integer wantedCount = mode.wantedCount();
        
        if (actualCount < wantedCount) {
            HasStackTrace lastInvocation = registeredInvocations.getLastInvocationStackTrace(wanted);
            Exceptions.tooLittleActualInvocations(wantedCount, actualCount, wanted.toString(), lastInvocation);
        } else if (actualCount > wantedCount) {
            HasStackTrace firstUndesired = registeredInvocations.getFirstUndesiredInvocationStackTrace(wanted, mode);
            Exceptions.tooManyActualInvocations(wantedCount, actualCount, wanted.toString(), firstUndesired);
        }
    }
}
