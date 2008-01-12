/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockito.internal.util.ListUtil;
import org.mockito.internal.util.ListUtil.Filter;

public class InvocationsFinder {

    public List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        return ListUtil.filter(invocations, new RemoveNotMatching(wanted));
    }

    public List<Invocation> findFirstUnverifiedChunk(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> unverified = ListUtil.filter(invocations, new RemoveVerifiedInOrder());
        List<Invocation> firstChunk = new LinkedList<Invocation>();
        for (Invocation invocation : unverified) {
            if (wanted.matches(invocation)) {
                firstChunk.add(invocation);
            } else if (firstChunk.isEmpty()) {
                firstChunk.add(invocation);
                break;
            } else {
                break;
            }
        }
        return firstChunk;
    }
    
    public Invocation findSimilarInvocation(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        for (Invocation invocation : invocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String currentMethodName = invocation.getMethod().getName();
            
            boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
            boolean isUnverified = !invocation.isVerified();
            boolean mockIsTheSame = wanted.getInvocation().getMock() == invocation.getMock();
            
            if (methodNameEquals && isUnverified && mockIsTheSame ) {
                return invocation;
            }
        }
        
        return null;
    }
    
    public Invocation findFirstUnverified(List<Invocation> invocations) {
        return findFirstUnverified(invocations, null);
    }
    
    Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        for (Invocation i : invocations) {
            boolean mockIsValid = mock == null || mock == i.getMock();
            if (!i.isVerified() && mockIsValid) {
                return i;
            }
        }
        return null;
    }

    public HasStackTrace getLastStackTrace(List<Invocation> invocations) {
        if (invocations.isEmpty()) {
            return null;
        } else {
            Invocation last = invocations.get(invocations.size() - 1);
            return last.getStackTrace();
        }
    }
    
    private class RemoveNotMatching implements Filter<Invocation> {
        private final InvocationMatcher wanted;

        private RemoveNotMatching(InvocationMatcher wanted) {
            this.wanted = wanted;
        }

        public boolean isOut(Invocation invocation) {
            return !wanted.matches(invocation);
        }
    }

    private final class RemoveVerifiedInOrder implements Filter<Invocation> {
        public boolean isOut(Invocation invocation) {
            return invocation.isVerifiedInOrder();
        }
    }
}