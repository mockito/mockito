/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.TestBase;

public class InvocationsPrinterTest extends TestBase{

    @Test
    public void shouldPrintWantedAndActualInvocation() throws Exception {
        InvocationMatcher simpleMethod = new InvocationBuilder().simpleMethod().arg("test").toInvocationMatcher();
        Invocation differentMethod = new InvocationBuilder().differentMethod().arg("foo").toInvocation();
        
        InvocationsPrinter printer = new InvocationsPrinter(simpleMethod, differentMethod);
        
        assertEquals("Object.simpleMethod(...)", printer.getWanted().toString());
        assertEquals("    1st: \"test\"", printer.getWantedArgs().toString());
        assertEquals("    1st: \"foo\"", printer.getActualArgs().toString());
    }
    
    class Super {
        void test(Object o) {};
    }
    
    class Sub extends Super {
        void test(String s) {};
    }

    @Test
    public void shouldPrintTypesWhenOnlyTypesDiffer() throws Exception {
        Method methodOne = Super.class.getDeclaredMethod("test", Object.class);
        Method methodTwo = Sub.class.getDeclaredMethod("test", String.class);
        
        InvocationMatcher invocationOne = new InvocationBuilder().method(methodOne).toInvocationMatcher();
        Invocation invocationTwo = new InvocationBuilder().method(methodTwo).toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(invocationOne, invocationTwo);
        
        assertEquals(invocationOne.toString(), invocationTwo.toString());
        
        assertEquals("Object.test(...)", printer.getWanted().toString());
        assertEquals("    1: class java.lang.Object", printer.getWantedArgs().toString());
        assertEquals("    1: class java.lang.String", printer.getActualArgs().toString());
    }
    
    class Dummy {
        void test(String ... s) {};
        void test(Object ... o) {};
    }
    
    @Test
    public void shouldPrintVarargTypesWhenOnlyTypesDiffer() throws Exception {
        Method methodOne = Dummy.class.getDeclaredMethod("test", new Object[]{}.getClass());
        Method methodTwo = Dummy.class.getDeclaredMethod("test", new String[]{}.getClass());
        
        InvocationMatcher invocationOne = new InvocationBuilder().method(methodOne).arg(new Object[]{}).toInvocationMatcher();
        Invocation invocationTwo = new InvocationBuilder().method(methodTwo).arg(new String[]{}).toInvocation();
        InvocationsPrinter printer = new InvocationsPrinter(invocationOne, invocationTwo);
        
        assertEquals(invocationOne.toString(), invocationTwo.toString());
        
        assertEquals("Object.test(...)", printer.getWanted().toString());
        assertEquals("    1: class [Ljava.lang.Object;", printer.getWantedArgs().toString());
        assertEquals("    1: class [Ljava.lang.String;", printer.getActualArgs().toString());
    }
}
