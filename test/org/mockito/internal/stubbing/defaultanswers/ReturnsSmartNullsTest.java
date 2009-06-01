/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

public class ReturnsSmartNullsTest extends TestBase {
    
    @Test
    public void shouldReturnTheUsualDefaultValuesForPrimitives() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();
        assertEquals(false  ,   answer.answer(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0,  answer.answer(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(0,         answer.answer(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0,         answer.answer(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0,         answer.answer(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0,         answer.answer(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }
    
    interface Foo {
        Foo get();
    }
    
    @Test
    public void shouldReturnAnObjectThatFailsOnAnyMethodInvocationForNonPrimitives() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();
        
        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "get"));
        
        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException expected) {}
    }
    
    @Test
    public void shouldReturnAnObjectThatAllowsObjectMethods() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();
        
        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "get"));
        
        //TODO: after 1.8 add functionality of printing params
        assertEquals("SmartNull returned by unstubbed get() method on mock", smartNull + "");
    }
}
