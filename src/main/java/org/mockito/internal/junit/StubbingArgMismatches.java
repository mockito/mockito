/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.mockito.invocation.Invocation;
import org.mockito.plugins.MockitoLogger;

/**
 * Contains stubbing arg mismatches, knows how to format them
 */
class StubbingArgMismatches {

    final Map<Invocation, Set<Invocation>> mismatches = new LinkedHashMap<>();

    public void add(Invocation invocation, Invocation stubbing) {
        Set<Invocation> matchingInvocations =
                mismatches.computeIfAbsent(
                        stubbing, (Invocation k) -> new LinkedHashSet<Invocation>());
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

    @Override
    public String toString() {
        return "" + mismatches;
    }
}
