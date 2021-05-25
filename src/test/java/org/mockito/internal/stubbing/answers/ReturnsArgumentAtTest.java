/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsArgumentAtTest {
    @Test
    public void should_be_able_to_return_the_first_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(0).answer(invocationWith("A", "B"))).isEqualTo("A");
    }

    @Test
    public void should_be_able_to_return_the_second_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(1).answer(invocationWith("A", "B", "C"))).isEqualTo("B");
    }

    @Test
    public void should_be_able_to_return_the_last_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(-1).answer(invocationWith("A"))).isEqualTo("A");
        assertThat(new ReturnsArgumentAt(-1).answer(invocationWith("A", "B"))).isEqualTo("B");
    }

    @Test
    public void should_be_able_to_return_the_specified_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(0).answer(invocationWith("A", "B", "C"))).isEqualTo("A");
        assertThat(new ReturnsArgumentAt(1).answer(invocationWith("A", "B", "C"))).isEqualTo("B");
        assertThat(new ReturnsArgumentAt(2).answer(invocationWith("A", "B", "C"))).isEqualTo("C");
    }

    @Test
    public void should_identify_bad_parameter_type_for_invocation() throws Exception {
        try {
            new ReturnsArgumentAt(1)
                    .validateFor(
                            new InvocationBuilder()
                                    .method("varargsReturningString")
                                    .argTypes(Object[].class)
                                    .args(new Object(), new Object(), new Object())
                                    .toInvocation());
            Assertions.fail("should scream");
        } catch (WrongTypeOfReturnValue ignored) {
        }
        try {
            new ReturnsArgumentAt(0)
                    .validateFor(
                            new InvocationBuilder()
                                    .method("oneArray")
                                    .argTypes(boolean[].class)
                                    .args(true, false, false)
                                    .toInvocation());
            Assertions.fail("should scream");
        } catch (WrongTypeOfReturnValue ignored) {
        }
        try {
            new ReturnsArgumentAt(0)
                    .validateFor(
                            new InvocationBuilder()
                                    .method("mixedVarargsReturningString")
                                    .argTypes(Object.class, String[].class)
                                    .args(new Object(), new String[] {"A", "B", "C"})
                                    .toInvocation());
            Assertions.fail("should scream");
        } catch (WrongTypeOfReturnValue ignored) {
        }
    }

    @Test
    public void should_not_scream_when_mixed_vararg_parameter_is_compatible_with_invocation()
            throws Exception {
        new ReturnsArgumentAt(1)
                .validateFor(
                        new InvocationBuilder()
                                .method("mixedVarargsReturningString")
                                .argTypes(Object.class, String[].class)
                                .args(new Object(), new String[] {"A", "B", "C"})
                                .toInvocation());
    }

    @Test
    public void should_handle_returning_vararg_as_array() throws Throwable {
        Invocation mixedVarargsReturningStringArray =
                new InvocationBuilder()
                        .method("mixedVarargsReturningStringArray")
                        .argTypes(Object.class, String[].class)
                        .args(new Object(), new String[] {"A", "B", "C"})
                        .toInvocation();
        new ReturnsArgumentAt(1).validateFor(mixedVarargsReturningStringArray);
        assertThat(new ReturnsArgumentAt(1).answer(mixedVarargsReturningStringArray))
                .isEqualTo(new String[] {"A", "B", "C"});

        Invocation mixedVarargsReturningObjectArray =
                new InvocationBuilder()
                        .method("mixedVarargsReturningStringArray")
                        .argTypes(Object.class, String[].class)
                        .args(new Object(), new String[] {"A", "B", "C"})
                        .toInvocation();
        new ReturnsArgumentAt(1).validateFor(mixedVarargsReturningObjectArray);
        assertThat(new ReturnsArgumentAt(1).answer(mixedVarargsReturningObjectArray))
                .isEqualTo(new String[] {"A", "B", "C"});
    }

    @Test
    public void should_raise_an_exception_if_index_is_not_in_allowed_range_at_creation_time()
            throws Throwable {
        try {
            new ReturnsArgumentAt(-30);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("argument index")
                    .containsIgnoringCase("positive number")
                    .contains("1")
                    .containsIgnoringCase("last argument");
        }
    }

    @Test
    public void should_allow_possible_argument_types() throws Exception {
        new ReturnsArgumentAt(0)
                .validateFor(
                        new InvocationBuilder()
                                .method("intArgumentReturningInt")
                                .argTypes(int.class)
                                .arg(1000)
                                .toInvocation());
        new ReturnsArgumentAt(0)
                .validateFor(
                        new InvocationBuilder()
                                .method("toString")
                                .argTypes(String.class)
                                .arg("whatever")
                                .toInvocation());
        new ReturnsArgumentAt(2)
                .validateFor(
                        new InvocationBuilder()
                                .method("varargsObject")
                                .argTypes(int.class, Object[].class)
                                .args(1000, "Object", "Object")
                                .toInvocation());
        new ReturnsArgumentAt(1)
                .validateFor(
                        new InvocationBuilder()
                                .method("threeArgumentMethod")
                                .argTypes(int.class, Object.class, String.class)
                                .args(1000, "Object", "String")
                                .toInvocation());
    }

    @Test
    public void should_fail_if_index_is_not_in_range_for_one_arg_invocation() throws Throwable {
        try {
            new ReturnsArgumentAt(30)
                    .validateFor(new InvocationBuilder().method("oneArg").arg("A").toInvocation());
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("invalid argument index")
                    .containsIgnoringCase("iMethods.oneArg")
                    .containsIgnoringCase("[0] String")
                    .containsIgnoringCase("position")
                    .contains("30");
        }
    }

    @Test
    public void should_fail_if_index_is_not_in_range_for_example_with_no_arg_invocation()
            throws Throwable {
        try {
            new ReturnsArgumentAt(ReturnsArgumentAt.LAST_ARGUMENT)
                    .validateFor(new InvocationBuilder().simpleMethod().toInvocation());
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("invalid argument index")
                    .containsIgnoringCase("iMethods.simpleMethod")
                    .containsIgnoringCase("no arguments")
                    .containsIgnoringCase("last parameter wanted");
        }
    }

    @Test
    public void should_fail_if_argument_type_of_signature_is_incompatible_with_return_type()
            throws Throwable {
        try {
            new ReturnsArgumentAt(2)
                    .validateFor(
                            new InvocationBuilder()
                                    .method("varargsReturningString")
                                    .argTypes(Object[].class)
                                    .args("anyString", new Object(), "anyString")
                                    .toInvocation());
            fail();
        } catch (WrongTypeOfReturnValue e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("argument of type")
                    .containsIgnoringCase("Object")
                    .containsIgnoringCase("varargsReturningString")
                    .containsIgnoringCase("should return")
                    .containsIgnoringCase("String")
                    .containsIgnoringCase("possible argument indexes");
        }
    }

    @Test
    public void shouldNotFailWhenArgumentIsGenericAndCompatibleWithReturnType() throws Exception {
        new ReturnsArgumentAt(0)
                .validateFor(
                        new InvocationBuilder()
                                .method("genericToString")
                                .argTypes(Object.class)
                                .args("anyString")
                                .toInvocation());
    }

    private static InvocationOnMock invocationWith(Object... parameters) {
        return new InvocationBuilder()
                .method("varargsReturningString")
                .argTypes(Object[].class)
                .args(new Object[] {parameters})
                .toInvocation(); // one vararg param (sic!)
    }
}
