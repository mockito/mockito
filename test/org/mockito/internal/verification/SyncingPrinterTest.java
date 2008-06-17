/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.internal.invocation.CanPrintInMultilines;

public class SyncingPrinterTest extends TestBase {

    private CanPrintInMultilines multi;
    private CanPrintInMultilines normal;

    @Before
    public void setup() {
        multi = new CanPrintInMultilinesStub(true, "multi", "normal");
        normal = new CanPrintInMultilinesStub(false, "multi", "normal");
    }
    
    @Test
    public void shouldPrintBothInMultilinesWhenAtLeastOneIsMulti() {
        SyncingPrinter printer = new SyncingPrinter(multi, normal);
        assertEquals("multi", printer.getWanted().toString());
        assertEquals("multi", printer.getActual().toString());
        
        printer = new SyncingPrinter(normal, multi);
        assertEquals("multi", printer.getWanted().toString());
        assertEquals("multi", printer.getActual().toString());
        
        printer = new SyncingPrinter(multi, multi);
        assertEquals("multi", printer.getWanted().toString());
        assertEquals("multi", printer.getActual().toString());
    }
    
    @Test
    public void shouldPrintBothInSingleLine() {
        SyncingPrinter printer = new SyncingPrinter(normal, normal);
        assertEquals("normal", printer.getWanted().toString());
        assertEquals("normal", printer.getActual().toString());
    }

    static class CanPrintInMultilinesStub implements CanPrintInMultilines {
        private final boolean printsInMultilines;
        private final String multilineString;
        private final String normalString;

        public CanPrintInMultilinesStub(boolean printsInMultilines, String multilineString, String normalString) {
            this.printsInMultilines = printsInMultilines;
            this.multilineString = multilineString;
            this.normalString = normalString;
        }
        
        public boolean printsInMultilines() {
            return printsInMultilines;
        }

        public String toMultilineString() {
            return multilineString;
        }
        
        public String toString() {
            return normalString;
        }
    }
}