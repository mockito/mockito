/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.internal.invocation.MatcherApplicationStrategy.getMatcherApplicationStrategyFor;
import static org.mockito.internal.matchers.Any.ANY;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MatcherApplicationStrategyTest extends TestBase {

    @Mock IMethods mock;
    private Invocation invocation;
    private List<? extends ArgumentMatcher<?>> matchers;

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
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(RETURN_ALWAYS_FALSE);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenActualArgsSizeIsDifferent2() {
        // given
        invocation = varargs("1");
        matchers = asList(new Equals("1"));

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(RETURN_ALWAYS_TRUE);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldKnowWhenActualArgsSizeIsDifferent() {
        // given
        invocation = varargs("1", "2");
        matchers = asList(new Equals("1"));

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(RETURN_ALWAYS_TRUE);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenMatchersSizeIsDifferent() {
        // given
        invocation = varargs("1");
        matchers = asList(new Equals("1"), new Equals("2"));

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(RETURN_ALWAYS_TRUE);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldKnowWhenVarargsMatch() {
        // given
        invocation = varargs("1", "2", "3");
        matchers = asList(new Equals("1"), Any.ANY, new InstanceOf(String.class));

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(recordAction);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldNotMatchVarargsWithNoMatchers() {
        // given
        invocation = varargs("1", "2");
        matchers = asList();
        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(recordAction);

        // then
        assertFalse("Should not match when matchers list is empty", match);
        recordAction.assertIsEmpty();
    }

    @Test
    public void shouldAllowAnyMatchEntireVararg() {
        // given
        invocation = varargs("1", "2");
        matchers = asList(ANY, ANY);

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(recordAction);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldNotAllowAnyWithMixedVarargs() {
        // given
        invocation = mixedVarargs(1, "1", "2");
        matchers = asList(new Equals(1));

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(recordAction);

        // then
        assertFalse(match);
    }

    @Test
    public void shouldAllowAnyWithMixedVarargs() {
        // given
        invocation = mixedVarargs(1, "1", "2");
        matchers = asList(new Equals(1), ANY, ANY);

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(recordAction);

        // then
        assertTrue(match);
    }

    @Test
    public void shouldAnyDealWithDifferentSizeOfArgs() {
        // given
        invocation = mixedVarargs(1, "1", "2");
        matchers = asList(new Equals(1));

        // when
        boolean match =
                getMatcherApplicationStrategyFor(invocation, matchers)
                        .forEachMatcherAndArgument(recordAction);

        // then
        assertFalse(match);

        recordAction.assertIsEmpty();
    }

    @Test
    public void shouldMatchAnyEvenIfOneOfTheArgsIsNull() {
        // given
        invocation = mixedVarargs(null, null, "2");
        matchers = asList(new Equals(null), ANY, ANY);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers)
                .forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(new Equals(null), ANY, ANY);
    }

    @Test
    public void shouldMatchAnyEvenIfMatcherIsDecorated() {
        // given
        invocation = varargs("1", "2");
        matchers = asList(ANY, ANY);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers)
                .forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(ANY, ANY);
    }

    @Test
    public void shouldMatchAnyEvenIfMatcherIsWrappedInHamcrestMatcher() {
        // given
        invocation = varargs("1", "2");
        HamcrestArgumentMatcher<Integer> argumentMatcher =
                new HamcrestArgumentMatcher<>(new IntMatcher());
        matchers = asList(argumentMatcher, argumentMatcher);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers)
                .forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(argumentMatcher, argumentMatcher);
    }

    @Test
    public void shouldMatchAnyThatMatchesRawVarArgType() {
        // given
        invocation = varargs("1", "2");
        InstanceOf any = new InstanceOf(String[].class, "<any String[]>");
        matchers = asList(any);

        // when
        getMatcherApplicationStrategyFor(invocation, matchers)
                .forEachMatcherAndArgument(recordAction);

        // then
        recordAction.assertContainsExactly(any);
    }

    // Helper interface to mock Scala Seq
    private interface MockScalaSeq {}

    @Test
    public void shouldDetectScalaVarargsProperly() throws Exception {
        // Create a mock Invocation that simulates Scala varargs behavior
        Invocation mockInvocation = mock(Invocation.class);

        // Create a mock method with our fake Scala Seq parameter in the middle,
        // corresponding to `def method(a: String)(b: String*)(c: String)`
        Method mockMethod = mock(Method.class);
        when(mockMethod.getParameterTypes())
                .thenReturn(new Class<?>[] {String.class, MockScalaSeq.class, String.class});
        when(mockMethod.getParameterCount()).thenReturn(3);
        when(mockMethod.getGenericParameterTypes())
                .thenReturn(
                        new java.lang.reflect.Type[] {
                            String.class, MockScalaSeq.class, String.class
                        });
        when(mockMethod.getName()).thenReturn("mockMethod");
        when(mockMethod.isVarArgs()).thenReturn(false); // Scala varargs are not Java varargs

        // Set up the invocation to return different lengths for raw vs processed arguments
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        when(mockInvocation.getRawArguments()).thenReturn(new Object[] {new Object[] {"1", "2"}});
        when(mockInvocation.getArguments()).thenReturn(new Object[] {"1", "2"}); // Different length

        // Set up matchers
        List<ArgumentMatcher<?>> matchers = asList(new Equals("1"), new Equals("2"));

        // Create a strategy with spied optionalClass method to force Scala detection
        MatcherApplicationStrategy spyStrategy =
                spy(getMatcherApplicationStrategyFor(mockInvocation, matchers));

        // Mock the optionalClass method behavior to simulate Scala presence
        doReturn(Optional.of(MockScalaSeq.class))
                .when(spyStrategy)
                .optionalClass(eq("scala.collection.Seq"));

        // When: Test the strategy
        boolean match = spyStrategy.forEachMatcherAndArgument(recordAction);

        // Then: Assert the match was successful
        assertTrue("Failed to match Scala varargs pattern", match);
        recordAction.assertContainsExactly(new Equals("1"), new Equals("2"));
    }

    @Test
    public void shouldLoadExistingClasses() {
        MatcherApplicationStrategy strategy =
                getMatcherApplicationStrategyFor(varargs("1"), List.of(new Equals("1")));

        Optional<?> result = strategy.optionalClass("java.lang.String");
        assertTrue(result.isPresent());
        assertEquals(String.class, result.get());
    }

    @Test
    public void shouldReturnEmptyLoadingNonExistingClasses() {
        MatcherApplicationStrategy strategy =
                getMatcherApplicationStrategyFor(varargs("1"), List.of(new Equals("1")));

        Optional<?> result = strategy.optionalClass("non.existing.ClassClass");
        assertFalse(result.isPresent());
    }

    private static class IntMatcher extends BaseMatcher<Integer> {
        public boolean matches(Object o) {
            return true;
        }

        public void describeTo(Description description) {}
    }

    private static class IntArrayMatcher extends BaseMatcher<Integer[]> {
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

    private static class RecordingAction implements ArgumentMatcherAction {
        private final List<ArgumentMatcher<?>> matchers = new ArrayList<ArgumentMatcher<?>>();

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

    private static final ArgumentMatcherAction RETURN_ALWAYS_TRUE =
            new ArgumentMatcherAction() {
                @Override
                public boolean apply(ArgumentMatcher<?> matcher, Object argument) {
                    return true;
                }
            };

    private static final ArgumentMatcherAction RETURN_ALWAYS_FALSE =
            new ArgumentMatcherAction() {
                @Override
                public boolean apply(ArgumentMatcher<?> matcher, Object argument) {
                    return false;
                }
            };
}
