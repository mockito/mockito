/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

public class ReturnsMocksTest extends TestBase {
    private ReturnsMocks values = new ReturnsMocks();

    interface FooInterface {
    }

    class BarClass {
    }

    final class Baz {
    }

    @Test
    // FIXME split into separate
    public void shouldReturnMockValueForInterface() throws Exception {
        Object interfaceMock = values.returnValueFor(FooInterface.class);
        assertTrue(new MockUtil().isMock(interfaceMock));
    }

    public void shouldReturnMockValueForClass() throws Exception {
        Object classMock = values.returnValueFor(BarClass.class);
        assertTrue(new MockUtil().isMock(classMock));
    }

    @Test
    public void shouldReturnNullForFinalClass() throws Exception {
        assertNull(values.returnValueFor(Baz.class));
    }

    private Invocation invocationOf(Class<?> type, String methodName)
            throws NoSuchMethodException {
        return new Invocation(new Object(), type.getMethod(methodName,
                new Class[0]), new Object[0], 1, null);
    }

    @Test
    public void shouldReturnTheUsualDefaultValuesForPrimitives()
            throws Throwable {
        ReturnsMocks answer = new ReturnsMocks();
        assertEquals(false, answer.answer(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(0, answer.answer(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0, answer.answer(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0, answer.answer(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0, answer.answer(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }
    
    interface StringMethods {
        String stringMethod();
        String[] stringArrayMethod();
    }
    
    @Test
    public void shouldReturnEmptyArray() throws Throwable {
        String[] ret = (String[]) values.answer(invocationOf(StringMethods.class, "stringArrayMethod"));
        
        assertTrue(ret.getClass().isArray());
        assertTrue(ret.length == 0);
    }
    
    @Test
    public void shouldReturnEmptyString() throws Throwable {
        assertEquals("", values.answer(invocationOf(StringMethods.class, "stringMethod")));
    }
}