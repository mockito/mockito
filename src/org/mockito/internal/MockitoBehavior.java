/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.*;

public class MockitoBehavior<T> {

    private T mock;
    
    private List<Invocation> registeredInvocations = new LinkedList<Invocation>();
    private Map<ExpectedInvocation, Result> results = new HashMap<ExpectedInvocation, Result>();

    private ExpectedInvocation invocationForStubbing;
    
    public void addInvocation(ExpectedInvocation invocation) {
        this.registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public void addResult(Result result) {
        assert invocationForStubbing != null;
        registeredInvocations.remove(invocationForStubbing.getInvocation());
        this.results.put(invocationForStubbing, result);
    }

    public void verify(ExpectedInvocation expected, VerifyingMode verifyingMode) {
        checkForMissingInvocation(expected, verifyingMode);
        checkOrderOfInvocations(expected, verifyingMode);
        checkForWrongNumberOfInvocations(expected, verifyingMode);        
        markInvocationsAsVerified(expected, verifyingMode);
    }
    
    void markInvocationsAsVerified(ExpectedInvocation expected, VerifyingMode mode) {
        if (mode.expectedCountIsZero()) {
            return;
        }
        
        if (mode.orderOfInvocationsMatters()) {
            List<InvocationChunk> chunks = getUnverifiedInvocationChunks(mode);
            chunks.get(0).markAllInvocationsAsVerified();
        } else {
            for (Invocation invocation : registeredInvocations) {
                if (expected.matches(invocation)) {
                    invocation.markVerified();
                }
            }
        }
    }

    private void checkForMissingInvocation(ExpectedInvocation expected, VerifyingMode verifyingMode) {
        int actualCount = numberOfActualInvocations(expected);
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
        
        int actuallyInvoked = numberOfActualInvocations(expected);
        Integer expectedInvoked = verifyingMode.expectedCount();
        boolean atLeastOnce = verifyingMode.atLeastOnceMode();
        
        if (!atLeastOnce && actuallyInvoked != expectedInvoked) {
            throw new NumberOfInvocationsAssertionError(expectedInvoked, actuallyInvoked, expected);
        }
    }

    private void reportMissingInvocationError(ExpectedInvocation invocation) throws VerificationAssertionError {
        //TODO refactor message building somewhere else...
        Invocation similarInvocation = findSimilarInvocation(invocation);
        String message = 
            "\n" +
            "Invocation differs from actual" +
            "\n";
        
        String expected = invocation.toString();
        if (similarInvocation != null) {
            String actual = similarInvocation.toString();
            if (expected.equals(actual)) {
                expected = invocation.getInvocation().toStringWithArgumentTypes();
                actual = similarInvocation.toStringWithArgumentTypes();
            }
            
            message += 
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
        
        List<InvocationChunk> chunks = getUnverifiedInvocationChunks(mode);
        
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

    private List<InvocationChunk> getUnverifiedInvocationChunks(VerifyingMode verifyingMode) {
        Set<Invocation> allInvocationsInOrder = new TreeSet<Invocation>(
                new Comparator<Invocation>(){
                    public int compare(Invocation o1, Invocation o2) {
                        int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
                        assert comparison != 0;
                        return comparison;
                    }});
        
        List<Object> allMocksToBeVerifiedInOrder = verifyingMode.getAllMocksToBeVerifiedInSequence();
        for (Object mock : allMocksToBeVerifiedInOrder) {
            List<Invocation> invocations = MockUtil.getControl(mock).getRegisteredInvocations();
            allInvocationsInOrder.addAll(invocations);
        }
        
        List<InvocationChunk> chunks = new LinkedList<InvocationChunk>();
        for (Invocation i : allInvocationsInOrder) {
            if (i.isVerified()) {
                continue;
            }
            if (!chunks.isEmpty() 
                    && chunks.get(chunks.size()-1).getInvocation().equals(i)) {
                chunks.get(chunks.size()-1).add(i);
            } else {
                chunks.add(new InvocationChunk(i));
            }
        }
        
        return chunks;
    }

    /**
     * gets first registered invocation with the same method name
     * or just first invocation
     */
    private Invocation findSimilarInvocation(ExpectedInvocation expectedInvocation) {
        for (Invocation registeredInvocation : registeredInvocations) {
            String expectedMethodName = expectedInvocation.getMethod().getName();
            String registeredInvocationName = registeredInvocation.getMethod().getName();
            if (expectedMethodName.equals(registeredInvocationName) && !registeredInvocation.isVerified()) {
                return registeredInvocation;
            }
        }

        return null;
    }

    private int numberOfActualInvocations(ExpectedInvocation expectedInvocation) {
        int verifiedInvocations = 0;
        for (Invocation registeredInvocation : registeredInvocations) {
            if (expectedInvocation.matches(registeredInvocation)) {
                verifiedInvocations++;
            }
        }

        return verifiedInvocations;
    }

    public void verifyNoMoreInteractions() {
        verifyNoMoreInteractions("No more interactions expected");
    }
    
    public void verifyZeroInteractions() {
        verifyNoMoreInteractions("Zero interactions expected");
    }
    
    private void verifyNoMoreInteractions(String verificationErrorMessage) {
        for (Invocation registeredInvocation : registeredInvocations) {
            if (!registeredInvocation.isVerified()) {
                String mockName = Namer.nameForMock(mock);
                throw new VerificationAssertionError(
                        "\n" +
                        verificationErrorMessage + " on " + mockName +
                        "\n" +
                        "Unexpected: " + registeredInvocation.toString());
            }
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
        return registeredInvocations;
    }

    public ExpectedInvocation getInvocationForStubbing() {
        return invocationForStubbing;
    }
}