/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.hamcrest.Matcher;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unchecked")
public class ArgumentMatcherStorageImpl implements ArgumentMatcherStorage {

    public static final int TWO_SUB_MATCHERS = 2;
    public static final int ONE_SUB_MATCHER = 1;
    private Stack<LocalizedMatcher> matcherStack = new Stack<LocalizedMatcher>();
    
    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportMatcher(org.hamcrest.Matcher)
     */
    public HandyReturnValues reportMatcher(Matcher matcher) {
        matcherStack.push(new LocalizedMatcher(matcher));
        return new HandyReturnValues();
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#pullLocalizedMatchers()
     */
    public List<LocalizedMatcher> pullLocalizedMatchers() {
        if (matcherStack.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<LocalizedMatcher> matchers = new ArrayList<LocalizedMatcher>(matcherStack);
        matcherStack.clear();
        return (List) matchers;
    }

    /* (non-Javadoc)
    * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportAnd()
    */
    public HandyReturnValues reportAnd() {
        assertStateFor("And(?)", TWO_SUB_MATCHERS);
        And and = new And(popLastArgumentMatchers(TWO_SUB_MATCHERS));
        matcherStack.push(new LocalizedMatcher(and));
        return new HandyReturnValues();
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportOr()
     */
    public HandyReturnValues reportOr() {
        assertStateFor("Or(?)", TWO_SUB_MATCHERS);
        Or or = new Or(popLastArgumentMatchers(TWO_SUB_MATCHERS));
        matcherStack.push(new LocalizedMatcher(or));
        return new HandyReturnValues();
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportNot()
     */
    public HandyReturnValues reportNot() {
        assertStateFor("Not(?)", ONE_SUB_MATCHER);
        Not not = new Not(popLastArgumentMatchers(ONE_SUB_MATCHER).get(0));
        matcherStack.push(new LocalizedMatcher(not));
        return new HandyReturnValues();
    }

    private void assertStateFor(String additionalMatcherName, int subMatchersCount) {
        assertMatchersFoundFor(additionalMatcherName);
        assertIncorrectUseOfAdditionalMatchers(additionalMatcherName, subMatchersCount);
    }

    private List<Matcher> popLastArgumentMatchers(int count) {
        List<Matcher> result = new LinkedList<Matcher>();
        result.addAll(matcherStack.subList(matcherStack.size() - count, matcherStack.size()));
        for (int i = 0; i < count; i++) {
            matcherStack.pop();
        }
        return result;
    }

    private void assertMatchersFoundFor(String additionalMatcherName) {
        if (matcherStack.isEmpty()) {
            matcherStack.clear();
            new Reporter().reportNoSubMatchersFound(additionalMatcherName);
        }
    }

    private void assertIncorrectUseOfAdditionalMatchers(String additionalMatcherName, int count) {
        if(matcherStack.size() < count) {
            ArrayList<LocalizedMatcher> lastMatchers = new ArrayList<LocalizedMatcher>(matcherStack);
            matcherStack.clear();
            new Reporter().incorrectUseOfAdditionalMatchers(additionalMatcherName, count, lastMatchers);
        }
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#validateState()
     */
    public void validateState() {
        if (!matcherStack.isEmpty()) {
            ArrayList lastMatchers = new ArrayList<LocalizedMatcher>(matcherStack);
            matcherStack.clear();
            new Reporter().misplacedArgumentMatcher(lastMatchers);
        }
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reset()
     */
    public void reset() {
        matcherStack.clear();
    }
}