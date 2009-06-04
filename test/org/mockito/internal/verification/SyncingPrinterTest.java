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

public class SyncingPrinterTest extends TestBase {

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
        SyncingPrinter printer = new SyncingPrinter(multi, shortie);
        
        //then
        assertContains("\n", printer.getWanted().toString());
        assertContains("\n", printer.getActual().toString());
    }

    @Test
    public void shouldPrintBothInMultilinesWhenSecondIsMulti() {
        //when
        SyncingPrinter printer = new SyncingPrinter(shortie, multi);
        
        //then
        assertContains("\n", printer.getWanted().toString());
        assertContains("\n", printer.getActual().toString());
    }

    @Test
    public void shouldPrintBothInMultilinesWhenBothAreMulti() {
        //when
        SyncingPrinter printer = new SyncingPrinter(multi, multi);
        
        //then
        assertContains("\n", printer.getWanted().toString());
        assertContains("\n", printer.getActual().toString());
    }

    @Test
    public void shouldPrintBothInSingleLineWhenBothAreShort() {
        //when
        SyncingPrinter printer = new SyncingPrinter(shortie, shortie);
        
        //then
        assertNotContains("\n", printer.getWanted().toString());
        assertNotContains("\n", printer.getActual().toString());
    }
    
    @Test
    public void shouldPrintVerboseArgumentsWhenStringOutputIsTheSame() {
        //given
        mock.longArg(1);
        Invocation withLongArg = getLastInvocation();
        
        mock.longArg(1);
        Invocation withIntArg = getLastInvocation();
        
        //when
        SyncingPrinter printer = new SyncingPrinter(withLongArg, withIntArg);
        
        //then
        assertContains("longArg((Long) 1)", printer.getWanted().toString());
        assertContains("longArg((Long) 1)", printer.getActual().toString());
    }
}