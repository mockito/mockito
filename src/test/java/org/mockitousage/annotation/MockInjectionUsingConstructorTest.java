/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.examples.use.ArticleCalculator;
import org.mockitousage.examples.use.ArticleDatabase;
import org.mockitousage.examples.use.ArticleManager;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockInjectionUsingConstructorTest {

    @Mock private ArticleCalculator calculator;
    @Mock private ArticleDatabase database;

    @InjectMocks private ArticleManager articleManager;
    @Spy @InjectMocks private ArticleManager spiedArticleManager;


//    @InjectMocks private ArticleVisitor should_be_initialized_3_times;

    @Test
    public void shouldNotFailWhenNotInitialized() {
        assertNotNull(articleManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void innerMockShouldRaiseAnExceptionThatChangesOuterMockBehavior() {
        when(calculator.countArticles("new")).thenThrow(new IllegalArgumentException());

        articleManager.updateArticleCounters("new");
    }

    @Test
    public void mockJustWorks() {
        articleManager.updateArticleCounters("new");
    }

    @Test
    public void constructor_is_called_for_each_test_in_test_class() throws Exception {
        // given
        junit_test_with_3_tests_methods.constructor_instantiation = 0;
        JUnitCore jUnitCore = new JUnitCore();
        jUnitCore.addListener(new TextListener(System.out));

        // when
        jUnitCore.run(junit_test_with_3_tests_methods.class);

        // then
        assertThat(junit_test_with_3_tests_methods.constructor_instantiation).isEqualTo(3);
    }

    @Test
    public void objects_created_with_constructor_initialization_can_be_spied() throws Exception {
        assertFalse(MockUtil.isMock(articleManager));
        assertTrue(MockUtil.isMock(spiedArticleManager));
    }

    @Test
    public void should_report_failure_only_when_object_initialization_throws_exception() throws Exception {

        try {
            MockitoAnnotations.initMocks(new ATest());
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("failingConstructor").contains("constructor").contains("threw an exception");
            assertThat(e.getCause()).isInstanceOf(IllegalStateException.class);
        }
    }


    @RunWith(MockitoJUnitRunner.class)
    public static class junit_test_with_3_tests_methods {
        private static int constructor_instantiation = 0;

        @Mock List<?> some_collaborator;
        @InjectMocks some_class_with_parametered_constructor should_be_initialized_3_times;

        @Test public void test_1() { }
        @Test public void test_2() { }
        @Test public void test_3() { }

        private static class some_class_with_parametered_constructor {
            public some_class_with_parametered_constructor(List<?> collaborator) {
                constructor_instantiation++;
            }
        }
    }

    private static class FailingConstructor {
        FailingConstructor(Set<?> set) {
            throw new IllegalStateException("always fail");
        }
    }

    @Ignore("don't run this code in the test runner")
    private static class ATest {
        @Mock Set<?> set;
        @InjectMocks FailingConstructor failingConstructor;
    }


}
