/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.valuerenderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitousage.plugins.valuerenderer.MyAnnotationValueRenderer.SomeAnnotation;
import org.mockitoutil.TestBase5;

public class ValueRendererTest extends TestBase5 {

    @Mock
    private IMethods mock;

    @Test
    public void objects_meeting_no_renderer_criteria_are_handled_by_default_renderer() {
        class SomeClass {
            final String someField;

            SomeClass(String someField) {
                this.someField = someField;
            }
        }
        SomeClass value = new SomeClass("some string");
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value);
        Throwable t = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));

        assertThat(t).hasMessageContaining("SomeClass[someField=some string]");
    }

    @Test
    public void objects_meeting_only_requirements_of_lowest_priority_custom_renderer_use_lowest_priority_renderer() {
        @SomeAnnotation
        class SomeAnnotatedClass {
            final String someField;

            SomeAnnotatedClass(String someField) {
                this.someField = someField;
            }
        }
        SomeAnnotatedClass value = new SomeAnnotatedClass("some string");
        assertThat(value.getClass()).hasAnnotation(SomeAnnotation.class);
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value);
        Throwable t = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));

        assertThat(t).hasMessageContaining("SomeAnnotatedClass: Rendered by MyAnnotationValueRenderer with priority 1");
    }

    @Test
    public void objects_meeting_requirements_of_highest_priority_renderer_are_rendered_with_highest_priority_renderer() {
        class SomeDebugClass {
            final String someField;

            SomeDebugClass(String someField) {
                this.someField = someField;
            }

            public String toDebugString() {
                return "debug string";
            }
        }
        SomeDebugClass value = new SomeDebugClass("some string");
        assertThat(value.getClass()).hasMethods("toDebugString");
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value);
        Throwable t = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));

        assertThat(t).hasMessageContaining("SomeDebugClass: Rendered by MyDebugValueRenderer with priority 2");
    }

    @Test
    public void objects_meeting_requirements_of_multiple_renderers_are_rendered_with_the_highest_priority_renderer() {
        @SomeAnnotation
        class SomeAnnotatedDebugClass {
            final String someField;

            SomeAnnotatedDebugClass(String someField) {
                this.someField = someField;
            }

            public String toDebugString() {
                return "debug string";
            }
        }
        SomeAnnotatedDebugClass value = new SomeAnnotatedDebugClass("some string");
        assertThat(value.getClass()).hasMethods("toDebugString");
        assertThat(value.getClass()).hasAnnotation(SomeAnnotation.class);
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value);
        Throwable t = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));

        assertThat(t).hasMessageContaining("SomeAnnotatedDebugClass: Rendered by MyDebugValueRenderer with priority 2");
    }
}
