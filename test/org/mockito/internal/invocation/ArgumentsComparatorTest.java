/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;
import org.junit.Test;
import org.mockito.internal.matchers.*;
import org.mockito.Mock;
import org.mockitousage.IMethods;

import java.util.List;
import static java.util.Arrays.asList;

@SuppressWarnings("unchecked")
public class ArgumentsComparatorTest extends TestBase {

    @Mock IMethods mock;
    ArgumentsComparator comparator = new ArgumentsComparator();
    
    @Test
    public void shouldKnowWhenArgumentsMatch() {
        //given
        Invocation invocation = new InvocationBuilder().args("1", 100).toInvocation();
        InvocationMatcher invocationMatcher = new InvocationBuilder().args("1", 100).toInvocationMatcher();

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void shouldKnowWhenArgsDifferent() {
        //given
        Invocation invocation = new InvocationBuilder().args("1", 100).toInvocation();
        InvocationMatcher invocationMatcher = new InvocationBuilder().args("100", 100).toInvocationMatcher();

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenActualArgsSizeIsDifferent() {
        //given
        Invocation invocation = new InvocationBuilder().args("100", 100).toInvocation();
        InvocationMatcher invocationMatcher = new InvocationBuilder().args("100").toInvocationMatcher();

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenMatchersSizeIsDifferent() {
        //given
        Invocation invocation = new InvocationBuilder().args("100").toInvocation();
        InvocationMatcher invocationMatcher = new InvocationBuilder().args("100", 100).toInvocationMatcher();

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenVarargsMatch() {
        //given
        mock.varargs("1", "2", "3");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals("1"), Any.ANY, new InstanceOf(String.class)));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void shouldKnowWhenVarargsDifferent() {
        //given
        mock.varargs("1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals("100"), Any.ANY));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldNotAllowAnyObjectMatchEntireVararg() {
        //given
        mock.varargs("1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(Any.ANY));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldAllowAnyVarargMatchEntireVararg() {
        //given
        mock.varargs("1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(AnyVararg.ANY_VARARG));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void shouldNotAllowAnyObjectWithMixedVarargs() {
        //given
        mock.mixedVarargs(1, "1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals(1)));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldAllowAnyObjectWithMixedVarargs() {
        //given
        mock.mixedVarargs(1, "1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals(1), AnyVararg.ANY_VARARG));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void shouldNotMatchWhenSomeOtherArgumentDoesNotMatch() {
        //given
        mock.mixedVarargs(1, "1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals(100), AnyVararg.ANY_VARARG));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldAnyObjectVarargDealWithDifferentSizeOfArgs() {
        //given
        mock.mixedVarargs(1, "1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals(1)));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertFalse(match);
    }

    @Test
    public void shouldMatchAnyVarargEvenIfOneOfTheArgsIsNull() {
        //given
        mock.mixedVarargs(null, null, "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals(null), AnyVararg.ANY_VARARG));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void shouldMatchAnyVarargEvenIfMatcherIsDecorated() {
        //given
        mock.varargs("1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new LocalizedMatcher(AnyVararg.ANY_VARARG)));

        //when
        boolean match = comparator.argumentsMatch(invocationMatcher, invocation);

        //then
        assertTrue(match);
    }
}