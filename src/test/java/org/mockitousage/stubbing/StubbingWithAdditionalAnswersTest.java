/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.AdditionalAnswers.answersWithDelay;
import static org.mockito.AdditionalAnswers.returnsArgAt;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsLastArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer1;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.Answer5;
import org.mockito.stubbing.Answer6;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.stubbing.VoidAnswer2;
import org.mockito.stubbing.VoidAnswer3;
import org.mockito.stubbing.VoidAnswer4;
import org.mockito.stubbing.VoidAnswer5;
import org.mockito.stubbing.VoidAnswer6;
import org.mockitousage.IMethods;

@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef", "RedundantThrows"})
@RunWith(MockitoJUnitRunner.class)
public class StubbingWithAdditionalAnswersTest {

    @Mock IMethods iMethods;

    @Test
    public void can_return_arguments_of_invocation() throws Exception {
        given(iMethods.objectArgMethod(any())).will(returnsFirstArg());
        given(iMethods.threeArgumentMethod(eq(0), any(), anyString())).will(returnsSecondArg());
        given(iMethods.threeArgumentMethod(eq(1), any(), anyString())).will(returnsLastArg());
        given(iMethods.mixedVarargsReturningString(eq(1), any(String[].class)))
                .will(returnsArgAt(2));

        assertThat(iMethods.objectArgMethod("first")).isEqualTo("first");
        assertThat(iMethods.threeArgumentMethod(0, "second", "whatever")).isEqualTo("second");
        assertThat(iMethods.threeArgumentMethod(1, "whatever", "last")).isEqualTo("last");
        assertThat(iMethods.mixedVarargsReturningString(1, "a", "b")).isEqualTo("b");
    }

    @Test
    public void can_return_var_arguments_of_invocation() throws Exception {
        given(iMethods.mixedVarargsReturningStringArray(eq(1), any(String[].class)))
                .will(returnsLastArg());
        given(iMethods.mixedVarargsReturningObjectArray(eq(1), any(String[].class)))
                .will(returnsArgAt(1));

        assertThat(iMethods.mixedVarargsReturningStringArray(1, "the", "var", "args"))
                .containsExactlyInAnyOrder("the", "var", "args");
        assertThat(iMethods.mixedVarargsReturningObjectArray(1, "the", "var", "args"))
                .containsExactlyInAnyOrder("the", "var", "args");
    }

    @Test
    public void returns_arg_at_throws_on_out_of_range_var_args() throws Exception {
        given(iMethods.mixedVarargsReturningString(eq(1), any(String[].class)))
                .will(returnsArgAt(3));

        assertThatThrownBy(() -> iMethods.mixedVarargsReturningString(1, "a", "b"))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Invalid argument index");
    }

    @Test
    public void returns_arg_at_throws_on_out_of_range_array_var_args() throws Exception {
        assertThatThrownBy(
                        () ->
                                given(iMethods.mixedVarargsReturningStringArray(eq(1), any()))
                                        .will(returnsArgAt(3)))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("The argument of type 'String' cannot be returned");
    }

    @Test
    public void can_return_after_delay() throws Exception {
        final long sleepyTime = 500L;

        given(iMethods.objectArgMethod(any()))
                .will(answersWithDelay(sleepyTime, returnsFirstArg()));

        final Date before = new Date();
        assertThat(iMethods.objectArgMethod("first")).isEqualTo("first");
        final Date after = new Date();

        final long timePassed = after.getTime() - before.getTime();
        assertThat(timePassed).isCloseTo(sleepyTime, within(15L));
    }

    @Test
    public void can_return_expanded_arguments_of_invocation() throws Exception {
        given(iMethods.varargsObject(eq(1), any(Object[].class))).will(returnsArgAt(3));

        assertThat(iMethods.varargsObject(1, "bob", "alexander", "alice", "carl"))
                .isEqualTo("alice");
    }

    @Test
    public void can_return_primitives_or_wrappers() throws Exception {
        given(iMethods.toIntPrimitive(anyInt())).will(returnsFirstArg());
        given(iMethods.toIntWrapper(anyInt())).will(returnsFirstArg());
        given(iMethods.toIntWrapperVarArgs(anyInt(), any())).will(returnsFirstArg());

        assertThat(iMethods.toIntPrimitive(1)).isEqualTo(1);
        assertThat(iMethods.toIntWrapper(1)).isEqualTo(1);
        assertThat(iMethods.toIntWrapperVarArgs(1, 10)).isEqualTo(1);
    }

