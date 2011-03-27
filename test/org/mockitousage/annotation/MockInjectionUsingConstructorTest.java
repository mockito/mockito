package org.mockitousage.annotation;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockInjectionUsingConstructorTest {
    private MockUtil mockUtil = new MockUtil();

    @Mock private ArticleCalculator calculator;
    @Mock private ArticleDatabase database;

    @InjectMocks private ArticleManager articleManager;
    @Spy @InjectMocks private ArticleManager spiedArticleManager;


    @InjectMocks private ArticleVisitor should_be_initialized_several_times;

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
    public void constructor_is_called_for_each_test() throws Exception {
        int minimum_number_of_test_before = 3;
        Assertions.assertThat(articleVisitorInstantiationCount).isGreaterThan(minimum_number_of_test_before);
        Assertions.assertThat(articleVisitorMockInjectedInstances.size()).isGreaterThan(minimum_number_of_test_before);
    }

    @Test
    public void objects_created_with_constructor_initialization_can_be_spied() throws Exception {
        assertFalse(mockUtil.isMock(articleManager));
        assertTrue(mockUtil.isMock(spiedArticleManager));
    }

    @Test
    public void should_report_failure_only_when_object_initialization_throws_exception() throws Exception {

        try {
            MockitoAnnotations.initMocks(new ATest());
            fail();
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("failingConstructor").contains("constructor").contains("threw an exception");
            Assertions.assertThat(e.getCause()).isInstanceOf(IllegalStateException.class);
        }
    }

    private static int articleVisitorInstantiationCount = 0;
    private static Set<Object> articleVisitorMockInjectedInstances = new HashSet<Object>();

    private static class ArticleVisitor {
        public ArticleVisitor(ArticleCalculator calculator) {
            articleVisitorInstantiationCount++;
            articleVisitorMockInjectedInstances.add(calculator);
        }
    }

    private static class FailingConstructor {
        FailingConstructor(Set set) {
            throw new IllegalStateException("always fail");
        }
    }

    @Ignore("don't run this code in the test runner")
    private static class ATest {
        @Mock Set set;
        @InjectMocks FailingConstructor failingConstructor;
    }


}
