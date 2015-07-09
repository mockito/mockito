package org.mockitousage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoPlugin;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.MockUtil;
import org.mockito.runners.MockitoJUnitRunner;

public class MockitoAnnotationsTest {

    @Before
    public void setUp() {
         GlobalConfiguration.removeIt();
    }
    
    @After
    public void tearDown() {
         GlobalConfiguration.removeIt();
    }

    @Test
    public void should_process_the_testcase_using_default_annotation_engine() throws Exception {
        NoMockitoPluginAnnotationTestClass noMockitoPluginAnnotationTestClass = new NoMockitoPluginAnnotationTestClass(); 

        MockitoAnnotations.initMocks(noMockitoPluginAnnotationTestClass);
        assertThat(isMock(noMockitoPluginAnnotationTestClass.someMock), is(true));
    }

    @Test
    public void should_process_the_testcase_using_MyAnnotationEngine() throws Exception {
        MockitoPluginAnnotationTestClass mockitoPluginAnnotationTestClass = new MockitoPluginAnnotationTestClass(); 

        MockitoAnnotations.initMocks(mockitoPluginAnnotationTestClass);
        
        MyAnnotationEngine annotationEngine = (MyAnnotationEngine) new GlobalConfiguration().getAnnotationEngine();
        assertThat(annotationEngine.processInvoked, is(true));
    }
    
    @RunWith(MockitoJUnitRunner.class)
    public static class NoMockitoPluginAnnotationTestClass {
     
        @Mock
        public SomeMock someMock;
    }
    
    public static interface SomeMock {
        
    }
    
    @RunWith(MockitoJUnitRunner.class)
    @MockitoPlugin(annotationEngine=MyAnnotationEngine.class)
    public static class MockitoPluginAnnotationTestClass {
        
        @Mock
        public SomeMock someMock;
    }
    
    public static class MyAnnotationEngine implements AnnotationEngine {

        public boolean processInvoked = false;
        
        @Override
        public Object createMockFor(Annotation annotation, Field field) {
            throw new IllegalStateException();
        }

        @Override
        public void process(Class<?> clazz, Object testInstance) {
            processInvoked = true;
        }
    }

    private boolean isMock(Object o) {
        return new MockUtil().isMock(o);
    }

}
