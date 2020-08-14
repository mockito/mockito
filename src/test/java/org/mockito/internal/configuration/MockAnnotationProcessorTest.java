/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.util.List;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.exceptions.base.MockitoException;

import static org.assertj.core.api.Assertions.*;

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

    @Test(expected = MockitoException.class)
    public void testGeneric() throws Exception {
        MockAnnotationProcessor.inferParameterizedType(
                MockAnnotationProcessorTest.class.getDeclaredField("generic").getGenericType(),
                "generic",
                "Sample");
    }

    @Test(expected = MockitoException.class)
    public void testRaw() throws Exception {
        MockAnnotationProcessor.inferParameterizedType(
                MockAnnotationProcessorTest.class.getDeclaredField("raw").getGenericType(),
                "raw",
                "Sample");
    }
}
