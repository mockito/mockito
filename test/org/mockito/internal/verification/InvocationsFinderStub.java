/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;

class InvocationsFinderStub extends InvocationsFinder {
    
    Invocation similarToReturn;
    Invocation firstUnverifiedToReturn;
    Invocation previousInOrderToReturn;
    
    final List<Invocation> actualToReturn = new LinkedList<Invocation>();
    final List<Invocation> validMatchingChunkToReturn = new LinkedList<Invocation>();
    final List<Invocation> allMatchingUnverifiedChunksToReturn = new LinkedList<Invocation>();
    
    List<Invocation> invocations;
    
    @Override
    public List<Invocation> findAllMatchingUnverifiedChunks(List<Invocation> invocations, InvocationMatcher wanted) {
        return allMatchingUnverifiedChunksToReturn;
    }
    
    @Override
    public List<Invocation> findMatchingChunk(List<Invocation> invocations, InvocationMatcher wanted, MockitoVerificationMode mode) {
        return validMatchingChunkToReturn;
    }

    @Override public List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted) {
        this.invocations = invocations;
        return actualToReturn;
    }
    
    @Override public Invocation findSimilarInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
        this.invocations = invocations;
        return similarToReturn;
    }
    
    @Override public Invocation findFirstUnverified(List<Invocation> invocations) {
        this.invocations = invocations;
        return firstUnverifiedToReturn;
    }
    
    @Override
    public Invocation findPreviousVerifiedInOrder(List<Invocation> invocations) {
        return previousInOrderToReturn;
    }
}