package org.mockito.internal.debugging;

import org.mockito.MockitoDebugger;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;

import java.util.List;

import static java.util.Arrays.*;

public class MockitoDebuggerImpl implements MockitoDebugger {
    public void printInvocations(Object ... mocks) {
        AllInvocationsFinder finder = new AllInvocationsFinder();
        List<Invocation> invocations = finder.getAllInvocations(asList(mocks));
        System.out.println("********************************");
        System.out.println("*** Mockito interactions log ***");
        System.out.println("********************************");
        for(Invocation i:invocations) {
            //TODO: this is very ugly
            System.out.println(i.toString());
            System.out.println(" invoked: " + i.getLocation());
            if (i.stubInfo() != null) {
                System.out.println(" stubbed: " + i.stubInfo().stubbingLocation());
            }
        }
    }
}