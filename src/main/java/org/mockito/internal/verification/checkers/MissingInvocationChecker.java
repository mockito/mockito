/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.argumentsAreDifferent;
import static org.mockito.internal.exceptions.Reporter.wantedButNotInvoked;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.findSimilarInvocation;

import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.SmartPrinter;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.invocation.Invocation;

public class MissingInvocationChecker {
        
    public void check(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);
        
        if (actualInvocations.isEmpty()) {
            Invocation similar = findSimilarInvocation(invocations, wanted);
            if (similar != null) {
                ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
                Integer[] indexesOfSuspiciousArgs = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(wanted.getMatchers(), similar.getArguments());
                SmartPrinter smartPrinter = new SmartPrinter(wanted, similar, indexesOfSuspiciousArgs);
                throw argumentsAreDifferent(smartPrinter.getWanted(), smartPrinter.getActual(), similar.getLocation());
            } 
            
            throw wantedButNotInvoked(wanted, invocations);
            
        }
    }
}
