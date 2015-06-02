/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.debugging;

import java.io.PrintStream;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;

/**
 * Logs all invocations to standard output.
 * 
 * Used for debugging interactions with a mock. 
 */
public class VerboseMockInvocationLogger implements InvocationListener {

    // visible for testing
    final PrintStream printStream;

    private int mockInvocationsCounter = 0;

    public VerboseMockInvocationLogger() {
        this(System.out);
    }

    public VerboseMockInvocationLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void reportInvocation(MethodInvocationReport methodInvocationReport) {
        printHeader();
        printStubInfo(methodInvocationReport);
        printInvocation(methodInvocationReport.getInvocation());
        printReturnedValueOrThrowable(methodInvocationReport);
        printFooter();
    }

    private void printReturnedValueOrThrowable(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.threwException()) {
            String message = methodInvocationReport.getThrowable().getMessage() == null ? "" : " with message " + methodInvocationReport.getThrowable().getMessage();
            printlnIndented("has thrown: " + methodInvocationReport.getThrowable().getClass() + message);
        } else {
            String type = (methodInvocationReport.getReturnedValue() == null) ? "" : " (" + methodInvocationReport.getReturnedValue().getClass().getName() + ")";
            printlnIndented("has returned: \"" + methodInvocationReport.getReturnedValue() + "\"" + type);
        }
    }

    private void printStubInfo(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.getLocationOfStubbing() != null) {
            printlnIndented("stubbed: " + methodInvocationReport.getLocationOfStubbing());
        }
    }

    private void printHeader() {
        mockInvocationsCounter++;
        printStream.println("############ Logging method invocation #" + mockInvocationsCounter + " on mock/spy ########");
    }

    private void printInvocation(DescribedInvocation invocation) {
        printStream.println(invocation.toString());
//        printStream.println("Handling method call on a mock/spy.");
        printlnIndented("invoked: " + invocation.getLocation().toString());
    }

    private void printFooter() {
        printStream.println("");
    }
    
    private void printlnIndented(String message) {
        printStream.println("   " + message);
    }
    
}
