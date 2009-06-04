/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.invocation.PrintSettings;
import org.mockito.internal.invocation.PrintingFriendlyInocation;

public class SyncingPrinter {

    private final String wanted;
    private final String actual;

    public SyncingPrinter(PrintingFriendlyInocation wanted, PrintingFriendlyInocation actual) {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(wanted.toString().contains("\n") || actual.toString().contains("\n"));
        //TODO: after 1.8 think about something smarter here. 
        //Let's have an information on what specific arguments have happened to have the same toString() but aren't equal() 
        printSettings.setVerboseArguments(wanted.toString().equals(actual.toString()));
        
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