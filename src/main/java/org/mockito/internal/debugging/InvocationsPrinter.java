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
        int x = 1;
        for (Invocation i : invocations) {
            if (x == 1) {
                sb.append("[Mockito] Interactions of: ").append(mock).append("\n");
            }
            sb.append(" ").append(x++).append(". ").append(i).append("\n");
            sb.append("  ").append(i.getLocation()).append("\n");
            if (i.stubInfo() != null) {
                sb.append("   - stubbed ").append(i.stubInfo().stubbedAt()).append("\n");
            }
        }

        List<Stubbing> unused =
                stubbings.stream()
                        .filter(stubbing -> !stubbing.wasUsed())
                        .collect(Collectors.toList());

        if (unused.isEmpty()) {
            return sb.toString();
        }
        sb.append("[Mockito] Unused stubbings of: ").append(mock).append("\n");

        x = 1;
        for (Stubbing s : stubbings) {
            sb.append(" ").append(x++).append(". ").append(s.getInvocation()).append("\n");
            sb.append("  - stubbed ").append(s.getInvocation().getLocation()).append("\n");
        }
        return sb.toString();
    }
}
