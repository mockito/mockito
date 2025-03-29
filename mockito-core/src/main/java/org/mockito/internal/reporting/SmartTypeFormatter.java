/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.reporting;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class SmartTypeFormatter {

    private final PrintSettings printSettings;

    SmartTypeFormatter(Integer[] indexesOfMatchersToBeDescribedWithExtraTypeInfo,
                       Set<String> classNamesToBeDescribedWithFullName,
                       MatchableInvocation wanted,
                       List<Invocation> actuals) {

        this.printSettings = new PrintSettings();
        this.printSettings.setMultiline(isMultiLine(wanted, actuals));
        this.printSettings.setMatchersToBeDescribedWithExtraTypeInfo(indexesOfMatchersToBeDescribedWithExtraTypeInfo);
        this.printSettings.setMatchersToBeDescribedWithFullName(classNamesToBeDescribedWithFullName);
    }

    String formatWanted(MatchableInvocation wanted) {
        return printSettings.print(wanted);
    }

    List<String> formatActuals(List<Invocation> actuals) {
        List<String> result = new ArrayList<>();
        for (Invocation actual : actuals) {
            result.add(printSettings.print(actual));
        }
        return Collections.unmodifiableList(result);
    }

    private static boolean isMultiLine(MatchableInvocation wanted, List<Invocation> actuals) {
        if (wanted.toString().contains("\n")) return true;
        for (Invocation invocation : actuals) {
            if (invocation.toString().contains("\n")) return true;
        }
        return false;
    }
}
