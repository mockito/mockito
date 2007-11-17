package org.mockito.sample;
import org.junit.Test;
import org.mockito.Mockito;

public class MockitoSampleTest {
    
    @Test
    public void managerCountsArticlesAndSavesThemInTheDatabase() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        Mockito.stub(mockCalculator.countArticles("Guardian")).andReturn(12);
        Mockito.stub(mockCalculator.countArticlesInPolish("Guardian")).andReturn(5);
        
        articleManager.updateArticleCounters("Guardian");
        
        Mockito.verify(mockDatabase).updateNumberOfArticles("Guardian", 12);
        Mockito.verify(mockDatabase).updateNumberOfPolishArticles("Guardian", 5);
        Mockito.verify(mockDatabase).updateNumberOfEnglishArticles("Guardian", 7);
    }
    
    @Test
    public void managerCountsArticlesUsingCalculator() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        articleManager.updateArticleCounters("Guardian");

        Mockito.verify(mockCalculator).countArticles("Guardian");
        Mockito.verify(mockCalculator).countArticlesInPolish("Guardian");
        
        Mockito.verifyNoMoreInteractions(mockCalculator);
    }
    
    @Test
    public void managerSavesArticlesInTheDatabase() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        articleManager.updateArticleCounters("Guardian");

        Mockito.verify(mockDatabase).updateNumberOfArticles("Guardian", 0);
        Mockito.verify(mockDatabase).updateNumberOfPolishArticles("Guardian", 0);
        Mockito.verify(mockDatabase).updateNumberOfEnglishArticles("Guardian", 0);
        
        Mockito.verifyNoMoreInteractions(mockDatabase);
    }
}