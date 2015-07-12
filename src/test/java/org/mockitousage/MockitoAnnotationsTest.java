package org.mockitousage;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoPlugin;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.MockUtil;
import org.mockito.runners.MockitoJUnitRunner;

public class MockitoAnnotationsTest {

    @Before
    public void setUp() {
        ConfigurationAccess.removeIt();
    }
    
    @After
    public void tearDown() {
        ConfigurationAccess.removeIt();
    }

    @Test
    public void should_process_the_testcase_using_default_annotation_engine() throws Exception {
        NoMockitoPluginAnnotationTestClass noMockitoPluginAnnotationTestClass = new NoMockitoPluginAnnotationTestClass(); 

        MockitoAnnotations.initMocks(noMockitoPluginAnnotationTestClass);
        assertThat(isMock(noMockitoPluginAnnotationTestClass.someMock)).isTrue();
    }

    @Test
    public void should_process_the_testcase_using_MyAnnotationEngine() throws Exception {
        MockitoPluginAnnotationTestClass mockitoPluginAnnotationTestClass = new MockitoPluginAnnotationTestClass(); 

        MockitoAnnotations.initMocks(mockitoPluginAnnotationTestClass);
        
        MyAnnotationEngine annotationEngine = (MyAnnotationEngine) new GlobalConfiguration().getAnnotationEngine();
        assertThat(annotationEngine.processInvoked).isTrue();
    }

    @Test
    public void ensure_when_that_different_testcases_can_have_different_annotation_engine() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.addListener(new TextListener(System.out));

        Result result1 = runner.run(TestClass1.class);
        AnnotationEngine annotationEngine1 = ConfigurationAccess.getConfigAsInterface().getAnnotationEngine();
        assertThat(result1.getFailureCount()).isEqualTo(0);
        assertThat(result1.wasSuccessful()).isTrue();
        assertThat(annotationEngine1).isInstanceOf(MyAnnotationEngineForClass1.class);
        MyAnnotationEngineForClass1 myAnnotationEngineForClass1 = (MyAnnotationEngineForClass1) annotationEngine1;
        assertThat(myAnnotationEngineForClass1.processInvoked).isTrue();

        Result result2 = runner.run(TestClass2.class);
        AnnotationEngine annotationEngine2 = ConfigurationAccess.getConfigAsInterface().getAnnotationEngine();
        assertThat(result2.getFailureCount()).isEqualTo(0);
        assertThat(result2.wasSuccessful()).isTrue();
        assertThat(annotationEngine2).isInstanceOf(MyAnnotationEngineForClass2.class);
        MyAnnotationEngineForClass2 myAnnotationEngineForClass2 = (MyAnnotationEngineForClass2) annotationEngine2;
        assertThat(myAnnotationEngineForClass2.processInvoked).isTrue();
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

    

    @RunWith(MockitoJUnitRunner.class)
    @MockitoPlugin(annotationEngine=MyAnnotationEngineForClass1.class)
    public static class TestClass1 {
        
        private SomeMock someMock;
        
        @Test
        public void ensure_single_test_is_run() throws Exception {
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    @MockitoPlugin(annotationEngine=MyAnnotationEngineForClass2.class)
    public static class TestClass2 {
        
        private SomeMock someMock;
        
        @Test
        public void ensure_single_test_is_run() throws Exception {
        }
    }

    static class ClassToBeInjectedWithMocks {
    }
    
    public static class MyAnnotationEngineForClass1 implements AnnotationEngine {

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

    public static class MyAnnotationEngineForClass2 implements AnnotationEngine {

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


    
}
