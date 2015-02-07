/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.reporting.PrintSettings;

@SuppressWarnings("unchecked")
public class ArgumentsPrinter {

    public List<SelfDescribing> describe(final Object[] arguments, final List<Matcher> originalMatchers, final PrintSettings printSettings) {

        List<SelfDescribing> withPrintSettings = new LinkedList<SelfDescribing>();
        final int numOriginalMatchers = originalMatchers.size();

        final Matcher lastMatcher;
        if(numOriginalMatchers > 0) {
            lastMatcher = originalMatchers.get(numOriginalMatchers - 1);
        } else {
            lastMatcher = null;
        }

        final boolean lastMatcherIsVararg;
        if(lastMatcher == null) {
            lastMatcherIsVararg = false;
        } else if (lastMatcher instanceof MatcherDecorator) {
            lastMatcherIsVararg = ((MatcherDecorator) lastMatcher).getActualMatcher() instanceof VarargMatcher;
        } else {
            lastMatcherIsVararg = lastMatcher instanceof VarargMatcher;
        }

        for (int i = 0; i < arguments.length; i++) {
            final int idx = i;
            withPrintSettings.add(new SelfDescribing() {

                private Method getDescribeMismatchMethod(Matcher matcher) throws NoSuchMethodException {
                    return matcher.getClass().getMethod("describeMismatch", Object.class, Description.class);
                }

                private void describeMismatch(final Matcher matcher, final Object argument, Description description) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
                    Matcher actualMatcher;
                    if (matcher instanceof MatcherDecorator) {
                        actualMatcher = ((MatcherDecorator) matcher).getActualMatcher();
                    } else {
                        actualMatcher = matcher;
                    }
                    Method m = getDescribeMismatchMethod(actualMatcher);
                    m.setAccessible(true);
                    m.invoke(actualMatcher, argument, description);
                }

                private void defaultDescribe(Description description) {
                    Matcher matcher = ArgumentsProcessor.argumentToMatcher(arguments[idx]);
                    if (matcher instanceof ContainsExtraTypeInformation && printSettings.extraTypeInfoFor(idx)) {
                        ((ContainsExtraTypeInformation) matcher).withExtraTypeInfo().describeTo(description);
                    } else {
                        matcher.describeTo(description);
                    }
                }

                private void describeMismatchOrFallbackToDefault(final Matcher matcher, final Object argument, Description description) {
                    try {
                        describeMismatch(matcher, argument, description);
                    } catch (ReflectiveOperationException e) {
                        defaultDescribe(description);
                    }
                }

                public void describeTo(Description description) {
                    if (idx < numOriginalMatchers) {
                        // describe using the corresponding matcher
                        describeMismatchOrFallbackToDefault(originalMatchers.get(idx), arguments[idx], description);
                    } else if (lastMatcherIsVararg) {
                        // more arguments than matchers but last matcher is vararg so use that
                        describeMismatchOrFallbackToDefault(lastMatcher, arguments[idx], description);
                    } else {
                        // then this must be in invocation of another, overloaded version of the method, fall back to default
                        defaultDescribe(description);
                    }
                }
            });

        }

        return withPrintSettings;
    }
}