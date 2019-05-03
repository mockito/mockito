/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.argumentsAreDifferent;
import static org.mockito.internal.exceptions.Reporter.wantedButNotInvoked;
import static org.mockito.internal.exceptions.Reporter.wantedButNotInvokedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.findPreviousVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findSimilarInvocation;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

import java.util.List;

import org.mockito.internal.reporting.SmartPrinter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

public class MissingInvocationChecker {

    private MissingInvocationChecker() {
    }

    public static void checkMissingInvocation(List<Invocation> invocations, MatchableInvocation wanted) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);

        if (!actualInvocations.isEmpty()){
            return;
        }

        Invocation similar = findSimilarInvocation(invocations, wanted);
        if (similar == null) {
            throw wantedButNotInvoked(wanted, invocations);
        }

        Integer[] indexesOfSuspiciousArgs = getSuspiciouslyNotMatchingArgsIndexes(wanted.getMatchers(), similar.getArguments());
        SmartPrinter smartPrinter = new SmartPrinter(wanted, invocations, indexesOfSuspiciousArgs);
        List<Location> actualLocations = ListUtil.convert(invocations, new ListUtil.Converter<Invocation, Location>() {
            @Override
            public Location convert(Invocation invocation) {
                return invocation.getLocation();
            }
        });

        throw argumentsAreDifferent(smartPrinter.getWanted(), smartPrinter.getActuals(), actualLocations);

    }

    public static void checkMissingInvocation(List<Invocation> invocations, MatchableInvocation wanted, InOrderContext context) {
        List<Invocation> chunk = findAllMatchingUnverifiedChunks(invocations, wanted, context);

        if (!chunk.isEmpty()) {
            return;
        }

        Invocation previousInOrder = findPreviousVerifiedInOrder(invocations, context);
        if (previousInOrder != null) {
            throw wantedButNotInvokedInOrder(wanted, previousInOrder);
        }

        checkMissingInvocation(invocations, wanted);
    }
}