    @Test
    public void can_return_based_on_strongly_types_one_parameter_function() throws Exception {
        given(iMethods.simpleMethod(anyString()))
                .will(
                        answer(
                                new Answer1<String, String>() {
                                    public String answer(String s) {
                                        return s;
                                    }
                                }));

        assertThat(iMethods.simpleMethod("string")).isEqualTo("string");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_one_parameter_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString()))
                .will(
                        answerVoid(
                                new VoidAnswer1<String>() {
                                    public void answer(String s) {
                                        target.simpleMethod(s);
                                    }
                                }));

        // invoke on iMethods
        iMethods.simpleMethod("string");

        // expect the answer to write correctly to "target"
        verify(target, times(1)).simpleMethod("string");
    }

    @Test
    public void can_return_based_on_strongly_typed_two_parameter_function() throws Exception {
        given(iMethods.simpleMethod(anyString(), anyInt()))
                .will(
                        answer(
                                new Answer2<String, String, Integer>() {
                                    public String answer(String s, Integer i) {
                                        return s + "-" + i;
                                    }
                                }));

        assertThat(iMethods.simpleMethod("string", 1)).isEqualTo("string-1");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_two_parameter_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString(), anyInt()))
                .will(
                        answerVoid(
                                new VoidAnswer2<String, Integer>() {
                                    public void answer(String s, Integer i) {
                                        target.simpleMethod(s, i);
                                    }
                                }));

        // invoke on iMethods
        iMethods.simpleMethod("string", 1);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).simpleMethod("string", 1);
    }

    @Test
    public void can_return_based_on_strongly_typed_three_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.threeArgumentMethodWithStrings(anyInt(), anyString(), anyString()))
                .will(
                        answer(
                                new Answer3<String, Integer, String, String>() {
                                    public String answer(Integer i, String s1, String s2) {
                                        target.threeArgumentMethodWithStrings(i, s1, s2);
                                        return "answered";
                                    }
                                }));

        assertThat(iMethods.threeArgumentMethodWithStrings(1, "string1", "string2"))
                .isEqualTo("answered");
        verify(target, times(1)).threeArgumentMethodWithStrings(1, "string1", "string2");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_three_parameter_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.threeArgumentMethodWithStrings(anyInt(), anyString(), anyString()))
                .will(
                        answerVoid(
                                new VoidAnswer3<Integer, String, String>() {
                                    public void answer(Integer i, String s1, String s2) {
                                        target.threeArgumentMethodWithStrings(i, s1, s2);
                                    }
                                }));

        // invoke on iMethods
        iMethods.threeArgumentMethodWithStrings(1, "string1", "string2");

        // expect the answer to write correctly to "target"
        verify(target, times(1)).threeArgumentMethodWithStrings(1, "string1", "string2");
    }

    @Test
    public void can_return_based_on_strongly_typed_four_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.fourArgumentMethod(anyInt(), anyString(), anyString(), any(boolean[].class)))
                .will(
                        answer(
                                new Answer4<String, Integer, String, String, boolean[]>() {
                                    public String answer(
                                            Integer i, String s1, String s2, boolean[] a) {
                                        target.fourArgumentMethod(i, s1, s2, a);
                                        return "answered";
                                    }
                                }));

        boolean[] booleanArray = {true, false};
        assertThat(iMethods.fourArgumentMethod(1, "string1", "string2", booleanArray))
                .isEqualTo("answered");
        verify(target, times(1)).fourArgumentMethod(1, "string1", "string2", booleanArray);
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_four_parameter_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.fourArgumentMethod(anyInt(), anyString(), anyString(), any(boolean[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer4<Integer, String, String, boolean[]>() {
                                    public void answer(
                                            Integer i, String s1, String s2, boolean[] a) {
                                        target.fourArgumentMethod(i, s1, s2, a);
                                    }
                                }));

        // invoke on iMethods
        boolean[] booleanArray = {true, false};
        iMethods.fourArgumentMethod(1, "string1", "string2", booleanArray);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).fourArgumentMethod(1, "string1", "string2", booleanArray);
    }

    @Test
    public void can_return_based_on_strongly_typed_five_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.simpleMethod(anyString(), anyInt(), anyInt(), anyInt(), anyInt()))
                .will(
                        answer(
                                new Answer5<String, String, Integer, Integer, Integer, Integer>() {
                                    public String answer(
                                            String s1,
                                            Integer i1,
                                            Integer i2,
                                            Integer i3,
                                            Integer i4) {
                                        target.simpleMethod(s1, i1, i2, i3, i4);
                                        return "answered";
                                    }
                                }));

        assertThat(iMethods.simpleMethod("hello", 1, 2, 3, 4)).isEqualTo("answered");
        verify(target, times(1)).simpleMethod("hello", 1, 2, 3, 4);
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_five_parameter_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString(), anyInt(), anyInt(), anyInt(), anyInt()))
                .will(
                        answerVoid(
                                new VoidAnswer5<String, Integer, Integer, Integer, Integer>() {
                                    public void answer(
                                            String s1,
                                            Integer i1,
                                            Integer i2,
                                            Integer i3,
                                            Integer i4) {
                                        target.simpleMethod(s1, i1, i2, i3, i4);
                                    }
                                }));

        // invoke on iMethods
        iMethods.simpleMethod("hello", 1, 2, 3, 4);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).simpleMethod("hello", 1, 2, 3, 4);
    }

    @Test
    public void can_return_based_on_strongly_typed_six_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.simpleMethod(anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .will(
                        answer(
                                new Answer6<
                                        String,
                                        String,
                                        Integer,
                                        Integer,
                                        Integer,
                                        Integer,
                                        Integer>() {
                                    public String answer(
                                            String s1,
                                            Integer i1,
                                            Integer i2,
                                            Integer i3,
                                            Integer i4,
                                            Integer i5) {
                                        target.simpleMethod(s1, i1, i2, i3, i4, i5);
                                        return "answered";
                                    }
                                }));

        assertThat(iMethods.simpleMethod("hello", 1, 2, 3, 4, 5)).isEqualTo("answered");
        verify(target, times(1)).simpleMethod("hello", 1, 2, 3, 4, 5);
    }

    @Test
    public void will_execute_a_void_returning_strongly_typed_six_parameter_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .will(
                        answerVoid(
                                new VoidAnswer6<
                                        String, Integer, Integer, Integer, Integer, Integer>() {
                                    public void answer(
                                            String s1,
                                            Integer i1,
                                            Integer i2,
                                            Integer i3,
                                            Integer i4,
                                            Integer i5) {
                                        target.simpleMethod(s1, i1, i2, i3, i4, i5);
                                    }
                                }));

        // invoke on iMethods
        iMethods.simpleMethod("hello", 1, 2, 3, 4, 5);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).simpleMethod("hello", 1, 2, 3, 4, 5);
    }

    @Test
    public void can_return_based_on_strongly_types_one_parameter_var_args_function()
            throws Exception {
        given(iMethods.varargs(any(String[].class)))
                .will(
                        answer(
                                new Answer1<Integer, String[]>() {
                                    public Integer answer(String[] strings) {
                                        return strings.length;
                                    }
                                }));

        assertThat(iMethods.varargs("some", "args")).isEqualTo(2);
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_one_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.varargs(any(String[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer1<String[]>() {
                                    public void answer(String[] s) {
                                        target.varargs(s);
                                    }
                                }));

        // invoke on iMethods
        iMethods.varargs("some", "args");

        // expect the answer to write correctly to "target"
        verify(target, times(1)).varargs("some", "args");
    }

    @Test
    public void can_return_based_on_strongly_typed_two_parameter_var_args_function()
            throws Exception {
        given(iMethods.mixedVarargsReturningString(any(), any(String[].class)))
                .will(
                        answer(
                                new Answer2<String, Object, String[]>() {
                                    public String answer(Object o, String[] s) {
                                        return String.join("-", s);
                                    }
                                }));

        assertThat(iMethods.mixedVarargsReturningString(1, "var", "args")).isEqualTo("var-args");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_two_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.mixedVarargsReturningString(any(), any(String[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer2<Object, String[]>() {
                                    public void answer(Object o, String[] s) {
                                        target.mixedVarargsReturningString(o, s);
                                    }
                                }));

        // invoke on iMethods
        iMethods.mixedVarargsReturningString(1, "var", "args");

        // expect the answer to write correctly to "target"
        verify(target).mixedVarargsReturningString(1, "var", "args");
    }

    @Test
    public void can_return_based_on_strongly_typed_three_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.threeArgumentVarArgsMethod(anyInt(), any(), any(String[].class)))
                .will(
                        answer(
                                new Answer3<String, Integer, String, String[]>() {
                                    public String answer(Integer i, String s1, String[] s2) {
                                        target.threeArgumentVarArgsMethod(i, s1, s2);
                                        return String.join("-", s2);
                                    }
                                }));

        // invoke on iMethods
        assertThat(iMethods.threeArgumentVarArgsMethod(1, "string1", "var", "args"))
                .isEqualTo("var-args");

        // expect the answer to write correctly to "target"
        verify(target).threeArgumentVarArgsMethod(1, "string1", "var", "args");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_three_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.threeArgumentVarArgsMethod(anyInt(), any(), any(String[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer3<Integer, String, String[]>() {
                                    public void answer(Integer i, String s1, String[] s2) {
                                        target.threeArgumentVarArgsMethod(i, s1, s2);
                                    }
                                }));

        // invoke on iMethods
        iMethods.threeArgumentVarArgsMethod(1, "string1", "var", "args");

        // expect the answer to write correctly to "target"
        verify(target, times(1)).threeArgumentVarArgsMethod(1, "string1", "var", "args");
    }

    @Test
    public void can_return_based_on_strongly_typed_four_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.fourArgumentVarArgsMethod(anyInt(), any(), anyInt(), any(String[].class)))
                .will(
                        answer(
                                new Answer4<String, Integer, String, Integer, String[]>() {
                                    public String answer(
                                            Integer i1, String s2, Integer i3, String[] s4) {
                                        target.fourArgumentVarArgsMethod(i1, s2, i3, s4);
                                        return String.join("-", s4);
                                    }
                                }));

        // invoke on iMethods
        String[] varargs = {"var", "args"};
        assertThat(iMethods.fourArgumentVarArgsMethod(1, "string1", 3, varargs))
                .isEqualTo("var-args");

        // expect the answer to write correctly to "target"
        verify(target, times(1)).fourArgumentVarArgsMethod(1, "string1", 3, varargs);
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_four_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.fourArgumentVarArgsMethod(anyInt(), any(), anyInt(), any(String[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer4<Integer, String, Integer, String[]>() {
                                    public void answer(
                                            Integer i, String s2, Integer i3, String[] s4) {
                                        target.fourArgumentVarArgsMethod(i, s2, i3, s4);
                                    }
                                }));

        // invoke on iMethods
        iMethods.fourArgumentVarArgsMethod(1, "string1", 3, "var", "args");

        // expect the answer to write correctly to "target"
        verify(target, times(1)).fourArgumentVarArgsMethod(1, "string1", 3, "var", "args");
    }

    @Test
    public void can_return_based_on_strongly_typed_five_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);
        given(
                        iMethods.fiveArgumentVarArgsMethod(
                                anyInt(), any(), anyInt(), any(), any(String[].class)))
                .will(
                        answer(
                                new Answer5<String, Integer, String, Integer, String, String[]>() {
                                    public String answer(
                                            Integer i1,
                                            String s2,
                                            Integer i3,
                                            String s4,
                                            String[] s5) {
                                        target.fiveArgumentVarArgsMethod(i1, s2, i3, s4, s5);
                                        return String.join("-", s5);
                                    }
                                }));

        // invoke on iMethods
        assertThat(iMethods.fiveArgumentVarArgsMethod(1, "two", 3, "four", "var", "args"))
                .isEqualTo("var-args");

        // expect the answer to write correctly to "target"
        verify(target).fiveArgumentVarArgsMethod(1, "two", 3, "four", "var", "args");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_five_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);

        given(
                        iMethods.fiveArgumentVarArgsMethod(
                                anyInt(), any(), anyInt(), any(), any(String[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer5<Integer, String, Integer, String, String[]>() {
                                    public void answer(
                                            Integer i1,
                                            String s2,
                                            Integer i3,
                                            String s4,
                                            String[] s5) {
                                        target.fiveArgumentVarArgsMethod(i1, s2, i3, s4, s5);
                                    }
                                }));

        // invoke on iMethods
        iMethods.fiveArgumentVarArgsMethod(1, "two", 3, "four", "var", "args");

        // expect the answer to write correctly to "target"
        verify(target).fiveArgumentVarArgsMethod(1, "two", 3, "four", "var", "args");
    }

    @Test
    public void can_return_based_on_strongly_typed_six_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);
        given(
                        iMethods.sixArgumentVarArgsMethod(
                                anyInt(), any(), anyInt(), any(), any(), any(String[].class)))
                .will(
                        answer(
                                new Answer6<
                                        String,
                                        Integer,
                                        String,
                                        Integer,
                                        String,
                                        String,
                                        String[]>() {
                                    public String answer(
                                            Integer i1,
                                            String s2,
                                            Integer i3,
                                            String s4,
                                            String s5,
                                            String[] s6) {
                                        target.sixArgumentVarArgsMethod(i1, s2, i3, s4, s5, s6);
                                        return "answered";
                                    }
                                }));

        // invoke on iMethods
        assertThat(iMethods.sixArgumentVarArgsMethod(1, "two", 3, "four", "five", "var", "args"))
                .isEqualTo("answered");

        // expect the answer to write correctly to "target"
        verify(target, times(1))
                .sixArgumentVarArgsMethod(1, "two", 3, "four", "five", "var", "args");
    }

    @Test
    public void will_execute_a_void_returning_strongly_typed_six_parameter_var_args_function()
            throws Exception {
        final IMethods target = mock(IMethods.class);
        given(
                        iMethods.sixArgumentVarArgsMethod(
                                anyInt(), any(), anyInt(), any(), any(), any(String[].class)))
                .will(
                        answerVoid(
                                new VoidAnswer6<
                                        Integer, String, Integer, String, String, String[]>() {
                                    public void answer(
                                            Integer i1,
                                            String s2,
                                            Integer i3,
                                            String s4,
                                            String s5,
                                            String[] s6) {
                                        target.sixArgumentVarArgsMethod(i1, s2, i3, s4, s5, s6);
                                    }
                                }));

        // invoke on iMethods
        iMethods.sixArgumentVarArgsMethod(1, "two", 3, "four", "five", "var", "args");

        // expect the answer to write correctly to "target"
        verify(target, times(1))
                .sixArgumentVarArgsMethod(1, "two", 3, "four", "five", "var", "args");
    }

    @Test
    public void can_accept_array_supertype_for_strongly_typed_var_args_function() throws Exception {
        given(iMethods.varargs(any(String[].class)))
                .will(
                        answer(
                                new Answer1<Integer, Object[]>() {
                                    public Integer answer(Object[] s) {
                                        return s.length;
                                    }
                                }));

        assertThat(iMethods.varargs("var", "args")).isEqualTo(2);
    }

    @Test
    public void can_accept_non_vararg_answer_on_var_args_function() throws Exception {
        given(iMethods.varargs(any(String[].class)))
                .will(
                        answer(
                                new Answer2<Integer, String, String>() {
                                    public Integer answer(String s1, String s2) {
                                        return s1.length() + s2.length();
                                    }
                                }));

        assertThat(iMethods.varargs("var", "args")).isEqualTo(7);
    }

    @Test
    public void should_work_with_var_args_with_no_elements() throws Exception {
        given(iMethods.varargs(any(String[].class)))
                .will(
                        answer(
                                new Answer1<Integer, String[]>() {
                                    public Integer answer(String[] s) {
                                        return s.length;
                                    }
                                }));

        assertThat(iMethods.varargs()).isEqualTo(0);
    }

    @Test
    public void should_work_with_array_var_args() throws Exception {
        given(iMethods.arrayVarargsMethod(any(String[][].class)))
                .will(
                        answer(
                                new Answer1<Integer, String[][]>() {
                                    public Integer answer(String[][] s) {
                                        return Arrays.stream(s).mapToInt(e -> e.length).sum();
                                    }
                                }));

        String[][] varArgs = {{}, {""}, {"", ""}};
        assertThat(iMethods.arrayVarargsMethod(varArgs)).isEqualTo(3);
    }
}
