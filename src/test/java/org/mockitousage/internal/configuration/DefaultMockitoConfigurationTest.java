package org.mockitousage.internal.configuration;

import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.MockitoPlugin;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

public class DefaultMockitoConfigurationTest extends TestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none(); 
    
    @Test
    public void should_have_injectingAnnotationEngine_when_using_default_constructor() throws Exception {
        DefaultMockitoConfiguration defaultMockitoConfiguration = new DefaultMockitoConfiguration();
        assertTrue(defaultMockitoConfiguration.getAnnotationEngine().getClass().equals(InjectingAnnotationEngine.class));
    }

    @Test
    public void should_have_injectingAnnotationEngine_when_using_constructor_with_mockitplugin_annotation() throws Exception {
        MockitoPlugin annotation = Mockito.mock(MockitoPlugin.class);
        when(annotation.annotationEngine())
            .thenAnswer(new Answer<Class<?>>() {
                @Override
                public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                    return MyEngine.class;
                }
            });
        
        DefaultMockitoConfiguration defaultMockitoConfiguration = new DefaultMockitoConfiguration(annotation);
        assertTrue(defaultMockitoConfiguration.getAnnotationEngine().getClass().equals(MyEngine.class));
    }
    
    @Test
    public void should_throw_illegalargument_exception_when_constructor_is_private() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        
        MockitoPlugin annotation = Mockito.mock(MockitoPlugin.class);
        when(annotation.annotationEngine())
            .thenAnswer(new Answer<Class<?>>() {
                @Override
                public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                    return MyEngineWithPrivateConstructor.class;
                }
            });
        
        DefaultMockitoConfiguration defaultMockitoConfiguration = new DefaultMockitoConfiguration(annotation);
        assertTrue(defaultMockitoConfiguration.getAnnotationEngine().getClass().equals(MyEngine.class));
    }
    
    public static class MyEngine implements AnnotationEngine {

        @Override
        public Object createMockFor(Annotation annotation, Field field) {
            return null;
        }

        @Override
        public void process(Class<?> clazz, Object testInstance) {
        }
    }
    
    public static class MyEngineWithPrivateConstructor implements AnnotationEngine {
        private MyEngineWithPrivateConstructor() {
        }
        
        @Override
        public Object createMockFor(Annotation annotation, Field field) {
            return null;
        }

        @Override
        public void process(Class<?> clazz, Object testInstance) {
        }
    }

}
