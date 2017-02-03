/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.Mockito;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;
import java.util.LinkedList;

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
        for(Invocation i:invocations) {
            if (x == 1) {
                sb.append("[Mockito] Interactions of: ").append(mock).append("\n");
            }
            sb.append(" ").append(x++).append(". ").append(i.toString()).append("\n");
            sb.append("  ").append(i.getLocation()).append("\n");
            if (i.stubInfo() != null) {
                sb.append("   - stubbed ").append(i.stubInfo().stubbedAt()).append("\n");
            }
        }

        LinkedList<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() {
            public boolean isOut(Stubbing s) {
                return s.wasUsed();
            }
        });

        if (unused.isEmpty()) {
            return sb.toString();
        }
        sb.append("[Mockito] Unused stubbings of: ").append(mock).append("\n");

        x = 1;
        for(Stubbing s:stubbings) {
            sb.append(" ").append(x++).append(". ").append(s.getInvocation()).append("\n");
            sb.append("  - stubbed ").append(s.getInvocation().getLocation()).append("\n");
        }
        return sb.toString();
    }
}
