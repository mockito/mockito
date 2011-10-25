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

@SuppressWarnings("unchecked")
public class ArgumentMatchingTool {

    /**
     * Suspiciously not matching arguments are those that don't match, the toString() representation is the same but types are different.
     */
    public Integer[] getSuspiciouslyNotMatchingArgsIndexes(List<Matcher> matchers, Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Integer[0];
        }
        
        List<Integer> suspicious = new LinkedList<Integer>();
        int i = 0;
        for (Matcher m : matchers) {
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

    private boolean safelyMatches(Matcher m, Object arg) {
        try {
            return m.matches(arg);
        } catch (Throwable t) {
            return false;
        }
    }

    private boolean toStringEquals(Matcher m, Object arg) {
        return StringDescription.toString(m).equals(arg == null? "null" : arg.toString());
    }
}
