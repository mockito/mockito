package org.mockito.internal.verification.argumentmatching;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

@SuppressWarnings("unchecked")
public class ArgumentMatchingTool {

    /**
     * Suspiciously not matching arguments are those that don't much BUT the toString() representation is the same.
     * 
     * @param matchers
     * @param arguments
     * @return
     */
    public Matcher[] getSuspiciouslyNotMatchingArgs(List<Matcher> matchers, Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Matcher[0];
        }
        
        List<Matcher> suspicious = new LinkedList<Matcher>();
        int i = 0;
        for (Matcher m : matchers) {
            if (!safelyMatches(m, arguments[i]) && toStringEquals(m, arguments[i])) {
                suspicious.add(m);
            }
            i++;
        }
        return suspicious.toArray(new Matcher[0]);
    }

    private boolean safelyMatches(Matcher m, Object arg) {
        try {
            return m.matches(arg);
        } catch (Throwable t) {
            return false;
        }
    }

    private boolean toStringEquals(Matcher m, Object arg) {
        return StringDescription.toString(m).equals(arg.toString());
    }
}
