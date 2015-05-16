/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import org.hamcrest.SelfDescribing;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsPrinter;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.internal.util.PrettyPrinter.*;

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

    private String getQualifiedName(Invocation invocation) {
        return new MockUtil().getMockName(invocation.getMock()) + "." + invocation.getMethod().getName();
    }

    private String print(List<SelfDescribing> descriptions, String qualifiedName) {
        String invocationString = qualifiedName + toStringLine(descriptions);
        if (isMultiline() || (!descriptions.isEmpty() && invocationString.length() > MAX_LINE_LENGTH)) {
            return qualifiedName + toStringBlock(descriptions);
        } else {
            return invocationString;
        }
    }

    public String printArgumentsDescribedByMatchers(Invocation invocation, InvocationMatcher originalMatcher) {
        List<SelfDescribing> descriptions = new ArgumentsPrinter().describe(invocation.getArguments(), originalMatcher.getMatchers(), this);
        return print(descriptions, getQualifiedName(invocation));
    }

    public String printArguments(Invocation invocation) {
        List<SelfDescribing> descriptions = new ArgumentsPrinter().describe(invocation.getArguments(), Collections.EMPTY_LIST, this);
        return print(descriptions, getQualifiedName(invocation));
    }

    public String printMatchers(InvocationMatcher invocationMatcher) {
        List<SelfDescribing> descriptions = new MatchersPrinter().describe(invocationMatcher.getMatchers(), this);
        return print(descriptions, getQualifiedName(invocationMatcher.getInvocation()));
    }
}