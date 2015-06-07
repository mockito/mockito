/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("rawtypes")
public class ArgumentMatchingTool {

    /**
     * Suspiciously not matching arguments are those that don't match, the toString() representation is the same but types are different.
     */
    public Integer[] getSuspiciouslyNotMatchingArgsIndexes(final List<Matcher> matchers, final Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Integer[0];
        }
        
        final List<Integer> suspicious = new LinkedList<Integer>();
        int i = 0;
        for (final Matcher m : matchers) {
            if (m instanceof ContainsExtraTypeInformation 
                    && !safelyMatches(m, arguments[i]) 
                    && toStringEquals(m, arguments[i])
                    && !((ContainsExtraTypeInformation) m).typeMatches(arguments[i])) {
                suspicious.add(i);
            }
            i++;
        }
        return suspicious.toArray(new Integer[0]);
    }

    private boolean safelyMatches(final Matcher m, final Object arg) {
        try {
            return m.matches(arg);
        } catch (final Throwable t) {
            return false;
        }
    }

    private boolean toStringEquals(final Matcher m, final Object arg) {
        return StringDescription.toString(m).equals(arg == null? "null" : arg.toString());
    }
}
