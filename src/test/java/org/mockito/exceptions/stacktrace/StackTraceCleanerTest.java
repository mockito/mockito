/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner;

public class StackTraceCleanerTest {

    private DefaultStackTraceCleaner cleaner = new DefaultStackTraceCleaner();

    @Test
    public void allow_or_disallow_mockito_mockito_objects_in_stacktrace() throws Exception {
        assertAcceptedInStackTrace("my.custom.Type");
        assertRejectedInStackTrace("org.mockito.foo.Bar");

        assertAcceptedInStackTrace("org.mockito.internal.junit.JUnitRule");

        assertAcceptedInStackTrace("org.mockito.junit.AllTypesOfThisPackage");
        assertAcceptedInStackTrace("org.mockito.junit.subpackage.AllTypesOfThisPackage");

        assertAcceptedInStackTrace("org.mockito.runners.AllTypesOfThisPackage");
        assertAcceptedInStackTrace("org.mockito.runners.subpackage.AllTypesOfThisPackage");

        assertAcceptedInStackTrace("org.mockito.internal.runners.AllTypesOfThisPackage");
        assertAcceptedInStackTrace("org.mockito.internal.runners.subpackage.AllTypesOfThisPackage");

        assertRejectedInStackTrace("my.custom.Type$$EnhancerByMockitoWithCGLIB$$Foo");
        assertRejectedInStackTrace("my.custom.Type$MockitoMock$Foo");
    }

    private void assertAcceptedInStackTrace(String className) {
        assertThat(cleaner.isIn(stackTraceElementWith(className)))
                .describedAs("Must be accepted in stacktrace %s", className)
                .isTrue();
    }

    private void assertRejectedInStackTrace(String className) {
        assertThat(cleaner.isIn(stackTraceElementWith(className)))
                .describedAs("Must be rejected in stacktrace %s", className)
                .isFalse();
    }

    private StackTraceElement stackTraceElementWith(String className) {
        return new StackTraceElement(className, "methodName", null, -1);
    }
}
