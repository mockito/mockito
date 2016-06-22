/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.exceptions.Reporter.argumentsAreDifferent;
import static org.mockito.exceptions.Reporter.wantedButNotInvoked;
import static org.mockito.exceptions.Reporter.wantedButNotInvokedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.findPreviousVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findSimilarInvocation;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.SmartPrinter;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
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
        
        /*
         * It is of course possible to have an issue where the arguments are different
         * rather that not invoked in order. Issue related to
         * http://code.google.com/p/mockito/issues/detail?id=27. If the previous order
         * is missing, then this method checks if the arguments are different or if the order
         * is not invoked.
         */
         List<Invocation> actualInvocations = findInvocations(invocations, wanted);
         if (actualInvocations == null || actualInvocations.isEmpty())  {
             Invocation similar = findSimilarInvocation(invocations, wanted);
             if (similar != null) {
                 Integer[] indicesOfSimilarMatchingArguments =
                         new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(wanted.getMatchers(),
                                 similar.getArguments());
                 SmartPrinter smartPrinter = new SmartPrinter(wanted, similar, indicesOfSimilarMatchingArguments);
                 throw argumentsAreDifferent(smartPrinter.getWanted(), smartPrinter.getActual(), similar.getLocation());
             } 
             throw wantedButNotInvoked(wanted);
        }
    }
}