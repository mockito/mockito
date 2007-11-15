package org.mockito.vs.easymock;
import org.easymock.EasyMock;
import org.junit.Test;
import org.mockito.Mockito;

public class MockitoVsEasyMockTest {
    
    @Test
    public void easyMockRocks() {
        ArticleCalculator mockCalculator = EasyMock.createMock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = EasyMock.createMock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);
              
        EasyMock.expect(mockCalculator.countArticles("Guardian")).andReturn(12);
        EasyMock.expect(mockCalculator.countArticlesInPolish("Guardian")).andReturn(5);
        
        mockDatabase.updateNumberOfArticles("Guardian", 12);
        mockDatabase.updateNumberOfPolishArticles("Guardian", 5);
        mockDatabase.updateNumberOfEnglishArticles("Guardian", 7);
        
        EasyMock.replay(mockCalculator, mockDatabase);
        
        articleManager.updateArticleCounters("Guardian");
        
        EasyMock.verify(mockCalculator, mockDatabase);
    }

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