package org.mockito.internal.runners;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.Invocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Renders the stubbing hint on argument mismatches
 */
class StubbingArgMismatches {

    Map<Invocation, Set<Invocation>> mismatches = new HashMap<Invocation, Set<Invocation>>();

    public void add(Invocation invocation, Invocation stubbing) {
        Set<Invocation> matchingInvocations = mismatches.get(stubbing);
        if (matchingInvocations == null) {
            matchingInvocations = new HashSet<Invocation>();
            mismatches.put(stubbing, matchingInvocations);
        }
        matchingInvocations.add(invocation);
    }

    public void log(MockitoLogger logger) {
        if (mismatches.isEmpty()) {
            return;
        }

        StringBuilder out = new StringBuilder("[MockitoHint] See javadoc for MockitoHint class.\n");
        int x = 1;
        for (Map.Entry<Invocation, Set<Invocation>> m : mismatches.entrySet()) {
            out.append("[MockitoHint] ").append(x++).append(". unused stub  ")
                    .append(m.getKey().getLocation()).append("\n");
            out.append("[MockitoHint]    similar call ").append(m.getValue().iterator().next().getLocation());
        }

        logger.log(out.toString());
    }
}
