package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.EqualsWithDelta;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualsWithDeltaTest {

    @Test
    public void testEqualsWithDelta_NullExpected() throws Exception {
        ArgumentMatcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(1.0)).isFalse();
    }

    @Test
    public void testEqualsWithDelta_NullActual() throws Exception {
        ArgumentMatcher<Number> matcher = equalsWithDelta(1.0);
        assertThat(matcher.matches(null)).isFalse();
    }

    @Test
    public void testEqualsWithDelta_NullActualAndExpected() throws Exception {
        ArgumentMatcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(null)).isTrue();
    }

    @Test
    public void testEqualsWithDelta_WhenActualAndExpectedAreTheSameObject() throws Exception {
        Double expected = 1.0;
        Double actual = expected;
        ArgumentMatcher<Number> matcher = equalsWithDelta(expected);
        assertThat(matcher.matches(actual)).isTrue();
    }

    public ArgumentMatcher<Number> equalsWithDelta(final Double expected) {
        return new EqualsWithDelta(expected, .000001);
    }
}