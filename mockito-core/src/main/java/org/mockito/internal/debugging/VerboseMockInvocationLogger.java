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

    /**
     * Track the last invocation signature printed on this thread with a timestamp.
     * This helps us ignore only the immediate duplicate produced by verification callbacks,
     * while preserving legitimate consecutive real invocations of the same method.
     */
    private static final ThreadLocal<String> LAST_SIG = new ThreadLocal<>();

    private static final ThreadLocal<Long> LAST_TS = new ThreadLocal<>();

    /** Window (nanoseconds) to consider 2 identical prints as the same logical event. */
    private static final long DUP_WINDOW_NANOS = 200_000_000L; // 200 ms

    public VerboseMockInvocationLogger() {
        this(System.out);
    }

    public VerboseMockInvocationLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void reportInvocation(MethodInvocationReport methodInvocationReport) {
        DescribedInvocation invocation = methodInvocationReport.getInvocation();

        // Signature key that represents "what line would be printed" for the invocation.
        // Using invocation.toString() is sufficient for detecting the verification-duplicate.
        final String sig = invocation != null ? String.valueOf(invocation) : "<null-invocation>";

        // Detect verification-origin callback via current stack.
        final boolean verificationFlow = isVerificationStack();

        // If this is a verification flow AND we just printed the same signature very recently
        // (same thread, within DUP_WINDOW), skip to avoid duplicate verbose line.
        if (verificationFlow) {
            String last = LAST_SIG.get();
            Long ts = LAST_TS.get();
            long now = System.nanoTime();
            if (sig.equals(last)
                    && ts != null
                    && (now - ts) >= 0
                    && (now - ts) <= DUP_WINDOW_NANOS) {
                return; // skip duplicate triggered by verification
            }
        }

        // Print and update the last-printed signature & timestamp (per-thread)
        printHeader();
        printStubInfo(methodInvocationReport);
        printInvocation(invocation);
        printReturnedValueOrThrowable(methodInvocationReport);
        printFooter();

        LAST_SIG.set(sig);
        LAST_TS.set(System.nanoTime());
    }

    /** Detects whether current call stack indicates a Mockito verification is in progress. */
    private boolean isVerificationStack() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : stack) {
            final String cls = ste.getClassName();
            final String mtd = ste.getMethodName();

            // Public API entry points
            if (("org.mockito.Mockito".equals(cls) && "verify".equals(mtd))
                    || ("org.mockito.InOrder".equals(cls) && "verify".equals(mtd))
                    || ("org.mockito.BDDMockito".equals(cls) && "then".equals(mtd))
                    || ("org.mockito.Mockito".equals(cls) && "verifyNoMoreInteractions".equals(mtd))
                    || ("org.mockito.Mockito".equals(cls)
                            && "verifyZeroInteractions".equals(mtd))) {
                return true;
            }

            // Internal verification machinery (package-level signal)
            if (cls.startsWith("org.mockito.internal.verification")
                    || cls.startsWith("org.mockito.verification") // public types used internally
                    || cls.startsWith(
                            "org.mockito.internal.progress") // verification progress tracking
                    || cls.startsWith(
                            "org.mockito.internal.invocation") // verification uses invocation
                    // details
                    || cls.startsWith(
                            "org.mockito.internal.debugging")) { // conservative: debugging during
                // verify
                return true;
            }
        }
        return false;
    }

    private void printReturnedValueOrThrowable(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.threwException()) {
            String message =
                    methodInvocationReport.getThrowable().getMessage() == null
                            ? ""
                            : " with message " + methodInvocationReport.getThrowable().getMessage();
            printlnIndented(
                    "has thrown: " + methodInvocationReport.getThrowable().getClass() + message);
        } else {
            String type =
                    (methodInvocationReport.getReturnedValue() == null)
                            ? ""
                            : " ("
                                    + methodInvocationReport.getReturnedValue().getClass().getName()
                                    + ")";
            printlnIndented(
                    "has returned: \"" + methodInvocationReport.getReturnedValue() + "\"" + type);
        }
    }

    private void printStubInfo(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.getLocationOfStubbing() != null) {
            printlnIndented("stubbed: " + methodInvocationReport.getLocationOfStubbing());
        }
    }

    private void printHeader() {
        mockInvocationsCounter++;
        printStream.println(
                "############ Logging method invocation #"
                        + mockInvocationsCounter
                        + " on mock/spy ########");
    }

    private void printInvocation(DescribedInvocation invocation) {
        printStream.println(invocation);
        printlnIndented("invoked: " + invocation.getLocation());
    }

    private void printFooter() {
        printStream.println();
    }

    private void printlnIndented(String message) {
        printStream.println("   " + message);
    }
}
