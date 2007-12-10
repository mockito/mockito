/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.*;
import org.mockito.exceptions.parents.HasStackTrace;

public class MockitoBehavior<T> {

    private RegisteredInvocations registeredInvocations = new RegisteredInvocations(new AllInvocationsFinder());

    private T mock;
    
    public void addInvocation(InvocationMatcher invocation) {
        this.registeredInvocations.add(invocation.getInvocation());
    }

    public void verify(InvocationMatcher wanted, VerifyingMode mode) {
        checkOrderOfInvocations(wanted, mode);
        checkForMissingInvocation(wanted, mode);
        checkForWrongNumberOfInvocations(wanted, mode);        
        registeredInvocations.markInvocationsAsVerified(wanted, mode);
    }
    
    private void checkForMissingInvocation(InvocationMatcher wanted, VerifyingMode mode) {
        int actualCount = registeredInvocations.countActual(wanted);
        Integer wantedCount = mode.wantedCount();
        boolean atLeastOnce = mode.atLeastOnceMode();
               
        if ((atLeastOnce || wantedCount == 1) && actualCount == 0) {
            reportMissingInvocationError(wanted);
        }
    }

    void checkForWrongNumberOfInvocations(InvocationMatcher wanted, VerifyingMode mode) {
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

    private void reportMissingInvocationError(InvocationMatcher wanted) {
        Invocation actual = registeredInvocations.findActualInvocation(wanted);
        
        if (actual != null) {
            reportDiscrepancy(wanted, actual);
        } else {
            Exceptions.wantedButNotInvoked(wanted.toString());
        }
    }

    private void reportDiscrepancy(InvocationMatcher wantedInvocation, Invocation actualInvocation) {
        String wanted = wantedInvocation.toString();
        String actual = actualInvocation.toString();
        if (wanted.equals(actual)) {
            wanted = wantedInvocation.getInvocation().toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        }
        
        Exceptions.wantedInvocationDiffersFromActual(wanted, actual, actualInvocation.getStackTrace());
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

    //TODO Cyclomatic Complexity = 10 :|
    private void checkOrderOfInvocations(InvocationMatcher wanted, VerifyingMode mode) {
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

    public void verifyNoMoreInteractions() {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            Exceptions.noMoreInteractionsWanted(unverified.toString(), unverified.getStackTrace());
        }
    }
    
    public void verifyZeroInteractions() {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            Exceptions.zeroInteractionsWanted(unverified.toString(), unverified.getStackTrace());
        }
    }
    
    public T getMock() {
        return mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations.all();
    }

    public void lastInvocationWasStubbed() {
        registeredInvocations.removeLast();
    }
}