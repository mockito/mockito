/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.plugins.MockitoLogger;
import org.mockito.invocation.Invocation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains stubbing arg mismatches, knows how to format them
 */
class StubbingArgMismatches {

    final Map<Invocation, Set<Invocation>> mismatches = new LinkedHashMap<Invocation, Set<Invocation>>();

    public void add(Invocation invocation, Invocation stubbing) {
        Set<Invocation> matchingInvocations = mismatches.get(stubbing);
        if (matchingInvocations == null) {
            matchingInvocations = new LinkedHashSet<Invocation>();
            mismatches.put(stubbing, matchingInvocations);
        }
        matchingInvocations.add(invocation);
    }

    public void format(String testName, MockitoLogger logger) {
        if (mismatches.isEmpty()) {
            return;
        }

        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Map.Entry<Invocation, Set<Invocation>> m : mismatches.entrySet()) {
            hint.appendLine(x++, ". Unused... ", m.getKey().getLocation());
            for (Invocation invocation : m.getValue()) {
                hint.appendLine(" ...args ok? ", invocation.getLocation());
            }
        }

        logger.log(hint.toString());
    }

    public int size() {
        return mismatches.size();
    }

    public String toString() {
        return "" + mismatches;
    }
}
