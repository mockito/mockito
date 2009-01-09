/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.hamcrest.Matcher;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;

@SuppressWarnings("unchecked")
public class ArgumentMatcherStorageImpl implements ArgumentMatcherStorage {
    
    private Stack<Matcher> matcherStack = new Stack<Matcher>();
    
    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportMatcher(org.hamcrest.Matcher)
     */
    public HandyReturnValues reportMatcher(Matcher matcher) {
        matcherStack.push(matcher);
        return new HandyReturnValues();
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#pullMatchers()
     */
    public List<Matcher> pullMatchers() {
        if (matcherStack.isEmpty()) {
            return null;
        }
        
        ArrayList<Matcher> matchers = new ArrayList<Matcher>(matcherStack);
        matcherStack.clear();
        return matchers;
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportAnd()
     */
    public HandyReturnValues reportAnd() {
        assertState(!matcherStack.isEmpty(), "No matchers found for And(?).");
        matcherStack.push(new And(popLastArgumentMatchers(2)));
        return new HandyReturnValues();
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportNot()
     */
    public HandyReturnValues reportNot() {
        assertState(!matcherStack.isEmpty(), "No matchers found for Not(?).");
        matcherStack.push(new Not(popLastArgumentMatchers(1).get(0)));
        return new HandyReturnValues();
    }

    private List<Matcher> popLastArgumentMatchers(int count) {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        assertState(matcherStack.size() >= count,
                "" + count + " matchers expected, " + matcherStack.size() + " recorded.");
        List<Matcher> result = new LinkedList<Matcher>();
        result.addAll(matcherStack.subList(matcherStack.size() - count, matcherStack.size()));
        for (int i = 0; i < count; i++) {
            matcherStack.pop();
        }
        return result;
    }

    private void assertState(boolean toAssert, String message) {
        if (!toAssert) {
            matcherStack.clear();
            throw new InvalidUseOfMatchersException(message);
        }
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reportOr()
     */
    public HandyReturnValues reportOr() {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        matcherStack.push(new Or(popLastArgumentMatchers(2)));
        return new HandyReturnValues();
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#validateState()
     */
    public void validateState() {
        if (!matcherStack.isEmpty()) {
            matcherStack.clear();
            new Reporter().misplacedArgumentMatcher();
        }
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.ArgumentMatcherStorage#reset()
     */
    public void reset() {
        matcherStack.clear();
    }
}