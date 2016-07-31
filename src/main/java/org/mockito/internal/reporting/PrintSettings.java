/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.text.MatchersPrinter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PrintSettings {

    public static final int MAX_LINE_LENGTH = 45;
    private boolean multiline;
    private List<Integer> withTypeInfo = new LinkedList<Integer>();

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public static PrintSettings verboseMatchers(Integer ... indexesOfMatchers) {
        PrintSettings settings = new PrintSettings();
        settings.setMatchersToBeDescribedWithExtraTypeInfo(indexesOfMatchers);
        return settings;
    }

    public boolean extraTypeInfoFor(int argumentIndex) {
        return withTypeInfo.contains(argumentIndex);
    }

    public void setMatchersToBeDescribedWithExtraTypeInfo(Integer[] indexesOfMatchers) {
        this.withTypeInfo = Arrays.asList(indexesOfMatchers);
    }

    public String print(List<ArgumentMatcher> matchers, Invocation invocation) {
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String qualifiedName = MockUtil.getMockName(invocation.getMock()) + "." + invocation.getMethod().getName();
        String invocationString = qualifiedName + matchersPrinter.getArgumentsLine(matchers, this);
        if (isMultiline() || (!matchers.isEmpty() && invocationString.length() > MAX_LINE_LENGTH)) {
            return qualifiedName + matchersPrinter.getArgumentsBlock(matchers, this);
        } else {
            return invocationString;
        }
    }

    public String print(Invocation invocation) {
        return print(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments()), invocation);
    }

    public String print(InvocationMatcher invocationMatcher) {
        return print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation());
    }
}