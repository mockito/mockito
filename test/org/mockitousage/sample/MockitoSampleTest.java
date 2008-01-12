/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.sample;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.InOrder;

public class MockitoSampleTest extends RequiresValidState {
    
    @Test
    public void managerCountsArticlesAndSavesThemInTheDatabase() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        stub(mockCalculator.countArticles("Guardian")).toReturn(12);
        stub(mockCalculator.countArticlesInPolish("Guardian")).toReturn(5);
        
        articleManager.updateArticleCounters("Guardian");
        
        verify(mockDatabase).updateNumberOfArticles("Guardian", 12);
        verify(mockDatabase).updateNumberOfPolishArticles("Guardian", 5);
//        verify(mockDatabase).updateNumberOfEnglishArticles("Guardianx", 7);
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
//        verify(mockDatabase).updateNumberOfEnglishArticles("Guardian", 2);
        
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
        
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).toReturn(1);
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).toReturn(12);
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).toReturn(0);
        
        stub(mockDatabase.getArticlesFor("Guardian")).toReturn(Arrays.asList(articleOne, articleTwo, articleThree)); 
        
        articleManager.updateRelatedArticlesCounters("Guardian");

        verify(mockDatabase).save(articleOne);
        verify(mockDatabase).save(articleTwo);
        verify(mockDatabase).save(articleThree);
    }
    
    @Test
    public void verificationInOrderAndArgumentMatchers() {
        ArticleCalculator mockCalculator = Mockito.mock(ArticleCalculator.class);
        ArticleDatabase mockDatabase = Mockito.mock(ArticleDatabase.class);
        
        ArticleManager articleManager = new ArticleManager(mockCalculator, mockDatabase);

        Article articleOne = new Article();
        Article articleTwo = new Article();
        
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).toReturn(1);
        stub(mockCalculator.countNumberOfRelatedArticles(articleOne)).toReturn(12);
        
        stub(mockDatabase.getArticlesFor("Guardian")).toReturn(Arrays.asList(articleOne, articleTwo)); 
        
        articleManager.updateRelatedArticlesCounters("Guardian");

        InOrder inOrder = inOrder(mockDatabase);
        
        inOrder.verify(mockDatabase, atLeastOnce()).getArticlesFor(anyString());
        inOrder.verify(mockDatabase, atLeastOnce()).save((Article) anyObject());
//        inOrder.verify(mockDatabase, atLeastOnce()).save(null);
        
        //TODO review messages once more and remove commented out code
    }
}