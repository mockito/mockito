/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.verify;

public class OrdinaryVerificationPrintsAllInteractionsTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;

    @Test
    public void shouldShowAllInteractionsOnMockWhenOrdinaryVerificationFail() throws Exception {
        //given
        firstInteraction();
        secondInteraction();

        verify(mock).otherMethod(); //verify 1st interaction
        try {
            //when
            verify(mock).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {
            //then
            assertContains("However, there were exactly 2 interactions with this mock", e.getMessage());
            assertContains("firstInteraction(", e.getMessage());
            assertContains("secondInteraction(", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotShowAllInteractionsOnDifferentMock() throws Exception {
        differentMockInteraction();
        firstInteraction();
        
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {
            assertContains("firstInteraction(", e.getMessage());
            assertNotContains("differentMockInteraction(", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotShowAllInteractionsHeaderWhenNoOtherInteractions() throws Exception {
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {
            assertContains("there were zero interactions with this mock.", e.getMessage());
        }
    }

    private void differentMockInteraction() {
        mockTwo.simpleMethod();
    }

    private void secondInteraction() {
        mock.booleanReturningMethod();
    }

    private void firstInteraction() {
        mock.otherMethod();
    }
}