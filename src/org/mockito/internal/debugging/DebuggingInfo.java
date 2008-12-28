package org.mockito.internal.debugging;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.util.MockitoLogger;

public class DebuggingInfo {

    private final List<Invocation> unusedStubs = new LinkedList<Invocation>();
    private List<Invocation> unstubbedInvocations = new LinkedList<Invocation>();
    private final String testName;

    public DebuggingInfo(String testName) {
        this.testName = testName;
    }

    public void addUnusedStub(Invocation invocation) {
        this.unusedStubs.add(invocation);
    }

    public void printInfo(MockitoLogger logger) {
        if (!shouldPrint()) {
            return;
        }
        
//        print("Mockito detected some of your stubs were not called. This *might* be the reason your test failed.");
//        print("Test:");
//        print(test);
        
        for (Invocation i : unusedStubs) {
            logger.print("Warning - unused stub detected here:");
            logger.print(i.getStackTrace().getStackTrace()[0]);
        }
        
        for (Invocation i : unstubbedInvocations) {
            logger.print("Warning - unstubbed method invoked here:");
            logger.print(i.getStackTrace().getStackTrace()[0]);
        }
    }

    private boolean shouldPrint() {
        //TODO test, include unstubbedInvocations...
        return !unusedStubs.isEmpty() || !unstubbedInvocations.isEmpty();
    }

    public void addUnstubbedInvocation(Invocation invocation) {
        unstubbedInvocations.add(invocation);
    }
}
