/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.TestAbortedException;

class JunitJupiterSkippedTest {
    @Nested
    class NestedSkipBeforeEachMockito {
        @Test
        @ExtendWith({SkipTestBeforeEachExtension.class, MockitoExtension.class})
        void should_handle_skip_in_before_each() {}

        @Test
        @ExtendWith({MockitoExtension.class, SkipTestBeforeEachExtension.class})
        void should_handle_skip_after_before_each() {}

        @Test
        @ExtendWith(MockitoExtension.class)
        void should_handle_skip_in_test() {
            skipTest();
        }
    }

    @Nested
    @ExtendWith({SkipTestBeforeAllExtension.class, MockitoExtension.class})
    class NestedSkipBeforeAllSkipThenMockito {
        @Test
        void should_handle_skip_in_before_all() {}
    }

    @Nested
    @ExtendWith({MockitoExtension.class, SkipTestBeforeAllExtension.class})
    class NestedSkipBeforeAllMockitoThenSkip {
        @Test
        void should_handle_skip_in_before_all() {}
    }

    private static class SkipTestBeforeEachExtension implements BeforeEachCallback {
        @Override
        public void beforeEach(ExtensionContext context) {
            skipTest();
        }
    }

    private static class SkipTestBeforeAllExtension implements BeforeAllCallback {
        @Override
        public void beforeAll(ExtensionContext context) {
            skipTest();
        }
    }

    private static void skipTest() {
        throw new TestAbortedException("Skipped test");
    }
}
