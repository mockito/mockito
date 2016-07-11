/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.wantedButNotInvokedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findPreviousVerifiedInOrder;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class MissingInvocationInOrderChecker {
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode, InOrderContext context) {
        List<Invocation> chunk = findAllMatchingUnverifiedChunks(invocations, wanted, context);
        
        if (!chunk.isEmpty()) {
            return;
        }
        
        Invocation previousInOrder = findPreviousVerifiedInOrder(invocations, context);
        if (previousInOrder != null) {
            throw wantedButNotInvokedInOrder(wanted, previousInOrder);
        } 
        
        new MissingInvocationChecker().check(invocations, wanted);
    }
}
