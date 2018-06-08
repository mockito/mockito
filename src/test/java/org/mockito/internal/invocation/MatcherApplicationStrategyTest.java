/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.MatcherApplicationStrategy.getMatcherApplicationStrategyFor;
import static org.mockito.internal.matchers.Any.ANY;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MatcherApplicationStrategyTest extends TestBase {

    @Mock
    IMethods mock;
    private Invocation invocation;
    private List matchers;

    private RecordingAction recordAction;

    @Before
    public void before() {
        recordAction = new RecordingAction();
    }

    @Test
    public void shouldKnowWhenActualArgsSizeIsDifferent1() {
        // given
        invocation = varargs("1");
        matchers = asList(new Equals("1"));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(RETURN_ALWAYS_FALSE);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenActualArgsSizeIsDifferent2() {
        // given
        invocation = varargs("1");
        matchers = asList(new Equals("1"));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(RETURN_ALWAYS_TRUE);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldKnowWhenActualArgsSizeIsDifferent() {
        // given
        invocation = varargs("1", "2");
        matchers = asList(new Equals("1"));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(RETURN_ALWAYS_TRUE);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenMatchersSizeIsDifferent() {
        // given
        invocation = varargs("1");
        matchers = asList(new Equals("1"), new Equals("2"));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(RETURN_ALWAYS_TRUE);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenVarargsMatch() {
        // given
        invocation = varargs("1", "2", "3");
        matchers = asList(new Equals("1"), Any.ANY, new InstanceOf(String.class));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldAllowAnyVarargMatchEntireVararg() {
        // given
        invocation = varargs("1", "2");
        matchers = asList(ANY);

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldNotAllowAnyObjectWithMixedVarargs() {
        // given
        invocation = mixedVarargs(1, "1", "2");
        matchers = asList(new Equals(1));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldAllowAnyObjectWithMixedVarargs() {
        // given
        invocation = mixedVarargs(1, "1", "2");
        matchers = asList(new Equals(1), ANY);

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldAnyObjectVarargDealWithDifferentSizeOfArgs() {
        // given
        invocation = mixedVarargs(1, "1", "2");
        matchers = asList(new Equals(1));

        // when
        boolean match = getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        assertFalse(match);

        recordAction.assertIsEmpty();
    }

    @Test
    public void shouldMatchAnyVarargEvenIfOneOfTheArgsIsNull() {
        // given
        invocation = mixedVarargs(null, null, "2");
        matchers = asList(new Equals(null), ANY);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(new Equals(null), ANY, ANY);

    }

    @Test
    public void shouldMatchAnyVarargEvenIfMatcherIsDecorated() {
        // given
        invocation = varargs("1", "2");
        matchers = asList(ANY);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(ANY, ANY);
    }

    @Test
    public void shouldMatchAnyVarargEvenIfMatcherIsWrappedInHamcrestMatcher() {
        // given
        invocation = varargs("1", "2");
        HamcrestArgumentMatcher argumentMatcher = new HamcrestArgumentMatcher(new IntMatcher());
        matchers = asList(argumentMatcher);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers).forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(argumentMatcher, argumentMatcher);
    }

    class IntMatcher extends BaseMatcher<Integer> implements VarargMatcher {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    private Invocation mixedVarargs(Object a, String... s) {
        mock.mixedVarargs(a, s);
        return getLastInvocation();
    }

    private Invocation varargs(String... s) {
        mock.varargs(s);
        return getLastInvocation();
    }

    private class RecordingAction implements ArgumentMatcherAction {
        private List<ArgumentMatcher<?>> matchers = new ArrayList<ArgumentMatcher<?>>();

        @Override
        public boolean apply(ArgumentMatcher<?> matcher, Object argument) {
            matchers.add(matcher);
            return true;
        }

        public void assertIsEmpty() {
            assertThat(matchers).isEmpty();
        }

        public void assertContainsExactly(ArgumentMatcher<?>... matchers) {
            assertThat(this.matchers).containsExactly(matchers);
        }
    }

    private static final ArgumentMatcherAction RETURN_ALWAYS_TRUE = new ArgumentMatcherAction() {
        @Override
        public boolean apply(ArgumentMatcher<?> matcher, Object argument) {
            return true;
        }
    };

    private static final ArgumentMatcherAction RETURN_ALWAYS_FALSE = new ArgumentMatcherAction() {
        @Override
        public boolean apply(ArgumentMatcher<?> matcher, Object argument) {
            return false;
        }
    };

}
