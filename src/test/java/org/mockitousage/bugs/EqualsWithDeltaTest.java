/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.EqualsWithDelta;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualsWithDeltaTest {

    @Test
    public void testEqualsWithDelta_NullExpected() {
        ArgumentMatcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(1.0)).isFalse();
    }

    @Test
    public void testEqualsWithDelta_NullActual() {
        ArgumentMatcher<Number> matcher = equalsWithDelta(1.0);
        assertThat(matcher.matches(null)).isFalse();
    }

    @Test
    public void testEqualsWithDelta_NullActualAndExpected() {
        ArgumentMatcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(null)).isTrue();
    }

    @Test
    public void testEqualsWithDelta_WhenActualAndExpectedAreTheSameObject() {
        Double expected = 1.0;
        ArgumentMatcher<Number> matcher = equalsWithDelta(expected);
        assertThat(matcher.matches(expected)).isTrue();
    }

    private ArgumentMatcher<Number> equalsWithDelta(final Double expected) {
        return new EqualsWithDelta(expected, .000001);
    }
}
