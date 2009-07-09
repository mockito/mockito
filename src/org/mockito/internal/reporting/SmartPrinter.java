/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;


/**
 * Makes sure both wanted and actual are printed consistently (single line or multiline)
 * <p>
 * Makes arguments printed with types if necessary
 */
public class SmartPrinter {

    private final String wanted;
    private final String actual;

    public SmartPrinter(PrintingFriendlyInvocation wanted, PrintingFriendlyInvocation actual, Integer ... indexesOfMatchersToBeDescribedWithExtraTypeInfo) {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(wanted.toString().contains("\n") || actual.toString().contains("\n"));
        printSettings.setMatchersToBeDescribedWithExtraTypeInfo(indexesOfMatchersToBeDescribedWithExtraTypeInfo);
        
        this.wanted = wanted.toString(printSettings);
        this.actual = actual.toString(printSettings);
    }

    public String getWanted() {
        return wanted;
    }

    public String getActual() {
        return actual;
    }
}