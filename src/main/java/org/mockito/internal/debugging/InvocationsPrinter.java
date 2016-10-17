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

        String out = "";
        int x = 1;
        for(Invocation i:invocations) {
            if (x == 1) {
                out += line("[Mockito] Interactions of: " + mock);
            }
            out += line(" " + x++ + ". " + i.toString());
            out += line("  " + i.getLocation());
            if (i.stubInfo() != null) {
                out += line("   - stubbed " + i.stubInfo().stubbedAt());
            }
        }

        LinkedList<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() {
            public boolean isOut(Stubbing s) {
                return s.wasUsed();
            }
        });

        if (unused.isEmpty()) {
            return out;
        }
        out += line("[Mockito] Unused stubbings of: " + mock);

        x = 1;
        for(Stubbing s:stubbings) {
            out += line(" " + x++ + ". " + s.getInvocation().toString());
            out += line("  - stubbed " + s.getInvocation().getLocation());
        }
        return out;
    }

    private String line(String text) {
        return text + "\n";
    }
}
