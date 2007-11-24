package org.mockito.sample;
import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class MockitoSampleTest {
    
    @Test
    public void managerCountsArticlesAndSavesThemInTheDatabase() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        stub(mockCalculator.countArticles("Guardian")).andReturn(12);
        stub(mockCalculator.countArticlesInPolish("Guardian")).andReturn(5);
        
        articleManager.updateArticleCounters("Guardian");
        
        verify(mockDatabase).updateNumberOfArticles("Guardian", 12);
        verify(mockDatabase).updateNumberOfPolishArticles("Guardian", 5);
        verify(mockDatabase).updateNumberOfEnglishArticles("Guardian", 7);
        
        verifyNoMoreInteractions(mockDatabase);
    }
    
    @Test
    public void managerCountsArticlesUsingCalculator() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        articleManager.updateArticleCounters("Guardian");

        verify(mockCalculator).countArticles("Guardian");
        verify(mockCalculator).countArticlesInPolish("Guardian");
        
        verifyNoMoreInteractions(mockCalculator);
    }
    
    @Test
    public void managerSavesArticlesInTheDatabase() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        articleManager.updateArticleCounters("Guardian");

        verify(mockDatabase).updateNumberOfArticles("Guardian", 0);
        verify(mockDatabase).updateNumberOfPolishArticles("Guardian", 0);
        verify(mockDatabase).updateNumberOfEnglishArticles("Guardian", 0);
        
        verifyNoMoreInteractions(mockDatabase);
    }
    
    @Test
    public void managerUpdatesNumberOfRelatedArticles() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        Article articleOne = new Article();
        Article articleTwo = new Article();
        Article articleThree = new Article();
        
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).andReturn(1);
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).andReturn(12);
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).andReturn(0);
        
        stub(mockDatabase.getArticlesFor("Guardian")).andReturn(Arrays.asList(articleOne, articleTwo, articleThree)); 
        
        articleManager.updateRelatedArticlesCounters("Guardian");

        verify(mockDatabase).save(articleOne);
        verify(mockDatabase).save(articleTwo);
        verify(mockDatabase).save(articleThree);
    }
}