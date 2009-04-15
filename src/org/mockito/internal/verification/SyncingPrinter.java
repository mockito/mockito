/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.invocation.CanPrintInMultilines;

public class SyncingPrinter {

    private final String wanted;
    private final String actual;

    public SyncingPrinter(CanPrintInMultilines wanted, CanPrintInMultilines actual) {
        if (wanted.printsInMultilines() || actual.printsInMultilines()) {
            this.wanted = wanted.toMultilineString();
            this.actual = actual.toMultilineString();
        } else {
            this.wanted = wanted.toString();
            this.actual = actual.toString();
        }
    }

    public String getWanted() {
        return wanted;
    }

    public String getActual() {
        return actual;
    }
}