/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.AdditionalAnswers.returnsArgAt;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsLastArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.AdditionalAnswers.answersWithDelay;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer1;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.Answer5;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.stubbing.VoidAnswer2;
import org.mockito.stubbing.VoidAnswer3;
import org.mockito.stubbing.VoidAnswer4;
import org.mockito.stubbing.VoidAnswer5;
import org.mockitousage.IMethods;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class StubbingWithAdditionalAnswersTest {

    @Mock IMethods iMethods;

    @Test
    public void can_return_arguments_of_invocation() throws Exception {
        given(iMethods.objectArgMethod(any())).will(returnsFirstArg());
        given(iMethods.threeArgumentMethod(eq(0), any(), anyString())).will(returnsSecondArg());
        given(iMethods.threeArgumentMethod(eq(1), any(), anyString())).will(returnsLastArg());

        assertThat(iMethods.objectArgMethod("first")).isEqualTo("first");
        assertThat(iMethods.threeArgumentMethod(0, "second", "whatever")).isEqualTo("second");
        assertThat(iMethods.threeArgumentMethod(1, "whatever", "last")).isEqualTo("last");
    }

    @Test
    public void can_return_after_delay() throws Exception {
        final long sleepyTime = 500L;

        given(iMethods.objectArgMethod(any())).will(answersWithDelay(sleepyTime, returnsFirstArg()));

        final Date before = new Date();
        assertThat(iMethods.objectArgMethod("first")).isEqualTo("first");
        final Date after = new Date();

        final long timePassed = after.getTime() - before.getTime();
        assertThat(timePassed).isCloseTo(sleepyTime, within(15L));
    }

    @Test
    public void can_return_expanded_arguments_of_invocation() throws Exception {
        given(iMethods.varargsObject(eq(1), any())).will(returnsArgAt(3));

        assertThat(iMethods.varargsObject(1, "bob", "alexander", "alice", "carl")).isEqualTo("alice");
    }

    @Test
    public void can_return_primitives_or_wrappers() throws Exception {
        given(iMethods.toIntPrimitive(anyInt())).will(returnsFirstArg());
        given(iMethods.toIntWrapper(anyInt())).will(returnsFirstArg());

        assertThat(iMethods.toIntPrimitive(1)).isEqualTo(1);
        assertThat(iMethods.toIntWrapper(1)).isEqualTo(1);
    }

    @Test
    public void can_return_based_on_strongly_types_one_parameter_function() throws Exception {
        given(iMethods.simpleMethod(anyString()))
                .will(answer(new Answer1<String, String>() {
                    public String answer(String s) {
                        return s;
                    }
                }));

        assertThat(iMethods.simpleMethod("string")).isEqualTo("string");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_one_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString()))
                .will(answerVoid(new VoidAnswer1<String>() {
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
            .will(answer(new Answer2<String, String, Integer>() {
                public String answer(String s, Integer i) {
                    return s + "-" + i;
                }
            }));

        assertThat(iMethods.simpleMethod("string",1)).isEqualTo("string-1");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_two_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString(), anyInt()))
            .will(answerVoid(new VoidAnswer2<String, Integer>() {
                public void answer(String s, Integer i) {
                    target.simpleMethod(s, i);
                }
            }));

        // invoke on iMethods
        iMethods.simpleMethod("string",1);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).simpleMethod("string", 1);
    }

    @Test
    public void can_return_based_on_strongly_typed_three_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.threeArgumentMethodWithStrings(anyInt(), anyString(), anyString()))
                .will(answer(new Answer3<String, Integer, String, String>() {
                    public String answer(Integer i, String s1, String s2) {
                        target.threeArgumentMethodWithStrings(i, s1, s2);
                        return "answered";
                    }
                }));

        assertThat(iMethods.threeArgumentMethodWithStrings(1, "string1", "string2")).isEqualTo("answered");
        verify(target, times(1)).threeArgumentMethodWithStrings(1, "string1", "string2");
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_three_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.threeArgumentMethodWithStrings(anyInt(), anyString(), anyString()))
                .will(answerVoid(new VoidAnswer3<Integer, String, String>() {
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
                .will(answer(new Answer4<String, Integer, String, String, boolean[]>() {
                    public String answer(Integer i, String s1, String s2, boolean[] a) {
                        target.fourArgumentMethod(i, s1, s2, a);
                        return "answered";
                    }
                }));

        boolean[] booleanArray = { true, false };
        assertThat(iMethods.fourArgumentMethod(1, "string1", "string2", booleanArray)).isEqualTo("answered");
        verify(target, times(1)).fourArgumentMethod(1, "string1", "string2", booleanArray);
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_four_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.fourArgumentMethod(anyInt(), anyString(), anyString(), any(boolean[].class)))
                .will(answerVoid(new VoidAnswer4<Integer, String, String, boolean[]>() {
                    public void answer(Integer i, String s1, String s2, boolean[] a) {
                        target.fourArgumentMethod(i, s1, s2, a);
                    }
                }));

        // invoke on iMethods
        boolean[] booleanArray = { true, false };
        iMethods.fourArgumentMethod(1, "string1", "string2", booleanArray);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).fourArgumentMethod(1, "string1", "string2", booleanArray);
    }

    @Test
    public void can_return_based_on_strongly_typed_five_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);
        given(iMethods.simpleMethod(anyString(), anyInt(), anyInt(), anyInt(), anyInt()))
                .will(answer(new Answer5<String, String, Integer, Integer, Integer, Integer>() {
                    public String answer(String s1, Integer i1, Integer i2, Integer i3, Integer i4) {
                        target.simpleMethod(s1, i1, i2, i3, i4);
                        return "answered";
                    }
                }));

        assertThat(iMethods.simpleMethod("hello", 1, 2, 3, 4)).isEqualTo("answered");
        verify(target, times(1)).simpleMethod("hello", 1, 2, 3, 4);
    }

    @Test
    public void will_execute_a_void_based_on_strongly_typed_five_parameter_function() throws Exception {
        final IMethods target = mock(IMethods.class);

        given(iMethods.simpleMethod(anyString(), anyInt(), anyInt(), anyInt(), anyInt()))
                .will(answerVoid(new VoidAnswer5<String, Integer, Integer, Integer, Integer>() {
                    public void  answer(String s1, Integer i1, Integer i2, Integer i3, Integer i4) {
                        target.simpleMethod(s1, i1, i2, i3, i4);
                    }
                }));

        // invoke on iMethods
        iMethods.simpleMethod("hello", 1, 2, 3, 4);

        // expect the answer to write correctly to "target"
        verify(target, times(1)).simpleMethod("hello", 1, 2, 3, 4);
    }

}
