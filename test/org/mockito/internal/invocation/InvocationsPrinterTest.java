/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.RequiresValidState;

public class InvocationsPrinterTest extends RequiresValidState{

    @Test
    public void shouldPrintWantedAndActualInvocation() throws Exception {
        InvocationMatcher simpleMethod = new InvocationBuilder().simpleMethod().toInvocationMatcher();
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(simpleMethod, differentMethod);
        
        assertEquals("Object.simpleMethod()", printer.getWanted().toString());
        assertEquals("Object.differentMethod()", printer.getActual().toString());
    }
    
    class Super {
        void test(Object o) {};
    }
    
    class Sub extends Super {
        void test(String s) {};
    }

    @Test
    public void shouldPrintSequenceNumbersWhenMatchesButMocksDifferent() throws Exception {
        Method methodOne = Super.class.getDeclaredMethod("test", Object.class);
        Method methodTwo = Sub.class.getDeclaredMethod("test", String.class);
        
        InvocationMatcher invocationOne = new InvocationBuilder().method(methodOne).toInvocationMatcher();
        Invocation invocationTwo = new InvocationBuilder().method(methodTwo).toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(invocationOne, invocationTwo);
        
        assertEquals(invocationOne.toString(), invocationTwo.toString());
        
        assertEquals("Object.test(class java.lang.Object)", printer.getWanted().toString());
        assertEquals("Object.test(class java.lang.String)", printer.getActual().toString());
    }
    
    class Dummy {
        void test(String ... s) {};
        void test(Object ... o) {};
    }
    
    @Test
    public void shouldPrintTypesWhenMockArgsAndMethodNameMatchButMethodNotEqual() throws Exception {
        Method methodOne = Dummy.class.getDeclaredMethod("test", new Object[]{}.getClass());
        Method methodTwo = Dummy.class.getDeclaredMethod("test", new String[]{}.getClass());
        
        InvocationMatcher invocationOne = new InvocationBuilder().method(methodOne).arg(new Object[]{}).toInvocationMatcher();
        Invocation invocationTwo = new InvocationBuilder().method(methodTwo).arg(new String[]{}).toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(invocationOne, invocationTwo);
        
        assertEquals(invocationOne.toString(), invocationTwo.toString());
        
        assertEquals("Object.test(class [Ljava.lang.Object;)", printer.getWanted().toString());
        assertEquals("Object.test(class [Ljava.lang.String;)", printer.getActual().toString());
    }
}
