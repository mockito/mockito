/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StubbingConsecutiveAnswersTest extends TestBase {

    @Mock
    private IMethods mock;

    @Test
    public void should_return_consecutive_values() throws Exception {
        when(mock.simpleMethod())
                .thenReturn("one")
                .thenReturn("two")
                .thenReturn("three");

        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
    }

    @Test
    public void should_return_consecutive_values_for_two_nulls() throws Exception {
        when(mock.simpleMethod()).thenReturn(null, (String[]) null);

        assertNull(mock.simpleMethod());
        assertNull(mock.simpleMethod());
    }

    @Test
    public void should_return_consecutive_values_first_var_arg_null() throws Exception {
        when(mock.simpleMethod()).thenReturn("one", (String) null);

        assertEquals("one", mock.simpleMethod());
        assertNull(mock.simpleMethod());
        assertNull(mock.simpleMethod());
    }

    @Test
    public void should_return_consecutive_values_var_arg_null() throws Exception {
        when(mock.simpleMethod()).thenReturn("one", (String[]) null);

        assertEquals("one", mock.simpleMethod());
        assertNull(mock.simpleMethod());
        assertNull(mock.simpleMethod());
    }

    @Test
    public void should_return_consecutive_values_var_args_contain_null() throws Exception {
        when(mock.simpleMethod()).thenReturn("one", "two", null);

        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertNull(mock.simpleMethod());
        assertNull(mock.simpleMethod());
    }

    @Test
    public void should_return_consecutive_values_set_by_shorten_then_return_method() throws Exception {
        when(mock.simpleMethod()).thenReturn("one", "two", "three");

        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
    }

    @Test
    public void should_return_consecutive_value_and_throw_exceptions_set_by_shorten_return_methods() {
        when(mock.simpleMethod()).thenReturn("zero")
                                 .thenReturn("one", "two")
                                 .thenThrow(new NullPointerException(), new RuntimeException())
                                 .thenReturn("three")
                                 .thenThrow(new IllegalArgumentException());

        assertEquals("zero", mock.simpleMethod());
        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }
        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException expected) { }
        assertEquals("three", mock.simpleMethod());
        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }
    }

    @Test
    public void should_throw_consecutively() throws Exception {
        when(mock.simpleMethod()).thenThrow(new RuntimeException())
                                 .thenThrow(new IllegalArgumentException())
                                 .thenThrow(new NullPointerException());

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }
    }

    @Test
    public void should_throw_consecutively_set_by_shorten_then_throw_method() throws Exception {
        when(mock.simpleMethod()).thenThrow(new RuntimeException(),
                                            new IllegalArgumentException(),
                                            new NullPointerException());

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }
    }

    @Test
    public void should_throw_classes() throws Exception {
        // Unavoidable JDK7+ 'unchecked generic array creation' warning
        when(mock.simpleMethod()).thenThrow(IllegalArgumentException.class);

        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_throw_consecutively_classes_set_by_shorten_then_throw_method() throws Exception {
        // Unavoidable JDK7+ 'unchecked generic array creation' warning
        when(mock.simpleMethod()).thenThrow(RuntimeException.class,
                                            IllegalArgumentException.class,
                                            NullPointerException.class);

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }
    }

    @Test
    public void should_mix_consecutive_returns_with_exceptions() throws Exception {
        when(mock.simpleMethod())
                .thenThrow(new IllegalArgumentException())
                .thenReturn("one")
                .thenThrow(new NullPointerException())
                .thenReturn(null);

        try {
            mock.simpleMethod();
            fail();
        } catch (IllegalArgumentException expected) { }

        assertEquals("one", mock.simpleMethod());

        try {
            mock.simpleMethod();
            fail();
        } catch (NullPointerException expected) { }

        assertEquals(null, mock.simpleMethod());
        assertEquals(null, mock.simpleMethod());
    }

    @Test(expected = MockitoException.class)
    public void should_validate_consecutive_exception() throws Exception {
        when(mock.simpleMethod())
                .thenReturn("one")
                .thenThrow(new Exception());
    }

    @Test
    public void should_stub_void_method_and_continue_throwing() throws Exception {
        doThrow(new IllegalArgumentException())
        .doNothing()
        .doThrow(new NullPointerException())
        .when(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (IllegalArgumentException expected) { }

        mock.voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (NullPointerException expected) { }

        try {
            mock.voidMethod();
            fail();
        } catch (NullPointerException expected) { }
    }

    @Test
    public void should_stub_void_method() throws Exception {
        doNothing()
        .doThrow(new NullPointerException())
        .doNothing()
        .when(mock)
        .voidMethod();

        mock.voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (NullPointerException expected) { }

        mock.voidMethod();
        mock.voidMethod();
    }

    @Test(expected = MockitoException.class)
    public void should_validate_consecutive_exception_for_void_method() throws Exception {
        doNothing()
        .doThrow(new Exception())
        .when(mock)
        .voidMethod();
    }
}
