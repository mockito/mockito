/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.InterceptedInvocation;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.mockref.MockStrongReference;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    interface GenericFoo<T> {
        T get();
    }

    interface GenericFooBar extends GenericFoo<Foo> {
        <I> I method();
        <I> I methodWithArgs(int firstArg, I secondArg);
        <I> I methodWithVarArgs(int firstArg, I... secondArg);
    }

    @Test
    public void should_return_an_object_that_has_been_defined_with_class_generic() throws Throwable {
        Answer<Object> answer = new ReturnsSmartNulls();

        Foo smartNull = (Foo) answer.answer(invocationOf(GenericFooBar.class, "get"));

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.get()");
    }

    @Test
    public void should_return_an_object_that_has_been_defined_with_method_generic() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        String smartNull = (String) answer.answer(invocationOf(GenericFooBar.class, "method"));

        assertThat(smartNull)
            .isNull();
    }

    private static <T> InterceptedInvocation invocationMethodWithArgs(final T obj) throws NoSuchMethodException {
        return new InterceptedInvocation(
            new MockStrongReference<Object>(mock(GenericFooBar.class), false),
            new SerializableMethod(GenericFooBar.class.getMethod("methodWithArgs", int.class, Object.class)),
            new Object[]{1, obj},
            InterceptedInvocation.NO_OP,
            new LocationImpl(),
            1);
    }

    @Test
    public void should_return_a_String_that_has_been_defined_with_method_generic_and_provided_in_argument() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithArgs("secondArg"));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(String.class)
            .asString()
            .isEmpty();
    }

    @Test
    public void should_return_a_empty_list_that_has_been_defined_with_method_generic_and_provided_in_argument() throws Throwable {

        final List<String> list = Collections.singletonList("String");
        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithArgs(list));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(List.class);
        assertThat((List) smartNull)
            .isEmpty();
    }

    @Test
    public void should_return_a_empty_map_that_has_been_defined_with_method_generic_and_provided_in_argument() throws Throwable {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("key-1", "value-1");
        map.put("key-2", "value-2");
        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithArgs(map));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(Map.class);
        assertThat((Map) smartNull)
            .isEmpty();
    }

    @Test
    public void should_return_a_empty_set_that_has_been_defined_with_method_generic_and_provided_in_argument() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull =
            answer.answer(invocationMethodWithArgs(new HashSet<String>(Arrays.asList("set-1", "set-2"))));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(Set.class);
        assertThat((Set) smartNull)
            .isEmpty();
    }

    @Test
    public void should_return_a_new_mock_that_has_been_defined_with_method_generic_and_provided_in_argument() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();
        final Foo mock = mock(Foo.class);

        Object smartNull = answer.answer(invocationMethodWithArgs(mock));

        assertThat(smartNull)
            .isNotNull()
            .isNotSameAs(mock);
        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.methodWithArgs(");
    }

    @Test
    public void should_return_an_Object_that_has_been_defined_with_method_generic_and_provided_in_argument() throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithArgs(new Object() {
        }));

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.methodWithArgs(");
    }

    @Test
    public void should_throw_a_error_on_invocation_of_returned_mock() throws Throwable {

        final Answer<Object> answer = new ReturnsSmartNulls();
        final Foo mock = mock(Foo.class);

        final Throwable throwable = Assertions.catchThrowable(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                ((Foo) answer.answer(invocationMethodWithArgs(mock))).get();
            }
        });

        Assertions.assertThat(throwable)
            .isInstanceOf(SmartNullPointerException.class)
            .hasMessageContaining("genericFooBar.methodWithArgs(")
            .hasMessageContaining("1")
            .hasMessageContaining(mock.toString());
    }

    private static <T> InterceptedInvocation invocationMethodWithVarArgs(final T[] obj) throws NoSuchMethodException {
        return new InterceptedInvocation(
            new MockStrongReference<Object>(mock(GenericFooBar.class), false),
            new SerializableMethod(GenericFooBar.class.getMethod("methodWithVarArgs", int.class, Object[].class)),
            new Object[]{1, obj},
            InterceptedInvocation.NO_OP,
            new LocationImpl(),
            1);
    }

    @Test
    public void should_return_a_String_that_has_been_defined_with_method_generic_and_provided_in_var_args()
        throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithVarArgs(new String[]{"varArg-1", "varArg-2"}));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(String.class)
            .asString()
            .isEmpty();
    }

    @Test
    public void should_return_a_empty_list_that_has_been_defined_with_method_generic_and_provided_in_var_args()
        throws Throwable {

        final List<String> arg1 = Collections.singletonList("String");
        final List<String> arg2 = Arrays.asList("str-1", "str-2");
        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithVarArgs(new List[]{arg1, arg2}));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(List.class);
        assertThat((List) smartNull)
            .isEmpty();
    }

    @Test
    public void should_return_a_empty_map_that_has_been_defined_with_method_generic_and_provided_in_var_args()
        throws Throwable {

        final Map<String, String> map1 = new HashMap<String, String>() {{
            put("key-1", "value-1");
            put("key-2", "value-2");
        }};
        final Map<String, String> map2 = new HashMap<String, String>() {{
            put("key-3", "value-1");
            put("key-4", "value-2");
        }};
        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithVarArgs(new Map[]{map1, map2}));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(Map.class);
        assertThat((Map) smartNull)
            .isEmpty();
    }

    @Test
    public void should_return_a_empty_set_that_has_been_defined_with_method_generic_and_provided_in_var_args()
        throws Throwable {

        final HashSet<String> set1 = new HashSet<String>(Arrays.asList("set-1", "set-2"));
        final HashSet<String> set2 = new HashSet<String>(Arrays.asList("set-1", "set-2"));
        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull =
            answer.answer(invocationMethodWithVarArgs(new HashSet[]{set1, set2}));

        assertThat(smartNull)
            .isNotNull()
            .isInstanceOf(Set.class);
        assertThat((Set) smartNull)
            .isEmpty();
    }

    @Test
    public void should_return_a_new_mock_that_has_been_defined_with_method_generic_and_provided_in_var_args()
        throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();
        final Foo mock1 = mock(Foo.class);
        final Foo mock2 = mock(Foo.class);

        Object smartNull = answer.answer(invocationMethodWithVarArgs(new Foo[]{mock1, mock2}));

        assertThat(smartNull)
            .isNotNull()
            .isNotSameAs(mock1)
            .isNotSameAs(mock2);
        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.methodWithVarArgs(");
    }

    @Test
    public void should_return_an_Object_that_has_been_defined_with_method_generic_and_provided_in_var_args()
        throws Throwable {

        Answer<Object> answer = new ReturnsSmartNulls();

        Object smartNull = answer.answer(invocationMethodWithVarArgs(new Object[]{new Object() {
        }, new Object() {
        }}));

        assertThat(smartNull.toString())
            .contains("SmartNull returned by")
            .contains("genericFooBar.methodWithVarArgs(");
    }

}
