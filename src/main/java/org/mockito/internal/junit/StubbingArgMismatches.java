package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.Invocation;

import java.util.*;

/**
 * Renders the stubbing hint on argument mismatches
 */
class StubbingArgMismatches {

    Map<Invocation, Set<Invocation>> mismatches = new LinkedHashMap<Invocation, Set<Invocation>>();

    public void add(Invocation invocation, Invocation stubbing) {
        Set<Invocation> matchingInvocations = mismatches.get(stubbing);
        if (matchingInvocations == null) {
            matchingInvocations = new LinkedHashSet<Invocation>();
            mismatches.put(stubbing, matchingInvocations);
        }
        matchingInvocations.add(invocation);
    }

    public void log(MockitoLogger logger) {
        if (mismatches.isEmpty()) {
            return;
        }

        StringBuilder out = new StringBuilder("[MockitoHint] See javadoc for MockitoHint class.");
        int x = 1;
        for (Map.Entry<Invocation, Set<Invocation>> m : mismatches.entrySet()) {
            out.append("\n[MockitoHint] ").append(x++).append(". unused stub  ")
                    .append(m.getKey().getLocation());
            for (Invocation invocation : m.getValue()) {
                out.append("\n[MockitoHint]  - arg mismatch ").append(invocation.getLocation());
            }
        }

        logger.log(out.toString());
    }
}
