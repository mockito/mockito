/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.junit.Test;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

public class ReturnsSmartNullsTest extends TestBase {
    
    private Invocation invocationOf(Class<?> type, String methodName) throws NoSuchMethodException {
        return new Invocation(new Object(), type.getMethod(methodName, new Class[0]), new Object[0], 1, null);
    }
    
    @Test
    public void shouldReturnTheUsualDefaultValuesForPrimitives() throws Throwable {
        Answer<Object> returnValues = new ReturnsSmartNulls();
        assertEquals(false  ,   returnValues.answer(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0,  returnValues.answer(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(0,         returnValues.answer(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0,         returnValues.answer(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0,         returnValues.answer(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0,         returnValues.answer(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }
    
    interface Foo {
        Foo get();
    }
    
    @Test
    public void shouldReturnAnObjectThatFailsOnAnyMethodInvocationForNonPrimitives() throws Throwable {
        Answer<Object> returnValues = new ReturnsSmartNulls();
        
        Foo smartNull = (Foo) returnValues.answer(invocationOf(Foo.class, "get"));
        
        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException expected) {}
    }
    
    @Test
    public void shouldReturnAnObjectThatAllowsObjectMethods() throws Throwable {
        Answer<Object> returnValues = new ReturnsSmartNulls();
        
        Foo smartNull = (Foo) returnValues.answer(invocationOf(Foo.class, "get"));
        
        //TODO: after 1.8 add functionality of printing params
        assertEquals("SmartNull returned by unstubbed get() method on mock", smartNull + "");
    }
}
