/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.junit.Test;
import org.mockito.ReturnValues;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.invocation.Invocation;
import org.mockitoutil.TestBase;

public class SmartNullReturnValuesTest extends TestBase {
    
    private Invocation invocationOf(Class<?> type, String methodName) throws NoSuchMethodException {
        return new Invocation(new Object(), type.getMethod(methodName, new Class[0]), new Object[0], 1, null);
    }
    
    @Test
    public void shouldReturnTheUsualDefaultValuesForPrimitives() throws Throwable {
        SmartNullReturnValues returnValues = new SmartNullReturnValues();
        assertEquals(false  ,   returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0,  returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }
    
    interface Foo {
        Foo get();
    }
    
    @Test
    public void shouldReturnAnObjectThatFailsOnAnyMethodInvocationForNonPrimitives() throws Throwable {
        ReturnValues returnValues = new SmartNullReturnValues();
        
        Foo smartNull = (Foo) returnValues.valueFor(invocationOf(Foo.class, "get"));
        
        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException expected) {}
    }
    
    @Test
    public void shouldReturnAnObjectThatAllowsObjectMethods() throws Throwable {
        ReturnValues returnValues = new SmartNullReturnValues();
        
        Foo smartNull = (Foo) returnValues.valueFor(invocationOf(Foo.class, "get"));
        
        //TODO: after 1.7 add functionality of printing params
        assertEquals("SmartNull returned by unstubbed get() method on mock", smartNull + "");
    }
}
