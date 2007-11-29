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

    public void verify(ExpectedInvocation expected, VerifyingMode verifyingMode) {
        checkOrderOfInvocations(expected, verifyingMode);
        checkForMissingInvocation(expected, verifyingMode);
        checkForWrongNumberOfInvocations(expected, verifyingMode);        
        registeredInvocations.markInvocationsAsVerified(expected, verifyingMode);
    }
    
    private void checkForMissingInvocation(ExpectedInvocation expected, VerifyingMode verifyingMode) {
        int actualCount = registeredInvocations.countActual(expected);
        Integer expectedCount = verifyingMode.expectedCount();
        boolean atLeastOnce = verifyingMode.atLeastOnceMode();
               
        if ((atLeastOnce || expectedCount == 1) && actualCount == 0) {
            reportMissingInvocationError(expected);
        }
    }

    void checkForWrongNumberOfInvocations(ExpectedInvocation expected, VerifyingMode verifyingMode) {
        if (verifyingMode.orderOfInvocationsMatters()) {
            return;
        }
        
        int actuallyInvoked = registeredInvocations.countActual(expected);
        Integer expectedInvoked = verifyingMode.expectedCount();
        boolean atLeastOnce = verifyingMode.atLeastOnceMode();
        
        if (!atLeastOnce && actuallyInvoked != expectedInvoked) {
            Exceptions.numberOfInvocationsDiffers(expectedInvoked, actuallyInvoked, expected.toString());
        }
    }

    private void reportMissingInvocationError(ExpectedInvocation wanted) {
        Invocation actual = registeredInvocations.findSimilarInvocation(wanted);
        
        if (actual != null) {
            reportDiscrepancy(wanted, actual, Exceptions.REGULAR_DISCREPANCY);
        } else {
            Exceptions.wantedButNotInvoked(wanted.toString());
        }
    }

    private void reportDiscrepancy(ExpectedInvocation wantedInvocation, Invocation actualInvocation, String message) {
        String wanted = wantedInvocation.toString();
        String actual = actualInvocation.toString();
        if (wanted.equals(actual)) {
            wanted = wantedInvocation.getInvocation().toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        }
        
        Exceptions.wantedInvocationDiffersFromActual(wanted, actual, message);
    }

    private void checkOrderOfInvocations(ExpectedInvocation wanted, VerifyingMode mode) {
        if (!mode.orderOfInvocationsMatters()) {
            return;
        }
        
        List<InvocationChunk> chunks = registeredInvocations.unverifiedInvocationChunks(mode);
        
        if (mode.expectedCountIsZero() && !chunks.isEmpty() && wanted.matches(chunks.get(0).getInvocation())) {
            Exceptions.numberOfInvocationsDiffers(0, chunks.get(0).getCount(), wanted.toString());
        } else if (mode.expectedCountIsZero()) {
            return;
        }
        
        if (chunks.isEmpty()) {
            Exceptions.wantedButNotInvoked(wanted.toString());
        }
        
        if (!wanted.matches(chunks.get(0).getInvocation())) {
            reportDiscrepancy(wanted, chunks.get(0).getInvocation(), Exceptions.STRICT_DISCREPANCY);
        }
        
        if (!mode.atLeastOnceMode() && chunks.get(0).getCount() != mode.expectedCount()) {
            Exceptions.numberOfInvocationsDiffers(mode.expectedCount(), chunks.get(0).getCount(), wanted.toString());
        }
    }

    public void verifyNoMoreInteractions() {
        verifyNoMoreInteractions("No more interactions expected");
    }
    
    public void verifyZeroInteractions() {
        verifyNoMoreInteractions("Zero interactions expected");
    }
    
    private void verifyNoMoreInteractions(String message) {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            Exceptions.noMoreInteractionsWanted(unverified, message);
        }
    }

    public Object resultFor(Invocation invocation) throws Throwable {
        for (ExpectedInvocation expectedInvocation : results.keySet()) {
            if (expectedInvocation.matches(invocation)) {
                return results.get(expectedInvocation).answer();
            }
        }

        return ToTypeMappings.emptyReturnValueFor(invocation.getMethod().getReturnType());
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