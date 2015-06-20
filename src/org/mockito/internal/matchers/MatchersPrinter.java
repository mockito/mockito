/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.text.ValuePrinter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MatchersPrinter {

    public String getArgumentsLine(List<Matcher> matchers, PrintSettings printSettings) {
        ValuePrinter printer = new ValuePrinter();
        printer.appendList("(", ", ", ");", applyPrintSettings(matchers, printSettings));
        return printer.toString();
    }

    public String getArgumentsBlock(List<Matcher> matchers, PrintSettings printSettings) {
        ValuePrinter printer = new ValuePrinter();
        printer.appendList("(\n    ", ",\n    ", "\n);", applyPrintSettings(matchers, printSettings));
        return printer.toString();
    }

    private Iterator applyPrintSettings(List<Matcher> matchers, PrintSettings printSettings) {
        List out = new LinkedList();
        int i = 0;
        for (final Matcher matcher : matchers) {
            if (matcher instanceof ContainsTypedDescription && printSettings.extraTypeInfoFor(i)) {
                SelfDescribing s = new SelfDescribing() {
                    public void describeTo(Description description) {
                        String d = ((ContainsTypedDescription) matcher).getTypedDescription();
                        description.appendText(d);
                    }
                };
                out.add(s);
            } else {
                out.add(matcher);
            }
            i++;
        }
        return out.iterator();
    }
}
