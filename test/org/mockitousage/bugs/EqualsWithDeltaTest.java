package org.mockitousage.bugs;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.EqualsWithDelta;

import static org.fest.assertions.Assertions.assertThat;

public class EqualsWithDeltaTest {

	@Test
	public void testEqualsWithDelta_NullExpected() throws Exception {
		Matcher<Number> matcher = equalsWithDelta(null);
		assertThat(matcher.matches(1.0)).isFalse();
	}

	@Test
	public void testEqualsWithDelta_NullActual() throws Exception {
		Matcher<Number> matcher = equalsWithDelta(1.0);
		assertThat(matcher.matches(null)).isFalse();
	}

    @Test
    public void testEqualsWithDelta_NullActualAndExpected() throws Exception {
        Matcher<Number> matcher = equalsWithDelta(null);
        assertThat(matcher.matches(null)).isTrue();
    }

    @Test
    public void testEqualsWithDelta_WhenActualAndExpectedAreTheSameObject() throws Exception {
        Double expected = 1.0;
        Double actual = expected;
        Matcher<Number> matcher = equalsWithDelta(expected);
        assertThat(matcher.matches(actual)).isTrue();
    }

	public Matcher<Number> equalsWithDelta(final Double expected) {
		return new EqualsWithDelta(expected, .000001);
	}
}