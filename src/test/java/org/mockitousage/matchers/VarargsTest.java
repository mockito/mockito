/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ObjectAssert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class VarargsTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Captor
    private ArgumentCaptor<String> captor;
    @Mock
    private IMethods mock;

    private static final Condition<Object> NULL = new Condition<Object>() {

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
    @Ignore("This test must succeed but is fails currently, see github issue #616")
    public void shouldMatchEmptyVarArgs_noArgsIsNotNull() {
        mock.varargs();

        verify(mock).varargs(isNotNull());
    }

    @Test
    @Ignore("This test must succeed but is fails currently, see github issue #616")
    public void shouldMatchEmptyVarArgs_noArgsIsNull() {
        mock.varargs();

        verify(mock).varargs(isNull());
    }

    @Test
    @Ignore("This test must succeed but is fails currently, see github issue #616")
    public void shouldMatchEmptyVarArgs_noArgsIsNotNullArray() {
        mock.varargs();

        verify(mock).varargs((String[]) isNotNull());
    }

    @Test
    public void shouldMatchVarArgs_oneNullArg_eqNull() {
        Object arg = null;
        mock.varargs(arg);

        verify(mock).varargs(eq(null));
    }

    @Test
    public void shouldMatchVarArgs_oneNullArg_isNull() {
        Object arg = null;
        mock.varargs(arg);

        verify(mock).varargs(isNull());
    }

    @Test
    public void shouldMatchVarArgs_nullArrayArg() {
        Object[] argArray = null;
        mock.varargs(argArray);

        verify(mock).varargs(isNull());
    }

    @Test
    public void shouldnotMatchVarArgs_twoArgsOneMatcher() {
        mock.varargs("1", "1");

        exception.expectMessage("Argument(s) are different");

        verify(mock).varargs(eq("1"));
    }

    @Test
    public void shouldMatchVarArgs_emptyVarArgsOneAnyMatcher() {
        mock.varargs();

        verify(mock).varargs((String[])any()); // any() -> VarargMatcher
    }

    @Test
    public void shouldMatchVarArgs_oneArgsOneAnyMatcher() {
        mock.varargs(1);

        verify(mock).varargs(any()); // any() -> VarargMatcher
    }

    @Test
    public void shouldMatchVarArgs_twoArgsOneAnyMatcher() {
        mock.varargs(1, 2);

        verify(mock).varargs(any()); // any() -> VarargMatcher
    }

    @Test
    public void shouldMatchVarArgs_twoArgsTwoAnyMatcher() {
        mock.varargs(1, 2);

        verify(mock).varargs(any(), any()); // any() -> VarargMatcher
    }

    @Test
    public void shouldMatchVarArgs_twoArgsThreeAnyMatcher() {
        mock.varargs(1, 2);

        exception.expectMessage("Argument(s) are different");

        verify(mock).varargs(any(), any(), any()); //any() -> VarargMatcher
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
    @Ignore
    public void shouldMatchEmptyVarArgs_emptyArrayIsNotNull() {
        mock.varargsbyte();

        verify(mock).varargsbyte((byte[]) isNotNull());
    }

    @Test
    public void shouldMatchVarArgs_oneArgIsNotNull() {
        mock.varargsbyte((byte) 1);

        verify(mock).varargsbyte((byte[]) isNotNull());
    }

    @Test
    public void shouldCaptureVarArgs_noArgs() {
        mock.varargs();

        verify(mock).varargs(captor.capture());

        assertThat(captor).isEmpty();
    }

    @Test
    public void shouldCaptureVarArgs_oneNullArg_eqNull() {
        String arg = null;
        mock.varargs(arg);

        verify(mock).varargs(captor.capture());

        assertThat(captor).areExactly(1, NULL);
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
        assertThat(captor).areExactly(1, NULL);
    }

    @Test
    public void shouldCaptureVarArgs_twoArgsOneCapture() {
        mock.varargs("1", "2");

        verify(mock).varargs(captor.capture());

        assertThat(captor).contains("1", "2");
    }

    @Test
    public void shouldCaptureVarArgs_twoArgsTwoCaptures() {
        mock.varargs("1", "2");

        verify(mock).varargs(captor.capture(), captor.capture());

        assertThat(captor).contains("1", "2");
    }

    @Test
    public void shouldCaptureVarArgs_oneNullArgument() {
        mock.varargs("1", null);

        verify(mock).varargs(captor.capture());

        assertThat(captor).contains("1", (String) null);
    }

    @Test
    public void shouldCaptureVarArgs_oneNullArgument2() {
        mock.varargs("1", null);

        verify(mock).varargs(captor.capture(), captor.capture());

        assertThat(captor).contains("1", (String) null);
    }

    @Test
    public void shouldNotCaptureVarArgs_3args2captures() {
        mock.varargs("1", "2", "3");

        exception.expect(ArgumentsAreDifferent.class);

        verify(mock).varargs(captor.capture(), captor.capture());

    }

    @Test
    public void shouldCaptureVarArgs_3argsCaptorMatcherMix() {
        mock.varargs("1", "2", "3");

        verify(mock).varargs(captor.capture(), eq("2"), captor.capture());

        assertThat(captor).containsExactly("1", "3");

    }

    @Test
    public void shouldNotCaptureVarArgs_3argsCaptorMatcherMix() {
        mock.varargs("1", "2", "3");

        try {
            verify(mock).varargs(captor.capture(), eq("X"), captor.capture());
            fail("The verification must fail, cause the second arg was not 'X' as expected!");
        } catch (ArgumentsAreDifferent expected) {
        }

        assertThat(captor).isEmpty();

    }

    @Test
    public void shouldNotCaptureVarArgs_1args2captures() {
        mock.varargs("1");

        exception.expect(ArgumentsAreDifferent.class);

        verify(mock).varargs(captor.capture(), captor.capture());

    }

    /**
     * As of v2.0.0-beta.118 this test fails. Once the github issues:
     * <ul>
     * <li>'#584 ArgumentCaptor can't capture varargs-arrays
     * <li>#565 ArgumentCaptor should be type aware' are fixed this test must
     * succeed
     * </ul>
     */
    @Test
    @Ignore("Blocked by github issue: #584 & #565")
    public void shouldCaptureVarArgsAsArray() {
        mock.varargs("1", "2");

        ArgumentCaptor<String[]> varargCaptor = ArgumentCaptor.forClass(String[].class);

        verify(mock).varargs(varargCaptor.capture());

        assertThat(varargCaptor).containsExactly(new String[] { "1", "2" });
    }

    @Test
    public void shouldNotMatchRegualrAndVaraArgs()   {
        mock.varargsString(1, "a","b");

        exception.expect(ArgumentsAreDifferent.class);

        verify(mock).varargsString(1);
    }
    @Test
    public void shouldNotMatchVaraArgs()   {
        when(mock.varargsObject(1, "a","b")).thenReturn("OK");

        Assertions.assertThat(mock.varargsObject(1)).isNull();
    }

    private static <T> AbstractListAssert<?, ?, T, ObjectAssert<T>> assertThat(ArgumentCaptor<T> captor) {
        return Assertions.assertThat(captor.getAllValues());
    }
}
