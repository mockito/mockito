/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;

import static java.util.Collections.emptyList;
import static org.mockito.internal.exceptions.Reporter.incorrectUseOfAdditionalMatchers;
import static org.mockito.internal.exceptions.Reporter.misplacedArgumentMatcher;
import static org.mockito.internal.exceptions.Reporter.reportNoSubMatchersFound;

import java.util.*;

public class ArgumentMatcherStorageImpl implements ArgumentMatcherStorage {

    private static final int TWO_SUB_MATCHERS = 2;
    private static final int ONE_SUB_MATCHER = 1;
    private final Stack<LocalizedMatcher> matcherStack = new Stack<LocalizedMatcher>();

    public void reportMatcher(ArgumentMatcher<?> matcher) {
        matcherStack.push(new LocalizedMatcher(matcher));
    }

    public List<LocalizedMatcher> pullLocalizedMatchers() {
        if (matcherStack.isEmpty()) {
            return emptyList();
        }

        List<LocalizedMatcher> lastMatchers = resetStack();
        return lastMatchers;
    }

    public void reportAnd() {
        assertStateFor("And(?)", TWO_SUB_MATCHERS);

        ArgumentMatcher<?> m1 = popMatcher();
        ArgumentMatcher<?> m2 = popMatcher();

        reportMatcher(new And(m1, m2));
    }

    public void reportOr() {
        assertStateFor("Or(?)", TWO_SUB_MATCHERS);

        ArgumentMatcher<?> m1 = popMatcher();
        ArgumentMatcher<?> m2 = popMatcher();

        reportMatcher(new Or(m1, m2));
    }

    public void reportNot() {
        assertStateFor("Not(?)", ONE_SUB_MATCHER);

        ArgumentMatcher<?> m = popMatcher();

        reportMatcher(new Not(m));
    }

    public void validateState() {
        if (!matcherStack.isEmpty()) {
            List<LocalizedMatcher> lastMatchers = resetStack();
            throw misplacedArgumentMatcher(lastMatchers);
        }
    }

    public void reset() {
        matcherStack.clear();
    }

    private void assertStateFor(String additionalMatcherName, int subMatchersCount) {
        if (matcherStack.isEmpty()) {
            throw reportNoSubMatchersFound(additionalMatcherName);
        }
        if (matcherStack.size() < subMatchersCount) {
            List<LocalizedMatcher> lastMatchers = resetStack();
            throw incorrectUseOfAdditionalMatchers(additionalMatcherName, subMatchersCount, lastMatchers);
        }
    }

    private ArgumentMatcher<?> popMatcher() {
        return matcherStack.pop().getMatcher();
    }

    private List<LocalizedMatcher> resetStack() {
        ArrayList<LocalizedMatcher> lastMatchers = new ArrayList<LocalizedMatcher>(matcherStack);
        reset();
        return lastMatchers;
    }

}
