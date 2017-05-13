/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.List;


public class ReturnsSmartNullsTest extends TestBase {

    @Test
    public void should_return_the_usual_default_values_for_primitives() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();
        assertEquals(false  ,   answer.answer(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0,  answer.answer(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals((byte) 0,  answer.answer(invocationOf(HasPrimitiveMethods.class, "byteMethod")));
        assertEquals((short) 0, answer.answer(invocationOf(HasPrimitiveMethods.class, "shortMethod")));
        assertEquals(0,         answer.answer(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0L,        answer.answer(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0f,        answer.answer(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0d,        answer.answer(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }

    @SuppressWarnings("unused")
    interface Foo {
        Foo get();
        Foo withArgs(String oneArg, String otherArg);
    }

    @Test
    public void should_return_an_object_that_fails_on_any_method_invocation_for_non_primitives() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();

        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "get"));

        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException expected) {}
    }

    @Test
    public void should_return_an_object_that_allows_object_methods() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();

        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "get"));

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("foo.get()");
    }

    @Test
    public void should_print_the_parameters_when_calling_a_method_with_args() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();

        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "withArgs", "oompa", "lumpa"));

        assertThat(smartNull.toString())
            .contains("foo.withArgs")
            .contains("oompa")
            .contains("lumpa");
    }

    @Test
    public void should_print_the_parameters_on_SmartNullPointerException_message() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();

        Foo smartNull = (Foo) answer.answer(invocationOf(Foo.class, "withArgs", "oompa", "lumpa"));

        try {
            smartNull.get();
            fail();
        } catch (SmartNullPointerException e) {
            assertThat(e)
                .hasMessageContaining("oompa")
                .hasMessageContaining("lumpa");
        }
    }

    // https://github.com/mockito/mockito/issues/357
    @Test
    public void generic_return_type_erasure() throws Throwable {
        class S<T> {
            public T f() {
                return null;
            }
        }

        S<Boolean> s = Mockito.mock(S.class, org.mockito.Answers.RETURNS_SMART_NULLS);

        Mockito.when(s.f()).thenReturn(true);
        assertTrue(s.f());
    }

    // https://github.com/mockito/mockito/issues/357
    @Test
    public void inferred_generic_type() throws Throwable {
        class F {
            <T> T g(Iterable<T> xs) {
                return null;
            }
        }

        F f = Mockito.mock(F.class, org.mockito.Answers.RETURNS_SMART_NULLS);

        List<Boolean> xs = Arrays.asList(false);

        Mockito.when(f.g(xs)).thenReturn(true);
        assertTrue(f.g(xs));
    }
}
