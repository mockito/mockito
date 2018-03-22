/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;

public class AnnotationsAreCopiedFromMockedTypeTest {

    @Test
    public void mock_should_have_annotations_copied_from_mocked_type_at_class_level() {
        AnnotationWithDefaultValue onClassDefaultValue = mock(OnClass.class).getClass().getAnnotation(AnnotationWithDefaultValue.class);
        AnnotationWithCustomValue onClassCustomValue = mock(OnClass.class).getClass().getAnnotation(AnnotationWithCustomValue.class);

        assumeTrue("Annotation copying does not apply for inline mocks", mock(OnClass.class).getClass() != OnClass.class);

        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    @Test
    public void mock_should_have_annotations_copied_from_mocked_type_on_methods() {
        AnnotationWithDefaultValue onClassDefaultValue = method("method", mock(OnMethod.class)).getAnnotation(AnnotationWithDefaultValue.class);
        AnnotationWithCustomValue onClassCustomValue = method("method", mock(OnMethod.class)).getAnnotation(AnnotationWithCustomValue.class);

        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    @Test
    public void mock_should_have_annotations_copied_from_mocked_type_on_method_parameters() {
        AnnotationWithDefaultValue onClassDefaultValue = firstParamOf(method("method", mock(OnMethod.class))).getAnnotation(AnnotationWithDefaultValue.class);
        AnnotationWithCustomValue onClassCustomValue = firstParamOf(method("method", mock(OnMethod.class))).getAnnotation(AnnotationWithCustomValue.class);

        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    private AnnotatedElement firstParamOf(Method method) {
        final Annotation[] firstParamAnnotations = method.getParameterAnnotations()[0];

        return new AnnotatedElement() {
            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
                return getAnnotation(annotationClass) != null;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                for (Annotation firstParamAnnotation : firstParamAnnotations) {
                    if (annotationClass.isAssignableFrom(firstParamAnnotation.annotationType())) {
                        return (T) firstParamAnnotation;
                    }
                }
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return firstParamAnnotations;
            }

            @Override
            public Annotation[] getDeclaredAnnotations() {
                return firstParamAnnotations;
            }
        };
    }

    private Method method(String methodName, Object mock) {
        for (Method method : mock.getClass().getDeclaredMethods()) {
            if(methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new IllegalArgumentException("method name not found : " + methodName);
    }

    private Field field(String fieldName, Object mock) {
        for (Field field : mock.getClass().getDeclaredFields()) {
            if(fieldName.equals(field.getName())) {
                return field;
            }
        }
        throw new IllegalArgumentException("method name not found : " + fieldName);
    }

    @AnnotationWithDefaultValue
    @AnnotationWithCustomValue("yay")
    public class OnClass { }


    public class OnMethod {
        @AnnotationWithDefaultValue
        @AnnotationWithCustomValue("yay")
        public String method(
                @AnnotationWithDefaultValue
                @AnnotationWithCustomValue("yay")
                String ignored
        ) { return ""; }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, PARAMETER, FIELD})
    public @interface AnnotationWithDefaultValue {
        String value() default "yup";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, PARAMETER, FIELD})
    public @interface AnnotationWithCustomValue {
        String value() default "";
    }
}
