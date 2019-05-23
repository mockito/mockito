/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.plugins.MockitoLogger;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains unused stubbings, knows how to format them
 */
public class UnusedStubbings {

    private final Collection<? extends Stubbing> unused;

    UnusedStubbings(Collection<? extends Stubbing> unused) {
        this.unused = unused;
    }

    void format(String testName, MockitoLogger logger) {
        if (unused.isEmpty()) {
            return;
        }

        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Stubbing candidate : unused) {
            if (!candidate.wasUsed()) {
                hint.appendLine(x++, ". Unused ", candidate.getInvocation().getLocation());
            }
        }
        logger.log(hint.toString());
    }

    public int size() {
        return unused.size();
    }

    public String toString() {
        return unused.toString();
    }

    void reportUnused() {
        if (unused.isEmpty()) {
            return;
        }

        List<Invocation> invocations = new LinkedList<Invocation>();
        for (Stubbing stubbing : unused) {
            invocations.add(stubbing.getInvocation());
        }
        if (invocations.isEmpty()) {
            return;
        }

        Reporter.unncessaryStubbingException(invocations);
    }
}
