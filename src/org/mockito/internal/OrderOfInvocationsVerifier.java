package org.mockito.internal;

import java.util.List;

import org.mockito.exceptions.Exceptions;

public class OrderOfInvocationsVerifier implements Verifier {

    public void verify(RegisteredInvocations registeredInvocations, InvocationMatcher wanted, VerifyingMode mode) {
        if (!mode.orderOfInvocationsMatters()) {
            return;
        }
        
        List<InvocationChunk> chunks = registeredInvocations.unverifiedInvocationChunks(mode);
        
        if (mode.wantedCountIsZero() && !chunks.isEmpty() && wanted.matches(chunks.get(0).getInvocation())) {
            Exceptions.numberOfInvocationsDiffers(0, chunks.get(0).getCount(), wanted.toString());
        } else if (mode.wantedCountIsZero()) {
            return;
        }
        
        if (chunks.isEmpty()) {
            Exceptions.wantedButNotInvoked(wanted.toString());
        }
        
        if (!wanted.matches(chunks.get(0).getInvocation())) {
            reportStrictOrderDiscrepancy(wanted, chunks.get(0).getInvocation());
        }
        
        if (!mode.atLeastOnceMode() && chunks.get(0).getCount() != mode.wantedCount()) {
            Exceptions.numberOfInvocationsDiffers(mode.wantedCount(), chunks.get(0).getCount(), wanted.toString());
        }
    }
    
    private void reportStrictOrderDiscrepancy(InvocationMatcher wantedInvocation, Invocation actualInvocation) {
        String wanted = wantedInvocation.toString();
        String actual = actualInvocation.toString();
        boolean sameMocks = wantedInvocation.getInvocation().getMock().equals(actualInvocation.getMock());
        boolean sameMethods = wanted.equals(actual);
        if (sameMethods && !sameMocks) {
            wanted = wantedInvocation.toStringWithSequenceNumber();
            actual = actualInvocation.toStringWithSequenceNumber();
        } else if (sameMethods) {
            wanted = wantedInvocation.getInvocation().toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        }
        
        Exceptions.strictlyWantedInvocationDiffersFromActual(wanted, actual, actualInvocation.getStackTrace());
    }
}
