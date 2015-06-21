package org.mockitousage.bugs;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.MockitoMatcher;
import org.mockito.internal.matchers.EqualsWithDelta;

import static org.fest.assertions.Assertions.assertThat;

public class EqualsWithDeltaTest {

    @Test
    public void testEqualsWithDelta_NullExpected() throws Exception {
        MockitoMatcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(1.0)).isFalse();
    }

    @Test
    public void testEqualsWithDelta_NullActual() throws Exception {
        MockitoMatcher<Number> matcher = equalsWithDelta(1.0);
        assertThat(matcher.matches(null)).isFalse();
    }

    @Test
    public void testEqualsWithDelta_NullActualAndExpected() throws Exception {
        MockitoMatcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(null)).isTrue();
    }

    @Test
    public void testEqualsWithDelta_WhenActualAndExpectedAreTheSameObject() throws Exception {
        Double expected = 1.0;
        Double actual = expected;
        MockitoMatcher<Number> matcher = equalsWithDelta(expected);
        assertThat(matcher.matches(actual)).isTrue();
    }

    public MockitoMatcher<Number> equalsWithDelta(final Double expected) {
        return new EqualsWithDelta(expected, .000001);
    }
}