/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stubVoid;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitoutil.TestBase;

public class UsingVarargsTest extends TestBase {

    private interface IVarArgs {
        void withStringVarargs(final int value, final String... s);

        String withStringVarargsReturningString(final int value, final String... s);

        void withObjectVarargs(final int value, final Object... o);

        boolean withBooleanVarargs(final int value, final boolean... b);

        int foo(final Object... objects);
    }

    @Mock
    IVarArgs mock;

    @SuppressWarnings("deprecation")
    @Test
    public void shouldStubStringVarargs() {
        when(mock.withStringVarargsReturningString(1)).thenReturn("1");
        when(mock.withStringVarargsReturningString(2, "1", "2", "3"))
                .thenReturn("2");

        final RuntimeException expected = new RuntimeException();
        stubVoid(mock).toThrow(expected).on()
                .withStringVarargs(3, "1", "2", "3", "4");

        assertEquals("1", mock.withStringVarargsReturningString(1));
        assertEquals(null, mock.withStringVarargsReturningString(2));

        assertEquals("2",
                mock.withStringVarargsReturningString(2, "1", "2", "3"));
        assertEquals(null, mock.withStringVarargsReturningString(2, "1", "2"));
        assertEquals(null,
                mock.withStringVarargsReturningString(2, "1", "2", "3", "4"));
        assertEquals(null,
                mock.withStringVarargsReturningString(2, "1", "2", "9999"));

        mock.withStringVarargs(3, "1", "2", "3", "9999");
        mock.withStringVarargs(9999, "1", "2", "3", "4");

        try {
            mock.withStringVarargs(3, "1", "2", "3", "4");
            fail();
        } catch (final Exception e) {
            assertEquals(expected, e);
        }
    }

    @Test
    public void shouldStubBooleanVarargs() {
        when(mock.withBooleanVarargs(1)).thenReturn(true);
        when(mock.withBooleanVarargs(1, true, false)).thenReturn(true);

        assertEquals(true, mock.withBooleanVarargs(1));
        assertEquals(false, mock.withBooleanVarargs(9999));

        assertEquals(true, mock.withBooleanVarargs(1, true, false));
        assertEquals(false, mock.withBooleanVarargs(1, true, false, true));
        assertEquals(false, mock.withBooleanVarargs(2, true, false));
        assertEquals(false, mock.withBooleanVarargs(1, true));
        assertEquals(false, mock.withBooleanVarargs(1, false, false));
    }

    @Test
    public void shouldVerifyStringVarargs() {
        mock.withStringVarargs(1);
        mock.withStringVarargs(2, "1", "2", "3");
        mock.withStringVarargs(3, "1", "2", "3", "4");

        verify(mock).withStringVarargs(1);
        verify(mock).withStringVarargs(2, "1", "2", "3");
        try {
            verify(mock).withStringVarargs(2, "1", "2", "79", "4");
            fail();
        } catch (final ArgumentsAreDifferent e) {
        }
    }

    @Test
    public void shouldVerifyObjectVarargs() {
        mock.withObjectVarargs(1);
        mock.withObjectVarargs(2, "1", new ArrayList<Object>(), new Integer(1));
        mock.withObjectVarargs(3, new Integer(1));

        verify(mock).withObjectVarargs(1);
        verify(mock).withObjectVarargs(2, "1", new ArrayList<Object>(),
                new Integer(1));
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (final NoInteractionsWanted e) {
        }
    }

    @Test
    public void shouldVerifyBooleanVarargs() {
        mock.withBooleanVarargs(1);
        mock.withBooleanVarargs(2, true, false, true);
        mock.withBooleanVarargs(3, true, true, true);

        verify(mock).withBooleanVarargs(1);
        verify(mock).withBooleanVarargs(2, true, false, true);
        try {
            verify(mock).withBooleanVarargs(3, true, true, true, true);
            fail();
        } catch (final ArgumentsAreDifferent e) {
        }
    }

    @Test
    public void shouldVerifyWithAnyObject() {
        final Foo foo = Mockito.mock(Foo.class);
        foo.varArgs("");
        Mockito.verify(foo).varArgs((String[]) Mockito.anyObject());
        Mockito.verify(foo).varArgs((String) Mockito.anyObject());
    }

    @Test
    public void shouldVerifyWithNullVarArgArray() {
        final Foo foo = Mockito.mock(Foo.class);
        foo.varArgs((String[]) null);
        Mockito.verify(foo).varArgs((String[]) Mockito.anyObject());
        Mockito.verify(foo).varArgs((String[]) null);
    }

    public class Foo {
        public void varArgs(final String... args) {
        }
    }

    interface MixedVarargs {
        String doSomething(final String one, final String... varargs);

        String doSomething(final String one, final String two, final String... varargs);
    }

    @SuppressWarnings("all")
    @Test
    // See bug #31
    public void shouldStubCorrectlyWhenMixedVarargsUsed() {
        final MixedVarargs mixedVarargs = mock(MixedVarargs.class);
        when(mixedVarargs.doSomething("hello", (String[]) null)).thenReturn("hello");
        when(mixedVarargs.doSomething("goodbye", (String[]) null)).thenReturn("goodbye");

        final String result = mixedVarargs.doSomething("hello", (String[]) null);
        assertEquals("hello", result);

        verify(mixedVarargs).doSomething("hello", (String[]) null);
    }

    @SuppressWarnings("all")
    @Test
    public void shouldStubCorrectlyWhenDoubleStringAndMixedVarargsUsed() {
        final MixedVarargs mixedVarargs = mock(MixedVarargs.class);
        when(mixedVarargs.doSomething("one", "two", (String[]) null)).thenReturn("hello");
        when(mixedVarargs.doSomething("1", "2", (String[]) null)).thenReturn("goodbye");

        final String result = mixedVarargs.doSomething("one", "two", (String[]) null);
        assertEquals("hello", result);
    }

    @Test
    // See bug #157
    public void shouldMatchEasilyEmptyVararg() throws Exception {
        // when
        when(mock.foo(anyVararg())).thenReturn(-1);

        // then
        assertEquals(-1, mock.foo());
    }
}