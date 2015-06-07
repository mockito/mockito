/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import static java.util.Arrays.asList;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.AnyVararg;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.NotNull;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "rawtypes"})
public class InvocationMatcherTest extends TestBase {

    private InvocationMatcher simpleMethod;
    @Mock private IMethods mock;

    @Before
    public void setup() {
        simpleMethod = new InvocationBuilder().mock(mock).simpleMethod().toInvocationMatcher();
    }

    @Test
    public void should_be_a_citizen_of_hashes() throws Exception {
        final Invocation invocation = new InvocationBuilder().toInvocation();
        final Invocation invocationTwo = new InvocationBuilder().args("blah").toInvocation();

        final Map map = new HashMap();
        map.put(new InvocationMatcher(invocation), "one");
        map.put(new InvocationMatcher(invocationTwo), "two");

        assertEquals(2, map.size());
    }

    @Test
    public void should_not_equal_if_number_of_arguments_differ() throws Exception {
        final InvocationMatcher withOneArg = new InvocationMatcher(new InvocationBuilder().args("test").toInvocation());
        final InvocationMatcher withTwoArgs = new InvocationMatcher(new InvocationBuilder().args("test", 100).toInvocation());

        assertFalse(withOneArg.equals(null));
        assertFalse(withOneArg.equals(withTwoArgs));
    }

    @Test
    public void should_to_string_with_matchers() throws Exception {
        final Matcher m = NotNull.NOT_NULL;
        final InvocationMatcher notNull = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(m));
        final Matcher mTwo = new Equals('x');
        final InvocationMatcher equals = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(mTwo));

        assertContains("simpleMethod(notNull())", notNull.toString());
        assertContains("simpleMethod('x')", equals.toString());
    }

    @Test
    public void should_know_if_is_similar_to() throws Exception {
        final Invocation same = new InvocationBuilder().mock(mock).simpleMethod().toInvocation();
        assertTrue(simpleMethod.hasSimilarMethod(same));

        final Invocation different = new InvocationBuilder().mock(mock).differentMethod().toInvocation();
        assertFalse(simpleMethod.hasSimilarMethod(different));
    }

    @Test
    public void should_not_be_similar_to_verified_invocation() throws Exception {
        final Invocation verified = new InvocationBuilder().simpleMethod().verified().toInvocation();
        assertFalse(simpleMethod.hasSimilarMethod(verified));
    }

    @Test
    public void should_not_be_similar_if_mocks_are_different() throws Exception {
        final Invocation onDifferentMock = new InvocationBuilder().simpleMethod().mock("different mock").toInvocation();
        assertFalse(simpleMethod.hasSimilarMethod(onDifferentMock));
    }

    @Test
    public void should_not_be_similar_if_is_overloaded_but_used_with_the_same_arg() throws Exception {
        final Method method = IMethods.class.getMethod("simpleMethod", String.class);
        final Method overloadedMethod = IMethods.class.getMethod("simpleMethod", Object.class);

        final String sameArg = "test";

        final InvocationMatcher invocation = new InvocationBuilder().method(method).arg(sameArg).toInvocationMatcher();
        final Invocation overloadedInvocation = new InvocationBuilder().method(overloadedMethod).arg(sameArg).toInvocation();

        assertFalse(invocation.hasSimilarMethod(overloadedInvocation));
    }

    @Test
    public void should_be_similar_if_is_overloaded_but_used_with_different_arg() throws Exception {
        final Method method = IMethods.class.getMethod("simpleMethod", String.class);
        final Method overloadedMethod = IMethods.class.getMethod("simpleMethod", Object.class);

        final InvocationMatcher invocation = new InvocationBuilder().mock(mock).method(method).arg("foo").toInvocationMatcher();
        final Invocation overloadedInvocation = new InvocationBuilder().mock(mock).method(overloadedMethod).arg("bar").toInvocation();

        assertTrue(invocation.hasSimilarMethod(overloadedInvocation));
    }

    @Test
    public void should_capture_arguments_from_invocation() throws Exception {
        //given
        final Invocation invocation = new InvocationBuilder().args("1", 100).toInvocation();
        final CapturingMatcher capturingMatcher = new CapturingMatcher();
        final InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals("1"), capturingMatcher));

        //when
        invocationMatcher.captureArgumentsFrom(invocation);

        //then
        assertEquals(1, capturingMatcher.getAllValues().size());
        assertEquals(100, capturingMatcher.getLastValue());
    }

    @Test
    public void should_match_varargs_using_any_varargs() throws Exception {
        //given
        mock.varargs("1", "2");
        final Invocation invocation = getLastInvocation();
        final InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, asList(AnyVararg.ANY_VARARG));

        //when
        final boolean match = invocationMatcher.matches(invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void should_capture_varargs_as_vararg() throws Exception {
        //given
        mock.mixedVarargs(1, "a", "b");
        final Invocation invocation = getLastInvocation();
        final CapturingMatcher m = new CapturingMatcher();
        final InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals(1), new LocalizedMatcher(m)));

        //when
        invocationMatcher.captureArgumentsFrom(invocation);

        //then
        Assertions.assertThat(m.getAllValues()).containsExactly("a", "b");
    }

    @Test  // like using several time the captor in the vararg
    public void should_capture_arguments_when_args_count_does_NOT_match() throws Exception {
        //given
        mock.varargs();
        final Invocation invocation = getLastInvocation();

        //when
        final InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new LocalizedMatcher(AnyVararg.ANY_VARARG)));

        //then
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void should_create_from_invocations() throws Exception {
        //given
        final Invocation i = new InvocationBuilder().toInvocation();
        //when
        final List<InvocationMatcher> out = InvocationMatcher.createFrom(asList(i));
        //then
        assertEquals(1, out.size());
        assertEquals(i, out.get(0).getInvocation());
    }
}