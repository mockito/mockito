/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockitousage.IMethods;

@Ignore("that's just prototyping")
public class AssertingMatchersTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }
    
    class Article {
        
        private int pageNumber;
        private String headline;
        
        public Article(int pageNumber, String headline) {
            super();
            this.pageNumber = pageNumber;
            this.headline = headline;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public String getHeadline() {
            return headline;
        }
    }
    
    @Test
    public void shouldUseCustomCharMatcher1() {
        mock.simpleMethod(new Article(12, "Fabulous article"));
        
        Article articleOnPage12 = argThat(new ArgumentMatcher<Article>() {
            public boolean matches(Object argument) {
                Article o = (Article) argument;
                assertEquals(12, o.getPageNumber());
                return true;
            }} );
        
        verify(mock).simpleMethod(articleOnPage12);
        
        //Assertors?
        
//        verify(mock).simpleMethod(argThat(new ArgumentAssertor<Article>() {
//            public void assertArgument(Object argument) {
//                Article o = (Article) argument;
//                assertEquals("two", o.getHeadline());
//            }} ));
    }
}