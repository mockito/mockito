/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.*;

public class MockitoBehavior<T> {

    private RegisteredInvocations registeredInvocations = new RegisteredInvocations(new AllInvocationsFinder());
    private Map<ExpectedInvocation, Result> results = new HashMap<ExpectedInvocation, Result>();

    private T mock;
    private ExpectedInvocation invocationForStubbing;
    
    public void addInvocation(ExpectedInvocation invocation) {
        this.registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public void addResult(Result result) {
        assert invocationForStubbing != null;
        registeredInvocations.removeLast();
        this.results.put(invocationForStubbing, result);
    }

    public void verify(ExpectedInvocation wanted, VerifyingMode mode) {
        checkOrderOfInvocations(wanted, mode);
        checkForMissingInvocation(wanted, mode);
        checkForWrongNumberOfInvocations(wanted, mode);        
        registeredInvocations.markInvocationsAsVerified(wanted, mode);
    }
    
    private void checkForMissingInvocation(ExpectedInvocation wanted, VerifyingMode mode) {
        int actualCount = registeredInvocations.countActual(wanted);
        Integer wantedCount = mode.wantedCount();
        boolean atLeastOnce = mode.atLeastOnceMode();
               
        if ((atLeastOnce || wantedCount == 1) && actualCount == 0) {
            reportMissingInvocationError(wanted);
        }
    }

    void checkForWrongNumberOfInvocations(ExpectedInvocation wanted, VerifyingMode mode) {
        if (mode.orderOfInvocationsMatters()) {
            return;
        }
        
        int actualCount = registeredInvocations.countActual(wanted);
        Integer wantedCount = mode.wantedCount();
        boolean atLeastOnce = mode.atLeastOnceMode();
        
        if (!atLeastOnce && actualCount != wantedCount) {
            Exceptions.numberOfInvocationsDiffers(wantedCount, actualCount, wanted.toString());
        }
    }

    private void reportMissingInvocationError(ExpectedInvocation wanted) {
        Invocation actual = registeredInvocations.findSimilarInvocation(wanted);
        
        if (actual != null) {
            reportDiscrepancy(wanted, actual);
        } else {
            Exceptions.wantedButNotInvoked(wanted.toString());
        }
    }

    private void reportDiscrepancy(ExpectedInvocation wantedInvocation, Invocation actualInvocation) {
        String wanted = wantedInvocation.toString();
        String actual = actualInvocation.toString();
        if (wanted.equals(actual)) {
            wanted = wantedInvocation.getInvocation().toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        }
        
        Exceptions.wantedInvocationDiffersFromActual(wanted, actual);
    }
    
    private void reportStrictOrderDiscrepancy(ExpectedInvocation wantedInvocation, Invocation actualInvocation) {
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
        
        Exceptions.strictlyWantedInvocationDiffersFromActual(wanted, actual);
    }

    private void checkOrderOfInvocations(ExpectedInvocation wanted, VerifyingMode mode) {
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
        verifyNoMoreInteractions("No more interactions wanted");
    }
    
    public void verifyZeroInteractions() {
        verifyNoMoreInteractions("Zero interactions wanted");
    }
    
    private void verifyNoMoreInteractions(String message) {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            Exceptions.noMoreInteractionsWanted(unverified.toString(), message);
        }
    }

    public Object resultFor(Invocation wanted) throws Throwable {
        for (ExpectedInvocation i : results.keySet()) {
            if (i.matches(wanted)) {
                return results.get(i).answer();
            }
        }

        return ToTypeMappings.emptyReturnValueFor(wanted.getMethod().getReturnType());
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

    public ExpectedInvocation getInvocationForStubbing() {
        return invocationForStubbing;
    }
}