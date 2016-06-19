package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Matchers that operate over strings
 */
public class StringMatchersTest {
	@Test
	public void startsWithString() {
		assertTrue(new StartsWith("mockito").matches("mockito is here"));
	}

	@Test
	public void doesNotStartWithString() {
		assertFalse(new StartsWith("junit").matches("mockito is here"));
	}

	@Test
	public void nullStartsWith() {
		assertFalse(new StartsWith("java").matches(null));
	}

	@Test
	public void endsWithString() {
		assertTrue(new EndsWith("mockito").matches("here is mockito"));
	}

	@Test
	public void doesNotEndWithString() {
		assertFalse(new EndsWith("junit").matches("here is mockito"));
	}

	@Test
	public void nullEndsWith() {
		assertFalse(new EndsWith("java").matches(null));
	}

	@Test
	public void containsString() {
		assertTrue(new Contains("mockito").matches("****mockito****"));
	}

	@Test
	public void stringDoesNotContain() {
		assertFalse(new Contains("junit").matches("****mockito****"));
	}

	@Test
	public void nullContainsNothing() {
		assertFalse(new Contains("mockito").matches(null));
	}

	@Test
	public void matchesRegex() {
		assertTrue(new Find("eleph.nt").matches("the elephant in the room"));
		assertTrue(new Find("eleph.nt").matches("the elephInt in the room"));
	}

	@Test
	public void doesNotMatchRegex() {
		assertFalse(new Find("eleph.nt").matches("the otter in the room"));
	}

	@Test
	public void nullDoesNotMatchRegex() {
		assertFalse(new Find("eleph.nt").matches(null));
	}

}
