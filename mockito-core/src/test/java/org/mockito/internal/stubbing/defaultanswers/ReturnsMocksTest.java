/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.stubbing.defaultanswers.ReturnsGenericDeepStubsTest.WithGenerics;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

public class ReturnsMocksTest extends TestBase {
    private ReturnsMocks values = new ReturnsMocks();

    interface AllInterface {
        FooInterface getInterface();

        BarClass getNormalClass();

        Baz getFinalClass();

        WithGenerics<String> withGenerics();
    }

    interface FooInterface {}

    class BarClass {}

    final class Baz {}

    @Test
    public void should_return_mock_value_for_interface() throws Throwable {
        Object interfaceMock = values.answer(invocationOf(AllInterface.class, "getInterface"));
        assertTrue(MockUtil.isMock(interfaceMock));
    }

    @Test
    public void should_return_mock_value_for_class() throws Throwable {
        Object classMock = values.answer(invocationOf(AllInterface.class, "getNormalClass"));
        assertTrue(MockUtil.isMock(classMock));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_return_mock_value_for_generic_class() throws Throwable {
        WithGenerics<String> classMock =
                (WithGenerics<String>)
                        values.answer(invocationOf(AllInterface.class, "withGenerics"));
        assertTrue(MockUtil.isMock(classMock));
        when(classMock.execute()).thenReturn("return");
        assertEquals("return", classMock.execute());
    }

    @Test
    public void should_return_null_for_final_class_if_unsupported() throws Throwable {
        assumeFalse(Plugins.getMockMaker().isTypeMockable(Baz.class).mockable());
        assertNull(values.answer(invocationOf(AllInterface.class, "getFinalClass")));
    }

    @Test
    public void should_return_the_usual_default_values_for_primitives() throws Throwable {
        ReturnsMocks answer = new ReturnsMocks();
        assertEquals(
                false, answer.answer(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals(
                (char) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(
                (byte) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "byteMethod")));
        assertEquals(
                (short) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "shortMethod")));
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
        String[] ret =
                (String[]) values.answer(invocationOf(StringMethods.class, "stringArrayMethod"));

        assertTrue(ret.getClass().isArray());
        assertTrue(ret.length == 0);
    }

    @Test
    public void should_return_empty_string() throws Throwable {
        assertEquals("", values.answer(invocationOf(StringMethods.class, "stringMethod")));
    }
}
