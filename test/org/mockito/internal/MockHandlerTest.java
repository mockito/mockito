/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockHandlerTest extends TestBase {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        MockingProgress state = new MockingProgressImpl();
        state.verificationStarted(VerificationModeFactory.atLeastOnce());
        MockHandler handler = new MockHandler(null, state, new ExceptionThrowingBinder(), null);
        
        try {
            handler.intercept(null, String.class.getDeclaredMethod("toString"), new Object[]{}, null);
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(state.pullVerificationMode());
    }
    
    private class ExceptionThrowingBinder extends MatchersBinder {
        @Override
        public InvocationMatcher bindMatchers(Invocation invocation) {
            throw new InvalidUseOfMatchersException("");
        }
    }
}