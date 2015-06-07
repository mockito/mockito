package org.mockito;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;

public class AnnotationsAreCopiedFromMockedTypeTest {

    @Test
    public void mock_should_have_annotations_copied_from_mocked_type_at_class_level() {
        final AnnotationWithDefaultValue onClassDefaultValue = mock(OnClass.class).getClass().getAnnotation(AnnotationWithDefaultValue.class);
        final AnnotationWithCustomValue onClassCustomValue = mock(OnClass.class).getClass().getAnnotation(AnnotationWithCustomValue.class);

        Assertions.assertThat(mock(OnClass.class).getClass()).isNotSameAs(OnClass.class);
        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    @Test
    @Ignore("fields are not copied in the byte buddy mock, this is currently expected")
    public void mock_should_have_annotations_copied_from_mocked_type_on_fields() {
        final AnnotationWithDefaultValue onClassDefaultValue = field("field", mock(OnField.class)).getAnnotation(AnnotationWithDefaultValue.class);
        final AnnotationWithCustomValue onClassCustomValue = field("field", mock(OnField.class)).getAnnotation(AnnotationWithCustomValue.class);

        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    @Test
    public void mock_should_have_annotations_copied_from_mocked_type_on_methods() {
        final AnnotationWithDefaultValue onClassDefaultValue = method("method", mock(OnMethod.class)).getAnnotation(AnnotationWithDefaultValue.class);
        final AnnotationWithCustomValue onClassCustomValue = method("method", mock(OnMethod.class)).getAnnotation(AnnotationWithCustomValue.class);

        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    @Test
    public void mock_should_have_annotations_copied_from_mocked_type_on_method_parameters() {
        final AnnotationWithDefaultValue onClassDefaultValue = firstParamOf(method("method", mock(OnMethod.class))).getAnnotation(AnnotationWithDefaultValue.class);
        final AnnotationWithCustomValue onClassCustomValue = firstParamOf(method("method", mock(OnMethod.class))).getAnnotation(AnnotationWithCustomValue.class);

        Assertions.assertThat(onClassDefaultValue.value()).isEqualTo("yup");
        Assertions.assertThat(onClassCustomValue.value()).isEqualTo("yay");
    }

    private AnnotatedElement firstParamOf(final Method method) {
        final Annotation[] firstParamAnnotations = method.getParameterAnnotations()[0];

        return new AnnotatedElement() {
            public boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
                return getAnnotation(annotationClass) != null;
            }

            @SuppressWarnings("unchecked")
            public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
                for (final Annotation firstParamAnnotation : firstParamAnnotations) {
                    if (annotationClass.isAssignableFrom(firstParamAnnotation.getClass())) {
                        return (T) firstParamAnnotation;
                    }
                }
                return null;
            }

            public Annotation[] getAnnotations() {
                return firstParamAnnotations;
            }

            public Annotation[] getDeclaredAnnotations() {
                return firstParamAnnotations;
            }
        };
    }

    private Method method(final String methodName, final Object mock) {
        for (final Method method : mock.getClass().getDeclaredMethods()) {
            if(methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new IllegalArgumentException("method name not found : " + methodName);
    }

    private Field field(final String fieldName, final Object mock) {
        for (final Field field : mock.getClass().getDeclaredFields()) {
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
                @AnnotationWithCustomValue("yay") final
                String ignored
        ) {
            return "";
        }
    }

    public class OnField {
        @AnnotationWithDefaultValue
        @AnnotationWithCustomValue("yay")
        public String field;
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
