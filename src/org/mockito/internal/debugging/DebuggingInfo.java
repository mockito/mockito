package org.mockito.internal.debugging;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;

public class DebuggingInfo {

    private final List<Invocation> unusedStubs = new LinkedList<Invocation>();
    private final String testName;

    public DebuggingInfo(String testName) {
        this.testName = testName;
    }

    public void addUnusedStub(Invocation invocation) {
        this.unusedStubs.add(invocation);
    }

    public void printInfo() {
        if (!shouldPrint()) {
            return;
        }
        
//        print("Mockito detected some of your stubs were not called. This *might* be the reason your test failed.");
//        print("Test:");
//        print(test);
        
        for (Invocation i : unusedStubs) {
            print("Warning - unused stub detected:");
            print(i.getStackTrace().getStackTrace()[0]);
        }
    }

    private void print(Object text) {
        System.out.println("[Mockito] " + text.toString());
    }

    private boolean shouldPrint() {
        return !unusedStubs.isEmpty();
    }
}
