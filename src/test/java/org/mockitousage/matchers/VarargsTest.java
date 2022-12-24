/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ObjectAssert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitousage.IMethods.BaseType;

public class VarargsTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Captor private ArgumentCaptor<String> captor;
    @Captor private ArgumentCaptor<String[]> arrayCaptor;
    @Mock private IMethods mock;

    private static final Condition<Object> NULL =
            new Condition<Object>() {

                @Override
                public boolean matches(Object value) {
                    return value == null;
                }
            };

    @Test
    public void shouldMatchVarArgs_noArgs() {
        mock.varargs();

        verify(mock).varargs();
    }

    @Test
    public void shouldMatchEmptyVarArgs_noArgsIsNotNull() {
        mock.varargs();

        verify(mock).varargs(isNotNull(String[].class));
    }

    @Test
    public void shouldMatchEmptyVarArgs_noArgsIsNull() {
        mock.varargs((String[]) null);

        verify(mock).varargs(isNull(String[].class));
    }

    @Test
    public void shouldMatchVarArgs_oneNullArg_eqNull() {
        Object arg = null;
        mock.varargs(arg);

        verify(mock).varargs(ArgumentMatchers.<Object[]>eq(null));
    }

    @Test
    public void shouldMatchVarArgs_oneNullArg_isNull() {
        mock.varargs((Object) null);

        verify(mock).varargs(ArgumentMatchers.<Object>isNull());
        verify(mock, never()).varargs(isNull(Object[].class));
    }

    @Test
    public void shouldMatchVarArgs_nullArrayArg() {
        mock.varargs((Object[]) null);

        verify(mock).varargs(isNull(Object[].class));
        verify(mock).varargs(ArgumentMatchers.<Object>isNull());
    }

    @Test
    public void shouldnotMatchVarArgs_twoArgsOneMatcher() {
        mock.varargs("1", "1");

        assertThatThrownBy(
                        () -> {
                            verify(mock).varargs(eq("1"));
                        })
                .hasMessageContaining("Argument(s) are different");
    }

    @Test
    public void shouldMatchVarArgs_emptyVarArgsOneAnyMatcher() {
        mock.varargs();

        verify(mock).varargs(any(String[].class));
    }

    @Test
    public void shouldMatchVarArgs_oneArgsOneAnyMatcher() {
        mock.varargs(1);

        verify(mock).varargs(any(Object[].class));
    }

    @Test
    public void shouldMatchVarArgs_twoArgsOneAnyMatcher() {
        mock.varargs(1, 2);

        verify(mock).varargs(any(Object[].class));
    }

    @Test
    public void shouldMatchVarArgs_twoArgsTwoAnyMatcher() {
        mock.varargs(1, 2);

        verify(mock).varargs(any(), ArgumentMatchers.<Object>any());
    }

    @Test
    public void shouldMatchVarArgs_twoArgsThreeAnyMatcher() {
        mock.varargs(1, 2);

        assertThatThrownBy(
                        () -> {
                            verify(mock).varargs(any(), any(), any());
                        })
                .hasMessageContaining("Argument(s) are different");
    }

    @Test
    public void shouldMatchVarArgs_oneNullArgument() {
        mock.varargs("1", null);

        verify(mock).varargs(eq("1"), (String) isNull());
    }

    @Test
    public void shouldMatchVarArgs_onebyte() {
        mock.varargsbyte((byte) 1);

        verify(mock).varargsbyte(eq((byte) 1));
    }

    @Test
    public void shouldMatchVarArgs_nullByteArray() {
        mock.varargsbyte(null);

        verify(mock).varargsbyte((byte[]) isNull());
    }

    @Test
    public void shouldMatchVarArgs_emptyByteArray() {
        mock.varargsbyte();

        verify(mock).varargsbyte();
    }

    @Test
    public void shouldMatchEmptyVarArgs_emptyArrayIsNotNull() {
        mock.varargsbyte();

        verify(mock).varargsbyte(isNotNull(byte[].class));
    }

    @Test
    public void shouldMatchVarArgs_oneArgIsNotNull() {
        mock.varargsbyte((byte) 1);

        verify(mock).varargsbyte((byte[]) isNotNull());
    }

    @Test
    public void shouldCaptureVarArgs_noArgs() {
        mock.varargs();

        verify(mock).varargs(arrayCaptor.capture());

        assertThatCaptor(arrayCaptor).contains(new String[] {});
    }

    @Test
    public void shouldCaptureVarArgs_oneNullArg_eqNull() {
        String arg = null;
        mock.varargs(arg);

        verify(mock).varargs(captor.capture());

        assertThatCaptor(captor).areExactly(1, NULL);
    }

    /**
     * Relates to Github issue #583 "ArgumentCaptor: NPE when an null array is
     * passed to a varargs method"
     */
    @Test
    public void shouldCaptureVarArgs_nullArrayArg() {
        String[] argArray = null;
        mock.varargs(argArray);

        verify(mock).varargs(captor.capture());
        assertThatCaptor(captor).areExactly(1, NULL);
    }

    @Test
    public void shouldCaptureVarArgs_twoArgsOneCapture() {
        mock.varargs("1", "2");

        verify(mock).varargs(arrayCaptor.capture());

        assertThatCaptor(arrayCaptor).contains(new String[] {"1", "2"});
    }

    @Test
    public void shouldCaptureVarArgs_twoArgsTwoCaptures() {
        mock.varargs("1", "2");

        verify(mock).varargs(captor.capture(), captor.capture());

        assertThatCaptor(captor).contains("1", "2");
    }

    @Test
    public void shouldCaptureVarArgs_oneNullArgument2() {
        mock.varargs("1", null);

        verify(mock).varargs(captor.capture(), captor.capture());

        assertThatCaptor(captor).contains("1", (String) null);
    }

    @Test
    public void shouldNotCaptureVarArgs_3args2captures() {
        mock.varargs("1", "2", "3");

        assertThatThrownBy(
                        () -> {
                            verify(mock).varargs(captor.capture(), captor.capture());
                        })
                .isInstanceOf(ArgumentsAreDifferent.class);
    }

    @Test
    public void shouldCaptureVarArgs_3argsCaptorMatcherMix() {
        mock.varargs("1", "2", "3");

        verify(mock).varargs(captor.capture(), eq("2"), captor.capture());

        assertThatCaptor(captor).containsExactly("1", "3");
    }

    @Test
    public void shouldNotCaptureVarArgs_3argsCaptorMatcherMix() {
        mock.varargs("1", "2", "3");

        try {
            verify(mock).varargs(captor.capture(), eq("X"), captor.capture());
            fail("The verification must fail, cause the second arg was not 'X' as expected!");
        } catch (ArgumentsAreDifferent expected) {
        }

        assertThatCaptor(captor).isEmpty();
    }

    @Test
    public void shouldNotCaptureVarArgs_1args2captures() {
        mock.varargs("1");

        assertThatThrownBy(
                        () -> {
                            verify(mock).varargs(captor.capture(), captor.capture());
                        })
                .isInstanceOf(ArgumentsAreDifferent.class);
    }

    @Test
    public void shouldCaptureVarArgsAsArray() {
        mock.varargs("1", "2");

        ArgumentCaptor<String[]> varargCaptor = ArgumentCaptor.forClass(String[].class);

        verify(mock).varargs(varargCaptor.capture());

        assertThatCaptor(varargCaptor).containsExactly(new String[] {"1", "2"});
    }

    @Test
    public void shouldNotMatchRegualrAndVaraArgs() {
        mock.varargsString(1, "a", "b");

        assertThatThrownBy(
                        () -> {
                            verify(mock).varargsString(1);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class);
    }

    @Test
    public void shouldNotMatchVaraArgs() {
        when(mock.varargsObject(1, "a", "b")).thenReturn("OK");

        Assertions.assertThat(mock.varargsObject(1)).isNull();
    }

    @Test
    public void shouldDifferentiateNonVarargVariant() {
        given(mock.methodWithVarargAndNonVarargVariants(any(String.class)))
                .willReturn("single arg method");

        assertThat(mock.methodWithVarargAndNonVarargVariants("a")).isEqualTo("single arg method");
        assertThat(mock.methodWithVarargAndNonVarargVariants(new String[] {"a"})).isNull();
        assertThat(mock.methodWithVarargAndNonVarargVariants("a", "b")).isNull();
    }

    @Test
    public void shouldMockVarargsInvocation_single_vararg_matcher() {
        given(mock.methodWithVarargAndNonVarargVariants(any(String[].class)))
                .willReturn("var arg method");

        assertThat(mock.methodWithVarargAndNonVarargVariants("a")).isNull();
        assertThat(mock.methodWithVarargAndNonVarargVariants(new String[] {"a"}))
                .isEqualTo("var arg method");
        assertThat(mock.methodWithVarargAndNonVarargVariants("a", "b")).isEqualTo("var arg method");
    }

    @Test
    public void shouldMockVarargsInvocation_multiple_vararg_matcher() {
        given(mock.methodWithVarargAndNonVarargVariants(any(String.class), any(String.class)))
                .willReturn("var arg method");

        assertThat(mock.methodWithVarargAndNonVarargVariants("a")).isNull();
        assertThat(mock.methodWithVarargAndNonVarargVariants(new String[] {"a"})).isNull();
        assertThat(mock.methodWithVarargAndNonVarargVariants("a", "b")).isEqualTo("var arg method");
        assertThat(mock.methodWithVarargAndNonVarargVariants(new String[] {"a", "b"}))
                .isEqualTo("var arg method");
        assertThat(mock.methodWithVarargAndNonVarargVariants("a", "b", "c")).isNull();
    }

    @Test
    public void shouldMockVarargsInvocationForSuperType() {
        given(mock.varargsReturningString(any(Object[].class))).willReturn("a");

        assertThat(mock.varargsReturningString("a", "b")).isEqualTo("a");
    }

    @Test
    public void shouldHandleArrayVarargsMethods() {
        given(mock.arrayVarargsMethod(any(String[][].class))).willReturn(1);

        assertThat(mock.arrayVarargsMethod(new String[] {})).isEqualTo(1);
    }

    @Test
    public void shouldCaptureVarArgs_NullArrayArg1() {
        mock.varargs((String[]) null);
        ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

        verify(mock).varargs(captor.capture());

        assertThat(captor.getValue()).isNull();
    }

    @Test
    public void shouldCaptureVarArgs_NullArrayArg2() {
        mock.varargs((String[]) null);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(mock).varargs(captor.capture());

        assertThat(captor.getValue()).isNull();
    }

    @Test
    public void shouldVerifyVarArgs_any_NullArrayArg1() {
        mock.varargs((String[]) null);

        verify(mock).varargs(any());
    }

    @Test
    public void shouldVerifyVarArgs_any_NullArrayArg2() {
        mock.varargs((String) null);

        verify(mock).varargs(any());
    }

    @Test
    public void shouldVerifyVarArgs_eq_NullArrayArg1() {
        mock.varargs((String[]) null);

        verify(mock).varargs(eq(null));
    }

    @Test
    public void shouldVerifyVarArgs_eq_NullArrayArg2() {
        mock.varargs((String) null);

        verify(mock).varargs(eq(null));
    }

    @Test
    public void shouldVerifyVarArgs_isNull_NullArrayArg() {
        mock.varargs((String) null);

        verify(mock).varargs(isNull(String.class));
    }

    @Test
    public void shouldVerifyVarArgs_isNull_NullArrayArg2() {
        mock.varargs((String) null);

        verify(mock).varargs(isNull());
    }

    @Test
    public void shouldVerifyExactlyOneVarArg_isA() {
        mock.varargs("one param");

        verify(mock).varargs(isA(String.class));
    }

    @Test
    public void shouldNotVerifyExactlyOneVarArg_isA() {
        mock.varargs("two", "params");

        verify(mock, never()).varargs(isA(String.class));
    }

    @Test
    public void shouldVerifyVarArgArray_isA() {
        mock.varargs("one param");

        verify(mock).varargs(isA(String[].class));
    }

    @Test
    public void shouldVerifyVarArgArray_isA2() {
        mock.varargs("two", "params");

        verify(mock).varargs(isA(String[].class));
    }

    @Test
    public void shouldVerifyExactlyOneVarArg_any() {
        mock.varargs("one param");

        verify(mock).varargs(any(String.class));
    }

    @Test
    @Ignore("Fails due to https://github.com/mockito/mockito/issues/1593")
    public void shouldNotVerifyExactlyOneVarArg_any() {
        mock.varargs("two", "params");

        verify(mock, never()).varargs(any(String.class));
    }

    @Test
    public void shouldMockVarargInvocation_eq() {
        given(mock.varargs(eq("one param"))).willReturn(1);

        assertThat(mock.varargs("one param")).isEqualTo(1);
        assertThat(mock.varargs()).isEqualTo(0);
        assertThat(mock.varargs("different")).isEqualTo(0);
        assertThat(mock.varargs("one param", "another")).isEqualTo(0);
    }

    @Test
    public void shouldVerifyInvocation_eq() {
        mock.varargs("one param");

        verify(mock).varargs(eq("one param"));
        verify(mock, never()).varargs();
        verify(mock, never()).varargs(eq("different"));
        verify(mock, never()).varargs(eq("one param"), eq("another"));
    }

    @Test
    public void shouldMockVarargInvocation_eq_raw() {
        given(mock.varargs(eq(new String[] {"one param"}))).willReturn(1);

        assertThat(mock.varargs("one param")).isEqualTo(1);
        assertThat(mock.varargs()).isEqualTo(0);
        assertThat(mock.varargs("different")).isEqualTo(0);
        assertThat(mock.varargs("one param", "another")).isEqualTo(0);
    }

    @Test
    public void shouldVerifyInvocation_eq_raw() {
        mock.varargs("one param");

        verify(mock).varargs(eq(new String[] {"one param"}));
        verify(mock, never()).varargs(eq(new String[] {}));
        verify(mock, never()).varargs(eq(new String[] {"different"}));
        verify(mock, never()).varargs(eq(new String[] {"one param", "another"}));
    }

    @Test
    public void shouldVerifyInvocation_not() {
        mock.varargs("one param");

        verify(mock).varargs(not(eq(new String[] {"diff"})));
        verify(mock, never()).varargs(not(eq(new String[] {"one param"})));
    }

    @Test
    public void shouldVerifyInvocation_same() {
        String[] args = {"two", "params"};

        mock.varargs(args);

        verify(mock).varargs(same(args));
        verify(mock, never()).varargs(same(new String[] {"two", "params"}));
    }

    @Test
    public void shouldVerifySubTypes() {
        mock.polyVararg(new SubType(), new SubType());

        verify(mock).polyVararg(eq(new SubType()), eq(new SubType()));
        verify(mock).polyVararg(eq(new SubType[] {new SubType(), new SubType()}));
        verify(mock).polyVararg(eq(new BaseType[] {new SubType(), new SubType()}));
    }

    @Test
    public void shouldVerifyInvocation_or() {
        mock.polyVararg(new SubType(), new SubType());

        verify(mock)
                .polyVararg(
                        or(
                                eq(new BaseType[] {new SubType()}),
                                eq(new SubType[] {new SubType(), new SubType()})));
        verify(mock)
                .polyVararg(
                        or(
                                eq(new BaseType[] {new SubType(), new SubType()}),
                                eq(new SubType[] {new SubType()})));
    }

    @Test
    public void shouldVerifyInvocation_and() {
        mock.polyVararg(new SubType(), new SubType());

        verify(mock)
                .polyVararg(
                        and(
                                eq(new BaseType[] {new SubType(), new SubType()}),
                                eq(new SubType[] {new SubType(), new SubType()})));
    }

    private static <T> AbstractListAssert<?, ?, T, ObjectAssert<T>> assertThatCaptor(
            ArgumentCaptor<T> captor) {
        return Assertions.assertThat(captor.getAllValues());
    }

    private static class SubType implements BaseType {
        @Override
        public boolean equals(final Object obj) {
            return obj != null && obj.getClass().equals(getClass());
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
