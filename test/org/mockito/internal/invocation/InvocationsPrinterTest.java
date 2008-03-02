/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.TestBase;

public class InvocationsPrinterTest extends TestBase{

    @Test
    //TODO kill it
    public void shouldPrintWantedAndActualInvocation() throws Exception {
        InvocationMatcher simpleMethod = new InvocationBuilder().simpleMethod().arg("test").toInvocationMatcher();
        Invocation differentMethod = new InvocationBuilder().differentMethod().arg("foo").toInvocation();
        
        InvocationsPrinter printer = new InvocationsPrinter(simpleMethod, differentMethod);
        
        assertEquals("Object.simpleMethod(...)", printer.getWanted().toString());
        assertEquals("    1st: \"test\"", printer.getWantedArgs().toString());
        assertEquals("    1st: \"foo\"", printer.getActualArgs().toString());
    }
}
