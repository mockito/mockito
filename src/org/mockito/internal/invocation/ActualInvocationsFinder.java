/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.progress.VerificationModeImpl;

public class ActualInvocationsFinder {

    public List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        List<Invocation> actual = new LinkedList<Invocation>();
        for (Invocation i : invocations) {
            if (wanted.matches(i)) {
                actual.add(i);
            }
        }
        return actual;
    }

    public List<Invocation> findFirstStrictlyUnverified(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> unverified = new LinkedList<Invocation>();
        //TODO refactor
        for (Invocation i : invocations) {
            if (i.isVerifiedStrictly()) {
                continue;
            }
            
            if (wanted.matches(i)) {
                unverified.add(i);
                continue;
            }
            
            if (unverified.isEmpty()) {
                unverified.add(i);
            }
            
            break;
        }
        return unverified;
    }
}