/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

@SuppressWarnings("rawtypes")
public class PrintSettings {

    public static final int MAX_LINE_LENGTH = 45;
    private boolean multiline;
    private List<Integer> withTypeInfo = new LinkedList<Integer>();

    public void setMultiline(final boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public static PrintSettings verboseMatchers(final Integer ... indexesOfMatchers) {
        final PrintSettings settings = new PrintSettings();
        settings.setMatchersToBeDescribedWithExtraTypeInfo(indexesOfMatchers);
        return settings;
    }

    public boolean extraTypeInfoFor(final int argumentIndex) {
        return withTypeInfo.contains(argumentIndex);
    }

    public void setMatchersToBeDescribedWithExtraTypeInfo(final Integer[] indexesOfMatchers) {
        this.withTypeInfo = Arrays.asList(indexesOfMatchers);
    }

    public String print(final List<Matcher> matchers, final Invocation invocation) {
        final MatchersPrinter matchersPrinter = new MatchersPrinter();
        final String qualifiedName = new MockUtil().getMockName(invocation.getMock()) + "." + invocation.getMethod().getName();
        final String invocationString = qualifiedName + matchersPrinter.getArgumentsLine(matchers, this);
        if (isMultiline() || (!matchers.isEmpty() && invocationString.length() > MAX_LINE_LENGTH)) {
            return qualifiedName + matchersPrinter.getArgumentsBlock(matchers, this);
        } else {
            return invocationString;
        }
    }

    public String print(final Invocation invocation) {
        return print(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments()), invocation);
    }

    public String print(final InvocationMatcher invocationMatcher) {
        return print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation());
    }
}