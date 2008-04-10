/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.progress.VerificationModeImpl;

@SuppressWarnings("unchecked")
public class MockHandlerTest extends TestBase {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        MockingProgressImpl state = new MockingProgressImpl();
        state.verificationStarted(VerificationModeImpl.atLeastOnce());
        MockHandler handler = new MockHandler(null, state, new ExceptionThrowingBinder());
        
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