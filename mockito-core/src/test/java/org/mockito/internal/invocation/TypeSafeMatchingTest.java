/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class TypeSafeMatchingTest {

    private static final Object NOT_A_COMPARABLE = new Object();

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock public IMethods mock;

    /**
     * Should not throw an {@link NullPointerException}
     *
     * @see <a href="https://github.com/mockito/mockito/issues/457">Bug 457</a>
     */
    @Test
    public void compareNullArgument() {
        boolean match = matchesTypeSafe().apply(new LessOrEqual<Integer>(5), null);
        assertThat(match).isFalse();
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToNonCompareable() {
        boolean match = matchesTypeSafe().apply(new LessOrEqual<Integer>(5), NOT_A_COMPARABLE);
        assertThat(match).isFalse();
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToNull() {
        boolean match = matchesTypeSafe().apply(new LessOrEqual<Integer>(null), null);
        assertThat(match).isFalse();
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToNull2() {
        boolean match = matchesTypeSafe().apply(Null.NULL, null);
        assertThat(match).isTrue();
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToStringVsInt() {
        boolean match = matchesTypeSafe().apply(new StartsWith("Hello"), 123);
        assertThat(match).isFalse();
    }

    @Test
    public void compareToIntVsString() throws Exception {
        boolean match = matchesTypeSafe().apply(new LessOrEqual<Integer>(5), "Hello");
        assertThat(match).isFalse();
    }

    @Test
    public void matchesOverloadsMustBeIgnored() {
        class TestMatcher implements ArgumentMatcher<Integer> {
            @Override
            public boolean matches(Integer arg) {
                return false;
            }

            @SuppressWarnings("unused")
            public boolean matches(Date arg) {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("unused")
            public boolean matches(Integer arg, Void v) {
                throw new UnsupportedOperationException();
            }
        }

        boolean match = matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(match).isFalse();
    }

    @Test
    public void matchesWithSubTypeExtendingGenericClass() {
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        class TestMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                return true;
            }
        }
        boolean match = matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(match).isTrue();
    }

    @Test
    public void dontMatchesWithSubTypeExtendingGenericClass() {
        final AtomicBoolean wasCalled = new AtomicBoolean();

        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        class TestMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                wasCalled.set(true);
                return true;
            }
        }
        wasCalled.set(false);
        matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(wasCalled.get()).isTrue();

        wasCalled.set(false);
        matchesTypeSafe().apply(new TestMatcher(), "");
        assertThat(wasCalled.get()).isFalse();
    }

    @Test
    public void passEveryArgumentTypeIfNoBridgeMethodWasGenerated() {
        final AtomicBoolean wasCalled = new AtomicBoolean();
        class GenericMatcher<T> implements ArgumentMatcher<T> {
            @Override
            public boolean matches(T argument) {
                wasCalled.set(true);
                return true;
            }
        }

        wasCalled.set(false);
        matchesTypeSafe().apply(new GenericMatcher<Integer>(), 123);
        assertThat(wasCalled.get()).isTrue();

        wasCalled.set(false);
        matchesTypeSafe().apply(new GenericMatcher<Integer>(), "");
        assertThat(wasCalled.get()).isTrue();
    }
}
