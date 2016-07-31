/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.MockitoDebugger;
import org.mockito.internal.invocation.UnusedStubsFinder;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;

import java.util.List;

import static java.util.Arrays.asList;

public class MockitoDebuggerImpl implements MockitoDebugger {

    private final UnusedStubsFinder unusedStubsFinder = new UnusedStubsFinder();

    public String printInvocations(Object ... mocks) {
        String out = "";
        List<Invocation> invocations = AllInvocationsFinder.find(asList(mocks));
        out += line("********************************");
        out += line("*** Mockito interactions log ***");
        out += line("********************************");
        for(Invocation i:invocations) {
            out += line(i.toString());
            out += line(" invoked: " + i.getLocation());
            if (i.stubInfo() != null) {
                out += line(" stubbed: " + i.stubInfo().stubbedAt().toString());
            }
        }

        invocations = unusedStubsFinder.find(asList(mocks));
        if (invocations.isEmpty()) {
            return print(out);
        }
        out += line("********************************");
        out += line("***       Unused stubs       ***");
        out += line("********************************");
        
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