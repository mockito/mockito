package org.mockito.exceptions.stacktrace;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner;

import static org.assertj.core.api.Assertions.assertThat;

public class StackTraceCleanerTest {

	private DefaultStackTraceCleaner cleaner;

	@Before
	public void setUp() {
		cleaner = new DefaultStackTraceCleaner();
	}

	@Test
	public void testName() throws Exception {
		assertAccepted("my.custom.Type");
		assertRejected("org.mockito.foo.Bar");
		
		assertAccepted("org.mockito.internal.junit.JUnitRule");
		
		assertAccepted("org.mockito.runners.AllTypesOfThisPackage");
		assertAccepted("org.mockito.runners.subpackage.AllTypesOfThisPackage");
		
		assertAccepted("org.mockito.internal.runners.AllTypesOfThisPackage");
		assertAccepted("org.mockito.internal.runners.subpackage.AllTypesOfThisPackage");
		
		assertRejected("my.custom.Type$$EnhancerByMockitoWithCGLIB$$Foo");
		assertRejected("my.custom.Type$MockitoMock$Foo");
	}

	private void assertAccepted(String className) {
		assertThat(cleaner.isIn(type(className))).describedAs("Must be accepted %s", className).isTrue();
	}

	private void assertRejected(String className) {
		assertThat(cleaner.isIn(type(className))).describedAs("Must be rejected %s", className).isFalse();
	}

	private StackTraceElement type(String className) {
		return new StackTraceElement(className, "methodName", null, -1);
	}
}
