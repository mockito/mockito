package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.invocation.Stubbing;

import java.util.Collection;

/**
 * Contains unused stubbings, knows how to format them
 */
class UnusedStubbings {

    private final Collection<Stubbing> unused;

    UnusedStubbings(Collection<Stubbing> unused) {
        this.unused = unused;
    }

    void format(String testName, MockitoLogger logger) {
        if (unused.isEmpty()) {
            return;
        }

        //TODO 544 it would be nice to make the String look good if x goes multiple digits (padding)
        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Stubbing candidate : unused) {
            if (!candidate.wasUsed()) {
                hint.appendLine(x++, ". Unused ", candidate.getInvocation().getLocation());
            }
        }
        logger.log(hint.toString());
    }

    int size() {
        return unused.size();
    }

    public String toString() {
        return unused.toString();
    }
}
