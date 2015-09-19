/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
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

    private void misplaced_anyObject_argument_matcher() {
        anyObject();
    }
    
    private void misplaced_anyInt_argument_matcher() {
        anyInt();
    }
    
    private void misplaced_anyBoolean_argument_matcher() {
        anyBoolean();
    }

    @Test
    public void should_fail_fast_when_argument_matchers_are_abused() {
        misplaced_anyObject_argument_matcher();
        try {
            mock(IMethods.class);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertContains("Misplaced argument matcher", e.getMessage());
        }
    }
    
    @Test
    public void should_report_argument_locations_when_argument_matchers_misused() {
        try {
            Observer observer = mock(Observer.class);
            
            misplaced_anyInt_argument_matcher();
            misplaced_anyObject_argument_matcher();
            misplaced_anyBoolean_argument_matcher();
            
            observer.update(null, null);
            
            validateMockitoUsage();
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertContains("DetectingMisusedMatchersTest.misplaced_anyInt_argument_matcher", e.getMessage());
            assertContains("DetectingMisusedMatchersTest.misplaced_anyObject_argument_matcher", e.getMessage());
            assertContains("DetectingMisusedMatchersTest.misplaced_anyBoolean_argument_matcher", e.getMessage());
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
