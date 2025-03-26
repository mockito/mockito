/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class SmartPrinter {

    private final String wanted;
    private final List<String> actuals;

    public SmartPrinter(
        MatchableInvocation wanted,
        Invocation actual,
        Integer... indexesOfMatchersToBeDescribedWithExtraTypeInfo) {
        this(
            wanted,
            Collections.singletonList(actual),
            indexesOfMatchersToBeDescribedWithExtraTypeInfo,
            Collections.emptySet());
    }

    public SmartPrinter(
        MatchableInvocation wanted,
        List<Invocation> allActualInvocations,
        Integer[] indexesOfMatchersToBeDescribedWithExtraTypeInfo,
        Set<String> classNamesToBeDescribedWithFullName) {

        SmartTypeFormatter formatter = new SmartTypeFormatter(
            indexesOfMatchersToBeDescribedWithExtraTypeInfo,
            classNamesToBeDescribedWithFullName,
            wanted,
            allActualInvocations);

        this.wanted = formatter.formatWanted(wanted);
        this.actuals = formatter.formatActuals(allActualInvocations);
    }

    public String getWanted() {
        return wanted;
    }

    public List<String> getActuals() {
        return actuals;
    }
}
