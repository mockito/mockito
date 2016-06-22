/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class AnswersValidatorTest {

    private AnswersValidator validator = new AnswersValidator();
    private Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();

    @Test
    public void should_validate_null_throwable() throws Throwable {
        try {
            validator.validate(new ThrowsException(null), new InvocationBuilder().toInvocation());
            fail();
        } catch (MockitoException expected) {}
    }

    @Test
    public void should_pass_proper_checked_exception() throws Throwable {
        validator.validate(new ThrowsException(new CharacterCodingException()), invocation);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_invalid_checked_exception() throws Throwable {
        validator.validate(new ThrowsException(new IOException()), invocation);
    }

    @Test
    public void should_pass_RuntimeExceptions() throws Throwable {
        validator.validate(new ThrowsException(new Error()), invocation);
        validator.validate(new ThrowsException(new RuntimeException()), invocation);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_when_return_Value_is_set_for_void_method() throws Throwable {
        validator.validate(new Returns("one"), new InvocationBuilder().method("voidMethod").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_when_non_void_method_does_nothing() throws Throwable {
        validator.validate(new DoesNothing(), new InvocationBuilder().simpleMethod().toInvocation());
    }

    @Test
    public void should_allow_void_return_for_void_method() throws Throwable {
        validator.validate(new DoesNothing(), new InvocationBuilder().method("voidMethod").toInvocation());
    }

    @Test
    public void should_allow_correct_type_of_return_value() throws Throwable {
        validator.validate(new Returns("one"), new InvocationBuilder().simpleMethod().toInvocation());
        validator.validate(new Returns(false), new InvocationBuilder().method("booleanReturningMethod").toInvocation());
        validator.validate(new Returns(Boolean.TRUE), new InvocationBuilder().method("booleanObjectReturningMethod").toInvocation());
        validator.validate(new Returns(1), new InvocationBuilder().method("integerReturningMethod").toInvocation());
        validator.validate(new Returns(1L), new InvocationBuilder().method("longReturningMethod").toInvocation());
        validator.validate(new Returns(1L), new InvocationBuilder().method("longObjectReturningMethod").toInvocation());
        validator.validate(new Returns(null), new InvocationBuilder().method("objectReturningMethodNoArgs").toInvocation());
        validator.validate(new Returns(1), new InvocationBuilder().method("objectReturningMethodNoArgs").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_return_type_mismatch() throws Throwable {
        validator.validate(new Returns("String"), new InvocationBuilder().method("booleanReturningMethod").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_wrong_primitive() throws Throwable {
        validator.validate(new Returns(1), new InvocationBuilder().method("doubleReturningMethod").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_null_with_primitive() throws Throwable {
        validator.validate(new Returns(null), new InvocationBuilder().method("booleanReturningMethod").toInvocation());
    }

    @Test
    public void should_fail_when_calling_real_method_on_interface() throws Throwable {
        //given
        Invocation invocationOnInterface = new InvocationBuilder().method("simpleMethod").toInvocation();
        try {
            //when
            validator.validate(new CallsRealMethods(), invocationOnInterface);
            //then
            fail();
        } catch (MockitoException expected) {}
    }

    @Test
    public void should_be_OK_when_calling_real_method_on_concrete_class() throws Throwable {
        //given
        ArrayList<?> mock = mock(ArrayList.class);
        mock.clear();
        Invocation invocationOnClass = new MockitoCore().getLastInvocation();
        //when
        validator.validate(new CallsRealMethods(), invocationOnClass);
        //then no exception is thrown
    }

    @Test
    public void should_allow_possible_argument_types() throws Exception {
        validator.validate(
                new ReturnsArgumentAt(0),
                new InvocationBuilder().method("intArgumentReturningInt").argTypes(int.class).arg(1000).toInvocation()
        );
        validator.validate(
                new ReturnsArgumentAt(0),
                new InvocationBuilder().method("toString").argTypes(String.class).arg("whatever").toInvocation()
        );
        validator.validate(
                new ReturnsArgumentAt(2),
                new InvocationBuilder().method("varargsObject")
                                       .argTypes(int.class, Object[].class)
                                       .args(1000, "Object", "Object")
                                       .toInvocation()
        );
        validator.validate(
                new ReturnsArgumentAt(1),
                new InvocationBuilder().method("threeArgumentMethod")
                                       .argTypes(int.class, Object.class, String.class)
                                       .args(1000, "Object", "String")
                                       .toInvocation()
        );
    }

    @Test
    public void should_fail_if_index_is_not_in_range_for_one_arg_invocation() throws Throwable {
        try {
            validator.validate(new ReturnsArgumentAt(30), new InvocationBuilder().method("oneArg").arg("A").toInvocation());
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
    public void should_fail_if_index_is_not_in_range_for_example_with_no_arg_invocation() throws Throwable {
        try {
            validator.validate(
                    new ReturnsArgumentAt(ReturnsArgumentAt.LAST_ARGUMENT),
                    new InvocationBuilder().simpleMethod().toInvocation()
            );
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
    public void should_fail_if_argument_type_of_signature_is_incompatible_with_return_type() throws Throwable {
        try {
            validator.validate(
                    new ReturnsArgumentAt(2),
                    new InvocationBuilder().method("varargsReturningString")
                                           .argTypes(Object[].class)
                                           .args("anyString", new Object(), "anyString")
                                           .toInvocation()
            );
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
    public void should_fail_if_returned_value_of_answer_is_incompatible_with_return_type() throws Throwable {
        try {
            validator.validateDefaultAnswerReturnedValue(
                    new InvocationBuilder().method("toString").toInvocation(),
                    AWrongType.WRONG_TYPE
            );
            fail();
        } catch (WrongTypeOfReturnValue e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("Default answer returned a result with the wrong type")
                    .containsIgnoringCase("AWrongType cannot be returned by toString()")
                    .containsIgnoringCase("toString() should return String");
        }
    }

    @Test
    public void should_not_fail_if_returned_value_of_answer_is_null() throws Throwable {
        validator.validateDefaultAnswerReturnedValue(
                new InvocationBuilder().method("toString").toInvocation(),
                null
        );
    }

    private static class AWrongType {
        public static final AWrongType WRONG_TYPE = new AWrongType();
    }
}
