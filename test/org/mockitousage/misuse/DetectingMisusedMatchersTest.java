/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;

import java.util.Observer;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class DetectingMisusedMatchersTest extends TestBase {

    class WithFinal {
        final Object finalMethod(Object object) {
            return null;
        }
    }

    @Mock private WithFinal withFinal;
    
    @After
    public void resetState() {
        super.resetState();
    }

    private void misplacedArgumentMatcher() {
        anyObject();
    }
    
    private void misplacedIntArgumentMatcher() {
        anyInt();
    }
    
    private void misplacedBooleanArgumentMatcher() {
        anyBoolean();
    }

    @Test
    public void shouldFailFastWhenArgumentMatchersAbused() {
        misplacedArgumentMatcher();
        try {
            mock(IMethods.class);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertContains("Misplaced argument matcher", e.getMessage());
        }
    }
    
    @Test
    public void shouldReportArgumentLocationsWhenArgumentMatchersMisused() {
        try {
        	Observer observer = mock(Observer.class);
        	
        	misplacedIntArgumentMatcher();
        	misplacedArgumentMatcher();
        	misplacedBooleanArgumentMatcher();
        	
        	observer.update(null, null);
        	
        	validateMockitoUsage();
        	fail();
        } catch (InvalidUseOfMatchersException e) {
            assertContains("DetectingMisusedMatchersTest.misplacedIntArgumentMatcher", e.getMessage());
            assertContains("DetectingMisusedMatchersTest.misplacedArgumentMatcher", e.getMessage());
            assertContains("DetectingMisusedMatchersTest.misplacedBooleanArgumentMatcher", e.getMessage());
            e.printStackTrace();
        }
    }
   
    
    @Test
    public void shouldSayUnfinishedVerificationButNotInvalidUseOfMatchers() {
        verify(withFinal).finalMethod(anyObject());
        try {
            verify(withFinal);
            fail();
        } catch (UnfinishedVerificationException e) {}
    }
}