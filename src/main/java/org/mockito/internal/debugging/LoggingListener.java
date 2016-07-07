/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.internal.util.StringJoiner.join;

public class LoggingListener implements FindingsListener {
    private final boolean warnAboutUnstubbed;

    private final List<String> argMismatchStubs = new LinkedList<String>();
    private final List<String> unusedStubs = new LinkedList<String>();
    private final List<String> unstubbedCalls = new LinkedList<String>();

    public LoggingListener(boolean warnAboutUnstubbed) {
        this.warnAboutUnstubbed = warnAboutUnstubbed;
    }

    public void foundStubCalledWithDifferentArgs(Invocation unused, InvocationMatcher unstubbed) {
        //TODO there is not good reason we should get Invocation and InvocationMatcher here
        // we should pass 2 InvocationMatchers and testing is easier
        // it's also confusing that unstubbed invocation is passed as InvocationMatcher (should be rather Invocation)

        //this information comes in pairs
        String index = Integer.toString(indexOfNextPair(argMismatchStubs.size()));
        //making sure indentation is correct
        String padding = index.replaceAll("\\d", " ");
        argMismatchStubs.add(index +   ". Stubbed " + unused.getLocation());
        argMismatchStubs.add(padding + "  Invoked " + unstubbed.getInvocation().getLocation());
    }

    static int indexOfNextPair(int collectionSize) {
        return (collectionSize / 2) + 1;
    }

    public void foundUnusedStub(Invocation unused) {
        unusedStubs.add((unusedStubs.size() + 1) + ". " + unused.getLocation());
    }

    public void foundUnstubbed(InvocationMatcher unstubbed) {
        if (warnAboutUnstubbed) {
            unstubbedCalls.add((unstubbedCalls.size() + 1) + ". " + unstubbed.getInvocation().getLocation());
        }
    }

    public String getStubbingInfo() {
        if (argMismatchStubs.isEmpty() && unusedStubs.isEmpty() && unstubbedCalls.isEmpty()) {
            return "";
        }

        List<String> lines = new LinkedList<String>();
        lines.add("[Mockito] Additional stubbing information (see javadoc for StubbingInfo class):");

        if (!argMismatchStubs.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Argument mismatch between stubbing and actual invocation (is stubbing correct in the test?):");
            lines.add("[Mockito]");
            addOrderedList(lines, argMismatchStubs);
        }

        if (!unusedStubs.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Unused stubbing (perhaps can be removed from the test?):");
            lines.add("[Mockito]");
            addOrderedList(lines, unusedStubs);
        }

        if (!unstubbedCalls.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Unstubbed method invocations (perhaps missing stubbing in the test?):");
            lines.add("[Mockito]");
            addOrderedList(lines, unstubbedCalls);
        }
        return join("", lines);
    }

    private void addOrderedList(List<String> target, List<String> additions) {
        for (String a : additions) {
            target.add("[Mockito] " + a);
        }
    }
}