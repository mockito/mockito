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
        assertThat(value.toDebugString()).isEqualTo("debug string");
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value);
        Throwable t = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));

        assertThat(t)
            .as("There are two renderers that can handle this, both with the same priority.  The 'renderer 1' is chosen as it its class name is lexicographically smaller.")
            .hasMessageContaining("SomeDebugClass: Rendered by MyDebugValueRenderer1 with priority 2")
            .as("The 'renderer 2' is never chosen despite being available")
            .hasMessageNotContaining("MyDebugValueRenderer2");
    }

    @Test
    public void objects_meeting_requirements_of_multiple_renderers_of_equal_priority_are_rendered_in_lexicographical_order_of_classname() {
        class SomeDebugClass { // name contains 'Debug' so should be rendered with MyDebugValueRenderer1
            final String someField;

            SomeDebugClass(String someField) {
                this.someField = someField;
            }

            public String toDebugString() {
                return "debug string 1";
            }
        }
        class SomeOtherClass { // name does not contain 'Debug' so should be rendered with MyDebugValueRenderer2, and MyDebugValueRenderer1 will be tried first
            final String someField;

            SomeOtherClass(String someField) {
                this.someField = someField;
            }

            public String toDebugString() {
                return "debug string 2";
            }
        }
        SomeDebugClass value1 = new SomeDebugClass("some string");
        SomeOtherClass value2 = new SomeOtherClass("some string");
        assertThat(value1.getClass()).hasMethods("toDebugString");
        assertThat(value2.getClass()).hasMethods("toDebugString");
        assertThat(value1.toDebugString()).isEqualTo("debug string 1");
        assertThat(value2.toDebugString()).isEqualTo("debug string 2");
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value1);
        Throwable t1 = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));
        assertThat(t1).hasMessageContaining("SomeDebugClass: Rendered by MyDebugValueRenderer1 with priority 2");

        Mockito.clearInvocations(mock);
        mock.forObject(value2);
        Throwable t2 = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));
        assertThat(t2).hasMessageContaining("SomeOtherClass: Rendered by MyDebugValueRenderer2 with priority 2");
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
        assertThat(value.toDebugString()).isEqualTo("debug string");
        Mockito.doReturn("return value").when(mock).forObject(any());

        mock.forObject(value);
        Throwable t = Assertions.catchThrowable(() -> Mockito.verify(mock).forObject(argThat(it -> false)));

        assertThat(t).hasMessageContaining("SomeAnnotatedDebugClass: Rendered by MyDebugValueRenderer1 with priority 2");
    }
}
