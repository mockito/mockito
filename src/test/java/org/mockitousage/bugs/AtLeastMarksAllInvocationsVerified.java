/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.*;

// see issue 112
public class AtLeastMarksAllInvocationsVerified extends TestBase {

    public static class SomeMethods {
        public void allowedMethod() {
        }
        public void disallowedMethod() {
        }
    }

    @Test(expected = org.mockito.exceptions.verification.NoInteractionsWanted.class)
    public void shouldFailBecauseDisallowedMethodWasCalled(){
        SomeMethods someMethods = mock(SomeMethods.class);

        someMethods.allowedMethod();
        someMethods.disallowedMethod();
        
        verify(someMethods, atLeast(1)).allowedMethod();
        verifyNoMoreInteractions(someMethods);
    }
}
