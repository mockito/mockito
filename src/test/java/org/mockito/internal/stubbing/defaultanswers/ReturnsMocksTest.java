/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.*;

public class ReturnsMocksTest extends TestBase {
    private ReturnsMocks values = new ReturnsMocks();

    interface FooInterface {
    }

    class BarClass {
    }

    final class Baz {
    }

    @Test
    public void should_return_mock_value_for_interface() throws Exception {
        Object interfaceMock = values.returnValueFor(FooInterface.class);
        assertTrue(MockUtil.isMock(interfaceMock));
    }

    @Test
    public void should_return_mock_value_for_class() throws Exception {
        Object classMock = values.returnValueFor(BarClass.class);
        assertTrue(MockUtil.isMock(classMock));
    }

    @Test
    public void should_return_null_for_final_class() throws Exception {
        assertNull(values.returnValueFor(Baz.class));
    }

    @Test
    public void should_return_the_usual_default_values_for_primitives() throws Throwable {
        ReturnsMocks answer = new ReturnsMocks();
        assertEquals(false, answer.answer(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals((byte) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "byteMethod")));
        assertEquals((short) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "shortMethod")));
        assertEquals(0, answer.answer(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0L, answer.answer(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0f, answer.answer(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0d, answer.answer(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }

    @SuppressWarnings("unused")
    interface StringMethods {
        String stringMethod();
        String[] stringArrayMethod();
    }
    
    @Test
    public void should_return_empty_array() throws Throwable {
        String[] ret = (String[]) values.answer(invocationOf(StringMethods.class, "stringArrayMethod"));
        
        assertTrue(ret.getClass().isArray());
        assertTrue(ret.length == 0);
    }
    
    @Test
    public void should_return_empty_string() throws Throwable {
        assertEquals("", values.answer(invocationOf(StringMethods.class, "stringMethod")));
    }
}
