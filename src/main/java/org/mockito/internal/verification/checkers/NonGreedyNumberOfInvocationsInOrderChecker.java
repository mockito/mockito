/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import static org.mockito.exceptions.Reporter.tooLittleActualInvocationsInOrder;

import java.util.List;

public class NonGreedyNumberOfInvocationsInOrderChecker {

    private final InvocationsFinder finder;
    private final InvocationMarker marker;

    public NonGreedyNumberOfInvocationsInOrderChecker() {
        this(new InvocationsFinder(), new InvocationMarker());
    }

    NonGreedyNumberOfInvocationsInOrderChecker(InvocationsFinder finder, InvocationMarker marker ) {
        this.finder = finder;
        this.marker = marker;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        int actualCount = 0;
        Location lastLocation = null;
        while( actualCount < wantedCount ){
            Invocation next = finder.findFirstMatchingUnverifiedInvocation( invocations, wanted, context );
            if( next == null ){
                throw tooLittleActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, lastLocation );
            }
            marker.markVerified( next, wanted );
            context.markVerified( next );
            lastLocation = next.getLocation();
            actualCount++;
        }
    }
}