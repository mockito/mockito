/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.exceptions.base.MockitoException;

@RunWith(Enclosed.class)
public class MockAnnotationProcessorTest {

    @SuppressWarnings("unused")
    private MockedStatic<Void> nonGeneric;

    @SuppressWarnings("unused")
    private MockedStatic<List<?>> generic;

    @SuppressWarnings({"rawtypes", "unused"})
    private MockedStatic raw;

    @SuppressWarnings("unused")
    private MockedConstruction<Void> nonGenericConstruction;

    @SuppressWarnings("unused")
    private MockedConstruction<List<?>> genericConstruction;

    @SuppressWarnings({"rawtypes", "unused"})
    private MockedConstruction rawConstruction;

    @RunWith(Parameterized.class)
    public static class NonGenericTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {{"nonGeneric"}, {"nonGenericConstruction"}});
        }

        @Parameter public String fieldName;

        @Test
        public void ensure_non_generic_fields_can_be_inferred() throws Exception {
            Class<?> type =
                    MockAnnotationProcessor.inferParameterizedType(
                            MockAnnotationProcessorTest.class
                                    .getDeclaredField(fieldName)
                                    .getGenericType(),
                            fieldName,
                            "Sample");
            assertThat(type).isEqualTo(Void.class);
        }
    }

    @RunWith(Parameterized.class)
    public static class GenericTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {{"generic"}, {"genericConstruction"}});
        }

        @Parameter public String fieldName;

        @Test
        public void ensure_generic_fields_can_be_inferred() throws Exception {
            Class<?> type =
                    MockAnnotationProcessor.inferParameterizedType(
                            MockAnnotationProcessorTest.class
                                    .getDeclaredField(fieldName)
                                    .getGenericType(),
                            fieldName,
                            "Sample");
            assertThat(type).isEqualTo(List.class);
        }
    }

    @RunWith(Parameterized.class)
    public static class RawTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {{"raw"}, {"rawConstruction"}});
        }

        @Parameter public String fieldName;

        @Test
        public void ensure_raw_fields_cannot_be_inferred() {
            assertThatThrownBy(
                            () ->
                                    MockAnnotationProcessor.inferParameterizedType(
                                            MockAnnotationProcessorTest.class
                                                    .getDeclaredField(fieldName)
                                                    .getGenericType(),
                                            fieldName,
                                            "Sample"))
                    .isInstanceOf(MockitoException.class)
                    .hasMessageContaining(
                            "Mockito cannot infer a static mock from a raw type for " + fieldName);
        }
    }

    @RunWith(Parameterized.class)
    public static class WrongNumberOfArgsTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {{"raw"}, {"rawConstruction"}});
        }

        @Parameter public String fieldName;

        @Test
        public void ensure_parameterized_types_with_more_than_one_arg_cannot_be_inferred() {
            final ParameterizedType parameterizedType = mock();
            when(parameterizedType.getActualTypeArguments())
                    .thenReturn(new Type[] {String.class, String.class});

            assertThatThrownBy(
                            () ->
                                    MockAnnotationProcessor.inferParameterizedType(
                                            parameterizedType, fieldName, "Sample"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(
                            "Incorrect number of type arguments for "
                                    + fieldName
                                    + " of type Sample: expected 1 but received 2");
        }
    }
}
