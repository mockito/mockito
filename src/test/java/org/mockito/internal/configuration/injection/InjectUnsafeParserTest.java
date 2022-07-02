/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.InjectUnsafe;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.NONE;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.STATIC_FINAL;

public class InjectUnsafeParserTest {

    private final InjectUnsafeParser parser = new InjectUnsafeParser();

    static class Fixture {
        @InjectUnsafe(STATIC_FINAL)
        public Object foo;
    }

    static class FieldWithoutAnnotationFixture {
        public Object noAnnotation;
    }

    @Test
    public void parses_value_from_the_annotation() throws NoSuchFieldException {
        Field field = Fixture.class.getDeclaredField("foo");

        InjectUnsafe annotation = parser.parse(field);

        assertThat(annotation.value()).isEqualTo(STATIC_FINAL);
    }

    @Test
    public void fallback_value_is_NONE() {
        assertThat(InjectUnsafe.FALLBACK_VALUE).isEqualTo(NONE);
    }

    @Test
    public void parses_field_without_annotation_to_fallback_value() throws NoSuchFieldException {
        Field field = FieldWithoutAnnotationFixture.class.getDeclaredField("noAnnotation");

        InjectUnsafe annotation = parser.parse(field);

        assertThat(annotation.value()).isEqualTo(InjectUnsafe.FALLBACK_VALUE);
    }
}
