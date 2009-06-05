/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.PrintingFriendlyInocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class SmartPrinterTest extends TestBase {

    private PrintingFriendlyInocation multi;
    private PrintingFriendlyInocation shortie;
    @Mock private IMethods mock;

    @Before
    public void setup() throws Exception {
        mock.varargs("first very long argument", "second very long argument", "another very long argument");
        multi = getLastInvocation();
        multi.toString();
        
        mock.varargs("short arg");
        shortie = getLastInvocation();
    }

    @Test
    public void shouldPrintBothInMultilinesWhenFirstIsMulti() {
        //when
        SmartPrinter printer = new SmartPrinter(multi, shortie);
        
        //then
        assertContains("\n", printer.getWanted().toString());
        assertContains("\n", printer.getActual().toString());
    }

    @Test
    public void shouldPrintBothInMultilinesWhenSecondIsMulti() {
        //when
        SmartPrinter printer = new SmartPrinter(shortie, multi);
        
        //then
        assertContains("\n", printer.getWanted().toString());
        assertContains("\n", printer.getActual().toString());
    }

    @Test
    public void shouldPrintBothInMultilinesWhenBothAreMulti() {
        //when
        SmartPrinter printer = new SmartPrinter(multi, multi);
        
        //then
        assertContains("\n", printer.getWanted().toString());
        assertContains("\n", printer.getActual().toString());
    }

    @Test
    public void shouldPrintBothInSingleLineWhenBothAreShort() {
        //when
        SmartPrinter printer = new SmartPrinter(shortie, shortie);
        
        //then
        assertNotContains("\n", printer.getWanted().toString());
        assertNotContains("\n", printer.getActual().toString());
    }
}