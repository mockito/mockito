package org.mockitousage;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoPlugin;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.TestClassWithMockitPluginAnnotation.MyAnnotationEngine;

@RunWith(MockitoJUnitRunner.class)
@MockitoPlugin(annotationEngine = MyAnnotationEngine.class)
public class TestClassWithMockitPluginAnnotation {
    @InjectMocks
    private ClassToBeInjectedWithMocks classToBeInjectedWithMocks;
    
    @Test
    public void ensure_class_is_instantiated_with_a_mock() throws Exception {
        assertThat(classToBeInjectedWithMocks).isNull();
    }
    
    public static class MyAnnotationEngine implements AnnotationEngine {

        @Override
        public Object createMockFor(Annotation annotation, Field field) {
            return null;
        }

        @Override
        public void process(Class<?> clazz, Object testInstance) {
            return;
        }
    }

    static class ClassToBeInjectedWithMocks {
    }

}