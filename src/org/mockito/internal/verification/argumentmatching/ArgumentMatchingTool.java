/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import org.mockito.MockitoMatcher;
import org.mockito.internal.matchers.ContainsTypedDescription;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentMatchingTool {

    /**
     * Suspiciously not matching arguments are those that don't match, the toString() representation is the same but types are different.
     */
    public Integer[] getSuspiciouslyNotMatchingArgsIndexes(List<MockitoMatcher> matchers, Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Integer[0];
        }
        
        List<Integer> suspicious = new LinkedList<Integer>();
        int i = 0;
        for (MockitoMatcher m : matchers) {
            if (m instanceof ContainsTypedDescription
                    && !safelyMatches(m, arguments[i]) 
                    && toStringEquals(m, arguments[i])
                    && !((ContainsTypedDescription) m).typeMatches(arguments[i])) {
                suspicious.add(i);
            }
            i++;
        }
        return suspicious.toArray(new Integer[0]);
    }

    private boolean safelyMatches(MockitoMatcher m, Object arg) {
        try {
            return m.matches(arg);
        } catch (Throwable t) {
            return false;
        }
    }

    private boolean toStringEquals(MockitoMatcher m, Object arg) {
        return m.describe().equals(arg == null ? "null" : arg.toString());
    }
}
