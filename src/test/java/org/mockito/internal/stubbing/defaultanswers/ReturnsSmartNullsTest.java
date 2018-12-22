/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.InterceptedInvocation;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.mockref.MockStrongReference;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

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

    interface GenericFoo<T> {
        T get();
    }

    interface GenericFooBar extends GenericFoo<Foo> {
        <I> I method();
        <I> I methodWithArgs(int firstArg, I secondArg);
        <I> I methodWithVarArgs(int firstArg, I... secondArg);
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
    public void should_return_an_object_when_it_came_from_a_generic_defined_on_class() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();

        Foo smartNull = (Foo) answer.answer(invocationOf(GenericFooBar.class, "get"));

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.get()");
    }

    @Test
    public void should_return_an_object_when_it_came_from_a_generic_defined_on_method_without_infer() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        String smartNull = (String) answer.answer(invocationOf(GenericFooBar.class, "method"));

        assertThat(smartNull).isNull();
    }

    @Test
    public void should_return_an_object_when_it_came_from_a_generic_method() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = new InterceptedInvocation(
            new MockStrongReference<Object>(mock(GenericFooBar.class), false),
            new SerializableMethod(GenericFooBar.class.getMethod("methodWithArgs", int.class, Object.class)),
            new Object[]{1, "secondArg"},
            InterceptedInvocation.NO_OP,
            new LocationImpl(),
            1);

        Object smartNull = answer.answer(invocation);

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(String.class)
            .asString()
            .isEmpty();
    }

    @Test
    public void should_return_a_list_when_it_came_from_a_generic_method() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = new InterceptedInvocation(
            new MockStrongReference<Object>(mock(GenericFooBar.class), false),
            new SerializableMethod(GenericFooBar.class.getMethod("methodWithArgs", int.class, Object.class)),
            new Object[]{1, Collections.singletonList("secondArg")},
            InterceptedInvocation.NO_OP,
            new LocationImpl(),
            1);

        Object smartNull = answer.answer(invocation);

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(List.class);
    }

    @Test
    public void should_return_an_object_when_it_came_from_a_generic_method_using_a_mock_as_type() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = new InterceptedInvocation(
            new MockStrongReference<Object>(mock(GenericFooBar.class), false),
            new SerializableMethod(GenericFooBar.class.getMethod("methodWithArgs", int.class, Object.class)),
            new Object[]{1, mock(GenericFooBar.class)},
            InterceptedInvocation.NO_OP,
            new LocationImpl(),
            1);

        Object smartNull = answer.answer(invocation);

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.methodWithArgs(");
    }

    @Test
    public void should_return_an_object_when_it_came_from_a_generic_method_using_Object_as_type() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = new InterceptedInvocation(
            new MockStrongReference<Object>(mock(GenericFooBar.class), false),
            new SerializableMethod(GenericFooBar.class.getMethod("methodWithArgs", int.class, Object.class)),
            new Object[]{1, new Object() {}},
            InterceptedInvocation.NO_OP,
            new LocationImpl(),
            1);

        Object smartNull = answer.answer(invocation);

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.methodWithArgs(");
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
}
