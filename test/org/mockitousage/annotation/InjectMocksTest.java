package org.mockitousage.annotation;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockitousage.examples.use.ArticleCalculator;
import org.mockitousage.examples.use.ArticleManager;
import org.mockitoutil.TestBase;

public class InjectMocksTest extends TestBase {

	@Mock
	ArticleCalculator calculator;

	@InjectMocks
	ArticleManager articleManager;

	@Test
	public void shouldNotFailWhenNotInitialized() {
		assertNotNull(articleManager);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInnerMockShouldRaiseAnExceptionThatChangesOuterMockBehavior() {
		when(calculator.countArticles("new")).thenThrow(
				new IllegalArgumentException());
		articleManager.updateArticleCounters("new");
	}

	@Test
	public void mockJustWorks() {
		articleManager.updateArticleCounters("new");
	}
}
