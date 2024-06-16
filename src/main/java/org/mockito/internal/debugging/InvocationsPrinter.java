/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

/**
 * Prints invocations in human-readable, printable way
 */
public class InvocationsPrinter {

    public String printInvocations(Object mock) {
        Collection<Invocation> invocations = Mockito.mockingDetails(mock).getInvocations();
        Collection<Stubbing> stubbings = Mockito.mockingDetails(mock).getStubbings();

        if (invocations.isEmpty() && stubbings.isEmpty()) {
            return "No interactions and stubbings found for mock: " + mock;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(printInteractions(invocations, mock));
        sb.append(printUnusedStubbings(stubbings, mock));

        return sb.toString();
    }

    private String printInteractions(Collection<Invocation> invocations, Object mock) {
        StringBuilder sb = new StringBuilder();
        int interactionCount = 1;

        for (Invocation i : invocations) {
            if (interactionCount == 1) {
                sb.append("[Mockito] Interactions of: ").append(mock).append("\n");
            }
            sb.append(" ").append(interactionCount++).append(". ").append(printInvocation(i)).append("\n");
            sb.append("  ").append(i.getLocation()).append("\n");
            if (i.stubInfo() != null) {
                sb.append("   - stubbed ").append(i.stubInfo().stubbedAt()).append("\n");
            }
        }

        return sb.toString();
    }

    private String printUnusedStubbings(Collection<Stubbing> stubbings, Object mock) {
        List<Stubbing> unusedStubbings = stubbings.stream()
                .filter(stubbing -> !stubbing.wasUsed())
                .collect(Collectors.toList());

        if (unusedStubbings.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[Mockito] Unused stubbings of: ").append(mock).append("\n");

        int stubbingCount = 1;
        for (Stubbing s : unusedStubbings) {
            sb.append(" ").append(stubbingCount++).append(". ").append(printInvocation(s.getInvocation())).append("\n");
            sb.append("  - stubbed ").append(s.getInvocation().getLocation()).append("\n");
        }

        return sb.toString();
    }

    private String printInvocation(Invocation invocation) {
        return invocation.toString();
    }
}
