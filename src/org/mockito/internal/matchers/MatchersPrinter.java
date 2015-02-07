/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.mockito.internal.reporting.PrintSettings;

@SuppressWarnings("unchecked")
public class MatchersPrinter {
    
    public List<SelfDescribing> describe(List<Matcher> matchers, PrintSettings printSettings) {
        List<SelfDescribing> withPrintSettings = new LinkedList<SelfDescribing>();
        int i = 0;
        for (final Matcher matcher : matchers) {
            if (matcher instanceof ContainsExtraTypeInformation && printSettings.extraTypeInfoFor(i)) {
                withPrintSettings.add(((ContainsExtraTypeInformation) matcher).withExtraTypeInfo());
            } else {
                withPrintSettings.add(matcher);
            }
            i++;
        }
        return withPrintSettings;
    }

}