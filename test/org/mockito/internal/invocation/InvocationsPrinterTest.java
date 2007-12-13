package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

public class InvocationsPrinterTest {

    @Test
    public void shouldPrintWantedAndActualInvocation() throws Exception {
        InvocationMatcher simpleMethod = new InvocationBuilder().simpleMethod().toInvocationMatcher();
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(simpleMethod, differentMethod);
        
        assertEquals("Object.simpleMethod()", printer.printWanted());
        assertEquals("Object.differentMethod()", printer.printActual());
    }
    
    @Test
    public void shouldPrintSequenceNumberWhenInvocationMatchesButMocksAreDifferent() throws Exception {
        InvocationMatcher mockOneMethod = new InvocationBuilder().mock("mockOne").seq(1).toInvocationMatcher();
        Invocation mockTwoMethod = new InvocationBuilder().mock("mockTwo").seq(2).toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(mockOneMethod, mockTwoMethod);
        
        assertEquals("Object#1.simpleMethod()", printer.printWanted());
        assertEquals("Object#2.simpleMethod()", printer.printActual());
    }
    
    @Ignore
    @Test
    public void shouldPrintTypesWantedAndActualWhenInvocationIsTheSame() throws Exception {
        fail("todo");
        InvocationMatcher mockOneMethod = new InvocationBuilder().mock("mockOne").seq(1).toInvocationMatcher();
        Invocation mockTwoMethod = new InvocationBuilder().mock("mockTwo").seq(2).toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(mockOneMethod, mockTwoMethod);
        
        assertEquals("Object#1.simpleMethod()", printer.printWanted());
        assertEquals("Object#2.simpleMethod()", printer.printActual());
    }
}
