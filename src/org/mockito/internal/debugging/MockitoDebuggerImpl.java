package org.mockito.internal.debugging;

import org.mockito.MockitoDebugger;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;

import java.util.List;

import static java.util.Arrays.*;

public class MockitoDebuggerImpl implements MockitoDebugger {
    public String printInvocations(Object ... mocks) {
        String out = "";
        AllInvocationsFinder finder = new AllInvocationsFinder();
        List<Invocation> invocations = finder.getAllInvocations(asList(mocks));
        out += line("********************************");
        out += line("*** Mockito interactions log ***");
        out += line("********************************");
        for(Invocation i:invocations) {
            out += line(i.toString());
            out += line(" invoked: " + i.getLocation());
            if (i.stubInfo() != null) {
                out += line(" stubbed: " + i.stubInfo().stubbedAt());
            }
        }
        invocations = finder.getAllUnusedStubs(asList(mocks));
        if (invocations.isEmpty()) {
            return print(out);
        }
        out += line("********************************");
        out += line("***       Unused stubs       ***");
        out += line("********************************");
        invocations = finder.getAllUnusedStubs(asList(mocks));
        for(Invocation i:invocations) {
            out += line(i.toString());
            out += line(" stubbed: " + i.getLocation());
        }
        return print(out);
    }

    private String line(String text) {
        return text + "\n";
    }

    private String print(String out) {
        System.out.println(out);
        return out;
    }
}