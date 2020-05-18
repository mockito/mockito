/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

/**
 * Makes sure both wanted and actual are printed consistently (single line or multiline)
 * <p>
 * Makes arguments printed with types if necessary
 */
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
                indexesOfMatchersToBeDescribedWithExtraTypeInfo);
    }

    public SmartPrinter(
            MatchableInvocation wanted,
            List<Invocation> allActualInvocations,
            Integer... indexesOfMatchersToBeDescribedWithExtraTypeInfo) {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(isMultiLine(wanted, allActualInvocations));
        printSettings.setMatchersToBeDescribedWithExtraTypeInfo(
                indexesOfMatchersToBeDescribedWithExtraTypeInfo);

        this.wanted = printSettings.print(wanted);

        List<String> actuals = new ArrayList<String>();
        for (Invocation actual : allActualInvocations) {
            actuals.add(printSettings.print(actual));
        }
        this.actuals = Collections.unmodifiableList(actuals);
    }

    public String getWanted() {
        return wanted;
    }

    public List<String> getActuals() {
        return actuals;
    }

    private static boolean isMultiLine(
            MatchableInvocation wanted, List<Invocation> allActualInvocations) {
        boolean isWantedMultiline = wanted.toString().contains("\n");
        boolean isAnyActualMultiline = false;
        for (Invocation invocation : allActualInvocations) {
            isAnyActualMultiline |= invocation.toString().contains("\n");
        }
        return isWantedMultiline || isAnyActualMultiline;
    }
}
