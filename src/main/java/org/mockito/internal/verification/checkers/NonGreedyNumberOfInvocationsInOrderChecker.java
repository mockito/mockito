/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.exceptions.Reporter.tooLittleActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class NonGreedyNumberOfInvocationsInOrderChecker {

    private final InvocationsFinder finder;

    public NonGreedyNumberOfInvocationsInOrderChecker() {
        this(new InvocationsFinder());
    }

    NonGreedyNumberOfInvocationsInOrderChecker(InvocationsFinder finder ) {
        this.finder = finder;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        int actualCount = 0;
        Location lastLocation = null;
        while( actualCount < wantedCount ){
            Invocation next = finder.findFirstMatchingUnverifiedInvocation( invocations, wanted, context );
            if( next == null ){
                throw tooLittleActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, lastLocation );
            }
            markVerified( next, wanted );
            context.markVerified( next );
            lastLocation = next.getLocation();
            actualCount++;
        }
    }
}