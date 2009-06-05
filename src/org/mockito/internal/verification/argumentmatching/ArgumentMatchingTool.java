package org.mockito.internal.verification.argumentmatching;

import java.util.List;

import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public class ArgumentMatchingTool {

    public Matcher[] getSuspiciouslyNotMatchingArgs(List<Matcher> matchers, Object[] arguments) {
        return new Matcher[0];
    }
}
