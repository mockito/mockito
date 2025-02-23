/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.exceptions.base.MockitoException;

public class MockAnnotationProcessorTest {

    @SuppressWarnings("unused")
    private MockedStatic<Void> nonGeneric;

    @SuppressWarnings("unused")
    private MockedStatic<List<?>> generic;

    @SuppressWarnings({"raw", "unused"})
    private MockedStatic raw;

    @Test
    public void testNonGeneric() throws Exception {
        Class<?> type =
                MockAnnotationProcessor.inferParameterizedType(
                        MockAnnotationProcessorTest.class
                                .getDeclaredField("nonGeneric")
                                .getGenericType(),
                        "nonGeneric",
                        "Sample");
        assertThat(type).isEqualTo(Void.class);
    }

    @Test
    public void testGeneric() {
        assertThatThrownBy(
                        () -> {
                            MockAnnotationProcessor.inferParameterizedType(
                                    MockAnnotationProcessorTest.class
                                            .getDeclaredField("generic")
                                            .getGenericType(),
                                    "generic",
                                    "Sample");
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining(
                        "Mockito cannot infer a static mock from a raw type for generic");
    }

    @Test
    public void testRaw() {
        assertThatThrownBy(
                        () -> {
                            MockAnnotationProcessor.inferParameterizedType(
                                    MockAnnotationProcessorTest.class
                                            .getDeclaredField("raw")
                                            .getGenericType(),
                                    "raw",
                                    "Sample");
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Mockito cannot infer a static mock from a raw type for raw");
    }
}
