/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;
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

    public void reportUnused() {
        if (unused.size() > 0) {
            List<Invocation> invocations = ListUtil.convert(unused, (ListUtil.Converter) new ListUtil.Converter<Stubbing, Invocation>() {
                public Invocation convert(Stubbing s) {
                    return s.getInvocation();
                }
            });


            Reporter.unncessaryStubbingException(invocations);
        }
    }
}
