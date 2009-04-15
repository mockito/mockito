/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.StateMaster;
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
        StateMaster.reset();
    }

    private void misplacedArgumentMatcher() {
        anyObject();
    }

    @Test
    public void shouldFailFastWhenArgumentMatchersAbused() {
        misplacedArgumentMatcher();
        try {
            mock(IMethods.class);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e, messageContains("Misplaced argument matcher"));
        }
    }
    
    @Test
    public void shouldSayUnfinishedVerificationButNotInvalidUseOfMatchers() {
        verify(withFinal).finalMethod(anyObject());
        try {
            verify(withFinal).finalMethod(anyObject());
            fail();
        } catch (UnfinishedVerificationException e) {}
    }
}