/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stacktrace;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ClickableStackTracesWhenFrameworkMisusedTest extends TestBase {
    
    @Mock private IMethods mock;

    @After
    public void resetState() {
        super.resetState();
    }
    
    private void misplacedArgumentMatcherHere() {
        anyString();
    }

    @Test
    public void shouldPointOutMisplacedMatcher() {
        misplacedArgumentMatcherHere();
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertContains("-> at ", e.getMessage());
            assertContains("misplacedArgumentMatcherHere(", e.getMessage());
        }
    }

    private void unfinishedStubbingHere() {
        when(mock.simpleMethod());
    }
    
    @Test
    public void shouldPointOutUnfinishedStubbing() {
        unfinishedStubbingHere();
        
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (UnfinishedStubbingException e) {
            assertContains("-> at ", e.getMessage());
            assertContains("unfinishedStubbingHere(", e.getMessage());
        }
    }
    
    @Test
    public void shouldShowWhereIsUnfinishedVerification() throws Exception {
        unfinishedVerificationHere();
        try {
            mock(IMethods.class);
            fail();
        } catch (UnfinishedVerificationException e) {
            assertContains("unfinishedVerificationHere(", e.getMessage());
        }
    }

    private void unfinishedVerificationHere() {
        verify(mock);
    }
}