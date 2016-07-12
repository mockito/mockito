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

import static org.mockito.internal.exceptions.Reporter.incorrectUseOfAdditionalMatchers;
import static org.mockito.internal.exceptions.Reporter.misplacedArgumentMatcher;
import static org.mockito.internal.exceptions.Reporter.reportNoSubMatchersFound;

import java.util.*;

public class ArgumentMatcherStorageImpl implements ArgumentMatcherStorage {

    public static final int TWO_SUB_MATCHERS = 2;
    public static final int ONE_SUB_MATCHER = 1;
    private final Stack<LocalizedMatcher> matcherStack = new Stack<LocalizedMatcher>();
    
    public void reportMatcher(ArgumentMatcher matcher) {
        matcherStack.push(new LocalizedMatcher(matcher));
    }

    public List<LocalizedMatcher> pullLocalizedMatchers() {
        if (matcherStack.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<LocalizedMatcher> matchers = new ArrayList<LocalizedMatcher>(matcherStack);
        matcherStack.clear();
        return matchers;
    }

    /* (non-Javadoc)
    * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportAnd()
    */
    public void reportAnd() {
        assertStateFor("And(?)", TWO_SUB_MATCHERS);
        And and = new And(popLastArgumentMatchers(TWO_SUB_MATCHERS));
        matcherStack.push(new LocalizedMatcher(and));
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportOr()
     */
    public void reportOr() {
        assertStateFor("Or(?)", TWO_SUB_MATCHERS);
        Or or = new Or(popLastArgumentMatchers(TWO_SUB_MATCHERS));
        matcherStack.push(new LocalizedMatcher(or));
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportNot()
     */
    public void reportNot() {
        assertStateFor("Not(?)", ONE_SUB_MATCHER);
        Not not = new Not(popLastArgumentMatchers(ONE_SUB_MATCHER).get(0));
        matcherStack.push(new LocalizedMatcher(not));
    }

    private void assertStateFor(String additionalMatcherName, int subMatchersCount) {
        assertMatchersFoundFor(additionalMatcherName);
        assertIncorrectUseOfAdditionalMatchers(additionalMatcherName, subMatchersCount);
    }

    private List<ArgumentMatcher> popLastArgumentMatchers(int count) {
        LinkedList<ArgumentMatcher> result = new LinkedList<ArgumentMatcher>();
        for (int i = 0; i < count; i++) {
            result.addFirst(matcherStack.pop().getMatcher());
        }
        return result;
    }

    private void assertMatchersFoundFor(String additionalMatcherName) {
        if (matcherStack.isEmpty()) {
            matcherStack.clear();
            throw reportNoSubMatchersFound(additionalMatcherName);
        }
    }

    private void assertIncorrectUseOfAdditionalMatchers(String additionalMatcherName, int count) {
        if(matcherStack.size() < count) {
            ArrayList<LocalizedMatcher> lastMatchers = new ArrayList<LocalizedMatcher>(matcherStack);
            matcherStack.clear();
            throw incorrectUseOfAdditionalMatchers(additionalMatcherName, count, lastMatchers);
        }
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#validateState()
     */
    public void validateState() {
        if (!matcherStack.isEmpty()) {
            ArrayList<LocalizedMatcher> lastMatchers = new ArrayList<LocalizedMatcher>(matcherStack);
            matcherStack.clear();
            throw misplacedArgumentMatcher(lastMatchers);
        }
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reset()
     */
    public void reset() {
        matcherStack.clear();
    }
}
