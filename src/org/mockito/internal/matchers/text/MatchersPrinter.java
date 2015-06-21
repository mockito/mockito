/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import org.mockito.MockitoMatcher;
import org.mockito.internal.matchers.ContainsTypedDescription;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MatchersPrinter {

    public String getArgumentsLine(List<MockitoMatcher> matchers, PrintSettings printSettings) {
        ValuePrinter printer = new ValuePrinter();
        printer.appendList("(", ", ", ");", applyPrintSettings(matchers, printSettings));
        return printer.toString();
    }

    public String getArgumentsBlock(List<MockitoMatcher> matchers, PrintSettings printSettings) {
        ValuePrinter printer = new ValuePrinter();
        printer.appendList("(\n    ", ",\n    ", "\n);", applyPrintSettings(matchers, printSettings));
        return printer.toString();
    }

    private Iterator applyPrintSettings(List<MockitoMatcher> matchers, PrintSettings printSettings) {
        List out = new LinkedList();
        int i = 0;
        for (final MockitoMatcher matcher : matchers) {
            if (matcher instanceof ContainsTypedDescription && printSettings.extraTypeInfoFor(i)) {
                out.add(new FormattedText(((ContainsTypedDescription) matcher).getTypedDescription()));
            } else {
                out.add(new FormattedText(matcher.describe()));
            }
            i++;
        }
        return out.iterator();
    }
}
