package org.mockito.sample;

public class ArticleManager {
    
    private final ArticleCalculator calculator;
    private final ArticleDatabase database;

    public ArticleManager(ArticleCalculator calculator, ArticleDatabase database) {
        this.calculator = calculator;
        this.database = database;
    }
    
    public void updateArticleCounters(String newspaper) {
        int articles = calculator.countArticles(newspaper);
        int polishArticles = calculator.countArticlesInPolish(newspaper);
        
        database.updateNumberOfArticles(newspaper, articles);
        database.updateNumberOfPolishArticles(newspaper, polishArticles);
        database.updateNumberOfEnglishArticles(newspaper, articles - polishArticles);
    }
}
