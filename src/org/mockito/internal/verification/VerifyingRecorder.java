/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Exceptions;
import org.mockito.internal.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.invocation.InvocationsCalculatorImpl;
import org.mockito.internal.invocation.InvocationsChunker;
import org.mockito.internal.invocation.InvocationsMarker;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class VerifyingRecorder<T> {

    private final LinkedList<Invocation> registeredInvocations = new LinkedList<Invocation>();
    private InvocationsMarker marker;
    private List<Verifier> verifiers;
    
    public VerifyingRecorder() {
        this(new InvocationsMarker(), Arrays.asList(new MissingInvocationVerifier(), new NumberOfInvocationsVerifier()));
    }
    
    VerifyingRecorder(InvocationsMarker marker, List<Verifier> verifiers) {
        this.marker = marker;  
        this.verifiers = verifiers;
    }
    
    public void recordInvocation(Invocation invocation) {
        this.registeredInvocations.add(invocation);
    }
    
    public void eraseLastInvocation() {
        registeredInvocations.removeLast();
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations;
    }

    public void verify(InvocationMatcher wanted, OngoingVerifyingMode mode) {
        List<Invocation> invocations = getInvocationsForEvaluation(mode);
        InvocationsCalculator calculator = new InvocationsCalculatorImpl(invocations);
        
        for (Verifier verifier : verifiers) {
            verifier.verify(calculator, wanted, mode);
        }
        
        marker.markInvocationsAsVerified(invocations, wanted, mode);
    }
    
    private List<Invocation> getInvocationsForEvaluation(OngoingVerifyingMode mode) {
        List<Invocation> invocations;        
        if (mode.orderOfInvocationsMatters()) {
            InvocationsChunker chunker = new InvocationsChunker(new AllInvocationsFinder(mode));
            invocations = chunker.getFirstUnverifiedInvocationChunk();
        } else {
            invocations = registeredInvocations;
        }
        return invocations;
    }
    
    public void verifyNoMoreInteractions() {
        //TODO refactor to have single verify method
        //TODO OngoingVerifyingMode.times(0) should be explicit
        InvocationsCalculator calculator1 = new InvocationsCalculatorImpl(getInvocationsForEvaluation(OngoingVerifyingMode.times(0)));
        InvocationsCalculator calculator = calculator1;
        Invocation unverified = calculator.getFirstUnverified();
        if (unverified != null) {
            Exceptions.noMoreInteractionsWanted(unverified.toString(), unverified.getStackTrace());
        }
    }
    
    public void verifyZeroInteractions() {
        //TODO OngoingVerifyingMode.times(0) should be explicit
        InvocationsCalculator calculator1 = new InvocationsCalculatorImpl(getInvocationsForEvaluation(OngoingVerifyingMode.times(0)));
        InvocationsCalculator calculator = calculator1;
        Invocation unverified = calculator.getFirstUnverified();
        if (unverified != null) {
            Exceptions.zeroInteractionsWanted(unverified.toString(), unverified.getStackTrace());
        }
    }
}