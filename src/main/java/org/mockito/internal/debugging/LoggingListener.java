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

    private List<String> argMismatchStubs = new LinkedList<String>();
    private List<String> unusedStubs = new LinkedList<String>();
    private List<String> unstubbedCalls = new LinkedList<String>();

    public LoggingListener(boolean warnAboutUnstubbed) {
        this.warnAboutUnstubbed = warnAboutUnstubbed;
    }

    public void foundStubCalledWithDifferentArgs(Invocation unused, InvocationMatcher unstubbed) {
        //TODO there is not good reason we should get Invocation and InvocationMatcher here
        // we should pass 2 InvocationMatchers and testing is easier
        // it's also confusing that unstubbed invocation is passed as InvocationMatcher (should be rather Invocation)

        //this information comes in pairs
        argMismatchStubs.add("[Mockito] stubbed with those args here   " + unused.getLocation());
        argMismatchStubs.add("[Mockito] BUT called with different args " + unstubbed.getInvocation().getLocation());
    }

    public void foundUnusedStub(Invocation unused) {
        unusedStubs.add("[Mockito] This stubbing was never used   " + unused.getLocation());
    }

    public void foundUnstubbed(InvocationMatcher unstubbed) {
        if (warnAboutUnstubbed) {
            unstubbedCalls.add("[Mockito] unstubbed method " + unstubbed.getInvocation().getLocation());
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
            lines.add("[Mockito] Unused stubbing due to argument mismatch (is stubbing correct in the test?):");
            lines.add("[Mockito]");
            for (String info : argMismatchStubs) {
                lines.add(info);
            }
        }

        if (!unusedStubs.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Unused stubbing (perhaps can be removed from the test?):");
            lines.add("[Mockito]");
            for (String info : unusedStubs) {
                lines.add(info);
            }
        }

        if (!unstubbedCalls.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Unstubbed method calls (perhaps missing stubbing in the test?):");
            lines.add("[Mockito]");
            for (String info : unstubbedCalls) {
                lines.add(info);
            }
        }
        return join("", lines);
    }
}