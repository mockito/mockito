/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import java.util.LinkedList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;

@SuppressWarnings("unchecked")
public class ArgumentMatchingTool {

    private ArgumentMatchingTool() {}
    /**
     * Suspiciously not matching arguments are those that don't match, the toString() representation is the same but types are different.
     */
    public static Integer[] getSuspiciouslyNotMatchingArgsIndexes(
            List<ArgumentMatcher> matchers, Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Integer[0];
        }

        List<Integer> suspicious = new LinkedList<Integer>();
        int i = 0;
        for (ArgumentMatcher m : matchers) {
            if (m instanceof ContainsExtraTypeInfo
                    && !safelyMatches(m, arguments[i])
                    && toStringEquals(m, arguments[i])
                    && !((ContainsExtraTypeInfo) m).typeMatches(arguments[i])) {
                suspicious.add(i);
            }
            i++;
        }
        return suspicious.toArray(new Integer[0]);
    }

    private static boolean safelyMatches(ArgumentMatcher m, Object arg) {
        try {
            return m.matches(arg);
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean toStringEquals(ArgumentMatcher m, Object arg) {
        return m.toString().equals(arg == null ? "null" : arg.toString());
    }
}
