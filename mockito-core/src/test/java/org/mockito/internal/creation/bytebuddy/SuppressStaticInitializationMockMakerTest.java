/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

public class SuppressStaticInitializationMockMakerTest {

    @Test
    public void default_suppress_throws_unsupported() {
        MockMaker mockMaker = new UnsupportedMockMaker();

        assertThatThrownBy(
                        () ->
                                mockMaker.suppressStaticInitializationFor(
                                        Collections.singletonList("some.Class")))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("does not support suppression")
                .hasMessageContaining("UnsupportedMockMaker");
    }

    @Test
    public void default_restore_throws_unsupported() {
        MockMaker mockMaker = new UnsupportedMockMaker();

        assertThatThrownBy(
                        () ->
                                mockMaker.restoreStaticInitializationFor(
                                        Collections.singletonList("some.Class")))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("does not support suppression")
                .hasMessageContaining("UnsupportedMockMaker");
    }

    @Test
    public void subclass_mock_maker_does_not_support_suppression() {
        MockMaker mockMaker = new SubclassByteBuddyMockMaker();

        assertThatThrownBy(
                        () ->
                                mockMaker.suppressStaticInitializationFor(
                                        Arrays.asList("com.example.Foo")))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("does not support suppression");
    }

    /**
     * Minimal MockMaker that only implements the required methods, relying on
     * default implementations for suppression methods.
     */
    private static class UnsupportedMockMaker implements MockMaker {
        @Override
        public <T> T createMock(
                MockCreationSettings<T> settings,
                MockHandler handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MockHandler getHandler(Object mock) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void resetMock(
                Object mock,
                MockHandler newHandler,
                MockCreationSettings settings) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeMockability isTypeMockable(Class<?> type) {
            throw new UnsupportedOperationException();
        }
    }
}
