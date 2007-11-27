/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.*;

public class MockitoBehavior<T> {

    private T mock;
    
    private RegisteredInvocations registeredInvocations = new RegisteredInvocations(new AllInvocationsFinder());
    private Map<ExpectedInvocation, Result> results = new HashMap<ExpectedInvocation, Result>();

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
        checkForMissingInvocation(expected, verifyingMode);
        checkOrderOfInvocations(expected, verifyingMode);
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

    void checkForWrongNumberOfInvocations(ExpectedInvocation expected, VerifyingMode verifyingMode) throws NumberOfInvocationsAssertionError {
        if (verifyingMode.orderOfInvocationsMatters()) {
            return;
        }
        
        int actuallyInvoked = registeredInvocations.countActual(expected);
        Integer expectedInvoked = verifyingMode.expectedCount();
        boolean atLeastOnce = verifyingMode.atLeastOnceMode();
        
        if (!atLeastOnce && actuallyInvoked != expectedInvoked) {
            throw new NumberOfInvocationsAssertionError(expectedInvoked, actuallyInvoked, expected);
        }
    }

    private void reportMissingInvocationError(ExpectedInvocation invocation) throws VerificationAssertionError {
        //TODO refactor message building somewhere else...
        Invocation similarInvocation = registeredInvocations.findSimilarInvocation(invocation);
        
        String message = null;
        String expected = invocation.toString();
        if (similarInvocation != null) {
            String actual = similarInvocation.toString();
            if (expected.equals(actual)) {
                expected = invocation.getInvocation().toStringWithArgumentTypes();
                actual = similarInvocation.toStringWithArgumentTypes();
            }
            
            message = 
                    "\n" +
                    "Invocation differs from actual" +
                    "\n" +
                    "Expected: " + expected +
                    "\n" +
            		"Actual:   " + actual;
        } else {
            message = 
                    "\n" +
                    "Expected but not invoked:" +
                    "\n" +    
                    expected;
        }
        
        throw new VerificationAssertionError(message);
    }

    private void checkOrderOfInvocations(ExpectedInvocation expected, VerifyingMode mode) {
        if (!mode.orderOfInvocationsMatters()) {
            return;
        }
        
        List<InvocationChunk> chunks = registeredInvocations.unverifiedInvocationChunks(mode);
        
        if (mode.expectedCountIsZero() && !chunks.isEmpty() && expected.matches(chunks.get(0).getInvocation())) {
            throw new NumberOfInvocationsAssertionError(0, chunks.get(0).getCount(), expected);
        } else if (mode.expectedCountIsZero()) {
            return;
        }
        
        if (chunks.isEmpty()) {
            throw new StrictVerificationError("everything was already verified");
        }
        
        if (!expected.matches(chunks.get(0).getInvocation())) {
            throw new StrictVerificationError("this is not expected here");
        }
        
        if (!mode.atLeastOnceMode() && chunks.get(0).getCount() != mode.expectedCount()) {
            throw new NumberOfInvocationsAssertionError(mode.expectedCount(), chunks.get(0).getCount(), expected);
        }
    }

    public void verifyNoMoreInteractions() {
        verifyNoMoreInteractions("No more interactions expected");
    }
    
    public void verifyZeroInteractions() {
        verifyNoMoreInteractions("Zero interactions expected");
    }
    
    private void verifyNoMoreInteractions(String verificationErrorMessage) {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            String mockName = Namer.nameForMock(mock);
            throw new VerificationAssertionError(
                    "\n" +
                    verificationErrorMessage + " on " + mockName +
                    "\n" +
                    "Unexpected: " + unverified.toString());
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